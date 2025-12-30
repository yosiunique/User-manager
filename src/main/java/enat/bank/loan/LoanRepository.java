package enat.bank.loan;

import enat.bank.Employee.Status;
import enat.bank.utils.CommonJpaRepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface LoanRepository extends CommonJpaRepo<Loan, Long> {

//    List<Loan> findByIdAndEmployee_Id(Long id, Long employeeId);
//    boolean existsByEmployee_IdAndLoan_IdAndStatusLoan_IdAndStatus(Long employeeId, String loanId, Status status);
    Loan findByEmployee_EmployeeIdAndStatus(Long employeeId ,Status status);
    Optional<Loan> findByEmployee_IdAndLoanIdAndStatus(Long employeeId, String loanId, Status status);
    boolean existsByEmployee_IdAndStatusAndLoanIdNot(Long employeeId, Status status,String  excludeLoanId);

}
