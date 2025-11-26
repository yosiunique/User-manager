package enat.bank.Employee;

import enat.bank.utils.CommonJpaRepo;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends CommonJpaRepo<Employee ,Long> {

}
