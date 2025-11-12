package enat.bank.role.roleTypes;

import enat.bank.role.RoleTypes;
import enat.bank.role.RolesTypesRepository;
import enat.bank.utils.CommonService;
import org.springframework.stereotype.Service;

@Service
public class RoleTypesServices extends CommonService<RoleTypes,Long,RoleTypes> {
    protected RoleTypesServices(RolesTypesRepository rolesTypesRepository){
        super(rolesTypesRepository);
    }
}
