package enat.bank.user;

import enat.bank.utils.CommonJpaRepo;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CommonJpaRepo<User, Long> {
    User findByUserName(String userName);

}
