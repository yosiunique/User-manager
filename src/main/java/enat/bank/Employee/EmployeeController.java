package enat.bank.Employee;

import enat.bank.utils.Common;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.security.PublicKey;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("api/employee")
@RequiredArgsConstructor
public class EmployeeController implements Common<Employee ,String,String,Employee> {
    private final EmployeeService employeeService;
    @Override
    public ResponseEntity<Employee> create(Employee employee) {
        if(employeeService.findByEmployeeIdAndStatus(employee.getEmployeeId(),Status.ACTIVE).isPresent()) {
         Employee emp=employeeService.findByEmployeeIdAndStatus(employee.getEmployeeId(),employee.getStatus()).get();
            if (emp.getStatus().equals(Status.ACTIVE) ) {
                throw new RuntimeException("This Employee Already Created !");
            } else if(Objects.equals(emp.getLoanId(),employee.getLoanId())){
                throw new RuntimeException("This Employee Already Created !");
            }
            else {
                System.out.println("this Employee ID ..."+employee.getLoanId() +"....."+emp.getLoanId());
            }
        }

        return employeeService.create(employee);
    }

    @Override
    public ResponseEntity<Employee> update(Long id, Employee employee) {
        if(employeeService.findByEmployeeIdAndStatus(employee.getEmployeeId(),Status.ACTIVE).isPresent()) {
            Employee emp = employeeService.findByEmployeeIdAndStatus(employee.getEmployeeId(), Status.ACTIVE).get();
            if (emp.getStatus().equals(Status.ACTIVE) && employee.getStatus().equals(Status.ACTIVE) && emp.getId()!=employee.getId()) {
                throw new RuntimeException("You can't Update this loanId b/c one  Active per Loan ID!");
            }
        }
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

    @PostMapping(value = "/import-csv",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public  List<Employee> importCsv(@RequestParam("file") MultipartFile file){
        System.out.println("called !.....");
    return employeeService.importCsv(file) ;
    }


}
