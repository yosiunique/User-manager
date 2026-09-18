package enat.bank.security;

import enat.bank.user.User;
import enat.bank.user.UserLogging;
import enat.bank.user.UserLoginRepo;
import enat.bank.user.UserRepository;
import enat.bank.utils.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(
        name = "Authentication",
        description = "Handles user authentication and JWT token generation for secured access."
)
@Log4j2
public class AuthController {
   private  final UserRepository userRepository;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserLoginRepo userLoginRepo;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @Operation(
            summary = "User Login",
            description = "Authenticates a user using username and password, and returns a JWT token if successful.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login successful. Returns a JWT token.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = "{\"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Invalid username or password.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = "{\"error\": \"Unauthorized\"}")
                            )
                    )
            }
    )
    public Map<String, String> login(@RequestBody User user) {


        log.info(passwordEncoder.encode(user.getPassword()));
        Authentication authentication=authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUserName(),
                        user.getPassword()
                )
        );



        User userDetails=userRepository.findByUserName(user.getUserName());
        String token = jwtService.generateToken(userDetails);

        UserLogging logging = new UserLogging();
        logging.setLoginTime(LocalDateTime.now());
        logging.setToken(token);
        logging.setUser(userDetails);
        userLoginRepo.save(logging);

        return Map.of("token", token);
    }

    @PostMapping("/logout")
    @Operation(summary = "User Logout", description = "Invalidates the current session by setting logout time.")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing or invalid token"));
        }
        String token = authHeader.substring(7);
        userLoginRepo.findByToken(token).ifPresent(logging -> {
            logging.setLogoutTime(LocalDateTime.now());
            userLoginRepo.save(logging);
        });
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
}
