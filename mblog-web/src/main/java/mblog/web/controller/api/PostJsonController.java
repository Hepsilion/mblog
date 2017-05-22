/**
 * 
 */
package mblog.web.controller.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import mblog.base.lang.Consts;
import mblog.core.biz.PostBiz;
import mblog.web.controller.BaseController;
import mtons.modules.pojos.Paging;

/**
 * @author langhsu
 *
 */
@Controller
@RequestMapping("/api")
public class PostJsonController extends BaseController {
	@Autowired
	private PostBiz postBiz;
	
	@RequestMapping("/posts.json")
	public @ResponseBody Paging posts(Integer pn, HttpServletRequest request) {
		String order = ServletRequestUtils.getStringParameter(request, "ord", Consts.order.NEWEST);
		int gid = ServletRequestUtils.getIntParameter(request, "gid", 0);
		Paging paging = wrapPage(pn);
		paging = postBiz.paging(paging, gid, order);
		
		return paging;
	}
}
