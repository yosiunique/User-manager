package enat.bank.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import enat.bank.utils.Auditable;
import enat.bank.role.Roles;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name ="jwt_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private String  firstName;
    private  String lastName;
    @Column(unique = true)
    private String userName;
    private String password;
    private String phoneNumber;
    private String email ;
    private String attribute;
    private  Boolean enable;
    private  Boolean set_;
    private  Boolean reset;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    @ToString.Exclude
    private List<Roles> roles;
}
