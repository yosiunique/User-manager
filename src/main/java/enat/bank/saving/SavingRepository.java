package enat.bank.saving;

import enat.bank.utils.CommonJpaRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingRepository extends CommonJpaRepo<Saving,Long> {
    Page<Saving> findByEmployeeId(String employeeId , Pageable pageable);
    List<Saving> deleteByEmployeeId(String employeeId);
    @Query("SELECT SUM(s.craSaving) FROM Saving s WHERE s.employeeId = :employeeId")
    Double findTotalSavingByEmployeeId(@Param("employeeId") String employeeId);
    @Query("SELECT SUM(s.craSaving) FROM Saving s")
    Double sumCraSaving();




}
