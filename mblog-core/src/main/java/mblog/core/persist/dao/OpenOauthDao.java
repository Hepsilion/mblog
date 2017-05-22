/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.core.persist.dao;

import mblog.core.persist.entity.OpenOauthPO;
import mtons.modules.persist.BaseRepository;

/**
 * 第三方开发授权登录
 * @author langhsu on 2015/8/12.
 */
public interface OpenOauthDao extends BaseRepository<OpenOauthPO> {
    OpenOauthPO getOauthToken(String accessToken);

    OpenOauthPO getOauthUserId(String oauthUserId);
    
    OpenOauthPO getOauthToken(long userId);
}
