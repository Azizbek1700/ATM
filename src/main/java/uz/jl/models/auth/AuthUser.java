package uz.jl.models.auth;

import lombok.*;
import uz.jl.App;
import uz.jl.configs.AppConfig;
import uz.jl.enums.Status;
import uz.jl.enums.auth.Role;
import uz.jl.enums.settings.Language;
import uz.jl.models.Auditable;

import java.util.Date;

/**
 * @author Elmurodov Javohir, Mon 6:18 PM. 11/29/2021
 */
@Getter
@Setter
@AllArgsConstructor
@ToString(callSuper = true)
@Builder(builderMethodName = "childBuilder")
public class AuthUser extends Auditable {
    private String fullName;
    private String username;
    private String password;
    private String passportNumber;
    private Status status = Status.ACTIVE;
    private Role role;
    private String phoneNumber;
    private String branchId;
    private Language language;

    public AuthUser() {
        super();
        this.language = AppConfig.language;
        this.role = Role.ANONYMOUS;
    }


}
