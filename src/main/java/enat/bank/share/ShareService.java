package enat.bank.share;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import enat.bank.exception.SavingAndLoanRepaymentSaveFileException;
import enat.bank.utils.CommonService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShareService extends CommonService<Share ,Long ,Share> {
    private final ShareRepository shareRepository;
    public ShareService(ShareRepository shareRepository) {
        super(shareRepository);
        this.shareRepository=shareRepository ;
    }
    public Optional<Share> getByEmployeeId(Long employeeId){
        return  shareRepository.findByEmployeeId(employeeId);
    }

    protected Share updateShare(Long id , Share share)
    {
        Share update=shareRepository.findByEmployeeId(share.getEmployeeId()).orElseThrow(()->
                new RuntimeException("Employee Not Found with  this ID:"+id)
        );
        update.setNoOfShare(share.getNoOfShare());
        update.setFullName(share.getFullName());
        update.setTotalSaving(share.getTotalSaving());
        return update ;
    }





    public List<Share> importCsv(MultipartFile file){
        List<Share> lsShare=new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String[] fields;
            boolean isFirst = true;
            int lineNumber = 0;

            while ((fields = reader.readNext()) != null) {
                lineNumber++;


                if (isFirst) {

                    isFirst = false;
                    continue; // skip headers
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
                Share  share=Share.builder()
                        .employeeId(Long.valueOf(fields[0].trim()))
                        .membershipId(fields[1].trim())
                        .fullName(fields[2].trim())
                        .totalSaving(parseDoubleSafe(fields[3].trim()))
                        .noOfShare(parseDoubleSafe(fields[4].trim()))
                        .share(parseDoubleSafe(fields[4].trim()))
                        .build();
                lsShare.add(share);

            }

            return   shareRepository.saveAll(lsShare);



        } catch (IOException | CsvValidationException e) {
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


}
