package mblog.core.persist.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import mblog.core.persist.dao.FavorDao;
import mblog.core.persist.entity.FavorPO;
import mtons.modules.annotation.Repository;
import mtons.modules.persist.impl.BaseRepositoryImpl;
import mtons.modules.pojos.Paging;

/**
 * @author langhsu on 2015/8/31.
 */
@Repository(entity = FavorPO.class)
public class FavorDaoImpl extends BaseRepositoryImpl<FavorPO> implements FavorDao {
	private static final long serialVersionUID = 2220117564378926421L;

	@Override
	@SuppressWarnings("unchecked")
    public FavorPO find(long ownId, long postId) {
        Criteria c = createCriteria();
        c.add(Restrictions.eq("ownId", ownId));
        c.add(Restrictions.eq("postId", postId));

        List<FavorPO> rets = c.list();

        if (rets != null && rets.size() > 0) {
            return rets.get(0);
        }
        return null;
    }

    @Override
    public List<FavorPO> paingByOwnId(Paging paging, long ownId) {
        PagingQuery<FavorPO> q = pagingQuery(paging, Restrictions.eq("ownId", ownId));
        q.desc("created");
        return q.list();
    }
}
