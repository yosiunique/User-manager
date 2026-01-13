package enat.bank.loan;

import enat.bank.employee.Employee;
import enat.bank.employee.EmployeeRepository;
import enat.bank.exception.LoanException;
import enat.bank.utils.Common;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/loan")
@RequiredArgsConstructor
public class LoanController implements Common<Loan, Long, String, Loan> {

    private final LoanService loanService;
    private final EmployeeRepository employeeRepository;

    @Override
    @PostMapping
    public ResponseEntity<Loan> create(@RequestBody Loan loan) throws LoanException {
        Employee employee=employeeRepository.findByEmployeeId(loan.getEmployee().getEmployeeId()).orElseThrow(()->new RuntimeException("Employee not found with this ID:"+loan.getEmployee().getEmployeeId()));
        loan.setEmployee(employee);
        loanService.validateNoOtherActiveLoan(loan);
        return loanService.create(loan);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Loan> update(@PathVariable Long id, @RequestBody Loan loan) throws LoanException {// set ID for validation
        Employee employee=employeeRepository.findByEmployeeId(loan.getEmployee().getEmployeeId()).orElseThrow(()->new RuntimeException("Employee not found with this ID:"+loan.getEmployee().getEmployeeId()));
        loan.setEmployee(employee);
        loanService.validateNoOtherActiveLoan(loan);
        Loan updated = loanService.updateLoan(id, loan);
        return ResponseEntity.ok(updated);
    }

    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        loanService.delete(id);
    }

    @Override
    @GetMapping("/{id}")
    public Optional<Loan> getById(@PathVariable Long id) {
        return loanService.getById(id);
    }

    @Override
    @GetMapping
    public Page<Loan> getAllPageable(Pageable pageable, @RequestParam(required = false) String name) {
        return loanService.getAllPageable(pageable);
    }

    @PostMapping(value = "/import-csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<Loan> importCsv(@RequestParam("file") MultipartFile file ){
        return loanService.importCsv(file) ;
    }




    @GetMapping("/search-by-employee-id/{employeeId}")
    public Page<Loan> searchByEmployeeId(
            @PathVariable("employeeId") Long employeeId,
            Pageable pageable) {
        return loanService.searchByEmployeeId(employeeId, pageable);
    }




}
