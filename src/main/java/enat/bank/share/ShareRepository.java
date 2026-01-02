package enat.bank.share;

import enat.bank.utils.CommonJpaRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShareRepository extends CommonJpaRepo<Share, Long> {

    Optional<Share> findByEmployee_EmployeeId(Long employeeId);

    @Query("SELECT s FROM Share s  where s.employee.employeeId =:employeeId ")
    Page<Share> findByEmployeeEmployeeIdContaining(@Param("employeeId") Long employeeId, Pageable pageable);
}
