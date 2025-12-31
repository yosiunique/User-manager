package enat.bank.admin;

import enat.bank.user.User;
import enat.bank.user.UserRepository;
import enat.bank.utils.CommonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
@Service
public class AdminService extends CommonService<User,Long ,String> {
    private UserRepository userRepository;
    public AdminService(UserRepository userRepository ) {
        super(userRepository);
        this.userRepository=userRepository;
    }


    public  Long countAllUsers(){
        return userRepository.count();
    }


  Page<User> searchByUsername(String username, Pageable pageable){

        return  userRepository.findByUserNameContainingIgnoreCase(username ,pageable);
  }

}
