package enat.bank.savingAndloanRepayment;

import enat.bank.Utils.CommonJpaRepo;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingAndLoanRepaymentRepository extends CommonJpaRepo<SavingAndLoanRepayment,Long> {
}
