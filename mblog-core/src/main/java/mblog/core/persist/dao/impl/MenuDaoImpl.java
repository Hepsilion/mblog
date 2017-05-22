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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import mblog.core.persist.dao.MenuDao;
import mblog.core.persist.entity.MenuPO;
import mtons.modules.annotation.Repository;
import mtons.modules.lang.Const;
import mtons.modules.persist.impl.BaseRepositoryImpl;

/**
 * @author langhsu
 *
 */
@Repository(entity = MenuPO.class)
public class MenuDaoImpl extends BaseRepositoryImpl<MenuPO> implements MenuDao {
	private static final long serialVersionUID = 2564594993409371310L;

	@Override
	@SuppressWarnings("unchecked")
	public List<MenuPO> findAll() {
		Criteria c = createCriteria();
		c.add(Restrictions.eq("status", Const.STATUS_NORMAL));
		c.addOrder(Order.desc("weight"));
		return c.list();
	}
}
