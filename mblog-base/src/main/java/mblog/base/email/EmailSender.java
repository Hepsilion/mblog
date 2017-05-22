/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.base.email;

import java.util.Map;

/**
 * @author langhsu on 2015/8/14.
 */
public interface EmailSender {

    void sendTemplete(String address, String subject, String template, Map<String, Object> data);

    void sendText(String address, String subject, String content, boolean html);
}
