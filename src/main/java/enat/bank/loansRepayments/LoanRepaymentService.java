package enat.bank.loansRepayments;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import enat.bank.exception.SavingAndLoanRepaymentSaveFileException;
import enat.bank.exception.SavingAndLoanRepaymentsNotFoundException;
import enat.bank.utils.CommonService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


@Service
public class LoanRepaymentService extends CommonService<LoanRepayment,Long,String> {
    private final LoanRepaymentRepository loanRepaymentRepository;
    private  final LoanRepaymentDetailsRepository loanRepaymentDetailsRepository;
    protected LoanRepaymentService(LoanRepaymentRepository loanRepaymentRepository
    , LoanRepaymentDetailsRepository loanRepaymentDetailsRepository
                                            ) {
        super(loanRepaymentRepository);
        this.loanRepaymentRepository = loanRepaymentRepository;
        this.loanRepaymentDetailsRepository = loanRepaymentDetailsRepository;

    }
    @Transactional
    public ResponseEntity<List<LoanRepayment>> importCsv(MultipartFile file) {
        List<LoanRepayment> dataList = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String[] fields;
            boolean isFirst = true;
            int lineNumber = 0;

            while ((fields = reader.readNext()) != null) {
                lineNumber++;
                if (fields.length < 3) {
                    System.err.println("Skipping invalid line: " + lineNumber);
                    continue;
                }

                if (isFirst) {
                    isFirst = false;
                    continue; // skip headers
                }

                String employeeId = fields[0].trim();
                String fullName = fields[1].trim();
                Double crassLoanRepayment = parseDoubleSafe(fields[2]);

                LoanRepayment entity = LoanRepayment.builder()
                        .employeeId(employeeId)
                        .fullName(fullName)
                        .crassLoanRepayment(crassLoanRepayment)
                        .build();

                dataList.add(entity);
            }

            if (dataList.isEmpty()) {
                throw new SavingAndLoanRepaymentSaveFileException("No valid records found in file!");
            }

            repository.saveAll(dataList);

            LoanRepaymentDetails saveDetails = LoanRepaymentDetails.builder()
                    .fileName(file.getOriginalFilename())
                    .fileSize(file.getSize())
                    .filePath(file.getResource().toString())
                    .remarks("Completed successfully: " + dataList.size() + " records imported.")
                    .tableName("loan_repayment")
                    .build();

            loanRepaymentDetailsRepository.save(saveDetails);

            return ResponseEntity.ok(dataList);

        } catch (IOException | CsvValidationException e) {
            LoanRepaymentDetails errorDetails = LoanRepaymentDetails.builder()
                    .fileName(file.getOriginalFilename())
                    .fileSize(file.getSize())
                    .filePath(file.getResource().toString())
                    .remarks("Failed due to: " + e.getMessage())
                    .build();
            loanRepaymentDetailsRepository.save(errorDetails);
            throw new SavingAndLoanRepaymentSaveFileException("Failed to read CSV file: " + e.getMessage());
        }
    }
    private Double parseDoubleSafe(String value) {
        try {
            return Double.parseDouble(value.replace(",", "").trim());
        } catch (Exception e) {
            return 0.0;
        }
    }

    public LoanRepayment findById(Long id, LoanRepayment update){

        LoanRepayment loanRepayment = loanRepaymentRepository.findById(id).orElseThrow(()->
                new SavingAndLoanRepaymentsNotFoundException("SavingAndLoanRepayments record not found with this Id:.."+id));
        loanRepayment.setCrassLoanRepayment(update.getCrassLoanRepayment());
        loanRepayment.setFullName(update.getFullName());
        return loanRepayment;

    }

    public Page<LoanRepayment> findByEmployeeIds(String employeeId, Pageable pageable){

        return  loanRepaymentRepository.findByEmployeeId(employeeId ,pageable);

    }

    @Transactional
    public ResponseEntity<List<LoanRepayment>> deleteByEmployeeId(String employeeId) {
       List<LoanRepayment> d= loanRepaymentRepository.deleteByEmployeeId(employeeId);

       return  ResponseEntity.ok(d);
    }



    public Double findTotalCraLoanRepaymenets(String employeeId){

        return loanRepaymentRepository.findTotalLoanRepaymentByEmployeeId(employeeId);
    }

  public Double  sumAllLoanRepayments(){

        return loanRepaymentRepository.sumCrassLoanRepayment();
  }

}
