/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.base.email.impl;

import java.util.Map;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.StringUtils;

import mblog.base.context.AppContext;
import mblog.base.email.EmailEngine;
import mblog.base.email.EmailSender;
import mblog.base.lang.SiteConfig;
import mtons.modules.exception.MtonsException;

/**
 * @author langhsu on 2015/8/14.
 */
@Service
@Lazy(false)
public class EmailSenderImpl implements EmailSender {
    private Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private EmailEngine emailEngine;
    @Autowired
    private AppContext appContext;

    // 发送器
    private JavaMailSenderImpl sender;
    private String domain;
    private boolean inited = false;

    @Override
    public void sendTemplete(String address, String subject, String template, Map<String, Object> data) {
        data.put("domain", getDomain());
        final String html = VelocityEngineUtils.mergeTemplateIntoString(emailEngine.getEngine(), template, "UTF-8", data);

        sendText(address, subject, html, true);
    }

    @Override
    public void sendText(String address, String subject, String content, boolean html) {
        init();
        MimeMessage msg = sender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(msg, true, "UTF-8");
            message.setFrom(sender.getUsername());
            message.setSubject(subject);
            message.setTo(address);
            message.setText(content, html);

            new Thread(() -> {
                sender.send(msg);
            }).start();

        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    public void init() {
        // 如果加载完毕直接返回
        if (inited) {
            return;
        }
        String host = appContext.getConfig().get(SiteConfig.SITE_MAIL_HS);
        String username = appContext.getConfig().get(SiteConfig.SITE_MAIL_UN);
        String password = appContext.getConfig().get(SiteConfig.SITE_MAIL_PW);

        if (StringUtils.isEmpty(host) || StringUtils.isEmpty(username) ||  StringUtils.isEmpty(password)) {
            throw new MtonsException(" 系统配置中的 mail.* 相关配置不完整, 不能正常使用邮件服务!");
        }

        sender = new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setDefaultEncoding("UTF-8");
        sender.setUsername(username);
        sender.setPassword(password);

        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.timeout", "25000");

        sender.setJavaMailProperties(props);

        // 标记加载完毕
        inited = true;
    }

    private String getDomain() {
        if (domain == null) {
            domain = appContext.getConfig().get(SiteConfig.SITE_DOMAIN);
            if (domain.endsWith("/")) {
                domain = domain.substring(0, domain.lastIndexOf("/"));
            }
        }
        return  domain;
    }
}
