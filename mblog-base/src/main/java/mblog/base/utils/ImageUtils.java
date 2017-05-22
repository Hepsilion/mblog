package mblog.base.utils;

import mtons.modules.utils.GMagickUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.log4j.Logger;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author langhsu on 2015/9/4.
 */
public class ImageUtils extends GMagickUtils {
    private static Logger log = Logger.getLogger(ImageUtils.class);

    public static boolean truncateImage(String ori, String dest, int width, int height) throws IOException, InterruptedException, IM4JavaException {
        File oriFile = new File(ori);

        validate(oriFile, dest);

        BufferedImage src = ImageIO.read(oriFile); // 读入文件
        int w = src.getWidth();
        int h = src.getHeight();

        int min = Math.min(w, h);
        int side = Math.min(width, height);

        IMOperation op = new IMOperation();
        op.addImage(ori);

        if (w <= width && h <= height) {
            // Don't do anything
        } else if (min < side) {
            op.gravity("center").extent(width, height);
        } else {
            op.resize(width, height, '^').gravity("center").extent(width, height);
        }

        op.addImage(dest);
        ConvertCmd convert = new ConvertCmd(true);
        convert.run(op);
        return true;
    }

    /**
     * 下载远程图片到本地，用于第三方登录下载头像
     * @param urlString		图片链接
     * @param savePath		保存路径
     * @throws Exception
     * @author A蛋壳  2015年9月13日 上午9:40:17
     */
    public static void download(String urlString, String savePath) throws Exception {

        URL url = new URL(urlString);	// 构造URL
        URLConnection connection = url.openConnection();	// 打开连接
        connection.setConnectTimeout(5 * 1000);		// 设置请求超时时间
        InputStream is = connection.getInputStream();	// 输入流

        byte[] bs = new byte[1024];		// 1K的数据缓存
        int len;
        File sf = new File(savePath);
        if (sf.getParentFile() != null && sf.getParentFile().exists() == false) {
            if (sf.getParentFile().mkdirs() == false) {
                throw new IOException("Destination '" + savePath + "' directory cannot be created");
            }
        }

        OutputStream os = new FileOutputStream(savePath);
        while((len = is.read(bs)) != -1){
            os.write(bs, 0, len);
        }
        os.close();
        is.close();
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
    public static boolean scaleImageByWidth(String ori, String dest, int maxSize) throws IOException {
        File oriFile = new File(ori);
        GMagickUtils.validate(oriFile, dest);

        BufferedImage src = ImageIO.read(oriFile); // 读入文件
        int w = src.getWidth();
        int h = src.getHeight();

        log.debug("origin with/height " + w + "/" + h);

        int size = (int) Math.max(w, h);
        int tow = w;
        int toh = h;

        if (size > maxSize) {
            if (w > maxSize) {
                tow = maxSize;
                toh = h * maxSize / w;
            } else {
                tow = w * maxSize / h;
                toh = maxSize;
            }
        }
        scale(ori, dest, tow, toh);
        return true;
    }

    public static void scale(String ori, String dest, int width, int height) throws IOException {
        File destFile = new File(dest);
        if (destFile.exists()) {
            destFile.delete();
        }
        log.debug("scaled with/height : " + width + "/" + height);
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
    public static boolean scaleImage(String ori, String dest, int maxSize) throws IOException {
        File oriFile = new File(ori);
        validate(oriFile, dest);

        BufferedImage src = ImageIO.read(oriFile); // 读入文件
        int w = src.getWidth();
        int h = src.getHeight();

        log.debug("origin with/height " + w + "/" + h);

        int size = (int) Math.max(w, h);
        int tow = w;
        int toh = h;

        if (size > maxSize) {
            if (w > maxSize) {
                tow = maxSize;
                toh = h * maxSize / w;
            } else {
                tow = w * maxSize / h;
                toh = maxSize;
            }
        }

        log.debug("scaled with/height : " + tow + "/" + toh);

        scale(ori, dest, tow, toh);

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
        File oriFile = new File(ori);

        validate(oriFile, dest);

        Thumbnails.of(ori).sourceRegion(x, y, width, height).size(width,height).keepAspectRatio(false).toFile(dest);

        return true;
    }

    public static boolean truncateImage(String ori, String dest, int x, int y, int size) throws IOException, InterruptedException, IM4JavaException {
        return truncateImage(ori, dest, x, y, size, size);
    }

}
