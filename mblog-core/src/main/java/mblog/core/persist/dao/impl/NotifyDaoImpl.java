package mblog.core.persist.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import mblog.base.lang.Consts;
import mblog.base.utils.NumberUtils;
import mblog.core.persist.dao.NotifyDao;
import mblog.core.persist.entity.NotifyPO;
import mtons.modules.annotation.Repository;
import mtons.modules.persist.impl.BaseRepositoryImpl;
import mtons.modules.pojos.Paging;

/**
 * @author langhsu on 2015/8/31.
 */
@Repository(entity = NotifyPO.class)
public class NotifyDaoImpl extends BaseRepositoryImpl<NotifyPO> implements NotifyDao {
	private static final long serialVersionUID = -4830458674452233964L;

	@Override
    public List<NotifyPO> findByOwnId(Paging paging, long ownId) {
        PagingQuery<NotifyPO> q = pagingQuery(paging);
        q.add(Restrictions.eq("ownId", ownId));
        q.desc("id");
        return q.list();
    }

    @Override
    public int unread4Me(long ownId) {
        Criteria count = createCriteria();
        count.setProjection(Projections.rowCount());
        count.add(Restrictions.eq("ownId", ownId));
        count.add(Restrictions.eq("status", Consts.UNREAD));
        return NumberUtils.changeToInt(count.uniqueResult());
    }

    @Override
    public void readed4Me(long ownId) {
        Query query = createQuery("update NotifyPO set status = :s1 where status = :s2 and ownId = :ownId");
        query.setInteger("s1", Consts.READED);
        query.setInteger("s2", Consts.UNREAD);
        query.setLong("ownId", ownId);
        query.executeUpdate();
    }
}
