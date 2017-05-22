/**
 * 
 */
package mblog.velocity.directive;

import java.io.IOException;
import java.util.List;

import mblog.base.context.SpringContextHolder;
import mblog.core.data.Tag;
import mblog.core.persist.service.TagService;
import mblog.velocity.BaseDirective;
import mblog.velocity.handler.RenderHandler;

/**
 * 获取标签列表
 * 
 * @author langhsu
 *
 */
public class TagsDirective extends BaseDirective {
	private TagService tagService;
	
	@Override
	public void initBean() {
		tagService = SpringContextHolder.getBean(TagService.class);
	}

	@Override
	public boolean render(RenderHandler handler) throws RuntimeException, IOException {
		List<Tag> result = tagService.topTags(12, true);
		String alias = handler.getStringParameter(1);
        
		handler.put(alias, result);
		handler.doRender();
		
		postRender(handler.getContext());
		return true;
	}

	@Override
	public String getName() {
		return "tags";
	}

	@Override
	public int getType() {
		return BLOCK;
	}

}
