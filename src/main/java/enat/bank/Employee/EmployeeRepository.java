package enat.bank.Employee;

import enat.bank.utils.CommonJpaRepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends CommonJpaRepo<Employee ,Long> {
    Optional<Employee> findByEmployeeId(Long employeeId);
}
