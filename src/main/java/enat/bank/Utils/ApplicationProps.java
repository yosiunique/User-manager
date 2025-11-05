package enat.bank.Utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "application")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Primary
public class ApplicationProps {

   private   List<String>  allowedOrigin;
   private  String jwt_secret_key;
   private String  jwt_expiration_time;

}
