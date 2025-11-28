package enat.bank.loansRepayments;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import enat.bank.Employee.Employee;
import enat.bank.Employee.EmployeeRepository;
import enat.bank.exception.LoanRepaymentsException;
import enat.bank.exception.SavingAndLoanRepaymentSaveFileException;
import enat.bank.exception.SavingAndLoanRepaymentsNotFoundException;
import enat.bank.utils.ApplicationProps;
import enat.bank.utils.CommonService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class LoanRepaymentService extends CommonService<LoanRepayment,Long,String> {
    private final ApplicationProps applicationProps ;
    private final String[] header={"employeeId","fullName","craSaving" };
    private final LoanRepaymentRepository loanRepaymentRepository;
    private  final LoanRepaymentDetailsRepository loanRepaymentDetailsRepository;
    private final EmployeeRepository employeeRepository;
    protected LoanRepaymentService(LoanRepaymentRepository loanRepaymentRepository
    , LoanRepaymentDetailsRepository loanRepaymentDetailsRepository,
                                   EmployeeRepository employeeRepository,
                                   ApplicationProps applicationProps
                                            ) {
        super(loanRepaymentRepository);
        this.loanRepaymentRepository = loanRepaymentRepository;
        this.loanRepaymentDetailsRepository = loanRepaymentDetailsRepository;
        this.employeeRepository=employeeRepository;
        this.applicationProps=applicationProps;

    }
    @Transactional
    public ResponseEntity<List<LoanRepayment>> importCsv(MultipartFile file ,LocalDate forMonth) {
        List<LoanRepayment> dataList = new ArrayList<>();
        List<Employee> lsEmployee=new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String[] fields;
            boolean isFirst = true;
            int lineNumber = 0;

            while ((fields = reader.readNext()) != null) {
                lineNumber++;
                if (fields.length < 3) {
                    System.err.println("Skipping invalid line: " + lineNumber);
                    continue;
                }

                if (isFirst) {
                    if(!fields[0].trim().equals(header[0]) &&!fields[1].trim().equals(header[1]) &&!fields[2].trim().equals(header[0]) &&!fields[2].trim().equals(header[2])  ){
                        throw new LoanRepaymentsException("invalid header pls Enter valid header Name !");
                    }
                    isFirst = false;
                    continue; // skip headers
                }

                /***
                 *
                 *
                 *   split the interset and principal
                 *
                 *
                 * ****/
                String employeeId = fields[0].trim();
                String fullName = fields[1].trim();
                Double crassLoanRepayment = parseDoubleSafe(fields[2]);
                Employee employee=employeeRepository.findById(Long.valueOf(employeeId))
                        .orElseThrow(()-> new LoanRepaymentsException("Employee Not Found with in this ID...."+employeeId));
                 double rate = applicationProps.getAnnualInterset() / applicationProps.getAnnualPeriod();
                 double emi=0;
                 double interset=0;
                 double principal=0;
                 double remainningBalance=0;
               if(employee.getEmi()==0) {

                     emi = (employee.getOutStanding() * rate * (Math.pow(1 + rate, applicationProps.getPeriod()))) /
                            ((Math.pow(1 + rate, applicationProps.getPeriod())) - 1);
                     interset = employee.getOutStanding() * rate;
                     principal = emi - interset;
                     remainningBalance = employee.getOutStanding() - principal;
                    System.out.println("outStand:..." + employee.getOutStanding());
                    System.out.println("rate:..." + rate);
                    System.out.println("Monthly:..." + emi);
                    System.out.println("interset:.." + interset);
                    System.out.println("princpal:.." + principal);
                    System.out.println("remaining Balance:....." + remainningBalance);
                    employee.setOutStanding(remainningBalance);
                    employee.setEmi(emi);
                } else{
                    System.out.println("outStand:..." + employee.getOutStanding());
                    System.out.println("rate:..." + rate);
                    System.out.println("Monthly:..." + employee.getEmi());
                    System.out.println("interset:.." + employee.getOutStanding()*rate);
                    System.out.println("princpal:.." + (employee.getEmi()-(employee.getOutStanding()*rate)));
                    System.out.println("remaining Balance:....." + (employee.getOutStanding()-(employee.getEmi()-(employee.getOutStanding()*rate))));
                    employee.setOutStanding((employee.getOutStanding()-(employee.getEmi()-(employee.getOutStanding()*rate))));
                   emi = employee.getEmi();
                   interset = employee.getOutStanding() * rate;
                   principal = emi - interset;

                }
                LoanRepayment entity = LoanRepayment.builder()
                        .employee(employee)
                        .fullName(fullName)
                        .principal(principal)
                        .interset(interset)
                        .forMonth(LocalDate.now())
                        .crassLoanRepayment(crassLoanRepayment)
                        .build();

                dataList.add(entity);
                lsEmployee.add(employee);
            }

            if (dataList.isEmpty()) {
                throw new SavingAndLoanRepaymentSaveFileException("No valid records found in file!");
            }

            employeeRepository.saveAll(lsEmployee);
            repository.saveAll(dataList);
            LoanRepaymentDetails saveDetails = LoanRepaymentDetails.builder()
                    .fileName(file.getOriginalFilename())
                    .forMonth(forMonth)
                    .dataSize(dataList.size())
                    .status("completed")
                    .build();

            loanRepaymentDetailsRepository.save(saveDetails);

            return ResponseEntity.ok(dataList);

        } catch (IOException | CsvValidationException e) {
            LoanRepaymentDetails errorDetails = LoanRepaymentDetails.builder()
                    .fileName(file.getOriginalFilename())
                    .forMonth(forMonth)
                    .status("fails du to "+e.getMessage())
                    .build();
            loanRepaymentDetailsRepository.save(errorDetails);
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

    public LoanRepayment findById(Long id, LoanRepayment update){

        LoanRepayment loanRepayment = loanRepaymentRepository.findById(id).orElseThrow(()->
                new SavingAndLoanRepaymentsNotFoundException("SavingAndLoanRepayments record not found with this Id:.."+id));
        loanRepayment.setCrassLoanRepayment(update.getCrassLoanRepayment());
        loanRepayment.setFullName(update.getFullName());
        return loanRepayment;

    }

    public Page<LoanRepayment> findByEmployeeIds(Long  employeeId, Pageable pageable){

        return  loanRepaymentRepository.findByEmployee_Id(employeeId ,pageable);

    }

    @Transactional
    public ResponseEntity<List<LoanRepayment>> deleteByEmployeeId(Long employeeId) {
       List<LoanRepayment> d= loanRepaymentRepository.deleteByEmployee_Id(employeeId);

       return  ResponseEntity.ok(d);
    }



    public Double findTotalCraLoanRepaymenets(Long  employeeId){

        return loanRepaymentRepository.findTotalLoanRepaymentByEmployeeId(employeeId);
    }

  public Double  sumAllLoanRepayments(){

        return loanRepaymentRepository.sumCrassLoanRepayment();
  }

}
