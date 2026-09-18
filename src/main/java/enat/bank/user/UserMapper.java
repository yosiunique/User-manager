package enat.bank.user;


import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper{


    UserDto toDto(User user);
    User toEntity(UserDto userDto) ;


}
