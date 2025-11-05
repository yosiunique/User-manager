package enat.bank.role;

import enat.bank.Utils.CommonJpaRepo;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends CommonJpaRepo<Roles ,Long > {
}
