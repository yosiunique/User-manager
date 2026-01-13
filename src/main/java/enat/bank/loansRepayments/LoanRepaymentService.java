package enat.bank.loansRepayments;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import enat.bank.employee.Status;
import enat.bank.exception.LoanRepaymentsException;
import enat.bank.exception.SavingAndLoanRepaymentSaveFileException;
import enat.bank.exception.SavingAndLoanRepaymentsNotFoundException;
import enat.bank.loan.Loan;
import enat.bank.loan.LoanRepository;
import enat.bank.utils.ApplicationProps;
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
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;


@Service
public class LoanRepaymentService extends CommonService<LoanRepayment,Long,String> {
    private final ApplicationProps applicationProps ;
    private final String[] header={"employeeId","fullName","loanAmount" };
    private final LoanRepaymentRepository loanRepaymentRepository;
    private  final LoanRepaymentDetailsRepository loanRepaymentDetailsRepository;
    private final LoanRepository loanRepository;
    protected LoanRepaymentService(LoanRepaymentRepository loanRepaymentRepository
    , LoanRepaymentDetailsRepository loanRepaymentDetailsRepository,
                                   LoanRepository loanRepository,
                                   ApplicationProps applicationProps
                                            ) {
        super(loanRepaymentRepository);
        this.loanRepaymentRepository = loanRepaymentRepository;
        this.loanRepaymentDetailsRepository = loanRepaymentDetailsRepository;
         this.loanRepository =  loanRepository;
        this.applicationProps=applicationProps;

    }
    @Transactional
    public ResponseEntity<List<LoanRepayment>> importCsv(MultipartFile file ,LocalDate forMonth) {
        List<LoanRepayment> dataList = new ArrayList<>();
        List<Loan> lsLoan=new ArrayList<>();
        LocalDate lastMonth=LocalDate.now().minusMonths(1);
        System.out.println("This Month:"+lastMonth);
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String[] fields;
            boolean isFirst = true;
            int lineNumber = 0;

            while ((fields = reader.readNext()) != null) {
                lineNumber++;

                if (isFirst) {
                    if(!fields[0].trim().equals(header[0]) &&!fields[1].trim().equals(header[1]) &&!fields[2].trim().equals(header[0]) &&!fields[2].trim().equals(header[2])  ){
                        throw new LoanRepaymentsException("invalid header pls Enter valid header Name !");
                    }
                    isFirst = false;
                    continue; // skip headers
                }

                for (String field : fields) {
                    System.out.println("fields :"+field);

                }
                /***
                 *
                 *
                 *   split the interset and principal
                 *
                 *
                 * ****/
                String employeeId = fields[0].trim();
                String fullName = fields[1].trim();
                String loanId=fields[3].trim();
                Double crassLoanRepayment = parseDoubleSafe(fields[2]);
                Loan loan=loanRepository.findByEmployee_EmployeeIdAndStatus(Long.valueOf(employeeId),Status.ACTIVE);
                if(loan==null){
                        throw  new LoanRepaymentsException("Employee Not Found with in this ID...."+employeeId);
                }
//                checkConsecutiveMonth(loan, forMonth);
                double rate = loan.getAnnualInterest() / applicationProps.getAnnualPeriod();
                 double emi=0;
                 double interset=0;
                 double principal=0;
                 double remainningBalance=0;
                 if (loan.getOutStanding()==0){
                     throw new LoanRepaymentsException("This Loan Repayments Completed  !");
                 }
               if(loan.getEmi()==0) {

                     emi = (loan.getOutStanding() * rate * (Math.pow(1 + rate, loan.getPeriod()))) /
                            ((Math.pow(1 + rate, loan.getPeriod())) - 1);
                     interset = loan.getOutStanding() * rate;
                     principal = emi - interset;
                     remainningBalance = loan.getOutStanding() - principal;
                    System.out.println("outStand:..." + loan.getOutStanding());
                    System.out.println("rate:..." + rate);
                    System.out.println("Monthly:..." + emi);
                    System.out.println("interset:.." + interset);
                    System.out.println("princpal:.." + principal);
                    System.out.println("remaining Balance:....." + remainningBalance);
                    loan.setOutStanding(remainningBalance);
                    loan.setEmi(emi);
                   loan.setRemainingPeriod(loan.getRemainingPeriod()-1);
                } else{
                    System.out.println("outStand:..." + loan.getOutStanding());
                    System.out.println("rate:..." + rate);
                    System.out.println("Monthly:..." + loan.getEmi());
                    System.out.println("interset:.." + loan.getOutStanding()*rate);
                    System.out.println("princpal:.." + (loan.getEmi()-(loan.getOutStanding()*rate)));
                    System.out.println("remaining Balance:....." + (loan.getOutStanding()-(loan.getEmi()-(loan.getOutStanding()*rate))));
                    loan.setOutStanding((loan.getOutStanding()-(loan.getEmi()-(loan.getOutStanding()*rate))));
                   loan.setRemainingPeriod(loan.getRemainingPeriod()-1);
                   emi = loan.getEmi();
                   interset = loan.getOutStanding() * rate;
                   principal = emi - interset;

                }
                LoanRepayment entity = LoanRepayment.builder()
                        .loan(loan)
                        .principal(principal)
                        .interset(interset)
                        .crassLoanRepayment(principal+interset)
                        .forMonth(LocalDate.now())
                        .crassLoanRepayment(crassLoanRepayment)
                        .build();

                dataList.add(entity);
                lsLoan.add(loan);
            }

            if (dataList.isEmpty()) {
                throw new SavingAndLoanRepaymentSaveFileException("No valid records found in file!");
            }

            loanRepository.saveAll(lsLoan);
            repository.saveAll(dataList);
            LoanRepaymentDetails saveDetails = LoanRepaymentDetails.builder()
                    .fileName(file.getOriginalFilename())
                    .forMonth(forMonth)
                    .dataSize(dataList.size())
                    .status("completed")
                    .build();

            loanRepaymentDetailsRepository.save(saveDetails);

            return ResponseEntity.ok(dataList);

        } catch (IOException | CsvValidationException e) {
            LoanRepaymentDetails errorDetails = LoanRepaymentDetails.builder()
                    .fileName(file.getOriginalFilename())
                    .forMonth(forMonth)
                    .status("fails du to "+e.getMessage())
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
        return loanRepayment;

    }

    public Page<LoanRepayment> findByLoanIds(Long   employeeId, Pageable pageable){

        return  loanRepaymentRepository.findByLoan_Employee_EmployeeId(employeeId ,pageable);

    }

    @Transactional
    public void  deleteByLoanId(String loanId) {
      loanRepaymentRepository.deleteByLoan_Id(loanId);


    }



    public Double findTotalCraLoanRepaymenets(Long  employeeId){

        return loanRepaymentRepository.findTotalLoanRepaymentByEmployeeId(employeeId);
    }

  public Double  sumAllLoanRepayments(){

        return loanRepaymentRepository.sumCrassLoanRepayment();
  }



  public LoanRepayment singleLoanRepaymnt(LoanRepayment loanRepayment){
      System.out.println("loan Repayments :"+loanRepayment);
      Loan loan=loanRepository.findByEmployee_EmployeeIdAndStatus(loanRepayment.getLoan().getEmployee().getEmployeeId(),Status.ACTIVE);
       double rate = loan.getAnnualInterest() / applicationProps.getAnnualPeriod();
      double emi=0;
      double interset=0;
      double principal=0;
      double remainningBalance=0;
      if (loan.getOutStanding()<=0){
          throw new LoanRepaymentsException("This Loan Repayments Completed  !");
      }
       checkConsecutiveMonth(loan,loanRepayment.getForMonth());
      if(loan.getEmi()==0) {

          emi = (loan.getOutStanding() * rate * (Math.pow(1 + rate, loan.getPeriod()))) /
                  ((Math.pow(1 + rate, loan.getPeriod())) - 1);
          interset = loan.getOutStanding() * rate;
          principal = emi - interset;
          remainningBalance = loan.getOutStanding() - principal;
          System.out.println("outStand:..." + loan.getOutStanding());
          System.out.println("rate:..." + rate);
          System.out.println("Monthly:..." + emi);
          System.out.println("interset:.." + interset);
          System.out.println("princpal:.." + principal);
          System.out.println("remaining Balance:....." + remainningBalance);
          loan.setOutStanding(remainningBalance);
          loan.setEmi(emi);
          loan.setRemainingPeriod(loan.getRemainingPeriod()-1);
      } else{
          System.out.println("outStand:..." + loan.getOutStanding());
          System.out.println("rate:..." + rate);
          System.out.println("Monthly:..." + loan.getEmi());
          System.out.println("interset:.." + loan.getOutStanding()*rate);
          System.out.println("princpal:.." + (loan.getEmi()-(loan.getOutStanding()*rate)));
          System.out.println("remaining Balance:....." + (loan.getOutStanding()-(loan.getEmi()-(loan.getOutStanding()*rate))));
          loan.setOutStanding((loan.getOutStanding()-(loan.getEmi()-(loan.getOutStanding()*rate))));
         loan.setRemainingPeriod(loan.getRemainingPeriod()-1);
          emi = loan.getEmi();
          interset = loan.getOutStanding() * rate;
          principal = emi - interset;

      }
      LoanRepayment entity = LoanRepayment.builder()
              .loan(loan)
              .principal(principal)
              .interset(interset)
              .forMonth(loanRepayment.getForMonth())
              .crassLoanRepayment(loanRepayment.getCrassLoanRepayment())
              .build();

    loanRepository.save(loan);

    return  entity ;
  }









    private void checkConsecutiveMonth(Loan loan, LocalDate repaymentDate) {
        if (loan.getLastPaidMonth() == null) {
            // No previous payment, allow the first repayment
            return;
        }

        // Convert last paid month to YearMonth
    YearMonth lastPaid = YearMonth.from(loan.getLastPaidMonth());
      YearMonth repaymentMonth =YearMonth.from(repaymentDate);

     YearMonth expectedNextMonth = lastPaid.plusMonths(1);

        if (!repaymentMonth.equals(expectedNextMonth)) {
            throw new LoanRepaymentsException(
                    "Invalid repayment date for employee " + loan.getEmployee().getEmployeeId() +
                            ". Expected: " + expectedNextMonth + ", but got: " + repaymentMonth
            );
        }
    }









    @Transactional
    public List<LoanRepayment> generateRepaymentsUpToNow(Long employeeId) {

        Loan loan = loanRepository
                .findByEmployee_EmployeeIdAndStatus(employeeId, Status.ACTIVE);

        if (loan == null) {
            throw new LoanRepaymentsException("Active loan not found for employee " + employeeId);
        }

        if (loan.getEffectiveDate() == null) {
            throw new LoanRepaymentsException("Loan effective date is missing");
        }

        if (loan.getOutStanding() <= 0 || loan.getRemainingPeriod() <= 0) {
            throw new LoanRepaymentsException("Loan already completed");
        }

        List<LoanRepayment> repayments = new ArrayList<>();

        double monthlyRate =
                loan.getAnnualInterest() / applicationProps.getAnnualPeriod();

        YearMonth startMonth = YearMonth.from(loan.getEffectiveDate());
        YearMonth endMonth = YearMonth.now().minusMonths(1);

        // If repayments already started, continue from lastPaidMonth
//        if (loan.getLastPaidMonth() != null && loan.em) {
//            startMonth = YearMonth.from(loan.getLastPaidMonth()).plusMonths(1);
//        }

        for (YearMonth ym = startMonth;
             !ym.isAfter(endMonth);
             ym = ym.plusMonths(1)) {

            if (loan.getOutStanding() <= 0 || loan.getRemainingPeriod() <= 0) {
                break;
            }

            double emi;
            double interest;
            double principal;

            if (loan.getEmi() == 0) {
                // EMI calculation (only once)
                emi = (loan.getOutStanding() * monthlyRate *
                        Math.pow(1 + monthlyRate, loan.getRemainingPeriod())) /
                        (Math.pow(1 + monthlyRate, loan.getRemainingPeriod()) - 1);

                loan.setEmi(emi);
            } else {
                emi = loan.getEmi();
            }

            interest = loan.getOutStanding() * monthlyRate;
            principal = emi - interest;

            if (principal > loan.getOutStanding()) {
                principal = loan.getOutStanding();
                emi = principal + interest;
            }

            double newOutstanding = loan.getOutStanding() - principal;

            LoanRepayment repayment = LoanRepayment.builder()
                    .loan(loan)
                    .forMonth(ym.atDay(1))
                    .principal(principal)
                    .interset(interest)
                    .crassLoanRepayment(emi)
                    .build();

            repayments.add(repayment);

            // update loan
            loan.setOutStanding(newOutstanding);
            loan.setRemainingPeriod(loan.getRemainingPeriod() - 1);
            loan.setLastPaidMonth(ym.atDay(1));
        }

        loanRepository.save(loan);
        loanRepaymentRepository.saveAll(repayments);

        return repayments;
    }












}
