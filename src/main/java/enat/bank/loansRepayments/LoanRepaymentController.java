package enat.bank.loansRepayments;

import enat.bank.utils.Common;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/loan-repayments")
@RequiredArgsConstructor
@Tag(name = "Saving and Loan Repayment", description = "Manage saving and loan repayment records and CSV import.")
public class LoanRepaymentController implements Common<LoanRepayment, String, String, LoanRepayment> {

    private final LoanRepaymentService loanRepaymentService;

    @Override
    @Operation(summary = "Create repayment record", description = "Creates a new saving or loan repayment record.")
    public ResponseEntity<LoanRepayment> create(@RequestBody LoanRepayment loanRepayment) {
        return loanRepaymentService.create(loanRepayment);
    }

    @Override
    @Operation(summary = "Update repayment record", description = "Updates an existing repayment record by ID.")
    public ResponseEntity<LoanRepayment> update(
            @PathVariable Long id, @RequestBody LoanRepayment loanRepayment) {
        return loanRepaymentService.update(
                loanRepaymentService.findById(id, loanRepayment), id);
    }

    @Override
    @Operation(summary = "Delete repayment record", description = "Deletes a repayment record by ID.")
    public void delete(@PathVariable Long id) {
        loanRepaymentService.delete(id);
    }

    @Override
    @Operation(summary = "Get repayment record by ID", description = "Fetches a single repayment record by ID.")
    public Optional<LoanRepayment> getById(@PathVariable Long id) {
        return loanRepaymentService.getById(id);
    }

    @Override
    @Operation(summary = "Get all repayments", description = "Retrieves all repayment records with pagination.")
    public Page<LoanRepayment> getAllPageable(Pageable pageable, String name) {
        return loanRepaymentService.getAllPageable(pageable);
    }


    @GetMapping("search-by-employee-id/{employeeId}")
    public Page<LoanRepayment>  findByEmployeeIds(@PathVariable("employeeId") Long  employeeId , Pageable pageable){
        return loanRepaymentService.findByEmployeeIds(employeeId,pageable);

    }

    @DeleteMapping("delete-by-employee-id/{employeeId}")
    public ResponseEntity<List<LoanRepayment>> deleteByEmployeeId(@PathVariable("employeeId") Long  employeeId) {
        return loanRepaymentService.deleteByEmployeeId(employeeId);
    }


    @PostMapping("import-csv")
    @Operation(summary = "Import repayment data from CSV", description = "Uploads and imports repayment records from a CSV file.")
    public ResponseEntity<List<LoanRepayment>> importCsvFile(@RequestParam("file") MultipartFile file, @RequestParam("forMonth")LocalDate forMonth) {
        return loanRepaymentService.importCsv(file,forMonth);
    }


    @GetMapping("total-cra-loan-repayments/{employeeId}")

    public Double findTotalCraLoanRepayemenst(@PathVariable("employeeId") Long  employeeId){
        return loanRepaymentService.findTotalCraLoanRepaymenets(employeeId);
    }

    @GetMapping("count")
    public Double countAllLoanRepayments(){
        return loanRepaymentService.sumAllLoanRepayments();
    }


}
