package uz.jl.services.auth;

import uz.jl.configs.Session;
import uz.jl.enums.Status;
import uz.jl.enums.settings.Language;
import uz.jl.mapper.auth.AuthUserMapper;
import uz.jl.models.auth.AuthUser;
import uz.jl.repository.auth.AuthUserRepository;
import uz.jl.response.ResponseEntity;
import uz.jl.response.ResponseStatus;
import uz.jl.services.BaseAbstractService;
import uz.jl.services.filesystems.UsersDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Elmurodov Javohir, Mon 6:23 PM. 11/29/2021
 */

public class AuthUserService extends BaseAbstractService<AuthUser, AuthUserRepository, AuthUserMapper> {
    private static AuthUserService authUserService;

    private AuthUserService(AuthUserRepository repository, AuthUserMapper mapper) {
        super(repository, mapper);
    }

    public static AuthUserService getInstance() {
        if (authUserService == null) {
            authUserService = new AuthUserService(AuthUserRepository.getInstance(), AuthUserMapper.getInstance());
        }
        return authUserService;
    }

    @Override
    public ResponseEntity<List<AuthUser>> getAll() {
        List<AuthUser> users = new ArrayList<>();
        return new ResponseEntity<>(users, ResponseStatus.HTTP_NOT_FOUND);
    }

    @Override
    public ResponseEntity<AuthUser> get(String id) {
        return new ResponseEntity<>(new AuthUser());
    }

    public ResponseEntity<String> login(String username, String password) {
        AuthUser user = repository.findByUserName(username);
        if (Objects.isNull(user) || !user.getPassword().equals(password) || user.getStatus().equals(Status.DELETED) ) {
            return new ResponseEntity<>("Bad Credentials", ResponseStatus.HTTP_FORBIDDEN);
        }

        if ( user.getStatus().equals(Status.BLOCKED) ) {
            return new ResponseEntity<>("This account blocked!!!", ResponseStatus.HTTP_FORBIDDEN);
        }
        Session.getInstance().setUser(user);
        return new ResponseEntity<>("Successfully");
    }

    public ResponseEntity<String> changeLanguage(String lan) {
        Language language = Language.getByCode(lan);

        if ( Objects.isNull(language) ) {
            return new ResponseEntity<>("Bad credentials", ResponseStatus.HTTP_FORBIDDEN);
        }

        Session.getInstance().getUser().setLanguage(language);
        UsersDB.writeUsers(UsersDB.getUsers());
        return new ResponseEntity<>("Language changed!!!");
    }
}
