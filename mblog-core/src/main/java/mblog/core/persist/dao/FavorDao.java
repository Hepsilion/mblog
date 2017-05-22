package mblog.core.persist.dao;

import mtons.modules.persist.BaseRepository;
import mtons.modules.pojos.Paging;

import java.util.List;

import mblog.core.persist.entity.FavorPO;

/**
 * @author langhsu on 2015/8/31.
 */
public interface FavorDao extends BaseRepository<FavorPO> {
    /**
     * 指定查询
     * @param ownId
     * @param postId
     * @return
     */
    FavorPO find(long ownId, long postId);

    List<FavorPO> paingByOwnId(Paging paging, long ownId);
}
