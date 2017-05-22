/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.core.persist.dao.impl;

import mblog.core.persist.dao.OpenOauthDao;
import mblog.core.persist.entity.OpenOauthPO;
import mtons.modules.annotation.Repository;
import mtons.modules.persist.impl.BaseRepositoryImpl;

/**
 * @author langhsu on 2015/8/12.
 */
@Repository(entity = OpenOauthPO.class)
public class OpenOauthDaoImpl extends BaseRepositoryImpl<OpenOauthPO> implements OpenOauthDao {
    private static final long serialVersionUID = 3953147809633630305L;

    @Override
    public OpenOauthPO getOauthToken(String accessToken) {
        return findUniqueBy("accessToken", accessToken);
    }

    @Override
    public OpenOauthPO getOauthToken(long userId) {
        return findUniqueBy("userId", userId);
    }

	@Override
	public OpenOauthPO getOauthUserId(String oauthUserId) {
		return findUniqueBy("oauthUserId", oauthUserId);
	}

}
