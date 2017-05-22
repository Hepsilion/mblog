/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.web.controller.desk.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import mblog.base.lang.EnumPrivacy;
import mblog.core.data.AccountProfile;
import mblog.core.data.User;
import mblog.core.persist.service.CommentService;
import mblog.core.persist.service.FavorService;
import mblog.core.persist.service.FeedsService;
import mblog.core.persist.service.FollowService;
import mblog.core.persist.service.NotifyService;
import mblog.core.persist.service.PostService;
import mblog.core.persist.service.UserService;
import mblog.shiro.authc.AccountSubject;
import mblog.web.controller.BaseController;
import mblog.web.controller.desk.Views;
import mtons.modules.lang.Const;
import mtons.modules.pojos.Paging;
import mtons.modules.pojos.UserProfile;

/**
 * 用户主页
 * @author langhsu
 *
 */
@Controller
public class HomeController extends BaseController {
	@Autowired
	private PostService postService;
	@Autowired
	private FeedsService feedsService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private UserService userService;
	@Autowired
	private FollowService followService;
	@Autowired
	private FavorService favorService;
	@Autowired
	private NotifyService notifyService;

	/**
	 * 用户主页
	 * @param pn
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/home")
	public String home(Integer pn, ModelMap model) {
		Paging paging = wrapPage(pn);
		AccountSubject subject = getSubject();

		feedsService.findUserFeeds(paging, subject.getProfile().getId(), Const.ZERO, Const.ZERO);

		model.put("page", paging);
		initUser(model);

		return getView(Views.HOME_FEEDS);
	}

	/**
	 * 我发布的文章
	 * @param pn
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/home", params = "method=posts")
	public String posts(Integer pn, ModelMap model) {
		Paging page = wrapPage(pn);
		UserProfile up = getSubject().getProfile();
		postService.pagingByAuthorId(page, up.getId(), EnumPrivacy.ALL);

		model.put("page", page);
		initUser(model);

		return getView(Views.HOME_POSTS);
	}

	/**
	 * 我发表的评论
	 * @param pn
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/home", params = "method=comments")
	public String comments(Integer pn, ModelMap model) {
		Paging page = wrapPage(pn);
		AccountSubject subject = getSubject();
		page = commentService.paging4Home(page, subject.getProfile().getId());

		model.put("page", page);
		initUser(model);

		return getView(Views.HOME_COMMENTS);
	}

	/**
	 * 我喜欢过的文章
	 * @param pn
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/home/favors")
	public String favors(Integer pn, ModelMap model) {
		Paging page = wrapPage(pn);
		UserProfile profile = getSubject().getProfile();
		favorService.pagingByOwnId(page, profile.getId());

		model.put("page", page);
		initUser(model);

		return getView(Views.HOME_FAVORS);
	}

	/**
	 * 我的关注
	 * @param pn
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/home/follows")
	public String follows(Integer pn, ModelMap model) {
		Paging page = wrapPage(pn);
		UserProfile profile = getSubject().getProfile();
		followService.follows(page, profile.getId());

		model.put("page", page);
		initUser(model);

		return getView(Views.HOME_FOLLOWS);
	}

	/**
	 * 我的粉丝
	 * @param pn
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/home/fans")
	public String fans(Integer pn, ModelMap model) {
		Paging page = wrapPage(pn);
		UserProfile profile = getSubject().getProfile();
		followService.fans(page, profile.getId());

		model.put("page", page);
		initUser(model);

		return getView(Views.HOME_FANS);
	}

	/**
	 * 我的通知
	 * @param pn
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/home/notifies")
	public String notifies(Integer pn, ModelMap model) {
		Paging page = wrapPage(pn);
		UserProfile profile = getSubject().getProfile();
		notifyService.findByOwnId(page, profile.getId());
		// 标记已读
		notifyService.readed4Me(profile.getId());

		model.put("page", page);
		initUser(model);

		pushBadgesCount();

		return getView(Views.HOME_NOTIFIES);
	}

	private void initUser(ModelMap model) {
		UserProfile up = getSubject().getProfile();
		User user = userService.get(up.getId());

		model.put("user", user);
	}

	private void pushBadgesCount() {
		AccountProfile profile = (AccountProfile) session.getAttribute("profile");
		if (profile != null && profile.getBadgesCount() != null) {
			profile.getBadgesCount().setNotifies(0);
			session.setAttribute("profile", profile);
		}
	}
}
