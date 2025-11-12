package enat.bank.savingAndloanRepayment;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import enat.bank.exception.SavingAndLoanRepaymentSaveFileException;
import enat.bank.saving.SavingDetails;
import enat.bank.utils.CommonService;
import enat.bank.exception.SavingAndLoanRepaymentsNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


@Service
public class SavingAndLoanRepaymentService extends CommonService<SavingAndLoanRepayment,Long,String> {
    private final  SavingAndLoanRepaymentRepository savingAndLoanRepaymentRepository;
    protected SavingAndLoanRepaymentService(SavingAndLoanRepaymentRepository savingAndLoanRepaymentRepository) {
        super(savingAndLoanRepaymentRepository);
        this.savingAndLoanRepaymentRepository=savingAndLoanRepaymentRepository;

    }
    public ResponseEntity<List<SavingAndLoanRepayment>> importCsv(MultipartFile file) {
        List<SavingAndLoanRepayment> dataList = new ArrayList<>();
        SavingDetails metaData=new SavingDetails();
        try (CSVReader  reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            int lineNumber=0;
            boolean isFirst=true;
            String[] fields;

            metaData.setRecordCount((reader.readAll().size()));


            while ((fields = reader.readNext()) != null) {

                lineNumber++;
                if (fields.length < 4) {
                    System.err.println("Skipping invalid line: " + lineNumber);
                    continue;
                }
                if(isFirst){
                    System.err.println("headers"+fields);
                    isFirst=false;
                    continue;
                }

                String employeeId = fields[0].trim();
                String fullName = fields[1].trim();
                Double craSaving = parseDoubleSafe(fields[2]);
                Double crassLoanRepayment = parseDoubleSafe(fields[3]);
                System.out.println("employee id..."+employeeId+"...crsSaving"+fields[2] + "...crsssloan"+fields[3]);
                SavingAndLoanRepayment entity = SavingAndLoanRepayment.builder()
                        .employeeId(employeeId)
                        .fullName(fullName)
                        .craSaving(craSaving)
                        .crassLoanRepayment(crassLoanRepayment)
                        .build();

                dataList.add(entity);
            }

            repository.saveAll(dataList);
            System.out.println("Imported records: " + dataList.size());

        } catch (IOException | CsvValidationException e) {
            throw new SavingAndLoanRepaymentSaveFileException("Failed to read CSV file: " + e.getMessage());
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Totals Coloumns:" + metaData.getRecordCount());
        return ResponseEntity.ok(dataList);
    }

    private Double parseDoubleSafe(String value) {
        try {
            return Double.parseDouble(value.replace(",", "").trim());
        } catch (Exception e) {
            return 0.0;
        }
    }

    public SavingAndLoanRepayment findById(Long id,SavingAndLoanRepayment update){

        SavingAndLoanRepayment savingAndLoanRepayment=savingAndLoanRepaymentRepository.findById(id).orElseThrow(()->
                new SavingAndLoanRepaymentsNotFoundException("SavingAndLoanRepayments record not found with this Id:.."+id));
        savingAndLoanRepayment.setCraSaving(update.getCraSaving());
        savingAndLoanRepayment.setCrassLoanRepayment(update.getCrassLoanRepayment());
        savingAndLoanRepayment.setFullName(update.getFullName());
        return savingAndLoanRepayment;

    }

    public Page<SavingAndLoanRepayment> findByEmployeeIds(String employeeId, Pageable pageable){

        return  savingAndLoanRepaymentRepository.findByEmployeeId(employeeId ,pageable);

    }

    @Transactional
    public ResponseEntity<List<SavingAndLoanRepayment>> deleteByEmployeeId(String employeeId) {
       List<SavingAndLoanRepayment> d=savingAndLoanRepaymentRepository.deleteByEmployeeId(employeeId);

       return  ResponseEntity.ok(d);
    }




}
