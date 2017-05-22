/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.core.persist.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import mblog.core.persist.dao.FollowDao;
import mblog.core.persist.entity.FollowPO;
import mtons.modules.annotation.Repository;
import mtons.modules.persist.impl.BaseRepositoryImpl;
import mtons.modules.pojos.Paging;

/**
 * @author langhsu
 *
 */
@Repository(entity = FollowPO.class)
public class FollowDaoImpl extends BaseRepositoryImpl<FollowPO> implements FollowDao {
	private static final long serialVersionUID = 2809599067409589271L;

	@Override
	@SuppressWarnings("unchecked")
	public FollowPO checkFollow(long userId, long followId) {
		Criteria c = createCriteria();
		c.add(Restrictions.eq("user.id", userId));
		c.add(Restrictions.eq("follow.id", followId));
		
		List<FollowPO> list = c.list();
		
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<FollowPO> following(Paging paging, long userId) {
		PagingQuery<FollowPO> q = pagingQuery(paging);
		q.add(Restrictions.eq("user.id", userId));
		q.desc("id");
		return q.list();
	}

	@Override
	public List<FollowPO> fans(Paging paging, long userId) {
		PagingQuery<FollowPO> q = pagingQuery(paging);
		q.add(Restrictions.eq("follow.id", userId));
		q.desc("id");
		return q.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean checkCrossFollow(long userId, long targetUserId) {
		String hql = "from mto_follows where user_id = :userId and follow_id = :followId and " +
				"user_id in (select follow_id from mto_follows where user_id = :followId)";
		Query q = createSQLQuery(hql);
		q.setLong("userId", userId);
		q.setLong("followId", targetUserId);
		
		q.setResultTransformer(Transformers.aliasToBean(FollowPO.class));

		List<FollowPO> list = q.list();
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public int unfollow(long userId, long followId) {
		Query q = createSQLQuery("delete from mto_follows where user_id = :userId and follow_id = :followId");
		q.setLong("userId", userId);
		q.setLong("followId", followId);
		return q.executeUpdate();
	}
}
