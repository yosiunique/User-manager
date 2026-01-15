package enat.bank.loan;

import enat.bank.employee.Employee;
import enat.bank.employee.Status;
import enat.bank.employee.StatusConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


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


}
