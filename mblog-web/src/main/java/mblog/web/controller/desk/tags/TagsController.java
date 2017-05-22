/**
 * 
 */
package mblog.web.controller.desk.tags;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import mblog.core.data.Tag;
import mblog.core.persist.service.PostService;
import mblog.core.persist.service.TagService;
import mblog.web.controller.BaseController;
import mblog.web.controller.desk.Views;
import mtons.modules.pojos.Paging;

/**
 * 发现
 * @author langhsu
 *
 */
@Controller
public class TagsController extends BaseController {
	@Autowired
	private PostService postService;
	@Autowired
	private TagService tagService;
	
	private int maxResults = 12;
	
	@RequestMapping("/tags")
	public String view(ModelMap model) {
		List<Tag> tags = tagService.topTags(maxResults, true);
		model.put("tags", tags);
		return getView(Views.TAGS_INDEX);
	}
	
	@RequestMapping("/tag/{tag}")
	public String tag(@PathVariable String tag, Integer pn, ModelMap model) {
		Paging page = wrapPage(pn);
		try {
			if (StringUtils.isNotEmpty(tag)) {
				postService.searchByTag(page, tag);
				tagService.identityHots(tag);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.put("page", page);
		model.put("tag", tag);
		return getView(Views.TAGS_TAG);
	}
	
}
