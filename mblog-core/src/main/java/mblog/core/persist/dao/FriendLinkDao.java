package mblog.core.persist.dao;

import mblog.core.persist.entity.FriendLinkPO;
import mtons.modules.persist.BaseRepository;

import java.util.List;

/**
 * @author Beldon
 */
public interface FriendLinkDao extends BaseRepository<FriendLinkPO> {

    List<FriendLinkPO> findAll();
}
