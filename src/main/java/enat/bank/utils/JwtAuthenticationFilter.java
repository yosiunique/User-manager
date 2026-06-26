package enat.bank.utils;


import enat.bank.user.CustomUserDetailsService;
import enat.bank.user.UserLoginRepo;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

     private final  JwtService jwtService;
     private final CustomUserDetailsService userDetailsService;
     private  final UserLoginRepo userLoginRepo ;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        // Skip Swagger and OpenAPI endpoints
        if (path.startsWith("/api-docs") || path.startsWith("/swagger-ui") || path.equals("/swagger-ui.html")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String token = null, username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtService.isTokenValid(token)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                userLoginRepo.findByToken(token).ifPresent(logging -> {
                    if (logging.getLogoutTime() == null) {
                        logging.setLogoutTime(LocalDateTime.now());
                        userLoginRepo.save(logging);
                    }
                });
            }
        }

        filterChain.doFilter(request, response);
    }

}

