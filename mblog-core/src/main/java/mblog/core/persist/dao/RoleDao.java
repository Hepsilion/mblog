package mblog.core.persist.dao;

import java.util.List;

import mblog.core.persist.entity.RolePO;
import mtons.modules.persist.BaseRepository;
import mtons.modules.pojos.Paging;

public interface RoleDao extends BaseRepository<RolePO>{

	List<RolePO> paging(Paging paging, String key);

}
