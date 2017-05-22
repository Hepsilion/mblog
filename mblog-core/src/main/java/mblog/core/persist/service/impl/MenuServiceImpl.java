/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.core.persist.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mblog.core.data.Menu;
import mblog.core.persist.dao.MenuDao;
import mblog.core.persist.entity.MenuPO;
import mblog.core.persist.service.MenuService;

/**
 * @author langhsu
 *
 */
@Service
public class MenuServiceImpl implements MenuService {
	@Autowired
	private MenuDao menuDao;

	@Override
	@Transactional(readOnly = true)
	public List<Menu> findAll() {
		List<MenuPO> list = menuDao.findAll();
		List<Menu> rets = new ArrayList<Menu>();
		for (MenuPO po : list) {
			Menu m = new Menu();
			BeanUtils.copyProperties(po, m);
			rets.add(m);
		}
		return rets;
	}
	
}
