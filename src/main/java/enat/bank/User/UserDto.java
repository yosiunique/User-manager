package enat.bank.User;

import enat.bank.role.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private  Long id;
    private String  firstName;
    private  String lastName;
    private String userName;
    private String email ;
    private String attribute;
    private  Boolean enable;
    private List<Roles> role;
}
