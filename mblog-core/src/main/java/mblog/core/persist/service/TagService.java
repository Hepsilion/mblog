/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.core.persist.service;

import java.util.List;

import mblog.core.data.Tag;
import mtons.modules.pojos.Paging;

/**
 * @author langhsu
 *
 */
public interface TagService {
	/**
	 * top 查询 Tag
	 * @param maxResults
	 * @param loadPost 是否加载 tag 最后更新文章
	 * @return
	 */
	List<Tag> topTags(int maxResults, boolean loadPost);
	
	Tag get(long id);
	
	/**
	 * 评论添加 Tag
	 * @param tags
	 */
	void batchPost(List<Tag> tags);
	
	/**
	 * 更新热度
	 * @param name
	 */
	void identityHots(String name);
	
	/**
	 * 更新热度
	 * @param name
	 */
	void identityHots(long id);
	
	void paging(Paging paging, String key, String order);
	
	void updateFeatured(long id, int status);
	
	void updateLock(long id, int status);
	
	void update(Tag tag);
	
	void delete(long id);
}
