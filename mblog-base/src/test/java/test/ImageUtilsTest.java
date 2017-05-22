package test;

import org.im4java.core.IM4JavaException;

import mblog.base.utils.ImageUtils;

import java.io.IOException;

/**
 * @author langhsu on 2015/9/4.
 */
public class ImageUtilsTest {
    public static void main(String[] args) throws InterruptedException, IOException, IM4JavaException {
        String ori = "F:\\data\\go\\ns10.jpg";
        ImageUtils.truncateImage(ori, "F:\\data\\go\\boot_640x960.jpg", 640, 960);
        ImageUtils.truncateImage(ori, "F:\\data\\go\\boot_640x1136.jpg", 640, 1136);
        ImageUtils.truncateImage(ori, "F:\\data\\go\\boot_750x1334.jpg", 750, 1334);
        ImageUtils.truncateImage(ori, "F:\\data\\go\\boot_1242x2208.jpg", 1242, 2208);
    }
}
