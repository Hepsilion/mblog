package mblog.core.persist.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import mblog.core.persist.dao.RoleDao;
import mblog.core.persist.entity.RolePO;
import mtons.modules.annotation.Repository;
import mtons.modules.persist.impl.BaseRepositoryImpl;
import mtons.modules.pojos.Paging;

@Repository(entity = RolePO.class)
public class RoleDaoImpl extends BaseRepositoryImpl<RolePO> implements RoleDao {
	private static final long serialVersionUID = 1L;

	@Override
	public List<RolePO> paging(Paging paging, String key) {
		PagingQuery<RolePO> q = pagingQuery(paging);
		if (StringUtils.isNotBlank(key)) {
			q.add(
				Restrictions.like("name", key, MatchMode.ANYWHERE)
			);
		}
		q.desc("id");
		return q.list();
	}

}
