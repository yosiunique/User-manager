package enat.bank.loansRepayments;

import enat.bank.Employee.Employee;
import enat.bank.utils.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="loan_repayments")
public class LoanRepayment extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employee;
    private String fullName;
    private double principal;
    private double interset;
    private LocalDate forMonth;
    private double  crassLoanRepayment;



}
