package enat.bank.admin;

import enat.bank.User.User;
import enat.bank.User.UserRepository;
import enat.bank.Utils.CommonService;
import org.springframework.stereotype.Service;
@Service
public class AdminService extends CommonService<User,Long ,String> {
    public AdminService(UserRepository userRepository ) {
        super(userRepository);
    }

}
