package enat.bank.saving;

import enat.bank.utils.CommonJpaRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingRepository extends CommonJpaRepo<Saving,Long> {
    Page<Saving> findByEmployeeId(String employeeId , Pageable pageable);
    List<Saving> deleteByEmployeeId(String employeeId);


}
