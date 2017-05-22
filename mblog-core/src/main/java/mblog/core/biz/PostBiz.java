/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.core.biz;

import java.util.Collection;
import java.util.List;

import mtons.modules.pojos.Paging;
import mblog.base.lang.EnumPrivacy;
import mblog.core.data.Post;

/**
 * 文章保卫层, 带缓存策略
 *
 * - 如果不使用缓存, 则直接调用Service
 * 
 * @author langhsu
 *
 */
public interface PostBiz {
	/**
	 * 分页查询文章
	 * 
	 * @param paging
	 * @param group
	 * @param ord
	 * @return
	 */
	Paging paging(Paging paging, int group, String ord);
	
	/**
	 * 查询指定用户的文章
	 * 
	 * @param paging
	 * @param uid
	 * @return
	 */
	Paging pagingByAuthorId(Paging paging, long uid, EnumPrivacy privacy);
	
	/**
	 * 走廊查询 - (只返回一张图片)
	 * 
	 * @param paging
	 * @param group
	 * @param ord
	 * @return
	 */
	Paging gallery(Paging paging, int group, String ord);
	
	/**
	 * 从缓存中获取文章
	 * 
	 * @param id
	 * @return
	 */
	Post getPost(long id);
	
	/**
	 * 发布文章, 并清除缓存
	 * 
	 * @param post
	 */
	void post(Post post);

	/**
	 * 修改文章推荐状态
	 * @param id
	 * @param featured
	 */
	void updateFeatured(long id, int featured);

	/**
	 * 查询最近更新的文章
	 *
	 * @param maxResults
	 * @param ignoreUserId
	 * @return
	 */
	List<Post> findRecents(int maxResults, long ignoreUserId);

	/**
	 * 查询热门文章
	 *
	 * @param maxResults
	 * @param ignoreUserId
	 * @return
	 */
	List<Post> findHots(int maxResults, long ignoreUserId);

	/**
	 * 删除文章, 且刷新缓存
	 *
	 * @param id
	 * @param authorId
	 */
	void delete(long id, long authorId);

	/**
	 * 批量删除文章, 且刷新缓存
	 *
	 * @param ids
	 */
	void delete(Collection<Long> ids);

	/**
	 * 喜欢文章
	 * @param userId
	 * @param postId
	 */
	void favor(long userId, long postId);

	/**
	 * 取消喜欢文章
	 * @param userId
	 * @param postId
	 */
	void unfavor(long userId, long postId);

	/**
	 * 更新文章：更新文章并清空缓存
	 * @param p
	 */
	void update(Post p);
	
}
