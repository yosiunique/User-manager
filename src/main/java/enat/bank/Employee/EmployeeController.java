package enat.bank.Employee;

import enat.bank.utils.Common;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/employee")
@RequiredArgsConstructor
public class EmployeeController implements Common<Employee ,String,String,Employee> {
    private final EmployeeService employeeService;
    @Override
    public ResponseEntity<Employee> create(Employee employee) {
        if(employeeService.findByEmployeeIdAndStatus(employee.getEmployeeId(),employee.getStatus()).isPresent()) {

            throw new RuntimeException("This Employee Already Created !");
        }

        return employeeService.create(employee);
    }

    @Override
    public ResponseEntity<Employee> update(Long id, Employee employee) {

        return employeeService.update(employeeService.updateEmployee(id , employee),id);
    }

    @Override
    public void delete(Long id) {
        employeeService.delete(id);

    }

    @Override
    public Optional<Employee> getById(Long id) {
        return employeeService.getById(id);
    }

    @Override
    public Page<Employee> getAllPageable(Pageable pageable, String name) {
        return employeeService.getAllPageable(pageable);
    }

    @GetMapping("/{employeeId}")
    public List<Employee> getByEmployeeId(@PathVariable("employeeId")Long employeeId){

        return employeeService.findByEmployeeId(employeeId);
    }


}
