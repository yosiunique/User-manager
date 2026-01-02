package enat.bank.admin;

import enat.bank.user.User;
import enat.bank.user.UserDto;
import enat.bank.user.UserMapper;
import enat.bank.user.UserRepository;
import enat.bank.utils.Common;
import enat.bank.exception.UserAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Optional;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
@Tag(
        name = "User Management (Admin)",
        description = "Provides CRUD operations for managing system users including create, update, delete, and fetch user details."
)
public class AdminController implements Common<User, String, String, UserDto> {

    private final PasswordEncoder passwordEncoder;
    private final AdminService adminService;
    private final UserRepository userRepository;

    @Override
    @Operation(
            summary = "Create a new user",
            description = "Allows an admin to create a new user account in the system. The password is automatically encoded before saving."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "User already exists or invalid input")
    })
    public ResponseEntity<User> create(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User exist = userRepository.findByUserName(user.getUserName());
        if (exist != null) {
            throw new UserAlreadyExistException("User already exists!");
        }
         adminService.create(user);
        user.setPassword("");
        return  ResponseEntity.ok(user);
    }


    @Override
    @Operation(
            summary = "Update an existing user",
            description = "Allows an admin to update an existing user by their ID. Passwords are re-encoded before saving."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully updated"),
            @ApiResponse(responseCode = "404", description = "User with the specified ID not found")
    })
    public ResponseEntity<User> update(@PathVariable("id") Long id, @RequestBody User user) {
        User exist = userRepository.findById(id).orElseThrow(
                () -> new UserAlreadyExistException("No user found with this ID: " + id)
        );
        exist.setFirstName(user.getFirstName());
        exist.setLastName(user.getLastName());
        exist.setEmail(user.getEmail());
        exist.setEnable(user.getEnable());
        exist.setPhoneNumber(user.getPhoneNumber());
        exist.setAttribute(user.getAttribute());
        adminService.update(exist, id);
          exist.setPassword("");
         return ResponseEntity.ok(exist);
    }


    @Override
    @Operation(
            summary = "Delete a user",
            description = "Allows an admin to delete a user from the system by their unique ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public void delete(@PathVariable("id") Long id) {
        adminService.delete(id);
    }


    @Override
    @Operation(
            summary = "Get user by ID",
            description = "Retrieves user information by their unique ID and returns it as a UserDto."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Optional<UserDto> getById(@PathVariable Long id) {
        Optional<User> user = adminService.getById(id);
        return user.map(UserMapper::toDto);
    }


    @Override
    @Operation(
            summary = "List all users (paginated)",
            description = "Retrieves all users with pagination support. Optional search by name."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users retrieved successfully")
    })
    public Page<UserDto> getAllPageable(
            @PageableDefault Pageable pageable,
            @RequestParam(name = "name", required = false) String name) {

        Page<User> users = adminService.getAllPageable(pageable);
        return users.map(UserMapper::toDto);
    }

 @GetMapping("find_by_username/{userName}")
    public UserDto findByUserName(@PathVariable("userName") String userName){
        return UserMapper.toDto(userRepository.findByUserName(userName));

 }

 @PutMapping("/reset")
 public ResponseEntity<UserDto> update(@RequestBody User user) {
     System.out.println("this is user data ...."+user);
     User exist = userRepository.findById(user.getId()).orElseThrow(
             () -> new UserAlreadyExistException("No user found with this ID: " + user.getId())
     );
     exist.setPassword(passwordEncoder.encode(user.getPassword()));
     exist.setReset(user.getReset());

     return ResponseEntity.ok(UserMapper.toDto(adminService.update(exist, user.getId()).getBody()));

 }

 @GetMapping("count")
    public Long countAllUsers(){

        return adminService.countAllUsers();
 }



    @GetMapping("/search-by-username/{username}")
    public Page<UserDto> searchByUsername(
            @PathVariable String username,
            Pageable pageable) {
        return adminService.searchByUsername(username, pageable);
    }





}
