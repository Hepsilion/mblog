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

import java.util.Collection;
import java.util.List;
import java.util.Set;

import mblog.core.persist.entity.CommentPO;

/**
 * @author langhsu
 *
 */
public interface CommentDao extends BaseRepository<CommentPO> {
	List<CommentPO> paging(Paging paging, String key);
	List<CommentPO> paging(Paging paging, long toId, long authorId, boolean desc);
	List<CommentPO> findByIds(Set<Long> ids);

	int deleteByIds(Collection<Long> ids);
}
