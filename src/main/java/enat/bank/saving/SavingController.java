package enat.bank.saving;

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
public class SavingController implements Common<Saving, String, String, Saving> {

    private final SavingService savingService;

    @Override
    @Operation(summary = "Create repayment record", description = "Creates a new saving or loan repayment record.")
    public ResponseEntity<Saving> create(@RequestBody Saving saving) {
        return savingService.create(saving);
    }

    @Override
    @Operation(summary = "Update repayment record", description = "Updates an existing repayment record by ID.")
    public ResponseEntity<Saving> update(
            @PathVariable Long id, @RequestBody Saving saving) {
        return savingService.update(
                savingService.findById(id, saving), id);
    }

    @Override
    @Operation(summary = "Delete repayment record", description = "Deletes a repayment record by ID.")
    public void delete(@PathVariable Long id) {
        savingService.delete(id);
    }

    @Override
    @Operation(summary = "Get repayment record by ID", description = "Fetches a single repayment record by ID.")
    public Optional<Saving> getById(@PathVariable Long id) {
        return savingService.getById(id);
    }

    @Override
    @Operation(summary = "Get all repayments", description = "Retrieves all repayment records with pagination.")
    public Page<Saving> getAllPageable(Pageable pageable, String name) {
        return savingService.getAllPageable(pageable);
    }


    @GetMapping("search-by-employee-id/{employeeId}")
    public Page<Saving>  findByEmployeeIds(@PathVariable("employeeId") String employeeId , Pageable pageable){
        return savingService.findByEmployeeIds(employeeId,pageable);

    }

    @DeleteMapping("delete-by-employee-id/{employeeId}")
    public ResponseEntity<List<Saving>> deleteByEmployeeId(@PathVariable("employeeId") String employeeId) {
        return savingService.deleteByEmployeeId(employeeId);
    }


    @PostMapping("import-csv")
    @Operation(summary = "Import repayment data from CSV", description = "Uploads and imports repayment records from a CSV file.")
    public ResponseEntity<List<Saving>> importCsvFile(@RequestParam("file") MultipartFile file) {
        return savingService.importCsv(file);
    }


}
