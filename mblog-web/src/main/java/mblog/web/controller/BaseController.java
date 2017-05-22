/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.web.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import mblog.base.context.AppContext;
import mblog.base.upload.FileRepoFactory;
import mblog.core.data.AccountProfile;
import mblog.core.data.Attach;
import mblog.shiro.authc.AccountSubject;
import mtons.modules.pojos.Paging;
import mtons.modules.security.MD5;

/**
 * Controller 基类
 * 
 * @author langhsu
 * 
 */
public class BaseController {
	@Autowired
	protected HttpSession session;
	@Autowired
	protected AppContext appContext;
	@Autowired
	protected FileRepoFactory fileRepoFactory;

	/**
	 * 获取登录信息
	 * 
	 * @return
	 */
	protected AccountSubject getSubject(){
	    return (AccountSubject) SecurityUtils.getSubject();
	}
	
	protected void putProfile(AccountProfile profile) {
		SecurityUtils.getSubject().getSession(true).setAttribute("profile", profile);
	}

	protected AuthenticationToken createToken(String username, String password) {
		return new UsernamePasswordToken(username, MD5.md5(password));
	}

	/**
	 * 包装分页对象
	 * 
	 * @param pn 页码
	 * @return
	 */
	protected Paging wrapPage(Integer pn) {
		if (pn == null || pn == 0) {
			pn = 1;
		}
		return wrapPage(pn, 10);
	}

	/**
	 * 包装分页对象
	 *
	 * @param pn 页码
	 * @param pn 页码
	 * @return
	 */
	protected Paging wrapPage(Integer pn, Integer maxResults) {
		if (pn == null || pn == 0) {
			pn = 1;
		}
		if (maxResults == null || maxResults == 0) {
			maxResults = 10;
		}
		return new Paging(pn, maxResults);
	}

	protected String getSuffix(String name) {
		int pos = name.lastIndexOf(".");
		return name.substring(pos);
	}

	public String toJson(Object obj) {
		return new Gson().toJson(obj);
	}

	protected String getView(String view) {
		return "/default" + view;
	}
	
	protected String routeView(String route, String group) {
		String format = "/default" + route;
		return String.format(format, group);
	}

	public static String getIpAddr(HttpServletRequest request) throws Exception {
		String ip = request.getHeader("X-Real-IP");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			int index = ip.indexOf(',');
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		} else {
			return request.getRemoteAddr();
		}
	}
	
	protected List<Attach> handleAlbums(String[] albums) {
		if (albums == null || albums.length == 0) {
			return Collections.emptyList();
		}

		List<Attach> rets = new ArrayList<>();

		for (String album : albums) {
			if (StringUtils.isBlank(album)) {
				continue;
			}

			String root = fileRepoFactory.select().getRoot();
			File temp = new File(root + album);
			Attach item = new Attach();
			try {
				// 保存原图
				String orig = fileRepoFactory.select().storeScale(temp, appContext.getOrigDir(), 750);
				item.setOriginal(orig);

				// 创建缩放图片
				String preview = fileRepoFactory.select().storeScale(temp, appContext.getThumbsDir(), 305);
				item.setPreview(preview);

				// 创建快照
				String screenshot = fileRepoFactory.select().storeScale(temp, appContext.getScreenshotDir(), 225, 140);
				item.setScreenshot(screenshot);

				rets.add(item);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (temp != null) {
					temp.delete();
				}
			}
		}

		return rets;
	}
	
}
