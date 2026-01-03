package enat.bank.loan;

import com.opencsv.CSVReader;
import enat.bank.employee.Employee;
import enat.bank.employee.EmployeeRepository;
import enat.bank.employee.Status;
import enat.bank.exception.LoanException;
import enat.bank.utils.CommonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Service
public class LoanService extends CommonService<Loan, Long, Loan> {

    private final LoanRepository loanRepository;
    private final EmployeeRepository employeeRepository;

    public LoanService(LoanRepository loanRepository, EmployeeRepository employeeRepository) {
        super(loanRepository);
        this.loanRepository = loanRepository;
        this.employeeRepository = employeeRepository;
    }

    // 🔒 Enforce: only one ACTIVE loan per employee
    public void validateNoOtherActiveLoan(Loan loanData) throws LoanException {
        boolean exists = loanRepository.existsByEmployee_IdAndStatusAndLoanIdNot(
                loanData.getEmployee().getId(),
                Status.ACTIVE,
                loanData.getLoanId()
        );

        if (exists) {
            throw new LoanException("Only one ACTIVE loan is allowed per employee. Please close the existing one first.");
        }
    }

    // 📦 Update loan
    protected Loan updateLoan(Long id, Loan loan) {
        Loan update = loanRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Loan Not Found with ID: " + id));

        Employee employee = employeeRepository.findByEmployeeId(
                loan.getEmployee().getEmployeeId()).get();

        update.setEmployee(employee);
        update.setEffectiveDate(loan.getEffectiveDate());
        update.setOutStanding(loan.getOutStanding());
        update.setFirstOutStanding(loan.getFirstOutStanding());
        update.setAnnualInterest(loan.getAnnualInterest());
        update.setPeriod(loan.getPeriod());
        update.setEmi(loan.getEmi());
        update.setStatus(loan.getStatus());

        return loanRepository.save(update);
    }


    public List<Loan> importCsv(MultipartFile file) {
        List<Loan> lsLoan = new ArrayList<>();
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
                String[] finalFields = fields;
                Employee employee = employeeRepository.findByEmployeeId(Long.valueOf(fields[0].trim()))
                        .orElseThrow(() -> new RuntimeException("not found employee with this ID:" + Long.valueOf(finalFields[0].trim())));

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                Loan loan = Loan.builder()
                        .loanId(fields[2].trim())
                        .employee(employee)
                        .effectiveDate(LocalDate.parse(validFormat(fields[4].trim()), formatter))
                        .lastPaidMonth(LocalDate.parse(validFormat(fields[4].trim()),formatter))
                        .outStanding(parseDoubleSafe(fields[5].trim()))
                        .emi(.0)
                        .annualInterest(0.09)
                        .period(84)
                        .remainingPeriod(84)
                        .firstOutStanding(parseDoubleSafe(fields[5].trim()))
                        .status(Status.ACTIVE)
                        .build();
                lsLoan.add(loan);


            }

        } catch (Exception e) {
            throw new RuntimeException("this is :" + e.getMessage());
        }


        return loanRepository.saveAll(lsLoan);
    }


    private Double parseDoubleSafe(String value) {
        try {
            return Double.parseDouble(value.replace(",", "").trim());
        } catch (Exception e) {
            return 0.0;
        }

    }




    private String  validFormat(String date){

        String[] parts = date.split("/");

        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid date format: " + date);
        }
// normalize date and month
        String month = parts[0].length() == 1 ? "0" + parts[0] : parts[0];
        String day = parts[1].length() == 1 ? "0" + parts[1] : parts[1];
        String year = parts[2];

        return day+"/"+month+"/"+year;


    }

    Page<Loan> searchByEmployeeId(Long employeeId, Pageable pageable){

        return loanRepository.findByEmployeeEmployeeIdContaining(employeeId ,pageable);
    }

}
