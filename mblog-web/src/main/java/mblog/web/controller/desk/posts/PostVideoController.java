/**
 * 
 */
package mblog.web.controller.desk.posts;

import java.net.MalformedURLException;
import java.util.Collections;

import mblog.base.analysis.videos.VideoAnalysis;
import mblog.base.data.Video;
import mblog.base.lang.Consts;
import mblog.core.biz.PostBiz;
import mblog.core.data.Attach;
import mblog.core.data.Post;
import mblog.core.persist.entity.PostAttribute;
import mblog.core.persist.service.AttachService;
import mblog.web.controller.BaseController;
import mblog.web.controller.desk.Views;
import mtons.modules.pojos.UserProfile;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * post 扩展实现 - 发布视频
 * @author langhsu
 *
 */
@Controller
@RequestMapping("/post")
public class PostVideoController extends BaseController {
	@Autowired
	private PostBiz postPlanet;
	@Autowired
	private VideoAnalysis videoAnalysis;
	@Autowired
	private AttachService attachService;
	
	@RequestMapping(value = "/submit/video", method = RequestMethod.POST)
	public String postVideo(Post post, String url) throws MalformedURLException {
		
		if (StringUtils.isNotBlank(url)) {
			UserProfile up = getSubject().getProfile();
			
			Video video = videoAnalysis.take(url);
			
			Attach att = handleAlbums(video.getBigThumbnail());
			
			post.setTitle(video.getTitle());
			post.setSummary(video.getDescription());
			post.setContent(post.getContent());
			post.setAuthorId(up.getId());
			post.setAlbums(Collections.singletonList(att));

			PostAttribute extend = new PostAttribute();
			extend.setVideoUrl(url);
			extend.setVideoBody(video.getBody());

			post.setAttribute(extend);
			postPlanet.post(post);
		}
		return Views.REDIRECT_HOME_POSTS;
	}

	/**
	 * 更新文章方法
	 * @author LBB
	 * @return
	 */
	@RequestMapping(value = "/update/video", method = RequestMethod.POST)
	public String subUpdate(Post post, String url, HttpServletRequest request) throws MalformedURLException {
		UserProfile up = getSubject().getProfile();
		if (StringUtils.isNotBlank(url) && post.getAuthorId() == up.getId()) {

			Video video = videoAnalysis.take(url);

			Attach att = handleAlbums(video.getBigThumbnail());

			post.setTitle(video.getTitle());
			post.setSummary(video.getDescription());
			post.setContent(post.getContent());
			post.setAuthorId(up.getId());
			post.setAlbums(Collections.singletonList(att));

			PostAttribute extend = new PostAttribute();
			extend.setVideoUrl(url);
			extend.setVideoBody(video.getBody());

			post.setAttribute(extend);
			
			// 先清理原先的图片
			attachService.deleteByToId(post.getId());
			
			postPlanet.update(post);
		}
		return Views.REDIRECT_HOME_POSTS;
	}

	private Attach handleAlbums(String thumbnail) {
		if (StringUtils.isBlank(thumbnail)) {
			return null;
		}
		
		Attach att = new Attach();
		att.setOriginal(thumbnail);
		att.setPreview(thumbnail);
		att.setScreenshot(thumbnail);
		att.setStore(Consts.ATTACH_STORE_NETWORK);
		return att;
	}

}
