package uz.jl.services.auth.employee;

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

public class EmployeeService extends BaseAbstractService<AuthUser, AuthUserRepository, AuthUserMapper> {

    private static EmployeeService employeeService;

    private EmployeeService(AuthUserRepository repository, AuthUserMapper mapper) {
        super(repository, mapper);
    }

    public static EmployeeService getInstance() {
        if (employeeService == null)
            employeeService = new EmployeeService(AuthUserRepository.getInstance(), AuthUserMapper.getInstance());
        return employeeService;
    }


    public ResponseEntity<String> create(String fullName, String username, String password, String phoneNumber,String passportNumber) {
        if (Objects.nonNull(repository.findByUserName(username))) {
            return new ResponseEntity<>("Username is already taken", ResponseStatus.HTTP_FORBIDDEN);
        }
        AuthUser user = new AuthUser();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);
        user.setRole(Role.EMPLOYEE);
        user.setPassportNumber(passportNumber);
        user.setCreatedBy(Session.getInstance().getUser().getUsername());
        user.setUpdatedBy(Session.getInstance().getUser().getUsername());
        UsersDB.getUsers().add(user);
        UsersDB.writeUsers(UsersDB.getUsers());
        return new ResponseEntity<>("Employee is successfully created!");
    }

    public ResponseEntity<String> create(String passportNumber, String username, String password) {

        if ( Objects.nonNull(repository.findByUserName(username)) ) {
            return new ResponseEntity<>("User already taken");
        }
        AuthUser user = repository.findByPassportSerial(passportNumber);

        user.setPassword(password);
        user.setUsername(username);
        user.setRole(Role.EMPLOYEE);
        user.setPassportNumber(passportNumber);
        user.setCreatedBy(Session.getInstance().getUser().getUsername());
        user.setUpdatedBy(Session.getInstance().getUser().getUsername());
        UsersDB.writeUsers(UsersDB.getUsers());
        return new ResponseEntity<>("Employee is successfully created!");
    }

    public ResponseEntity<String> block(String username) {
        AuthUser user = repository.findByUserName(username);
        if (Objects.isNull(user)) {
            return new ResponseEntity<>("User not found", ResponseStatus.HTTP_NOT_FOUND);
        }
        if (user.getStatus().equals(Status.BLOCKED)) {
            return new ResponseEntity<>("User is already blocked", ResponseStatus.HTTP_FORBIDDEN);
        }
        user.setStatus(Status.BLOCKED);
        user.setUpdatedBy(Session.getInstance().getUser().getUsername());
        user.setUpdatedAt(new Date());
        UsersDB.writeUsers(UsersDB.getUsers());
        return new ResponseEntity<>("User is successfully blocked");
    }

    public ResponseEntity<String> unblock(String username) {
        AuthUser user = repository.findByUserName(username);
        if (Objects.isNull(user)) {
            return new ResponseEntity<>("User not found", ResponseStatus.HTTP_NOT_FOUND);
        }
        if (user.getStatus().equals(Status.ACTIVE)) {
            return new ResponseEntity<>("User is already unblocked", ResponseStatus.HTTP_FORBIDDEN);
        }
        user.setStatus(Status.ACTIVE);
        user.setUpdatedBy(Session.getInstance().getUser().getUsername());
        user.setUpdatedAt(new Date());
        UsersDB.writeUsers(UsersDB.getUsers());
        return new ResponseEntity<>("User is successfully unblocked");
    }

    public ResponseEntity<String> delete(String username) {
        AuthUser user = repository.findByUserName(username, Status.DELETED);
        if (Objects.isNull(user)) {
            return new ResponseEntity<>("User not found", ResponseStatus.HTTP_NOT_FOUND);
        }
        if (user.getStatus().equals(Status.DELETED)) {
            return new ResponseEntity<>("User is already deleted", ResponseStatus.HTTP_FORBIDDEN);
        }
        user.setStatus(Status.DELETED);
        user.setUpdatedBy(Session.getInstance().getUser().getUsername());
        user.setUpdatedAt(new Date());
        UsersDB.writeUsers(UsersDB.getUsers());
        return new ResponseEntity<>("User is successfully deleted");
    }


    @Override
    public ResponseEntity<List<AuthUser>> getAll() {
        return null;
    }

    @Override
    public ResponseEntity<AuthUser> get(String id) {
        return null;
    }

    public ResponseEntity<List<AuthUserDto>> showByStatus(Role role, Status status) {
        List<AuthUserDto> employeeDtoList = new ArrayList<>();
        for (AuthUser user : UsersDB.getUsers()) {
            if (user.getRole().equals(role) && user.getStatus().equals(status)) {
                AuthUserDto employeeDto = new AuthUserDto();
                employeeDto.setFullName(user.getFullName());
                employeeDto.setUsername(user.getUsername());
                employeeDto.setPhoneNumber(user.getPhoneNumber());
                employeeDtoList.add(employeeDto);
            }
        }
        return new ResponseEntity<>(employeeDtoList);
    }

    public ResponseEntity<List<AuthUserDto>> showIgnoreStatus(Role role, Status status) {
        List<AuthUserDto> employeeDtoList = new ArrayList<>();
        for (AuthUser user : UsersDB.getUsers()) {
            if (user.getRole().equals(role) && !user.getStatus().equals(status)) {
                AuthUserDto employeeDto = new AuthUserDto();
                employeeDto.setFullName(user.getFullName());
                employeeDto.setUsername(user.getUsername());
                employeeDto.setPhoneNumber(user.getPhoneNumber());
                employeeDto.setStatus(user.getStatus());
                employeeDtoList.add(employeeDto);
            }
        }
        return new ResponseEntity<>(employeeDtoList);
    }
}
