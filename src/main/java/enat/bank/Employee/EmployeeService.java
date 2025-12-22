package enat.bank.Employee;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import enat.bank.exception.LoanRepaymentsException;
import enat.bank.exception.SavingAndLoanRepaymentSaveFileException;
import enat.bank.loansRepayments.LoanRepayment;
import enat.bank.loansRepayments.LoanRepaymentDetails;
import enat.bank.utils.CommonService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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


    public Optional<Employee>   findByEmployeeIdAndStatus(Long  employeeId ,Status status){

        return  employeeRepository.findByEmployeeIdAndStatus(employeeId ,status);

    }
    public List<Employee> findByEmployeeId(Long employeeId){

        return  employeeRepository.findByEmployeeId(employeeId);
    }



   public List<Employee> importCsv(MultipartFile file){
        List<Employee> lsEmployee=new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
           String[] fields;
           boolean isFirst = true;
           int lineNumber = 0;

           while ((fields = reader.readNext()) != null) {
               lineNumber++;
//               if (fields.length <7) {
//                   System.err.println("Skipping invalid line: " + lineNumber);
//                   continue;
//               }

               if (isFirst) {

                   isFirst = false;
                   continue; // skip headers
               }

               DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
               Employee employee=Employee.builder()
                       .employeeId(Long.valueOf(fields[0].trim()))
                       .membershipId(fields[1].trim())
                       .loanId(fields[2].trim())
                       .employeeFullName(fields[3].trim())
                       .effectiveDate(LocalDate.parse(fields[4].trim(),formatter))
                       .outStanding((parseDoubleSafe(fields[5])))
                       .firstOutStanding(parseDoubleSafe(fields[5]))
                       .emi(0.0)
                       .annualInterest(0.07)
                       .period(60)
                       .status(Status.ACTIVE)
                       .build();

               lsEmployee.add(employee);

           }

       return   employeeRepository.saveAll(lsEmployee);



       } catch (IOException | CsvValidationException e) {
           throw new SavingAndLoanRepaymentSaveFileException("Failed to read CSV file: " + e.getMessage());
       }
   }


    private Double parseDoubleSafe(String value) {
        try {
            return Double.parseDouble(value.replace(",", "").trim());
        } catch (Exception e) {
            return 0.0;
        }
    }


   }





