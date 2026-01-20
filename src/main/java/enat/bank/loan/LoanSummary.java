package enat.bank.loan;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;


@Getter
@Setter
@Builder
public class  LoanSummary {
private double totalPrincipal;
    private double totalInterest;
    private double totalGross;

}