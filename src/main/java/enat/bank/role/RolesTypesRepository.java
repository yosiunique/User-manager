package enat.bank.role;

import enat.bank.utils.CommonJpaRepo;

public interface RolesTypesRepository extends CommonJpaRepo<RoleTypes ,Long> {
    RoleTypes findByRole(String role);
}
