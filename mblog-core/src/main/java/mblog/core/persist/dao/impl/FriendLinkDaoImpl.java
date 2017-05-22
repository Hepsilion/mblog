package mblog.core.persist.dao.impl;

import java.util.List;

import org.hibernate.Query;

import mblog.core.persist.dao.FriendLinkDao;
import mblog.core.persist.entity.FriendLinkPO;
import mtons.modules.annotation.Repository;
import mtons.modules.persist.impl.BaseRepositoryImpl;

/**
 * @author Beldon
 */
@Repository(entity = FriendLinkPO.class)
public class FriendLinkDaoImpl extends BaseRepositoryImpl<FriendLinkPO> implements FriendLinkDao {
	private static final long serialVersionUID = 754755214307906383L;

	@Override
	@SuppressWarnings("unchecked")
    public List<FriendLinkPO> findAll(){
        Query query = createQuery("from FriendLinkPO am order by am.sort");
        return query.list();
    }
}
