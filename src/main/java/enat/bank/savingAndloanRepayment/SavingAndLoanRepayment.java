package enat.bank.savingAndloanRepayment;

import enat.bank.Utils.Auditable;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="saving_and_loan_repayment")
public class SavingAndLoanRepayment  extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String employeeId;
    private String fullName;
    private double   craSaving;
    private double  crassLoanRepayment;


}
