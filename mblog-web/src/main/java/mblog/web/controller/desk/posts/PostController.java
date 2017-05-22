/**
 *
 */
package mblog.web.controller.desk.posts;

import javax.servlet.http.HttpServletRequest;

import mblog.core.persist.service.AttachService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mblog.core.biz.PostBiz;
import mblog.core.data.Group;
import mblog.core.data.Post;
import mblog.core.persist.service.GroupService;
import mblog.web.controller.BaseController;
import mblog.web.controller.desk.Views;
import mtons.modules.pojos.Data;
import mtons.modules.pojos.UserProfile;

/**
 * 文章操作
 * @author langhsu
 *
 */
@Controller
@RequestMapping("/post")
public class PostController extends BaseController {
	@Autowired
	private PostBiz postPlanet;
	@Autowired
	private GroupService groupService;
	@Autowired
	private AttachService attachService;

	/**
	 * 发布文章页
	 * @param groupKey
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/new/{groupKey}", method = RequestMethod.GET)
	public String view(@PathVariable String groupKey, ModelMap model) {
		Group group = groupService.getByKey(groupKey);
		model.put("group", group);
		return routeView(Views.ROUTE_POST_PUBLISH, group.getTemplate());
	}

	/**
	 * 提交发布
	 * @param blog
	 * @return
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public String post(Post blog, HttpServletRequest request) {

		if (blog != null && StringUtils.isNotBlank(blog.getTitle())) {
			UserProfile profile = getSubject().getProfile();

			String[] ablums = request.getParameterValues("delayImages");
			blog.setAlbums(handleAlbums(ablums));
			blog.setAuthorId(profile.getId());

			postPlanet.post(blog);
		}
		return Views.REDIRECT_HOME_POSTS;
	}

	/**
	 * 删除文章
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete/{id}")
	public @ResponseBody Data delete(@PathVariable Long id) {
		Data data = Data.failure("操作失败");
		if (id != null) {
			UserProfile up = getSubject().getProfile();
			try {
				postPlanet.delete(id, up.getId());
				data = Data.success("操作成功", Data.NOOP);
			} catch (Exception e) {
				data = Data.failure(e.getMessage());
			}
		}
		return data;
	}

	/**
	 * 修改文章
	 * @param id
	 * @return
	 */
	@RequestMapping("/to_update/{id}")
	public String toUpdate(@PathVariable Long id, ModelMap model) {
		UserProfile up = getSubject().getProfile();
		Post ret = postPlanet.getPost(id);

		Assert.notNull(ret, "该文章已被删除");

		Assert.isTrue(ret.getAuthorId() == up.getId(), "该文章不属于你");
		Group group = groupService.getById(ret.getGroup());

		model.put("view", ret);
		model.put("group", group);
		return routeView(Views.ROUTE_POST_UPDATE, group.getTemplate());
	}

	/**
	 * 更新文章方法
	 * @author LBB
	 * @return
	 */
	@RequestMapping(value = "/update/{groupKey}", method = RequestMethod.POST)
	public String subUpdate(Post p, HttpServletRequest request) {
		UserProfile up = getSubject().getProfile();
		if (p != null && p.getAuthorId() == up.getId()) {
			String[] ablums = request.getParameterValues("delayImages");
			p.setAlbums(handleAlbums(ablums));
			postPlanet.update(p);
		}
		return Views.REDIRECT_HOME_POSTS;
	}

	@RequestMapping("/delete_album")
	public @ResponseBody Data deleteAlbum(Long id) {
		Data data = Data.failure("操作失败");
		if (id != null) {
			try {
				attachService.delete(id);
				data = Data.success("操作成功", Data.NOOP);
			} catch (Exception e) {
				data = Data.failure(e.getMessage());
			}
		}
		return data;
	}


}
