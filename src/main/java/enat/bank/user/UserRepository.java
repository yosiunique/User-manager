package enat.bank.user;

import enat.bank.utils.CommonJpaRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CommonJpaRepo<User, Long> {
    User findByUserName(String userName);
    @Query("""
    SELECT u
    FROM User u
    WHERE LOWER(u.userName) LIKE LOWER(CONCAT('%', :username, '%'))
    """)
     Page<User> findByUserNameContainingIgnoreCase(@Param("username") String username, Pageable pageable);

}
