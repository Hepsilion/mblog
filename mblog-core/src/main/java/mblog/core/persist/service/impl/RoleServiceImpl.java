package mblog.core.persist.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mblog.core.data.AuthMenu;
import mblog.core.data.Role;
import mblog.core.persist.dao.AuthMenuDao;
import mblog.core.persist.dao.RoleDao;
import mblog.core.persist.entity.AuthMenuPO;
import mblog.core.persist.entity.RolePO;
import mblog.core.persist.service.RoleService;
import mblog.core.persist.utils.BeanMapUtils;
import mtons.modules.pojos.Paging;

@Service
public class RoleServiceImpl implements RoleService{

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private AuthMenuDao authMenuDao;

	@Override
	@Transactional(readOnly = true)
	public void paging(Paging paging, String key) {
		List<RolePO> rolePOs = roleDao.paging(paging, key);
		List<Role> roles = new ArrayList<Role>();
		rolePOs.forEach(po -> {
			roles.add(BeanMapUtils.copy(po));
		});

		paging.setResults(roles);

	}

	@Override
	@Transactional(readOnly = true)
	public Role get(Long id) {
		RolePO po = roleDao.get(id);
		Role role = BeanMapUtils.copy(po);
		return role;
	}

	@Override
	@Transactional(readOnly=false)
	public void save(Role role){
		RolePO rolePO = new RolePO();
		List<AuthMenu> authMenus = role.getAuthMenus();
		List<AuthMenuPO> authMenuPOs = new ArrayList<>();
		for(AuthMenu authMenu : authMenus){
			AuthMenuPO authMenuPO = authMenuDao.get(authMenu.getId());
			authMenuPOs.add(authMenuPO);
		}
		BeanUtils.copyProperties(role, rolePO);
		rolePO.setAuthMenus(authMenuPOs);
		roleDao.saveOrUpdate(rolePO);
	}

	@Override
	@Transactional(readOnly=false)
	public void delete(Long id) {
		roleDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Role> getAll() {
		List<RolePO> list = roleDao.list();
		List<Role> roles = new ArrayList<Role>();
		list.forEach(po -> {
			roles.add(BeanMapUtils.copy(po));
		});
		return roles;
	}

}
