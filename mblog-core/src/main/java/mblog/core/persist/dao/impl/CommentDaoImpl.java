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

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import mblog.core.persist.dao.CommentDao;
import mblog.core.persist.entity.CommentPO;
import mtons.modules.annotation.Repository;
import mtons.modules.persist.impl.BaseRepositoryImpl;
import mtons.modules.pojos.Paging;

/**
 * @author langhsu
 *
 */
@Repository(entity = CommentPO.class)
public class CommentDaoImpl extends BaseRepositoryImpl<CommentPO> implements CommentDao {
	private static final long serialVersionUID = 1023552695901348149L;

	@Override
	public List<CommentPO> paging(Paging paging, String key) {
		PagingQuery<CommentPO> q = pagingQuery(paging);
		if (StringUtils.isNotBlank(key)) {
			q.add(Restrictions.like("content", key, MatchMode.ANYWHERE));
		}
		q.desc("created");
		return q.list();
	}
	
	@Override
	public List<CommentPO> paging(Paging paging,long toId, long authorId, boolean desc) {
		PagingQuery<CommentPO> q = pagingQuery(paging);
		q.add(Restrictions.eq("status", 0));

		if (toId > 0) {
			q.add(Restrictions.eq("toId", toId));
		}

		if (authorId > 0) {
			q.add(Restrictions.eq("authorId", authorId));
		}
		if (desc) {
			q.desc("created");
		} else {
			q.asc("created");
		}
		return q.list();
	}

	@Override
	public List<CommentPO> findByIds(Set<Long> ids) {
		return find(Restrictions.in("id", ids));
	}

	@Override
	public int deleteByIds(Collection<Long> ids) {
		Query query = createQuery("delete from CommentPO where id in (:ids)");
		query.setParameterList("ids", ids);
		return query.executeUpdate();
	}


}
