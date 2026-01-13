package enat.bank.employee;

import enat.bank.utils.CommonJpaRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends CommonJpaRepo<Employee ,Long> {
    Optional<Employee> findByEmployeeId(Long employeeId);
    @Query("SELECT e FROM  Employee e WHERE e.employeeId=:employeeId")
    Page<Employee> findByEmployeeIdContaining(@Param("employeeId") Long  employeeId, Pageable pageable);

}
