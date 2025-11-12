package enat.bank.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SavingAndLoanRepaymentsExcelMapper {
    private String employeeId;
    private String fullName;
    private String   craSaving;
    private String   crassLoanRepayment;
}
