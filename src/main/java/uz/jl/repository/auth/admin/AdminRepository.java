package uz.jl.repository.auth.admin;

import uz.jl.models.auth.AuthUser;
import uz.jl.repository.BaseRepository;
import uz.jl.repository.auth.AuthUserRepository;

/**
 * @author Doston Bokhodirov, Thu 6:36 PM. 12/9/2021
 */
public class AdminRepository extends BaseRepository<AuthUser> {
    private static AuthUserRepository authUserRepository;

    public static AuthUserRepository getInstance() {
        if (authUserRepository == null) {
            authUserRepository = new AuthUserRepository();
        }
        return authUserRepository;
    }
}
