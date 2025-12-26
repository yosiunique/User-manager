package enat.bank.share;
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
   @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long   employeeId;
    private String  membershipId;
    private String fullName;
    private Double  totalSaving;
    private Double  share;
    private Double  noOfShare;

}
