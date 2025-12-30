package enat.bank.Employee;

import enat.bank.utils.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Entity
@Table(name="employee")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee extends Auditable {
    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private Long employeeId;
    @Column(nullable = false)
    private String membershipId;
    @Column(nullable = false)
    private String employeeFullName;

}
