package enat.bank.saving;

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
@Table(name="saving")
public class Saving extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    private double   craSaving;
    private Boolean status;
    private LocalDate forMonth;
}
