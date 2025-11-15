package enat.bank.role.roleTypes;

import enat.bank.role.RoleTypes;
import enat.bank.role.RolesTypesRepository;
import enat.bank.utils.CommonService;
import org.springframework.stereotype.Service;

@Service
public class RoleTypesServices extends CommonService<RoleTypes,Long,RoleTypes> {
    private RolesTypesRepository rolesTypesRepository;
    protected RoleTypesServices(RolesTypesRepository rolesTypesRepository){
        super(rolesTypesRepository);
        this.rolesTypesRepository=rolesTypesRepository;

    }

   public Boolean isRole(String roleName){

        RoleTypes roleTypes=this.rolesTypesRepository.findByRole(roleName);
        if(roleTypes!=null){
            return  true;

        }

        return  false;
   }


}
