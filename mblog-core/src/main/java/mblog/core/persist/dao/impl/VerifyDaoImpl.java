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

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import mblog.core.persist.dao.VerifyDao;
import mblog.core.persist.entity.VerifyPO;
import mtons.modules.annotation.Repository;
import mtons.modules.persist.impl.BaseRepositoryImpl;

/**
 * @author langhsu on 2015/8/14.
 */
@Repository(entity = VerifyPO.class)
public class VerifyDaoImpl extends BaseRepositoryImpl<VerifyPO> implements VerifyDao  {
	private static final long serialVersionUID = -8077903744203247894L;

	@Override
    public VerifyPO get(long userId, int type) {
        Criteria c = createCriteria();
        c.add(Restrictions.eq("userId", userId));
        c.add(Restrictions.eq("type", type));
        return (VerifyPO) c.uniqueResult();
    }

    @Override
    public VerifyPO getByUserId(long userId) {
        return findUniqueBy("userId", userId);
    }
}
