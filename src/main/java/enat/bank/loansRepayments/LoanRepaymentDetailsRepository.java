package enat.bank.loansRepayments;

import enat.bank.utils.CommonJpaRepo;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepaymentDetailsRepository extends CommonJpaRepo<LoanRepaymentDetails,Long>  {
}
