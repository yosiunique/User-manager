package enat.bank.savingAndloanRepayment;

import enat.bank.Utils.CommonService;
import enat.bank.exception.SavingAndLoanRepaymentsNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
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
    public ResponseEntity<List<SavingAndLoanRepayment>> importCsvFile(MultipartFile file) {

        List<SavingAndLoanRepayment> savingAndRepaymnts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            boolean isFirstLine = true;



            while ((line = reader.readLine()) != null) {
                if (isFirstLine) { // Skip header
                    isFirstLine = false;
                    continue;
                }

                String[] fields = line.split(",");

                // Assuming CSV has fullName,email,age
                if (fields.length == 4) {
                    SavingAndLoanRepayment savingAndLoanRepayment = SavingAndLoanRepayment.builder()
                            .employeeId(fields[0].trim())
                            .fullName(fields[1].trim())
                            .craSaving(Integer.parseInt(fields[2].trim()))
                            .crassLoanRepayment(Double.parseDouble(fields[3].trim()))
                            .build();

                    savingAndRepaymnts.add(savingAndLoanRepayment);
                }
            }

         savingAndLoanRepaymentRepository.saveAll(savingAndRepaymnts);

        } catch (Exception e) {
            throw new SavingAndLoanRepaymentsNotFoundException("Failed to upload CSV: " + e.getMessage());
        }

  return ResponseEntity.ok(savingAndRepaymnts);

    }

    public SavingAndLoanRepayment findById(Long id,SavingAndLoanRepayment update){

        SavingAndLoanRepayment savingAndLoanRepayment=savingAndLoanRepaymentRepository.findById(id).orElseThrow(()->
                new SavingAndLoanRepaymentsNotFoundException("SavingAndLoanRepayments record not found with this Id:.."+id));

        savingAndLoanRepayment.setCraSaving(update.getCraSaving());
        savingAndLoanRepayment.setCrassLoanRepayment(update.getCrassLoanRepayment());
        savingAndLoanRepayment.setFullName(update.getFullName());
        return savingAndLoanRepayment;

    }






}
