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
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import mblog.core.persist.dao.UserDao;
import mblog.core.persist.entity.UserPO;
import mtons.modules.annotation.Repository;
import mtons.modules.persist.impl.BaseRepositoryImpl;
import mtons.modules.pojos.Paging;

/**
 * @author langhsu
 *
 */
@Repository(entity = UserPO.class)
public class UserDaoImpl extends BaseRepositoryImpl<UserPO> implements UserDao {
	private static final long serialVersionUID = -3396151113305189145L;

	@Override
	public UserPO getByUsername(String username) {
		return findUniqueBy("username", username);
	}

	@Override
	public UserPO getByEmail(String email) {
		return findFirst("email", email);
	}

	@Override
	public List<UserPO> paging(Paging paging, String key) {
		PagingQuery<UserPO> q = pagingQuery(paging);
		if (StringUtils.isNotBlank(key)) {
			q.add(Restrictions.or(
				Restrictions.like("username", key, MatchMode.ANYWHERE),
				Restrictions.like("name", key, MatchMode.ANYWHERE)
			));
		}
		q.desc("id");
		return q.list();
	}
	
	@Override
	public List<UserPO> getHotUserByfans(int maxResults) {
		Criteria c = createCriteria();
		c.setMaxResults(maxResults);
		c.addOrder(Order.desc("fans"));
		return c.list();
		
	}

	@Override
	public List<UserPO> findByIds(Set<Long> ids) {
		return find(Restrictions.in("id", ids));
	}

	@Override
	public void identityPost(List<Long> userIds, boolean identity) {
		String substm = "+ 1";

		if (!identity) {
			substm = "- 1";
		}
		Query query = createSQLQuery("update mto_users set posts = posts " + substm + " where id in (:ids)");
		query.setParameterList("ids", userIds);
		query.executeUpdate();
	}

	@Override
	public void identityComment(List<Long> userIds, boolean identity) {
		String substm = "+ 1";

		if (!identity) {
			substm = "- 1";
		}
		Query query = createSQLQuery("update mto_users set comments = comments " + substm + " where id in (:ids)");
		query.setParameterList("ids", userIds);
		query.executeUpdate();
	}

	@Override
	public void identityFollow(List<Long> userIds, boolean identity) {
		String substm = "+ 1";

		if (!identity) {
			substm = "- 1";
		}
		Query query = createSQLQuery("update mto_users set follows = follows " + substm + " where id in (:ids)");
		query.setParameterList("ids", userIds);
		query.executeUpdate();
	}

	@Override
	public void identityFans(List<Long> userIds, boolean identity) {
		String substm = "+ 1";

		if (!identity) {
			substm = "- 1";
		}
		Query query = createSQLQuery("update mto_users set fans = fans " + substm + " where id in (:ids)");
		query.setParameterList("ids", userIds);
		query.executeUpdate();
	}

	@Override
	public void identityFavors(List<Long> userIds, boolean identity) {
		String substm = "+ 1";

		if (!identity) {
			substm = "- 1";
		}
		Query query = createSQLQuery("update mto_users set favors = favors " + substm + " where id in (:ids)");
		query.setParameterList("ids", userIds);
		query.executeUpdate();
	}

	

}
