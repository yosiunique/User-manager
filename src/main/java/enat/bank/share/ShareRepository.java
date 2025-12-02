package enat.bank.share;

import enat.bank.utils.CommonJpaRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShareRepository extends CommonJpaRepo<Share, Long> {

    Optional<Share> findByEmployeeId(Long employeeId);
}
