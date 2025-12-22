package enat.bank.security;

import enat.bank.user.User;
import enat.bank.user.UserRepository;
import enat.bank.utils.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(
        name = "Authentication",
        description = "Handles user authentication and JWT token generation for secured access."
)
public class AuthController {
   private  final UserRepository userRepository;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    @GetMapping("/{id}")
    public void updateUser(@PathVariable("id") Long id){
        User user=userRepository.findById(id).get();

        user.setPassword(passwordEncoder.encode("1234"));
        userRepository.save(user);

    }

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

        Authentication authentication=authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUserName(),
                        user.getPassword()
                )
        );



        User userDetails=userRepository.findByUserName(user.getUserName());
        String token = jwtService.generateToken(userDetails);
        return Map.of("token", token);
    }
}
