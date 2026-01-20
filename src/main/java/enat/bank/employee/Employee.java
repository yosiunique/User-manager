package enat.bank.employee;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import enat.bank.loan.Loan;
import enat.bank.saving.Saving;
import enat.bank.utils.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Entity
@Table(name="employee")
@Getter
@Setter
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
   @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Loan> loans;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    @JsonIgnore 
    private List<Saving> savings;

    @JsonProperty("totalRepayments")
    public Double totalRepaymnts() {
        if (loans == null) return 0.0;
        return loans.stream()
                .filter(loan -> loan.getRepayments() != null)
                .flatMap(loan -> loan.getRepayments().stream())
                .mapToDouble(r -> r.getInterset() + r.getPrincipal())
                .sum(); 
    }

    @JsonProperty("totalSaving")
    public Double totalSavings() {
        if (savings == null) return 0.0;  
        return savings.stream()
                .mapToDouble(Saving::getCraSaving)
                .sum();
    }
}
