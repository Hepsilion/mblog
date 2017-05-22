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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mblog.base.lang.Consts;
import mblog.core.data.Post;
import mblog.core.data.Tag;
import mblog.core.persist.dao.TagDao;
import mblog.core.persist.entity.TagPO;
import mblog.core.persist.service.PostService;
import mblog.core.persist.service.TagService;
import mblog.core.persist.utils.BeanMapUtils;
import mtons.modules.pojos.Paging;

/**
 * @author langhsu
 *
 */
@Service
public class TagServiceImpl implements TagService {
	@Autowired
	private TagDao tagDao;
	@Autowired
	private PostService postService;
	
	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "tagsCaches", key = "'top_tags_' + #maxResults + '_' + #loadPost")
	public List<Tag> topTags(int maxResults, boolean loadPost) {
		List<TagPO> list = tagDao.tops(maxResults);
		List<Tag> rets = new ArrayList<>();
		
		Set<Long> postIds = new HashSet<>();
		for (TagPO po : list) {
			rets.add(BeanMapUtils.copy(po));
			postIds.add(po.getLastPostId());
		}
		
		if (loadPost && postIds.size() > 0) {
			Map<Long, Post> posts = postService.findSingleMapByIds(postIds);
			
			for (Tag t : rets) {
				Post p = posts.get(t.getLastPostId());
				t.setPost(p);
			}
		}
		return rets;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Tag get(long id) {
		return BeanMapUtils.copy(tagDao.get(id));
	}
	
	@Override
	@Transactional
	public void batchPost(List<Tag> tags) {
		if (tags == null || tags.size() == 0) {
			return;
		}

		tags.forEach(t -> {
			if (StringUtils.isNotBlank(t.getName())) {
				TagPO po = tagDao.getByName(t.getName());
				if (po != null) {

					// 如果不锁定则更新文章ID
					if (po.getLocked() != Consts.STATUS_LOCKED) {
						po.setLastPostId(t.getLastPostId());
					}
					po.setPosts(po.getPosts() + 1);
				} else {
					po = new TagPO();
					BeanUtils.copyProperties(t, po);
					tagDao.save(po);
				}
			}

		});
	}

	@Override
	@Transactional
	public void identityHots(String name) {
		TagPO po = tagDao.getByName(name);
		if (po != null) {
			po.setHots(po.getHots() + Consts.IDENTITY_STEP);
		}
	}

	@Override
	@Transactional
	public void identityHots(long id) {
		TagPO po = tagDao.get(id);
		if (po != null) {
			po.setHots(po.getHots() + Consts.IDENTITY_STEP);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public void paging(Paging paging, String key, String order) {
		List<TagPO> list = tagDao.paging(paging, key, order);
		List<Tag> rets = new ArrayList<>();
		
		Set<Long> postIds = new HashSet<>();

		list.forEach(po -> {
			rets.add(BeanMapUtils.copy(po));
			postIds.add(po.getLastPostId());
		});

		paging.setResults(rets);
	}

	@Override
	@Transactional
	public void updateFeatured(long id, int status) {
		TagPO po = tagDao.get(id);
		if (po != null) {
			if (po.getFeatured() == Consts.STATUS_NORMAL) {
				po.setFeatured(Consts.STATUS_FEATURED);
			} else {
				po.setFeatured(Consts.STATUS_NORMAL);
			}
		}
	}
	
	@Override
	@Transactional
	public void updateLock(long id, int status) {
		TagPO po = tagDao.get(id);
		if (po != null) {
			po.setLocked(status);
		}
	}

	@Override
	@Transactional
	public void update(Tag tag) {
		TagPO po = tagDao.get(tag.getId());
		
		if (po != null) {
			po.setLastPostId(tag.getLastPostId());
			po.setLocked(tag.getLocked());
		}
	}
	
	@Override
	@Transactional
	@CacheEvict(value = "tagsCaches", allEntries = true)
	public void delete(long id) {
		tagDao.deleteById(id);
	}

}
