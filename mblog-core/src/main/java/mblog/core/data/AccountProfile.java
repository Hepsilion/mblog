/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.core.data;

import java.util.List;

import mtons.modules.pojos.UserProfile;

/**
 * @author langhsu
 *
 */
public class AccountProfile extends UserProfile {
    private static final long serialVersionUID = 1748764917028425871L;

    private int roleId;
    private int activeEmail;

    private List<AuthMenu> authMenus;

    private BadgesCount badgesCount;

    public AccountProfile(long id, String username) {
        super(id, username);
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public List<AuthMenu> getAuthMenus() {
        return authMenus;
    }

    /**
     * 我也是拼了。。。冒泡排序法居然还能默写出来
     * @param authMenus
     */
    public void setAuthMenus(List<AuthMenu> authMenus) {
        for (int i = 0; i < authMenus.size(); i++) {
            for (int j = authMenus.size() - 1; j > 0; j--) {
                if (authMenus.get(i).getSort() > authMenus.get(j).getSort()) {
                    AuthMenu temp = authMenus.get(i);
                    authMenus.set(i, authMenus.get(j));
                    authMenus.set(j, temp);
                }
            }
        }
        this.authMenus = authMenus;
    }

    public int getActiveEmail() {
        return activeEmail;
    }

    public void setActiveEmail(int activeEmail) {
        this.activeEmail = activeEmail;
    }

    public BadgesCount getBadgesCount() {
        return badgesCount;
    }

    public void setBadgesCount(BadgesCount badgesCount) {
        this.badgesCount = badgesCount;
    }
}
