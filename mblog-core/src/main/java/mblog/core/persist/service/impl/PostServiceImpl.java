/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.core.persist.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import mblog.base.lang.Consts;
import mblog.base.lang.EnumPrivacy;
import mblog.core.data.Attach;
import mblog.core.data.Post;
import mblog.core.data.Tag;
import mblog.core.data.User;
import mblog.core.persist.dao.PostDao;
import mblog.core.persist.entity.PostAttribute;
import mblog.core.persist.entity.PostPO;
import mblog.core.persist.service.AttachService;
import mblog.core.persist.service.FavorService;
import mblog.core.persist.service.PostService;
import mblog.core.persist.service.TagService;
import mblog.core.persist.service.UserEventService;
import mblog.core.persist.service.UserService;
import mblog.core.persist.utils.BeanMapUtils;
import mtons.modules.lang.EntityStatus;
import mtons.modules.pojos.Paging;
import mtons.modules.utils.PreviewTextUtils;

/**
 * @author langhsu
 *
 */
@Service
public class PostServiceImpl implements PostService {
	@Autowired
	private PostDao postDao;
	@Autowired
	private AttachService attachService;
	@Autowired
	private TagService tagService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserEventService userEventService;
	@Autowired
	private FavorService favorService;

	@Override
	@Transactional(readOnly = true)
	public void paging(Paging paging, int group, String ord, boolean whetherHasAlbums) {
		List<PostPO> list = postDao.paging(paging, group, ord);
		paging.setResults(toPosts(list, whetherHasAlbums));
	}

	@Override
	@Transactional(readOnly = true)
	public void paging4Admin(Paging paging, long id, String title, int group) {
		List<PostPO> list = postDao.paging4Admin(paging, id, title, group);
		paging.setResults(toPosts(list, false));
	}

	@Override
	@Transactional(readOnly = true)
	public void pagingByAuthorId(Paging paging, long userId, EnumPrivacy privacy) {
		List<PostPO> list = postDao.pagingByAuthorId(paging, userId, privacy);
		paging.setResults(toPosts(list, true));
	}
	
	@Override
	@Transactional(readOnly = true)
	public void search(Paging paging, String q) throws Exception {
		List<Post> list = postDao.search(paging, q);

		HashSet<Long> ids = new HashSet<>();
		HashSet<Long> uids = new HashSet<>();

		for (Post po : list) {
			ids.add(po.getId());
			uids.add(po.getAuthorId());
		}

		// 加载相册
		buildAttachs(list, ids);

		// 加载用户信息
		buildUsers(list, uids);

		paging.setResults(list);
	}
	
	@Override
	@Transactional(readOnly = true)
	public void searchByTag(Paging paigng, String tag) {
		paigng.setResults(toPosts(postDao.searchByTag(paigng, tag), true));
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Post> findLatests(int maxResults, long ignoreUserId) {
		List<PostPO> list = postDao.findLatests(maxResults, ignoreUserId);
		List<Post> rets = new ArrayList<>();

		list.forEach(po -> rets.add(BeanMapUtils.copy(po, 0)));

		return rets;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Post> findHots(int maxResults, long ignoreUserId) {
		List<PostPO> list = postDao.findHots(maxResults, ignoreUserId);
		List<Post> rets = new ArrayList<>();

		list.forEach(po -> rets.add(BeanMapUtils.copy(po, 0)));
		return rets;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Map<Long, Post> findSingleMapByIds(Set<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return Collections.emptyMap();
		}

		List<PostPO> list = postDao.findByIds(ids);
		Map<Long, Post> rets = new HashMap<>();

		HashSet<Long> imageIds = new HashSet<>();
		HashSet<Long> uids = new HashSet<>();

		list.forEach(po -> {
			rets.put(po.getId(), BeanMapUtils.copy(po, 0));

			// 此处加载最后一张图片
			if (po.getLastImageId() > 0) {
				imageIds.add(po.getLastImageId());
			}

			uids.add(po.getAuthorId());
		});
		
		Map<Long, Attach> ats = attachService.findByIds(imageIds);

		rets.forEach((k, v) -> {
			if (v.getLastImageId() > 0) {
				Attach a = ats.get(v.getLastImageId());
				v.setAlbum(a);
			}
		});

		// 加载用户信息
		buildUsers(rets.values(), uids);

		return rets;
	}

	@Override
	@Transactional(readOnly = true)
	public Map<Long, Post> findMultileMapByIds(Set<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return Collections.emptyMap();
		}

		List<PostPO> list = postDao.findByIds(ids);
		Map<Long, Post> rets = new HashMap<>();

		HashSet<Long> uids = new HashSet<>();

		list.forEach(po -> {
			rets.put(po.getId(), BeanMapUtils.copy(po, 0));
			uids.add(po.getAuthorId());
		});

		// 加载相册
		buildAttachs(rets.values(), ids);

		// 加载用户信息
		buildUsers(rets.values(), uids);

		return rets;
	}

	@Override
	@Transactional
	public long post(Post post) {
		PostPO po = new PostPO();

		BeanUtils.copyProperties(post, po);

		po.setCreated(new Date());
		po.setStatus(EntityStatus.ENABLED);

		// 处理摘要
		if (StringUtils.isBlank(post.getSummary())) {
			po.setSummary(trimSummary(post.getContent()));
		} else {
			po.setSummary(post.getSummary());
		}

		if (post.getAttribute() != null) {
			// 保存扩展
			PostAttribute extend = post.getAttribute();
			extend.setPost(po);
			po.setAttribute(extend);
		}

		postDao.save(po);
		
		// 处理相册
		if (post.getAlbums() != null) {
			long lastImageId = attachService.batchPost(po.getId(), post.getAlbums());
			po.setLastImageId(lastImageId);
			po.setImages(post.getAlbums().size());
		}
		
		// 处理标签
		if (StringUtils.isNotBlank(post.getTags())) {
			List<Tag> tags = BeanMapUtils.convertTags(po.getId(), post.getTags());
			tagService.batchPost(tags);
		}

		// 更新文章统计
		userEventService.identityPost(Collections.singletonList(po.getAuthorId()), po.getId(), true);

		return po.getId();
	}
	
	@Override
	@Transactional
	public Post get(long id) {
		PostPO po = postDao.get(id);
		Post d = null;
		if (po != null) {
			d = BeanMapUtils.copy(po, 1);

			d.setAuthor(userService.get(d.getAuthorId()));
			d.setAlbums(attachService.findByTarget(d.getId()));
		}
		return d;
	}

	/**
	 * 更新文章方法
	 * @param p
	 */
	@Override
	@Transactional
	public void update(Post p){
		PostPO po = postDao.get(p.getId());

		if (po != null) {
			po.setTitle(p.getTitle());//标题

			// 处理摘要
			if (StringUtils.isBlank(p.getSummary())) {
				po.setSummary(trimSummary(p.getContent()));
			} else {
				po.setSummary(p.getSummary());
			}

			po.setContent(p.getContent());//内容
			po.setTags(p.getTags());//标签
			po.setPrivacy(p.getPrivacy());

			// 处理相册
			if (p.getAlbums() != null && !p.getAlbums().isEmpty()) {
				long lastImageId = attachService.batchPost(po.getId(), p.getAlbums());
				po.setLastImageId(lastImageId);
				po.setImages(po.getImages() + p.getAlbums().size());
			}

			// 保存扩展
			if (p.getAttribute() != null) {
				PostAttribute extend = po.getAttribute();

				if (extend != null) {
					extend.setVideoUrl(p.getAttribute().getVideoUrl());
					extend.setVideoBody(p.getAttribute().getVideoBody());
				} else {
					extend = p.getAttribute();
					extend.setPost(po);
					po.setAttribute(extend);
				}
			}
		}
	}

	@Override
	@Transactional
	public void updateFeatured(long id, int featured) {
		PostPO po = postDao.get(id);

		if (po != null) {
			int max = featured;
			if (Consts.FEATURED_ACTIVE == featured) {
				max = postDao.maxFeatured() + 1;
			}
			po.setFeatured(max);
		}
	}

	@Override
	@Transactional
	public void delete(long id) {
		PostPO po = postDao.get(id);
		if (po != null) {
			attachService.deleteByToId(id);
			postDao.delete(po);
		}
	}
	
	@Override
	@Transactional
	public void delete(long id, long authorId) {
		PostPO po = postDao.get(id);
		if (po != null) {
			// 判断文章是否属于当前登录用户
			Assert.isTrue(po.getAuthorId() == authorId, "认证失败");

			attachService.deleteByToId(id);
			postDao.delete(po);
		}
	}
	
	@Override
	@Transactional
	public void identityViews(long id) {
		PostPO po = postDao.get(id);
		if (po != null) {
			po.setViews(po.getViews() + Consts.IDENTITY_STEP);
		}
	}

	@Override
	@Transactional
	public void identityComments(long id) {
		PostPO po = postDao.get(id);
		if (po != null) {
			po.setComments(po.getComments() + Consts.IDENTITY_STEP);
		}
	}

	@Override
	@Transactional
	public void favor(long userId, long postId) {
		PostPO po = postDao.get(postId);

		Assert.notNull(po, "文章不存在");

		favorService.add(userId, postId);

		po.setFavors(po.getFavors() + Consts.IDENTITY_STEP);
	}

	@Override
	@Transactional
	public void unfavor(long userId, long postId) {
		PostPO po = postDao.get(postId);

		Assert.notNull(po, "文章不存在");

		favorService.delete(userId, postId);

		po.setFavors(po.getFavors() - Consts.IDENTITY_STEP);
	}
	
	@Override
	@Transactional
	public void resetIndexs() {
		postDao.resetIndexs();
	}

	/**
	 * 截取文章内容
	 * @param text
	 * @return
	 */
	private String trimSummary(String text){
		return PreviewTextUtils.getText(text, 126);
	}

	private List<Post> toPosts(List<PostPO> posts, boolean whetherHasAlbums) {
		List<Post> rets = new ArrayList<>();

		HashSet<Long> pids = new HashSet<>();
		HashSet<Long> uids = new HashSet<>();

		posts.forEach(po -> {
			pids.add(po.getId());
			uids.add(po.getAuthorId());

			rets.add(BeanMapUtils.copy(po, 0));
		});

		// 加载用户信息
		buildUsers(rets, uids);

		// 判断是否加载相册
		if (whetherHasAlbums) {
			buildAttachs(rets, pids);
		}
		return rets;
	}

	private void buildAttachs(Collection<Post> posts, Set<Long> postIds) {
    	Map<Long, List<Attach>> attMap = attachService.findByTarget(postIds);

		posts.forEach(p -> p.setAlbums(attMap.get(p.getId())));
    }

	private void buildUsers(Collection<Post> posts, Set<Long> uids) {
		Map<Long, User> userMap = userService.findMapByIds(uids);

		posts.forEach(p -> p.setAuthor(userMap.get(p.getAuthorId())));
	}

}
