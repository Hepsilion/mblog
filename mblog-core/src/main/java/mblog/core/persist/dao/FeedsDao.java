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

import mtons.modules.persist.BaseRepository;
import mtons.modules.pojos.Paging;

import java.util.List;

import mblog.core.data.Feeds;
import mblog.core.persist.entity.FeedsPO;

/**
 * @author langhsu
 *
 */
public interface FeedsDao extends BaseRepository<FeedsPO> {
	/**
	 * 添加动态, 同时会分发给粉丝
	 * 
	 * @param feeds
	 * @return
	 */
	int batchAdd(Feeds feeds);
	
	int deleteByAuthorId(long ownId, long authorId);
	
	List<FeedsPO> findUserFeeds(Paging paging, long ownId, long authorId, long ignoreId);
	
	void deleteByTarget(long postId);
}
