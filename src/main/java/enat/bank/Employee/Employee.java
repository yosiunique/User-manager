package enat.bank.Employee;

import enat.bank.utils.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name="employee")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee extends Auditable {
    @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable=false)
    private Long employeeId;
    @Column(nullable = false)
    private String employeeFullName;
    @Column(nullable = false)
    private LocalDate effectiveDate;
    @Column(nullable = false)
    private  Double outStanding;
    @Convert(converter = StatusConverter.class)
    private  Status status;
    @Column(nullable = false)
    private Double emi;
    @Column(nullable = false)
    private Long loanId;
    @Column(nullable = false)
    private  double annualInterest;
    @Column(nullable = false)
    private double  period;
    @Column(nullable = false)
    private double firstOutStanding;


}
