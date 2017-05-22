/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.core.persist.dao;

import java.util.Collection;
import java.util.List;

import mblog.base.lang.EnumPrivacy;
import mblog.core.data.Post;
import mblog.core.persist.entity.PostPO;
import mtons.modules.persist.BaseRepository;
import mtons.modules.pojos.Paging;

/**
 * @author langhsu
 *
 */
public interface PostDao extends BaseRepository<PostPO> {
	/**
	 * 前台查询
	 * @param paging
	 * @param group
	 * @param ord
	 * @return
	 */
	List<PostPO> paging(Paging paging, int group, String ord);

	/**
	 * 后台查询
	 * @param paging
	 * @param id
	 * @param title
	 * @param group
	 * @return
	 */
	List<PostPO> paging4Admin(Paging paging, long id, String title, int group);

	/**
	 * 查询指定用户
	 * @param paging
	 * @param userId
	 * @return
	 */
	List<PostPO> pagingByAuthorId(Paging paging, long userId, EnumPrivacy privacy);

	List<PostPO> findLatests(int maxResults, long ignoreUserId);
	List<PostPO> findHots(int maxResults, long ignoreUserId);
	List<PostPO> findByIds(Collection<Long> ids);

	int maxFeatured();
	
	List<Post> search(Paging paging, String q) throws Exception;
	List<PostPO> searchByTag(Paging paigng, String tag);
	void resetIndexs();

}
