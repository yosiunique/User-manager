package enat.bank.admin;

import enat.bank.user.User;
import enat.bank.user.UserDto;
import enat.bank.user.UserMapper;
import enat.bank.user.UserRepository;
import enat.bank.utils.CommonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
@Service
public class AdminService extends CommonService<User,Long ,String> {
    private final UserMapper userMapper;
    private UserRepository userRepository;
    public AdminService(UserRepository userRepository, UserMapper userMapper) {
        super(userRepository);
        this.userRepository=userRepository;
        this.userMapper = userMapper;
    }


    public  Long countAllUsers(){
        return userRepository.count();
    }


  Page<UserDto> searchByUsername(String username, Pageable pageable){

        Page<User>  users=userRepository.findByUserNameContainingIgnoreCase(username ,pageable);

        return  users.map(userMapper::toDto);
  }

}
