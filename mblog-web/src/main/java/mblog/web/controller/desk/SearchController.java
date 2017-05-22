/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.web.controller.desk;

import mtons.modules.pojos.Paging;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import mblog.core.persist.service.PostService;
import mblog.core.persist.service.TagService;
import mblog.web.controller.BaseController;

/**
 * 文章搜索
 * @author langhsu
 *
 */
@Controller
@RequestMapping("/browse")
public class SearchController extends BaseController {
	@Autowired
	private PostService postService;
	@Autowired
	private TagService tagService;
	
	@RequestMapping("/search")
	public String search(Integer pn, String q, ModelMap model) {
		Paging page = wrapPage(pn);
		try {
			if (StringUtils.isNotEmpty(q)) {
				postService.search(page, q);
				tagService.identityHots(q);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.put("page", page);
		model.put("q", q);
		return getView(Views.BROWSE_SEARCH);
	}
	
}
