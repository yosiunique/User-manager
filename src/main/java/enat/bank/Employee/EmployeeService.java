package enat.bank.Employee;

import enat.bank.utils.CommonService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService extends CommonService<Employee ,Long, Employee> {
    private final  EmployeeRepository employeeRepository ;
    public EmployeeService(EmployeeRepository employeeRepository){
        super(employeeRepository);
        this.employeeRepository = employeeRepository;
    }

    protected Employee updateEmployee(Long id ,Employee employee)
    {
        Employee update=employeeRepository.findById(id).orElseThrow(()->
                new RuntimeException("Employee Not Found with  this ID:"+id)
        );
        update.setEffectiveDate(employee.getEffectiveDate());
        update.setEmployeeFullName(employee.getEmployeeFullName());
        update.setOutStanding(employee.getOutStanding());
        update.setStatus(employee.getStatus());
        update.setLoanId(employee.getLoanId());
        return update ;
    }


    public Optional<Employee>   findByEmployeeId(Long  employeeId){

        return  employeeRepository.findById(employeeId);

    }
}
