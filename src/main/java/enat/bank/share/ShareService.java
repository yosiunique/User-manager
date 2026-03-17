package enat.bank.share;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import enat.bank.employee.Employee;
import enat.bank.employee.EmployeeRepository;
import enat.bank.exception.SavingAndLoanRepaymentSaveFileException;
import enat.bank.utils.CommonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShareService extends CommonService<Share ,Long ,Share> {
    private final ShareRepository shareRepository;
    private final EmployeeRepository employeeRepositor;
    public ShareService(ShareRepository shareRepository , EmployeeRepository employeeRepository) {
        super(shareRepository);
        this.shareRepository=shareRepository ;
        this. employeeRepositor=employeeRepository;
    }
    public Page<Share> getByEmployeeId(Long employeeId ,Pageable pageable){
        return  shareRepository.findByEmployee_EmployeeId(employeeId , pageable);
    }

    protected Share updateShare(Long id , Share share)
    {
        Share update=shareRepository.findById(id).orElseThrow(()->new RuntimeException("Share not found by this id:"+id));
        Employee employee=employeeRepositor.findByEmployeeId(share.getEmployee().getEmployeeId()).get();
        update.setEmployee(employee);
        update.setNoOfShare(share.getNoOfShare());
//        update.setTotalSaving(share.getTotalSaving());
        return update ;
    }





    public List<Share> importCsv(MultipartFile file ,String remark){

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
               lineNumber ++;
                System.out.println("line Number: "+lineNumber +"  employeeID ..." +parseLongSafe(fields[0].trim() )+"    share:"+parseDoubleSafe(fields[1].trim()));
                final String employeeID=fields[0].trim();
                if (!employeeID.isEmpty()||!employeeID.isBlank()||!employeeID.equals("")){
                Employee employee = employeeRepositor
                        .findByEmployeeId(parseLongSafe(fields[0].trim()))
                        .orElseThrow(() -> new RuntimeException(
                                "Employee not found with ID: " + employeeID));

                Share  share=Share.builder()
                        .employee(employee)
                        .remark(remark)
                        .noOfShare(parseDoubleSafe(fields[1].trim()))
                        .share(parseDoubleSafe(fields[1].trim()))
                        .build();
                lsShare.add(share);

            }}

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

    private Long parseLongSafe(String value) {
        try {
            return Long.parseLong(value.replace(",", "").trim());
        } catch (Exception e) {
            return 0L;
        }
    }


    public Page<Share>  search(Long employeeId , Pageable pageable ){

        return shareRepository.findByEmployeeEmployeeIdContaining(employeeId,pageable);
    }
    public Page<Share> searchByName(String name, Pageable pageable){
        return shareRepository.findByEmployeeEmployeeFullNameContaining(name ,pageable);
    }

}
