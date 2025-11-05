package enat.bank.Security;

import enat.bank.User.User;
import enat.bank.Utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

     private final AuthenticationManager authManager;
     private final JwtService jwtService;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User user) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
        String token = jwtService.generateToken(user.getUserName());
        return Map.of("token", token);
    }
}
