package enat.bank.savingAndloanRepayment;

import enat.bank.Utils.Common;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("api/saving-and-loan-repayments")
@RequiredArgsConstructor
public class SavingAndLoanRepaymentController implements Common<SavingAndLoanRepayment ,String,String,SavingAndLoanRepayment> {
    private final SavingAndLoanRepaymentService savingAndLoanRepaymentService;


    @Override
    public ResponseEntity<SavingAndLoanRepayment> create(SavingAndLoanRepayment savingAndLoanRepayment) {
        return savingAndLoanRepaymentService.create(savingAndLoanRepayment);
    }

    @Override
    public ResponseEntity<SavingAndLoanRepayment> update(Long id, SavingAndLoanRepayment savingAndLoanRepayment) {
        return savingAndLoanRepaymentService.update(savingAndLoanRepaymentService.findById(id,savingAndLoanRepayment),id);
    }

    @Override
    public void delete(Long id) {
savingAndLoanRepaymentService.delete(id);
    }

    @Override
    public Optional<SavingAndLoanRepayment> getById(Long id) {
        return  savingAndLoanRepaymentService.getById(id);
    }

    @Override
    public Page<SavingAndLoanRepayment> getAllPageable(Pageable pageable, String name) {
        return savingAndLoanRepaymentService.getAllPageable(pageable);
    }

    @PostMapping("import-csv")
    public ResponseEntity<List<SavingAndLoanRepayment>>  importCsvFile(@RequestBody MultipartFile file){




        return savingAndLoanRepaymentService.importCsvFile(file) ;
    }

}
