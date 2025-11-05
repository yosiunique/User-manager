package enat.bank.User;

import enat.bank.Utils.CommonJpaRepo;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CommonJpaRepo<User, Long> {
    User findByUserName(String userName);

}
