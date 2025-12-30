package enat.bank.loan;

import enat.bank.Employee.Employee;
import enat.bank.Employee.EmployeeRepository;
import enat.bank.Employee.Status;
import enat.bank.exception.LoanException;
import enat.bank.utils.CommonService;
import org.springframework.stereotype.Service;


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
    protected Loan updateLoan(Long  id, Loan loan) {
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
}
