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

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import mblog.core.persist.dao.TagDao;
import mblog.core.persist.entity.TagPO;
import mtons.modules.annotation.Repository;
import mtons.modules.persist.impl.BaseRepositoryImpl;
import mtons.modules.pojos.Paging;

/**
 * @author langhsu
 * 
 */
@Repository(entity = TagPO.class)
public class TagDaoImpl extends BaseRepositoryImpl<TagPO> implements TagDao {
	private static final long serialVersionUID = 3787316111507159374L;

	@Override
	public TagPO getByName(String name) {
		return findUniqueBy("name", name);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TagPO> tops(int maxResults) {
		Criteria c = createCriteria();
		c.setMaxResults(maxResults);
		
		c.addOrder(Order.desc("featured"));
		c.addOrder(Order.desc("id"));
		return c.list();
	}

	@Override
	public List<TagPO> paging(Paging paging, String key, String order) {
		PagingQuery<TagPO> q = pagingQuery(paging);
		
		if (StringUtils.isNotBlank(key)) {
			q.add(Restrictions.like("name", key, MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotBlank(order)) {
			q.desc(order);
		} else {
			q.desc("id");
		}
		return q.list();
	}
}
