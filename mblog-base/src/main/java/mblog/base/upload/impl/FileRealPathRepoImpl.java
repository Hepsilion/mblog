/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.base.upload.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;

import mblog.base.utils.ImageHandleUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

import mblog.base.context.AppContext;
import mblog.base.utils.ImageUtils;
import mtons.modules.utils.FileNameUtils;
import mtons.modules.utils.GMagickUtils;

/**
 * @author langhsu
 *
 */
@Service("fileRealPathRepo")
public class FileRealPathRepoImpl extends AbstractFileRepo implements ServletContextAware {
	private Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private AppContext appContext;
	
	private ServletContext context;

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.context = servletContext;
	}
	
	@Override
	public String temp(MultipartFile file, String basePath) throws IOException {
		validateFile(file);
		
		String root = context.getRealPath("/");
		
		String name = FileNameUtils.genFileName(getExt(file.getOriginalFilename()));
		String path = basePath + "/" + name;
		File temp = new File(root + path);
		checkDirAndCreate(temp);
		file.transferTo(temp);
		return path;
	}
	
	@Override
	public String tempScale(MultipartFile file, String basePath, int maxWidth) throws Exception {
		validateFile(file);
		
		String root = context.getRealPath("/");
		
		String name = FileNameUtils.genFileName(getExt(file.getOriginalFilename()));
		String path = basePath + "/" + name;
		
		// 存储临时文件
		File temp = new File(root + path);
		checkDirAndCreate(temp);
		
		try {
			file.transferTo(temp);
			
			// 根据临时文件生成略缩图
			String scaleName = FileNameUtils.genFileName(getExt(file.getOriginalFilename()));
			String dest = root + basePath + "/" + scaleName;

			ImageHandleUtils.scaleImageByWidth(temp.getAbsolutePath(), dest, maxWidth);

			path = basePath + "/" + scaleName;
		} catch (Exception e) {
			throw e;
		} finally {
			if (temp != null) {
				temp.delete();
			}
		}
		return path;
	}
	
	@Override
	public String store(MultipartFile file, String basePath) throws IOException {
		validateFile(file);
		
		String root = context.getRealPath("/");
		
		String path = FileNameUtils.genPathAndFileName(getExt(file.getOriginalFilename()));
		
		File temp = new File(root + basePath + path);
		checkDirAndCreate(temp);
		file.transferTo(temp);
		return basePath + path;
	}
	
	@Override
	public String store(File file, String basePath) throws IOException {
		String root = context.getRealPath("/");
		
		String path = FileNameUtils.genPathAndFileName(getExt(file.getName()));
		
		File dest = new File(root + basePath + path);
		checkDirAndCreate(dest);
		FileUtils.copyDirectory(file, dest);
		return basePath + path;
	}

	@Override
	public String storeScale(MultipartFile file, String basePath, int maxWidth) throws Exception {
		validateFile(file);
		
		String root = context.getRealPath("/");
		
		String path = FileNameUtils.genPathAndFileName(getExt(file.getOriginalFilename()));
		
		File temp = new File(root + appContext.getTempDir() + path);
		checkDirAndCreate(temp);
		
		try {
			file.transferTo(temp);
			
			// 根据临时文件生成略缩图
			String dest = root + basePath + path;
			ImageHandleUtils.scaleImageByWidth(temp.getAbsolutePath(), dest, maxWidth);

		} catch (Exception e) {
			throw e;
		} finally {
			temp.delete();
		}
		return basePath + path;
	}

	@Override
	public String storeScale(File file, String basePath, int maxWidth) throws Exception {
		String root = context.getRealPath("/");
		String path = FileNameUtils.genPathAndFileName(getExt(file.getName()));
		
		String dest = root + basePath + path;
		ImageHandleUtils.scaleImageByWidth(file.getAbsolutePath(), dest, maxWidth);

		return basePath + path;
	}

	@Override
	public String storeScale(File file, String basePath, int width, int height) throws Exception {
		String root = context.getRealPath("/");
		String path = FileNameUtils.genPathAndFileName(getExt(file.getName()));

		String dest = root + basePath + path;
		ImageUtils.truncateImage(file.getAbsolutePath(), dest, width, height);
		return basePath + path;
	}

	@Override
	public int[] imageSize(String storePath) {
		String root = context.getRealPath("/");
		
		File dest = new File(root + storePath);
		int[] ret = new int[2];

		try {
			// 读入文件
			BufferedImage src = ImageIO.read(dest);
			int w = src.getWidth();
			int h = src.getHeight();
			
			ret = new int[] {w, h};
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return ret;
	}

	@Override
	public String getRoot() {
		return context.getRealPath("/");
	}

	@Override
	public void deleteFile(String storePath) {
		String root = context.getRealPath("/");

		File file = new File(root + storePath);

		// 文件存在, 且不是目录
		if (file.exists() && !file.isDirectory()) {
			file.delete();
			log.info("fileRepo delete " + storePath);
		}
	}

	@Override
	public String getKey() {
		return "relative";
	}

}
