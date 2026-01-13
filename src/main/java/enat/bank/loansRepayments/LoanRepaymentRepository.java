package enat.bank.loansRepayments;

import enat.bank.utils.CommonJpaRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface LoanRepaymentRepository extends CommonJpaRepo<LoanRepayment,Long> {
    Page<LoanRepayment> findByLoan_Employee_EmployeeId(Long employeeID, Pageable pageable);

    @Modifying
    @Query("UPDATE LoanRepayment l SET l.deleted = true, l.deletedAt = CURRENT_TIMESTAMP WHERE l.loan.employee.id = :employeeId")
    void  deleteByLoan_Id(@Param("employeeId") String  loanId);

    @Query("SELECT SUM(l.crassLoanRepayment) FROM LoanRepayment l WHERE l.loan.employee.id = :employeeId")
    Double findTotalLoanRepaymentByEmployeeId(@Param("employeeId") Long employeeId);

    @Query("SELECT SUM(l.crassLoanRepayment) FROM LoanRepayment l")
    Double sumCrassLoanRepayment();


}
