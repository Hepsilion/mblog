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

import java.util.List;

import mblog.core.persist.entity.GroupPO;

/**
 * @author langhsu
 *
 */
public interface GroupDao extends BaseRepository<GroupPO> {
	List<GroupPO> findAll();
	GroupPO getByKey(String key);
}
