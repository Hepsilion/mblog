package mblog.web.tools;

import java.util.List;

import org.apache.velocity.tools.Scope;
import org.apache.velocity.tools.config.DefaultKey;
import org.apache.velocity.tools.config.ValidScope;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import mblog.core.data.AuthMenu;
import mblog.core.persist.service.AuthMenuService;

/**
 * Created by 011938 on 2015/9/23.
 */
@DefaultKey("auth")
@ValidScope(Scope.APPLICATION)
public class AuthMenuTool {

    public List<AuthMenu> findByParentId(long pid) {
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();

        AuthMenuService authMenuService = (AuthMenuService) wac.getBean(AuthMenuService.class);
        List<AuthMenu> list = authMenuService.findByParentId(pid);
        return list;
    }

}
