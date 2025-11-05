package enat.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class SavingAndLoanReapaymentTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SavingAndLoanReapaymentTrackerApplication.class, args);
	}

}
