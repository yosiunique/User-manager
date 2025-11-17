package enat.bank.loansRepayments;

import enat.bank.utils.CommonJpaRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepaymentRepository extends CommonJpaRepo<LoanRepayment,Long> {
    Page<LoanRepayment> findByEmployeeId(String employeeId , Pageable pageable);
    List<LoanRepayment> deleteByEmployeeId(String employeeId);
    @Query("SELECT SUM(l.crassLoanRepayment) FROM LoanRepayment l WHERE l.employeeId = :employeeId")
    Double findTotalLoanRepaymentByEmployeeId(@Param("employeeId") String employeeId);



}
