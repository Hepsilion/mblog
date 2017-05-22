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

import mtons.modules.exception.MtonsException;
import mtons.modules.pojos.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import mblog.core.data.User;
import mblog.core.persist.dao.FollowDao;
import mblog.core.persist.entity.FollowPO;
import mblog.core.persist.entity.UserPO;
import mblog.core.persist.service.FollowService;
import mblog.core.persist.service.UserEventService;
import mblog.core.persist.utils.BeanMapUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author langhsu
 *
 */
@Service
public class FollowServiceImpl implements FollowService {
	@Autowired
	private FollowDao followDao;
	@Autowired
	private UserEventService userEventService;

	@Override
	@Transactional
	public long follow(long userId, long followId) {
		long ret = 0;

		Assert.state(userId != followId, "您不能关注自己");

		FollowPO po = followDao.checkFollow(userId, followId);

		if (po == null) {
			po = new FollowPO();
			po.setUser(new UserPO(userId));
			po.setFollow(new UserPO(followId));
			po.setCreated(new Date());

			followDao.save(po);

			ret = po.getId();

			userEventService.identityFollow(Collections.singletonList(userId), followId, true);
			userEventService.identityFans(Collections.singletonList(followId), userId, true);
		} else {
			throw new MtonsException("您已经关注过此用户了");
		}
		return ret;
	}

	@Override
	@Transactional
	public void unfollow(long userId, long followId) {
		int ret = followDao.unfollow(userId, followId);

		if (ret <= 0) {
			throw new MtonsException("取消关注失败");
		} else {
			userEventService.identityFollow(Collections.singletonList(userId), followId, false);
			userEventService.identityFans(Collections.singletonList(followId), userId, false);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public void follows(Paging paging, long userId) {
		List<FollowPO> list = followDao.following(paging, userId);
		List<User> rets = new ArrayList<>();

		for (FollowPO po : list) {
			rets.add(BeanMapUtils.copy(po.getFollow(), 0));
		}
		paging.setResults(rets);
	}

	@Override
	@Transactional(readOnly = true)
	public void fans(Paging paging, long userId) {
		List<FollowPO> list = followDao.fans(paging, userId);
		List<User> rets = new ArrayList<>();

		for (FollowPO po : list) {
			rets.add(BeanMapUtils.copy(po.getUser(), 0));
		}
		paging.setResults(rets);
	}

	@Override
	@Transactional
	public boolean checkFollow(long userId, long followId) {
		return (followDao.checkFollow(userId, followId) != null);
	}

	@Override
	@Transactional
	public boolean checkCrossFollow(long userId, long targetUserId) {
		return followDao.checkCrossFollow(userId, targetUserId);
	}

}
