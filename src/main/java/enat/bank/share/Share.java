package enat.bank.share;
import enat.bank.Employee.Employee;
import enat.bank.utils.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="share")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Share extends Auditable {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name="employee_id")
    private Employee employee;
    private Double  totalSaving;
    private Double  share;
    private Double  noOfShare;

}
