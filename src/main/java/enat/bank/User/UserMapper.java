package enat.bank.User;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserDto toDto(User user) {
        if (user == null) return null;

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setAttribute(user.getAttribute());
        dto.setEnable(user.getEnable());
        dto.setRole(user.getRole());
        return dto;
    }

    public static User toEntity(UserDto dto) {
        if (dto == null) return null;

        User user = new User();
        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setAttribute(dto.getAttribute());
        user.setEnable(dto.getEnable());
        user.setRole(dto.getRole());
        return user;
    }

    public static java.util.List<UserDto> toDtoList(java.util.List<User> users) {
        return users.stream().map(UserMapper::toDto).collect(Collectors.toList());
    }
}
