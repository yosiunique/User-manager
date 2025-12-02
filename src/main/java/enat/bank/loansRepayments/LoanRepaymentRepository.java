package enat.bank.loansRepayments;

import enat.bank.utils.CommonJpaRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepaymentRepository extends CommonJpaRepo<LoanRepayment,Long> {
    Page<LoanRepayment> findByEmployee_Id(Long employeeId, Pageable pageable);

    @Modifying
    @Query("UPDATE LoanRepayment l SET l.deleted = true, l.deletedAt = CURRENT_TIMESTAMP WHERE l.employee.id = :employeeId")
    void  deleteByEmployee_Id(@Param("employeeId") Long employeeId);

    @Query("SELECT SUM(l.crassLoanRepayment) FROM LoanRepayment l WHERE l.employee.id = :employeeId")
    Double findTotalLoanRepaymentByEmployeeId(@Param("employeeId") Long employeeId);

    @Query("SELECT SUM(l.crassLoanRepayment) FROM LoanRepayment l")
    Double sumCrassLoanRepayment();


}
