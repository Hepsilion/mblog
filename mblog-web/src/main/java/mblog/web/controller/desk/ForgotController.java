package mblog.web.controller.desk;

import mblog.base.data.DataExt;
import mblog.base.email.EmailSender;
import mblog.base.lang.Consts;
import mblog.core.data.User;
import mblog.core.persist.service.UserService;
import mblog.core.persist.service.VerifyService;
import mblog.web.controller.BaseController;
import mtons.modules.pojos.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * @author langhsu on 2015/8/14.
 */
@Controller
@RequestMapping("/forgot")
public class ForgotController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private VerifyService verifyService;
    @Autowired
    private EmailSender emailSender;

    @RequestMapping("/apply")
    public String apply(String username, ModelMap model) {
        DataExt data = null;

        if (StringUtils.isNotBlank(username)) {
            User user = userService.getByUsername(username);

            if (user != null) {
                String code = verifyService.generateCode(user.getId(), Consts.VERIFY_FORGOT, user.getEmail());
                Map<String, Object> context = new HashMap<>();
                context.put("userId", user.getId());
                context.put("code", code);
                context.put("type", Consts.VERIFY_FORGOT);

                emailSender.sendTemplete(user.getEmail(), "找回密码", Consts.EMAIL_TEMPLATE_FORGOT, context);

                data = DataExt.success("邮件发送成功", Data.NOOP);

                model.put("data", data);
                return getView(Views.REG_RESULT);
            } else {
                data = DataExt.failure("查无此用户");
            }
        }
        model.put("data", data);
        return getView(Views.FORGOT_APPLY);
    }

    @RequestMapping("/reset")
    public String reset(Long userId, String token, String password, ModelMap model) {
        DataExt data;

        try {
            Assert.notNull(userId, "缺少必要的参数");
            Assert.hasLength(token, "缺少必要的参数");

            verifyService.verifyToken(userId, Consts.VERIFY_FORGOT, token);
            userService.updatePassword(userId, password);

            data = DataExt.success("恭喜您! 密码重置成功。", Data.NOOP);
            data.addLink("login", "去登陆");

        } catch (Exception e) {
            data = DataExt.failure(e.getMessage());
        }

        model.put("data", data);
        model.put("userId", userId);
        model.put("token", token);
        return getView(Views.REG_RESULT);
    }
}
