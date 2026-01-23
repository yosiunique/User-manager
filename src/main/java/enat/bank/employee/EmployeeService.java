package enat.bank.employee;

import com.opencsv.CSVReader;
import enat.bank.utils.CommonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class EmployeeService extends CommonService<Employee ,Long, Employee> {
    private final  EmployeeRepository employeeRepository ;
    public EmployeeService(EmployeeRepository employeeRepository ){
        super(employeeRepository);
        this.employeeRepository = employeeRepository;
    }

    protected Optional<Employee>   findByEmployeeId(Long employeeId){

        return employeeRepository.findByEmployeeId(employeeId);
    }


    protected Employee updateEmployee(Long id, Employee employee) {
        Employee update = employeeRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Loan Not Found with ID: " + id));

              update.setMembershipId(employee.getMembershipId());
              update.setEmployeeId(update.getEmployeeId());
              update.setEmployeeFullName(update.getEmployeeFullName());

        return update;
    }



    public List<Employee> importCsv(MultipartFile file ){

        List<Employee> lsEmployee=new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String[] fields;
            boolean isFirst = true;
            int lineNumber = 0;

            while ((fields = reader.readNext()) != null) {
                lineNumber++;


                if (isFirst) {
                    isFirst = false;
                    continue; // skip headers
                }

              Employee employee =Employee.builder()
                      .employeeId(Long.valueOf(fields[0].trim()))
                      .membershipId(fields[1].trim())
                      .employeeFullName(fields[2])
                      .build();
                lsEmployee.add(employee);




            }

        } catch (Exception e) {
            throw new RuntimeException("this is :"+e.getMessage());
        }



    return employeeRepository.saveAll(lsEmployee);

    }


    private Double parseDoubleSafe(String value) {
        try {
            return Double.parseDouble(value.replace(",", "").trim());
        } catch (Exception e) {
            return 0.0;
        }
    }





   public Page<Employee> searchByEmployeeId(Long  employeeId , Pageable pageable){
        return employeeRepository.findByEmployeeIdContaining(employeeId ,pageable);
   }

   public Page<Employee> searchByName(String name, Pageable pageable) {
   return employeeRepository.findByEmployeeFullNameContaining(name, pageable);
   }







}





