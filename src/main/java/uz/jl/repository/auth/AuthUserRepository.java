package uz.jl.repository.auth;

import uz.jl.enums.Status;
import uz.jl.models.auth.AuthUser;
import uz.jl.repository.BaseRepository;
import uz.jl.services.filesystems.UsersDB;

import java.util.Objects;

/**
 * @author Elmurodov Javohir, Mon 6:19 PM. 11/29/2021
 */
public class AuthUserRepository extends BaseRepository<AuthUser> {
    private static AuthUserRepository instance;

    public static AuthUserRepository getInstance() {
        if (instance == null) {
            instance = new AuthUserRepository();
        }
        return instance;
    }

    @Override
    protected void save(AuthUser user) {

    }

    public AuthUser findByUserName(String username) {
        for (AuthUser user : UsersDB.getUsers()) {
            if (Objects.nonNull(user)&& user.getUsername() != null && user.getUsername().equals( username )) return user;
            if (Objects.nonNull(user) && Objects.nonNull(user.getUsername()) && user.getUsername().equals( username )) return user;
        }
        return null;
    }

    public AuthUser findByUserName(String username, Status status) {
        for (AuthUser user : UsersDB.getUsers()) {
            if (Objects.nonNull(user) && user.getUsername() != null && user.getUsername().equals( username ) && !user.getStatus().equals( status )) return user;
        }
        return null;
    }


    public AuthUser findByPassportSerial(String passportCode) {
        for (AuthUser user : UsersDB.getUsers()) {
            if (Objects.nonNull( user )&&  user.getPassportNumber() != null && user.getPassportNumber().equals(passportCode) ) {
                return user;
            }
        }
        return null;
    }
}
