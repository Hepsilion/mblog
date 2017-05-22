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

import mblog.base.utils.ImageHandleUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import mblog.base.context.AppContext;
import mblog.base.utils.ImageUtils;
import mtons.modules.utils.FileNameUtils;

/**
 * @author langhsu
 *
 */
@Service("fileRepo")
public class FileRepoImpl extends AbstractFileRepo {
	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private AppContext appContext;

	@Override
	public String temp(MultipartFile file, String basePath) throws IOException {
		validateFile(file);
		
		String root = appContext.getRoot();
		
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
		
		String root = appContext.getRoot();
		
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
		
		String realPath = appContext.getRoot();
		
		String path = FileNameUtils.genPathAndFileName(getExt(file.getOriginalFilename()));
		
		File temp = new File(realPath + basePath + path);
		checkDirAndCreate(temp);
		file.transferTo(temp);
		return basePath + path;
	}
	
	@Override
	public String store(File file, String basePath) throws IOException {
		String root = appContext.getRoot();
		
		String path = FileNameUtils.genPathAndFileName(getExt(file.getName()));
		
		File dest = new File(root + basePath + path);
		checkDirAndCreate(dest);
		FileUtils.copyDirectory(file, dest);
		return basePath + path;
	}

	@Override
	public String storeScale(MultipartFile file, String basePath, int maxWidth) throws Exception {
		validateFile(file);
		
		String realPath = appContext.getRoot();
		
		String path = FileNameUtils.genPathAndFileName(getExt(file.getOriginalFilename()));
		
		File temp = new File(realPath + appContext.getTempDir() + path);
		checkDirAndCreate(temp);
		try {
			file.transferTo(temp);
			
			// 根据临时文件生成略缩图
			String dest = realPath + basePath + path;

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
		String root = appContext.getRoot();

		String path = FileNameUtils.genPathAndFileName(getExt(file.getName()));
		
		String dest = root + basePath + path;

		ImageHandleUtils.scaleImageByWidth(file.getAbsolutePath(), dest, maxWidth);
		return basePath + path;
	}

	@Override
	public String storeScale(File file, String basePath, int width, int height) throws Exception {
		String root = appContext.getRoot();

		String path = FileNameUtils.genPathAndFileName(getExt(file.getName()));

		String dest = root + basePath + path;
		ImageUtils.truncateImage(file.getAbsolutePath(), dest, width, height);
		return basePath + path;
	}

	@Override
	public int[] imageSize(String storePath) {
		String root = appContext.getRoot();
		
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
		return appContext.getRoot();
	}

	@Override
	public void deleteFile(String storePath) {
		String root = appContext.getRoot();

		File file = new File(root + storePath);

		// 文件存在, 且不是目录
		if (file.exists() && !file.isDirectory()) {
			file.delete();
			log.info("fileRepo delete " + storePath);
		}
	}
	
	@Override
	public String getKey() {
		return "absolute";
	}
}
