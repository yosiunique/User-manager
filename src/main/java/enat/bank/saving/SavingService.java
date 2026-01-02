package enat.bank.saving;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import enat.bank.employee.Employee;
import enat.bank.employee.EmployeeRepository;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class SavingService extends CommonService<Saving,Long,String > {
    private final String[] header={"employeeId","fullName","craSaving" };
    private final SavingRepository savingRepository;
    private  final SavingDetailsRepository savingDetailsRepository;
    private final EmployeeRepository employeeRepository;
    protected SavingService(SavingRepository savingRepository
    , SavingDetailsRepository savingDetailsRepository ,
                            EmployeeRepository employeeRepository
                                            ) {
        super(savingRepository);
        this.employeeRepository=employeeRepository;
        this.savingRepository = savingRepository;
        this.savingDetailsRepository =savingDetailsRepository;

    }

    @Transactional
    public ResponseEntity<List<Saving>> importCsv(MultipartFile file, LocalDate forMonth) {
        List<Saving> dataList = new ArrayList<>();
        try (CSVReader  reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            int lineNumber=0;
            boolean isFirst=true;
            String[] fields;
            while ((fields = reader.readNext()) != null) {

                lineNumber++;

                if(isFirst){

                    if(!fields[0].trim().equals(header[0]) &&!fields[1].trim().equals(header[1]) &&!fields[2].trim().equals(header[0]) &&!fields[2].trim().equals(header[2])  ){
                        throw new SavingAndLoanRepaymentSaveFileException("invalid header pls Enter valid heeaders Name !");
                    }

                    System.err.println("headers"+fields);
                    isFirst=false;
                    continue;
                }

                String employeeId = fields[0].trim();
                Double craSaving = parseDoubleSafe(fields[2]);
                System.out.println("employee id..."+employeeId+"...crsSaving"+fields[2] + "...crsLoan"+fields[3]);


                Employee employee =this.employeeRepository.findByEmployeeId(Long.valueOf(employeeId)).orElseThrow(()-> new SavingAndLoanRepaymentsNotFoundException("No employee is registered by this ID:..."+employeeId));
                Saving entity = Saving.builder()
                        .employee(employee)
                        .forMonth(forMonth)
                        .craSaving(craSaving)
                        .build();

                dataList.add(entity);
            }
            repository.saveAll(dataList);
        } catch (IOException | CsvValidationException e) {
            SavingDetails saveDetails =SavingDetails.builder()
                    .fileName(file.getOriginalFilename())
                    .status("failds du to ..." +e.getMessage())
                    .forMonth(forMonth)
                    .build();
            savingDetailsRepository.save(saveDetails);
            throw new SavingAndLoanRepaymentSaveFileException("Failed to read CSV file: " + e.getMessage());
        }


        SavingDetails saveDetails =SavingDetails.builder()
                .fileName(file.getOriginalFilename())
                .status("completed")
                .dataSize(dataList.size())
                .forMonth(forMonth)
                .build();
        savingDetailsRepository.save(saveDetails);
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
        return saving;

    }

    public Page<Saving> findByEmployeeIds(Long  employeeId, Pageable pageable){

        return  savingRepository.findByEmployee_EmployeeId(employeeId ,pageable);

    }

    @Transactional
    public ResponseEntity<List<Saving>> deleteByEmployeeId(Long  employeeId) {
       List<Saving> d= savingRepository.deleteByEmployee_Id(employeeId);

       return  ResponseEntity.ok(d);
    }

    public Double findTotalCraSaving(Long  employeeId)
    {

        return savingRepository.findTotalSavingByEmployeeId(employeeId);

    }


    public Double  sumAllSaving(){
        return savingRepository.sumCraSaving();
    }


}
