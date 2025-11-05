package enat.bank.admin;
import enat.bank.User.User;
import enat.bank.User.UserDto;
import enat.bank.User.UserMapper;
import enat.bank.User.UserRepository;
import enat.bank.Utils.Common;
import enat.bank.exception.UserAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class AdminController implements Common<User, String, String,UserDto> {

    private final PasswordEncoder passwordEncoder;

    private final  AdminService adminService;

    private final  UserRepository userRepository;

    @Override
    public ResponseEntity<User> create(@RequestBody  User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User exist=userRepository.findByUserName(user.getUserName());
        if(exist!=null){
           throw new UserAlreadyExistException("user already exists!");
        }
        return adminService.create(user);
    }

    @Override
    public ResponseEntity<User> update(@PathVariable("id")  Long id ,@RequestBody User user) {
        User exist=userRepository.findById(id).orElseThrow(()->new
                        UserAlreadyExistException("no User with in this ID:.."+id)
                );

            exist.setFirstName(user.getFirstName());
            exist.setLastName(user.getLastName());
            exist.setUserName(user.getUserName());
            exist.setEmail(user.getEmail());
            exist.setPassword(passwordEncoder.encode(user.getPassword()));
         return   adminService.update(exist ,id);
    }

    @Override
    public void delete(@PathVariable("id") Long id) {
 adminService.delete(id);
    }

    @Override
    public Optional<UserDto> getById(Long id) {



               Optional<User> user=adminService.getById(id);


        return  user.map(UserMapper::toDto);
    }

    @Override
    public Page<UserDto> getAllPageable(@PageableDefault Pageable pageable,@RequestParam(name="name" ,required = false) String name) {
        Page<User> users=adminService.getAllPageable(pageable);

        return  users.map(UserMapper::toDto);
    }




}
