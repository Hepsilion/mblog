package mblog.base.utils;

import mblog.base.context.AppContext;
import mtons.modules.utils.GMagickUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.im4java.core.IM4JavaException;
import java.io.IOException;
import mblog.base.context.SpringContextHolder;

/**
 * 图片处理工具
 * @author Beldon 2015/10/24
 */
public class ImageHandleUtils {

    private static AppContext appContext;

    static {
        appContext = SpringContextHolder.getBean(AppContext.class);
    }

    /**
     * 根据最大宽度图片压缩
     *
     * @param ori     原图位置
     * @param dest    目标位置
     * @param maxSize 指定压缩后最大边长
     * @return boolean
     * @throws IOException
     */
    public static boolean scaleImageByWidth(String ori, String dest, int maxSize) throws IOException, IM4JavaException, InterruptedException {
        String imageProcessor = appContext.getConfig().get("image_processor");
        if (imageProcessor != null && "Thumbnailator".equals(imageProcessor)) {
            ImageUtils.scaleImageByWidth(ori, dest, maxSize);
        }else {
            GMagickUtils.scaleImageByWidth(ori, dest, maxSize);
        }
        return true;
    }

    public static void scale(String ori, String dest, int width, int height) throws IOException, IM4JavaException, InterruptedException {
        String imageProcessor = appContext.getConfig().get("image_processor");
        if (imageProcessor != null && "Thumbnailator".equals(imageProcessor)) {
            ImageUtils.scale(ori, dest, width, height);
        } else{
            GMagickUtils.scale(ori, dest, width,height);
        }
        Thumbnails.of(ori).size(width, height).toFile(dest);
    }

    /**
     * 图片压缩,各个边按比例压缩
     *
     * @param ori     原图位置
     * @param dest    目标位置
     * @param maxSize 指定压缩后最大边长
     * @return boolean
     * @throws IOException
     */
    public static boolean scaleImage(String ori, String dest, int maxSize) throws IOException, IM4JavaException, InterruptedException {
        String imageProcessor = appContext.getConfig().get("image_processor");
        if (imageProcessor != null && "Thumbnailator".equals(imageProcessor)) {
            ImageUtils.scaleImage(ori, dest, maxSize);
        }else {
            GMagickUtils.scaleImage(ori, dest, maxSize);
        }
        return true;
    }

    /**
     * 裁剪图片
     *
     * @param ori  源图片路径
     * @param dest 处理后图片路径
     * @param x    起始X坐标
     * @param y    起始Y坐标
     * @param width  裁剪宽度
     * @param height  裁剪高度
     * @return boolean
     *
     * @throws java.io.IOException io异常
     * @throws IM4JavaException    im4j 异常
     * @throws InterruptedException 中断异常
     */
    public static boolean truncateImage(String ori, String dest, int x, int y, int width, int height) throws IOException, InterruptedException, IM4JavaException {
        String imageProcessor = appContext.getConfig().get("image_processor");
        if (imageProcessor != null && "Thumbnailator".equals(imageProcessor)) {
            return ImageUtils.truncateImage(ori, dest, x, y, width, height);
        }else {
            return GMagickUtils.truncateImage(ori, dest, x, y, width, height);
        }
    }

    public static boolean truncateImage(String ori, String dest, int x, int y, int size) throws IOException, InterruptedException, IM4JavaException {
        String imageProcessor = appContext.getConfig().get("image_processor");
        if (imageProcessor != null && "Thumbnailator".equals(imageProcessor)) {
            return ImageUtils.truncateImage(ori, dest, x, y, size);
        }else {
            return GMagickUtils.truncateImage(ori, dest, x, y, size);
        }
    }
}
