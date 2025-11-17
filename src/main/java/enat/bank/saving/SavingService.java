package enat.bank.saving;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import enat.bank.exception.SavingAndLoanRepaymentSaveFileException;
import enat.bank.utils.CommonService;
import enat.bank.exception.SavingAndLoanRepaymentsNotFoundException;
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
public class SavingService extends CommonService<Saving,Long,String> {
    private final SavingRepository savingRepository;
    private  final SavingDetailsRepository savingDetailsRepository;
    protected SavingService(SavingRepository savingRepository
    , SavingDetailsRepository savingDetailsRepository
                                            ) {
        super(savingRepository);
        this.savingRepository = savingRepository;
        this.savingDetailsRepository =savingDetailsRepository;

    }
    public ResponseEntity<List<Saving>> importCsv(MultipartFile file) {
        List<Saving> dataList = new ArrayList<>();

        try (CSVReader  reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            int lineNumber=0;
            boolean isFirst=true;
            String[] fields;
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
                Saving entity = Saving.builder()
                        .employeeId(employeeId)
                        .fullName(fullName)
                        .craSaving(craSaving)
                        .build();

                dataList.add(entity);
            }

            repository.saveAll(dataList);
            System.out.println("Imported records: " + dataList.size());

        } catch (IOException | CsvValidationException e) {
            SavingDetails saveDetails =SavingDetails.builder()
                    .fileName(file.getOriginalFilename())
                    .fileSize(file.getSize())
                    .filePath(file.getResource().toString())
                    .remarks("failds du to ..." +e.getMessage())
                    .build();
            savingDetailsRepository.save(saveDetails);
            throw new SavingAndLoanRepaymentSaveFileException("Failed to read CSV file: " + e.getMessage());
        }


        SavingDetails saveDetails =SavingDetails.builder()
                .fileName(file.getOriginalFilename())
                .fileSize(file.getSize())
                .tableName(dataList.get(0).getCreatedAt().toString())
                .filePath(file.getResource().toString())
                .remarks("compleleted ...")
                .build();
               savingDetailsRepository.save(saveDetails);





        return ResponseEntity.ok(dataList);
    }

    private Double parseDoubleSafe(String value) {
        try {
            return Double.parseDouble(value.replace(",", "").trim());
        } catch (Exception e) {
            return 0.0;
        }
    }

    public Saving findById(Long id, Saving update){

        Saving saving = savingRepository.findById(id).orElseThrow(()->
                new SavingAndLoanRepaymentsNotFoundException("SavingAndLoanRepayments record not found with this Id:.."+id));
        saving.setCraSaving(update.getCraSaving());
        saving.setFullName(update.getFullName());
        return saving;

    }

    public Page<Saving> findByEmployeeIds(String employeeId, Pageable pageable){

        return  savingRepository.findByEmployeeId(employeeId ,pageable);

    }

    @Transactional
    public ResponseEntity<List<Saving>> deleteByEmployeeId(String employeeId) {
       List<Saving> d= savingRepository.deleteByEmployeeId(employeeId);

       return  ResponseEntity.ok(d);
    }

    public Double findTotalCraSaving(String employeeId)
    {

        return savingRepository.findTotalSavingByEmployeeId(employeeId);

    }


}
