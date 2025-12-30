package enat.bank.loan;

import enat.bank.Employee.Employee;
import enat.bank.Employee.Status;
import enat.bank.Employee.StatusConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Builder
@Entity
@Table(name="loan")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false)
    private String loanId;
    @ManyToOne()
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @Column(nullable = false)
    private LocalDate effectiveDate;
    @Column(nullable = false)
    private  Double outStanding;
    @Convert(converter = StatusConverter.class)
    private Status status;
    @Column(nullable = false)
    private Double emi;
    @Column(nullable = false)
    private  double annualInterest;
    @Column(nullable = false)
    private double  period;
    @Column(nullable = false)
    private double firstOutStanding;



}
