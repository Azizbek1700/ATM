package uz.jl.mapper.auth;

import uz.jl.dtos.auth.AuthUserDto;
import uz.jl.mapper.BaseMapper;
import uz.jl.models.auth.AuthUser;

/**
 * @author Elmurodov Javohir, Mon 6:20 PM. 12/6/2021
 */
public class AuthUserMapper {
    //TODO o'zim qushdim

    private static AuthUserMapper authUserMapper;

    public static AuthUserMapper getInstance() {
        if (authUserMapper == null)
            authUserMapper = new AuthUserMapper();

        return authUserMapper;
    }

//    public void fromDBToDto(AuthUser user){
//        AuthUserDto authUserDto = new AuthUserDto();
//        authUserDto.setUsername(user.getUsername());
//        authUserDto.setPhoneNumber(user.getPhoneNumber());
//        authUserDto.setId(user.getId());
//
//    }
}
