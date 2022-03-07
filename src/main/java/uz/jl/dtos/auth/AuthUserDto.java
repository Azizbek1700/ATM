package uz.jl.dtos.auth;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import uz.jl.dtos.BaseGenericDto;
import uz.jl.enums.Status;
import uz.jl.models.auth.AuthUser;
import uz.jl.services.filesystems.UsersDB;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Elmurodov Javohir, Mon 6:24 PM. 12/6/2021
 */


@Getter
@Setter
public class AuthUserDto extends BaseGenericDto {
    private String fullName;
    private String username;
    private String phoneNumber;
    private Status status;

}
