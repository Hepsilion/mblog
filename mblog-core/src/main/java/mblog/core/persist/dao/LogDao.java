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

import java.util.Date;
import java.util.List;

import mblog.core.persist.entity.LogPO;
import mtons.modules.persist.BaseRepository;

/**
 * @author langhsu
 *
 */
public interface LogDao extends BaseRepository<LogPO> {
	List<LogPO> findByDay(int logType, long userId, long targetId, String ip, Date day);
}
