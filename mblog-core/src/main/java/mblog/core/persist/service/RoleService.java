package mblog.core.persist.service;

import java.util.List;

import mblog.core.data.Role;
import mtons.modules.pojos.Paging;

public interface RoleService {
	
	void paging(Paging paging, String key);
	
	Role get(Long id);
	
	void save(Role role);
	
	void delete(Long id);
	
	List<Role> getAll();

}
