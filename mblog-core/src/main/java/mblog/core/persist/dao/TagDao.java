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

import java.util.List;

import mblog.core.persist.entity.TagPO;
import mtons.modules.persist.BaseRepository;
import mtons.modules.pojos.Paging;

/**
 * @author langhsu
 *
 */
public interface TagDao extends BaseRepository<TagPO> {
	TagPO getByName(String name);
	List<TagPO> tops(int maxResults);
	List<TagPO> paging(Paging paging, String key, String order);
}
