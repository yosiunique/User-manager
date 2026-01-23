package enat.bank.saving;

import enat.bank.employee.Employee;
import enat.bank.utils.Auditable;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="saving")
public class Saving extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "employee_id")
   @JsonIgnoreProperties({"loans", "savings"})
    private Employee employee;
    private double   craSaving;
    private LocalDate forMonth;
}
