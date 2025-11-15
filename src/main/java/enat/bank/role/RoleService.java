package enat.bank.role;


import enat.bank.utils.CommonService;
import enat.bank.exception.RolesNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class RoleService extends CommonService<Roles ,Long ,Roles> {
    private final RolesRepository rolesRepository;
    public RoleService(RolesRepository rolesRepository){
        super(rolesRepository);
        this.rolesRepository=rolesRepository;

    }

    protected Roles update(Long id ,Roles update){
        Roles exist=this.rolesRepository.findById(id).orElseThrow(()->
                new RolesNotFoundException("there is no role within this Id:.."+id));

        exist.setRoleTypes(update.getRoleTypes());
        exist.setUser(update.getUser());
        return exist;
    }

    Boolean isAssigned(Long roleId,Long userId){
        Roles roles=this.rolesRepository.findByRoleTypes_IdAndUser_Id(roleId ,userId);

        if(roles !=null){
            return  true;

        }

        return  false;
    }



}
