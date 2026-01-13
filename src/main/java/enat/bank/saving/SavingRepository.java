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
    Page<Saving> findByEmployee_EmployeeId(Long  employeeId , Pageable pageable);
    List<Saving> deleteByEmployee_Id(Long employeeId);
    @Query("SELECT SUM(s.craSaving) FROM Saving s WHERE s.employee.id = :employeeId")
    Double findTotalSavingByEmployeeId(@Param("employeeId") Long employeeId);
    @Query("SELECT SUM(s.craSaving) FROM Saving s")
    Double sumCraSaving();




}
