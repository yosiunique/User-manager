package enat.bank.loan;

import enat.bank.employee.Employee;
import enat.bank.employee.Status;
import enat.bank.employee.StatusConverter;
import enat.bank.loansRepayments.LoanRepayment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Builder
@Entity
@Table(name = "loan")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String loanId;
    @ManyToOne()
    @JoinColumn(name = "employee_id")
   @JsonIgnoreProperties({"loans", "savings"})
    private Employee employee;
    @Column(nullable = false)
    private LocalDate effectiveDate;
    //    @Column(nullable = false)
    private LocalDate lastPaidMonth;
    @Column(nullable = false)
    private Double outStanding;
    @Convert(converter = StatusConverter.class)
    private Status status;
    @Column(nullable = false)
    private Double emi;
    @Column(nullable = false)
    private double annualInterest;
    @Column(nullable = false)
    private double period;
    private Integer remainingPeriod;
    @Column(nullable = false)
    private double firstOutStanding;
    @OneToMany(mappedBy = "loan", fetch = FetchType.LAZY)
    @JsonManagedReference 
    private List<LoanRepayment> repayments;


}
