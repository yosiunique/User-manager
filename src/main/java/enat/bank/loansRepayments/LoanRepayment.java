package enat.bank.loansRepayments;
import enat.bank.loan.Loan;
import enat.bank.utils.Auditable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "loan_repayments")

@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE loan_repayments SET deleted = true  ,updatedAt = ?  WHERE id =?")

public class LoanRepayment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="loan_id")
    private Loan loan;
    private double principal;
    private double interset;
    private LocalDate forMonth;
    private double crassLoanRepayment;
}
