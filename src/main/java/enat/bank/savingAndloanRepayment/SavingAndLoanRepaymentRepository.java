package enat.bank.savingAndloanRepayment;

import enat.bank.utils.CommonJpaRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingAndLoanRepaymentRepository extends CommonJpaRepo<SavingAndLoanRepayment,Long> {
    Page<SavingAndLoanRepayment> findByEmployeeId(String employeeId , Pageable pageable);
    List<SavingAndLoanRepayment> deleteByEmployeeId(String employeeId);


}
