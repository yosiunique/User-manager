package enat.bank.config;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Saving and Loan Repayment Tracker API",
                version = "1.0",
                description = "This API manages loan repayments, savings, and customer transactions in the Savings and Loan Repayment Tracking System.",
                contact = @Contact(
                        name = "Yoseph Getachew",
                        email = "getachewy307@gmail.com",
                        url = " http://10.1.12.72/dev-team/savingandloan-repayments.git"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local Development Server"),
                @Server(url = "http://10.1.22.70:8061", description = "Test Server"),
                @Server(url = "http://10.1.12.70:8061", description = "Production Server")
        }
)
public class OpenApiConfig {
}
