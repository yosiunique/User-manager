package enat.bank.role;

import com.fasterxml.jackson.annotation.JsonBackReference;
import enat.bank.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false,unique = true)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id",nullable = false)
    private RoleTypes roleTypes;
    @ManyToOne
    @JoinColumn(name="user_id" ,nullable = false)
    @JsonBackReference
//    @ToString.Exclude
    private User user;
}
