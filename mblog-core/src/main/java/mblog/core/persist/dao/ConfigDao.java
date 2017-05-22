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

import mblog.core.persist.entity.ConfigPO;
import mtons.modules.persist.BaseRepository;

/**
 * @author langhsu
 *
 */
public interface ConfigDao extends BaseRepository<ConfigPO> {
	ConfigPO findByName(String key);
}
