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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import mblog.core.data.AccountProfile;
import mblog.core.data.AuthMenu;
import mblog.core.data.BadgesCount;
import mblog.core.data.User;
import mblog.core.persist.dao.RoleDao;
import mblog.core.persist.dao.UserDao;
import mblog.core.persist.entity.AuthMenuPO;
import mblog.core.persist.entity.RolePO;
import mblog.core.persist.entity.UserPO;
import mblog.core.persist.service.NotifyService;
import mblog.core.persist.service.UserService;
import mblog.core.persist.utils.BeanMapUtils;
import mtons.modules.exception.MtonsException;
import mtons.modules.lang.EntityStatus;
import mtons.modules.pojos.Paging;
import mtons.modules.security.MD5;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private NotifyService notifyService;

	@Autowired
	private RoleDao roleDao;

	@Override
	@Transactional
	public AccountProfile login(String username, String password) {
		UserPO po = userDao.getByUsername(username);
		AccountProfile u = null;

		Assert.notNull(po, "账户不存在");

//		Assert.state(po.getStatus() != Const.STATUS_CLOSED, "您的账户已被封禁");

		if (StringUtils.equals(po.getPassword(), password)) {
			po.setLastLogin(Calendar.getInstance().getTime());

			u = BeanMapUtils.copyPassport(po);
		}

		BadgesCount badgesCount = new BadgesCount();
		badgesCount.setNotifies(notifyService.unread4Me(u.getId()));

		u.setBadgesCount(badgesCount);

		return u;
	}

	@Override
	@Transactional
	public AccountProfile getProfileByName(String username) {
		UserPO po = userDao.getByUsername(username);
		AccountProfile u = null;

		Assert.notNull(po, "账户不存在");

//		Assert.state(po.getStatus() != Const.STATUS_CLOSED, "您的账户已被封禁");
		po.setLastLogin(Calendar.getInstance().getTime());

		u = BeanMapUtils.copyPassport(po);

		BadgesCount badgesCount = new BadgesCount();
		badgesCount.setNotifies(notifyService.unread4Me(u.getId()));

		u.setBadgesCount(badgesCount);

		return u;
	}

	@Override
	@Transactional
	public User register(User user) {
		Assert.notNull(user, "Parameter user can not be null!");

		Assert.hasLength(user.getUsername(), "用户名不能为空!");
//		Assert.hasLength(user.getEmail(), "邮箱不能为空!");
		Assert.hasLength(user.getPassword(), "密码不能为空!");

		UserPO check = userDao.getByUsername(user.getUsername());

		Assert.isNull(check, "用户名已经存在!");

		if (StringUtils.isNotBlank(user.getEmail())) {
			check = userDao.getByEmail(user.getEmail());
			Assert.isNull(check, "邮箱已经被注册!");
		}

		UserPO po = new UserPO();

		BeanUtils.copyProperties(user, po);

		Date now = Calendar.getInstance().getTime();
		po.setPassword(MD5.md5(user.getPassword()));
		po.setStatus(EntityStatus.ENABLED);
		po.setActiveEmail(EntityStatus.ENABLED);
		po.setCreated(now);

		userDao.save(po);

		return BeanMapUtils.copy(po, 0);
	}

	@Override
	@Transactional
	@CacheEvict(value = "usersCaches", key = "#user.getId()")
	public AccountProfile update(User user) {
		UserPO po = userDao.get(user.getId());
		if (null != po) {
			po.setName(user.getName());
			po.setSignature(user.getSignature());
		}
		return BeanMapUtils.copyPassport(po);
	}

	@Override
	@Transactional
	@CacheEvict(value = "usersCaches", key = "#id")
	public AccountProfile updateEmail(long id, String email) {
		UserPO po = userDao.get(id);

		if (null != po) {

			if (email.equals(po.getEmail())) {
				throw new MtonsException("邮箱地址没做更改");
			}

			UserPO check = userDao.getByEmail(email);

			if (check != null && check.getId() != po.getId()) {
				throw new MtonsException("该邮箱地址已经被使用了");
			}
			po.setEmail(email);
			po.setActiveEmail(EntityStatus.ENABLED);
		}

		return BeanMapUtils.copyPassport(po);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "usersCaches", key = "#userId")
	public User get(long userId) {
		UserPO po = userDao.get(userId);
		User ret = null;
		if (po != null) {
			ret = BeanMapUtils.copy(po, 0);
		}
		return ret;
	}

	
	@Override
	@Transactional(readOnly = true)
	public List<User> getHotUserByfans(int maxResults){
		List<User> rets = new ArrayList<>();
		List<UserPO> list = userDao.getHotUserByfans(maxResults);
		for (UserPO po : list) {
			User u = BeanMapUtils.copy(po , 1);
			rets.add(u);
		}
		return rets;
	}
	
	@Override
	@Transactional(readOnly = true)
	public User getByUsername(String username) {
		UserPO po = userDao.getByUsername(username);
		User ret = null;
		if (po != null) {
			ret = BeanMapUtils.copy(po, 1);
		}
		return ret;
	}

	@Override
	@Transactional
	@CacheEvict(value = "usersCaches", key = "#id")
	public AccountProfile updateAvatar(long id, String path) {
		UserPO po = userDao.get(id);
		if (po != null) {
			po.setAvatar(path);
		}
		return BeanMapUtils.copyPassport(po);
	}

	@Override
	@Transactional
	public void updatePassword(long id, String newPassword) {
		UserPO po = userDao.get(id);

		Assert.hasLength(newPassword, "密码不能为空!");

		if (null != po) {
			po.setPassword(MD5.md5(newPassword));
		}
	}

	@Override
	@Transactional
	public void updatePassword(long id, String oldPassword, String newPassword) {
		UserPO po = userDao.get(id);

		Assert.hasLength(newPassword, "密码不能为空!");

		if (po != null) {
			Assert.isTrue(MD5.md5(oldPassword).equals(po.getPassword()), "当前密码不正确");
			po.setPassword(MD5.md5(newPassword));
		}
	}

	@Override
	@Transactional
	public void updateStatus(long id, int status) {
		UserPO po = userDao.get(id);

		if (po != null) {
			po.setStatus(status);
		}
	}

	@Override
	@Transactional
	public AccountProfile updateActiveEmail(long id, int activeEmail) {
		UserPO po = userDao.get(id);

		if (po != null) {
			po.setActiveEmail(activeEmail);
		}
		return BeanMapUtils.copyPassport(po);
	}

	@Override
	@Transactional
	public void updateRole(long id, Long[] roleIds) {
		List<RolePO> rolePOs = new ArrayList<RolePO>();
		for(Long roleId:roleIds){
			RolePO rolePO = roleDao.get(roleId);
			rolePOs.add(rolePO);
		}
		UserPO po = userDao.get(id);

		if (po != null) {
			po.setRoles(rolePOs);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public void paging(Paging paging, String key) {
		List<UserPO> list = userDao.paging(paging, key);
		//TODO 不知道为啥，当一个用户有多角色时，查询出来的用户会显示重复的，下面这段去掉重复
//		Set<UserPO> userSet = new HashSet<UserPO>();
//		for(int i=0;i<list.size();i++){
//			userSet.add(list.get(i));
////			for(int j=i+1;j<list.size();j++){
////				if(list.get(i).equals(list.get(j))){
////					list.remove(i);
////				}
////			}
//		}
//		list.clear();
//		for(UserPO userPO : userSet){
//			list.add(userPO);
//		}
		List<User> rets = new ArrayList<>();

		for (UserPO po : list) {
			User u = BeanMapUtils.copy(po , 1);
			rets.add(u);
		}
		paging.setResults(rets);
	}

	@Override
	@Transactional(readOnly = true)
	public Map<Long, User> findMapByIds(Set<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return Collections.emptyMap();
		}
		List<UserPO> list = userDao.findByIds(ids);
		Map<Long, User> ret = new HashMap<>();

		list.forEach(po -> {
			ret.put(po.getId(), BeanMapUtils.copy(po, 0));
		});
		return ret;
	}

	@Override
	@Transactional(readOnly = true)
	public List<AuthMenu> getMenuList(long id) {
		List<AuthMenu> menus = new ArrayList<AuthMenu>();
		UserPO userPO = userDao.get(id);
		List<RolePO> roles = userPO.getRoles();
		for(RolePO role : roles){
			List<AuthMenuPO> menuPOs = role.getAuthMenus();
			for(AuthMenuPO menuPO : menuPOs){
				AuthMenu menu = BeanMapUtils.copy(menuPO);
				if(!menus.contains(menu)){
					menus.add(menu);
				}
			}
		}
		return menus;
	}

}
