package enat.bank.employee;

import enat.bank.utils.Common;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/employee")
@RequiredArgsConstructor
public class EmployeeController implements Common<Employee ,String,String,Employee> {
    private final EmployeeService employeeService;
    @Override
    public ResponseEntity<Employee> create(Employee employee) {
        if(employeeService.findByEmployeeId(employee.getEmployeeId()).isPresent()) {

                throw new RuntimeException("This Employee Already Created !");
        }

        return employeeService.create(employee);
    }

    @Override
    public ResponseEntity<Employee> update(Long id, Employee employee) {
        if(employeeService.findByEmployeeId(employee.getEmployeeId()).isEmpty()) {
                throw new RuntimeException("Employee not Found with this ID:"+employee.getEmployeeId());

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
        return employeeService.getAllPageable(pageable ,"employeeFullName");
    }



    @PostMapping(value = "/import-csv",

            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public  List<Employee> importCsv(@RequestParam("file") MultipartFile file ){

    return employeeService.importCsv(file) ;
    }




    @GetMapping("/search-by-name/{name}")
    public Page<Employee> searchByName(
            @PathVariable("name") String name,
            Pageable pageable) {
        return employeeService.searchByName(name, pageable);
    }


    @GetMapping("/search-by-employee-id/{employeeId}")
    public Page<Employee> searchByEmployeeId(
            @PathVariable("employeeId") Long employeeId,
            Pageable pageable) {
        return employeeService.searchByEmployeeId(employeeId, pageable);
    }


}
