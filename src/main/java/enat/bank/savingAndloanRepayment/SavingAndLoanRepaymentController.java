package enat.bank.savingAndloanRepayment;

import enat.bank.utils.Common;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/saving-and-loan-repayments")
@RequiredArgsConstructor
@Tag(name = "Saving and Loan Repayment", description = "Manage saving and loan repayment records and CSV import.")
public class SavingAndLoanRepaymentController implements Common<SavingAndLoanRepayment, String, String, SavingAndLoanRepayment> {

    private final SavingAndLoanRepaymentService savingAndLoanRepaymentService;

    @Override
    @Operation(summary = "Create repayment record", description = "Creates a new saving or loan repayment record.")
    public ResponseEntity<SavingAndLoanRepayment> create(@RequestBody SavingAndLoanRepayment savingAndLoanRepayment) {
        return savingAndLoanRepaymentService.create(savingAndLoanRepayment);
    }

    @Override
    @Operation(summary = "Update repayment record", description = "Updates an existing repayment record by ID.")
    public ResponseEntity<SavingAndLoanRepayment> update(
            @PathVariable Long id, @RequestBody SavingAndLoanRepayment savingAndLoanRepayment) {
        return savingAndLoanRepaymentService.update(
                savingAndLoanRepaymentService.findById(id, savingAndLoanRepayment), id);
    }

    @Override
    @Operation(summary = "Delete repayment record", description = "Deletes a repayment record by ID.")
    public void delete(@PathVariable Long id) {
        savingAndLoanRepaymentService.delete(id);
    }

    @Override
    @Operation(summary = "Get repayment record by ID", description = "Fetches a single repayment record by ID.")
    public Optional<SavingAndLoanRepayment> getById(@PathVariable Long id) {
        return savingAndLoanRepaymentService.getById(id);
    }

    @Override
    @Operation(summary = "Get all repayments", description = "Retrieves all repayment records with pagination.")
    public Page<SavingAndLoanRepayment> getAllPageable(Pageable pageable, String name) {
        return savingAndLoanRepaymentService.getAllPageable(pageable);
    }


    @GetMapping("search-by-employee-id/{employeeId}")
    public Page<SavingAndLoanRepayment>  findByEmployeeIds(@PathVariable("employeeId") String employeeId ,Pageable pageable){
        return savingAndLoanRepaymentService.findByEmployeeIds(employeeId,pageable);

    }

    @DeleteMapping("delete-by-employee-id/{employeeId}")
    public ResponseEntity<List<SavingAndLoanRepayment>> deleteByEmployeeId(@PathVariable("employeeId") String employeeId) {
        return savingAndLoanRepaymentService.deleteByEmployeeId(employeeId);
    }


    @PostMapping("import-csv")
    @Operation(summary = "Import repayment data from CSV", description = "Uploads and imports repayment records from a CSV file.")
    public ResponseEntity<List<SavingAndLoanRepayment>> importCsvFile(@RequestParam("file") MultipartFile file) {
        return savingAndLoanRepaymentService.importCsv(file);
    }


}
