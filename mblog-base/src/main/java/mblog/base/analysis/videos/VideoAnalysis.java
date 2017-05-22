/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.base.analysis.videos;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import mblog.base.analysis.videos.support.Youku;
import mblog.base.data.Video;
import mblog.base.utils.URLUtils;

/**
 * 视频分析
 * 
 * @author langhsu
 *
 */
@Component
public class VideoAnalysis {
	private Map<String, VideoStrategy> strategies = new HashMap<String, VideoStrategy>();
	
	@PostConstruct
	public void init() {
		strategies.put("v.youku.com", new Youku());
	}
	
	public void setStrategies(Map<String, VideoStrategy> strategies) {
		this.strategies = strategies;
	}

	public void addStrategy(String host, VideoStrategy strategy) {
		strategies.put(host, strategy);
	}
	
	public Video take(String url) throws MalformedURLException {
		String host = URLUtils.getHost(url);
		
		VideoStrategy strategy = strategies.get(host);
		
		Assert.notNull(strategy, "地址格式不对, 或不支持该网站" + host);
		
		return strategy.take(url);
	}
	
}
