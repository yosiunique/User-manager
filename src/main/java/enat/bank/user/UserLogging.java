package enat.bank.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name ="session_tacking")
@AllArgsConstructor
@NoArgsConstructor
public class UserLogging {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private LocalDateTime loginTime ;
    private LocalDateTime logoutTime ;
    private  String token ;
    @ManyToOne
    @JoinColumn(name = "user_id" ,nullable = false )
    private User user ;
}
