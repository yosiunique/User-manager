package enat.bank.saving;

import enat.bank.utils.CommonJpaRepo;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingDetailsRepository extends CommonJpaRepo<SavingDetails ,Long>  {
}
