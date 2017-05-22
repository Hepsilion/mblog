package mblog.core.persist.dao;

import mtons.modules.persist.BaseRepository;
import mtons.modules.pojos.Paging;

import java.util.List;

import mblog.core.persist.entity.NotifyPO;

/**
 * @author langhsu on 2015/8/31.
 */
public interface NotifyDao extends BaseRepository<NotifyPO> {
    List<NotifyPO> findByOwnId(Paging paging, long ownId);
    /**
     * 查询我的未读消息
     * @param ownId
     * @return
     */
    int unread4Me(long ownId);

    /**
     * 标记我的消息为已读
     * @param ownId
     */
    void readed4Me(long ownId);
}
