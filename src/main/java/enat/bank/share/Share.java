package enat.bank.share;
import enat.bank.employee.Employee;
import enat.bank.utils.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="share")
@Getter
@Setter
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
    private Double  share;
    private Double  noOfShare;




}
