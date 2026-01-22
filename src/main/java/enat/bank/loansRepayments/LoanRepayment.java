package enat.bank.loansRepayments;
import enat.bank.loan.Loan;
import enat.bank.utils.Auditable;
import jakarta.persistence.*;
import lombok.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "loan_repayments")

public class LoanRepayment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="loan_id")
@JsonBackReference
    private Loan loan;
    private double principal;
    private double interset;
    private LocalDate forMonth;
    private double crassLoanRepayment;
    
}
