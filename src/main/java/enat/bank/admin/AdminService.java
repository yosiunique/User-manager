package enat.bank.admin;

import enat.bank.user.User;
import enat.bank.user.UserRepository;
import enat.bank.utils.CommonService;
import org.springframework.stereotype.Service;
@Service
public class AdminService extends CommonService<User,Long ,String> {
    public AdminService(UserRepository userRepository ) {
        super(userRepository);
    }

}
