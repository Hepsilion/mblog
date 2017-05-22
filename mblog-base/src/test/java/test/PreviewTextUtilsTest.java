package test;

import mtons.modules.utils.PreviewTextUtils;

/**
 * @author langhsu on 2015/8/29.
 */
public class PreviewTextUtilsTest {

    public static void main(String[] args) {
        String html = "<p>这是一个测试文本</p><p><img src='test.png'></p>";

        System.out.println(PreviewTextUtils.getText(html, 5));

        System.out.println(PreviewTextUtils.getImgSrc(html));
    }

}
