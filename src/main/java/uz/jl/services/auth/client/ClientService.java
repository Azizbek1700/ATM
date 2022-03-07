package uz.jl.services.auth.client;

import uz.jl.configs.Session;
import uz.jl.dtos.auth.AuthUserDto;
import uz.jl.enums.Status;
import uz.jl.enums.auth.Role;
import uz.jl.mapper.auth.AuthUserMapper;
import uz.jl.models.auth.AuthUser;
import uz.jl.repository.auth.AuthUserRepository;
import uz.jl.response.ResponseEntity;
import uz.jl.response.ResponseStatus;
import uz.jl.services.BaseAbstractService;
import uz.jl.services.filesystems.UsersDB;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ClientService extends BaseAbstractService<AuthUser, AuthUserRepository, AuthUserMapper> {

    private static ClientService clientService;

    private ClientService(AuthUserRepository repository, AuthUserMapper mapper){
        super(repository, mapper);
    }

    public static ClientService getInstance() {
        if ( clientService == null )
            clientService = new ClientService(AuthUserRepository.getInstance(), AuthUserMapper.getInstance());
        return clientService;
    }

    public ResponseEntity<String> create(String fullName, String phoneNumber, String passportCode) {
        if ( Objects.nonNull(repository.findByPassportSerial(passportCode))) {
            return new ResponseEntity<>("Username is already taken", ResponseStatus.HTTP_FORBIDDEN);
        }
        AuthUser user = new AuthUser();
        user.setFullName(fullName);
        user.setPhoneNumber(phoneNumber);
        user.setRole(Role.CLIENT);
        user.setPassportNumber(passportCode);
        user.setCreatedBy(Session.getInstance().getUser().getUsername());
        user.setUpdatedBy(Session.getInstance().getUser().getUsername());
        UsersDB.getUsers().add(user);
        UsersDB.writeUsers(UsersDB.getUsers());
        return new ResponseEntity<>("Employee is successfully created!");
    }

    public ResponseEntity<String> block(String passportSerial) {

        AuthUser user = repository.findByPassportSerial(passportSerial);
        if (Objects.isNull(user)) {
            return new ResponseEntity<>("User not found", ResponseStatus.HTTP_NOT_FOUND);
        }

        if ( !user.getRole().equals(Role.CLIENT) ) {
            return new ResponseEntity<>("User not found", ResponseStatus.HTTP_NOT_FOUND);
        }

        if ( user.getStatus().equals(Status.BLOCKED) ) {
            return new ResponseEntity<>("User is already blocked", ResponseStatus.HTTP_FORBIDDEN);
        }

        user.setStatus(Status.BLOCKED);
        user.setUpdatedAt(new Date());
        user.setUpdatedBy(Session.getInstance().getUser().getUsername());
        UsersDB.writeUsers(UsersDB.getUsers());
        return new ResponseEntity<>("User is successfully blocked");
    }

    public ResponseEntity<String> unblock(String passportCode) {

        AuthUser user = repository.findByPassportSerial(passportCode);
        if (Objects.isNull(user)) {
            return new ResponseEntity<>("User not found", ResponseStatus.HTTP_NOT_FOUND);
        }
        if ( !user.getRole().equals(Role.CLIENT)) {
            return new ResponseEntity<>("User not found", ResponseStatus.HTTP_NOT_FOUND);
        }

        if (user.getStatus().equals(Status.ACTIVE)) {
            return new ResponseEntity<>("User is active", ResponseStatus.HTTP_FORBIDDEN);
        }
        user.setUpdatedAt(new Date());
        user.setUpdatedBy(Session.getInstance().getUser().getUsername());
        user.setStatus(Status.ACTIVE);
        UsersDB.writeUsers(UsersDB.getUsers());
        return new ResponseEntity<>("User is successfully unblocked");
    }

    public ResponseEntity<String> delete(String passportCode) {
        AuthUser user = repository.findByPassportSerial(passportCode);
        if (Objects.isNull(user)) {
            return new ResponseEntity<>("User not found", ResponseStatus.HTTP_NOT_FOUND);
        }

        if ( !user.getRole().equals(Role.CLIENT) ) {
            return new ResponseEntity<>("User not found", ResponseStatus.HTTP_NOT_FOUND);
        }

        if (user.getStatus().equals(Status.DELETED)) {
            return new ResponseEntity<>("User is already deleted", ResponseStatus.HTTP_FORBIDDEN);
        }
        user.setCreatedBy(Session.getInstance().getUser().getUsername());
        user.setUpdatedBy(Session.getInstance().getUser().getUsername());
        user.setUpdatedAt(new Date());
        user.setStatus(Status.DELETED);
        UsersDB.writeUsers(UsersDB.getUsers());
        return new ResponseEntity<>("User is successfully deleted");
    }

    public ResponseEntity<List<AuthUserDto>> showByStatus(Role role, Status status) {
        List<AuthUserDto> clientDtoList = new ArrayList<>();
        for (AuthUser user : UsersDB.getUsers()) {
            if (user.getRole().equals(role) && user.getStatus().equals(status)) {
                AuthUserDto clientDto = new AuthUserDto();
                clientDto.setFullName(user.getFullName());
                clientDto.setUsername(user.getUsername());
                clientDto.setPhoneNumber(user.getPhoneNumber());
                clientDtoList.add(clientDto);
            }
        }
        return new ResponseEntity<>(clientDtoList);
    }

    public ResponseEntity<List<AuthUserDto>> showIgnoreStatus(Role role, Status status) {
        List<AuthUserDto> clientDtoList = new ArrayList<>();
        for (AuthUser user : UsersDB.getUsers()) {
            if (user.getRole().equals(role) && !user.getStatus().equals(status)) {
                AuthUserDto clientDto = new AuthUserDto();
                clientDto.setFullName(user.getFullName());
                clientDto.setUsername(user.getUsername());
                clientDto.setPhoneNumber(user.getPhoneNumber());
                clientDtoList.add(clientDto);
            }
        }
        return new ResponseEntity<>(clientDtoList);
    }

    @Override
    public ResponseEntity<List<AuthUser>> getAll() {
        return null;
    }

    @Override
    public ResponseEntity<AuthUser> get(String id) {
        return null;
    }
}
