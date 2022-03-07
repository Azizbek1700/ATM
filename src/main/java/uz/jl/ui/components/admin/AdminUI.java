package uz.jl.ui.components.admin;

import uz.jl.dtos.auth.AuthUserDto;
import uz.jl.enums.Status;
import uz.jl.enums.auth.Role;
import uz.jl.models.auth.AuthUser;
import uz.jl.repository.auth.AuthUserRepository;
import uz.jl.response.ResponseEntity;
import uz.jl.services.auth.AuthUserService;
import uz.jl.services.auth.admin.AdminService;
import uz.jl.ui.AuthUI;
import uz.jl.ui.BaseAbstractUI;
import uz.jl.ui.BaseUI;
import uz.jl.utils.Color;
import uz.jl.utils.Input;
import uz.jl.utils.Print;

import java.util.List;
import java.util.Objects;

/**
 * @author Elmurodov Javohir, Wed 4:47 PM. 12/8/2021
 */
public class AdminUI extends BaseAbstractUI implements BaseUI {

    private AuthUserService authUserService = AuthUserService.getInstance();
    private static AuthUI authUI;


    private static AdminUI adminUI;

    public static AdminUI getInstance() {
        if (Objects.isNull(adminUI)) {
            adminUI = new AdminUI();
        }
        return adminUI;
    }

    private AdminUI() {

    }

    @Override
    public void create() {
        String passportCode = Input.getStr("Enter passportSerial: ");
        AuthUser user = AuthUserRepository.getInstance().findByPassportSerial(passportCode);
        if (Objects.isNull(user)) {
            String fullName = Input.getStr("Enter fullName: ");
            String username = Input.getStr("Enter username: ");
            String password = Input.getStr("Enter password: ");
            String phoneNumber = Input.getStr("Enter phone number: ");
            ResponseEntity<String> response = AdminService.getInstance().create(fullName, username, password, phoneNumber, passportCode);
            show(response);
        } else if (!user.getRole().equals(Role.ADMIN)) {
            String username = Input.getStr("Enter username: ");
            String password = Input.getStr("Enter password: ");

            ResponseEntity<String> response = AdminService.getInstance().create(passportCode, username, password);
            show(response);
        } else {
            ResponseEntity<String> response = new ResponseEntity<>("User is already Admin!!");
            show(response);
        }
    }

    @Override
    public void block() {
        if (showByStatus(Role.ADMIN, Status.ACTIVE) == 1) {
            Print.println(Color.RED, "There is no active admin");
            return;
        }
        String username = Input.getStr("Enter username: ");
        ResponseEntity<String> response = AdminService.getInstance().block(username);
        show(response);
    }

    @Override
    public void unblock() {
        if (showByStatus(Role.ADMIN, Status.BLOCKED) == 1) {
            Print.println(Color.RED, "There is no blocked admin");
            return;
        }
        String username = Input.getStr("Enter username: ");
        ResponseEntity<String> response = AdminService.getInstance().unblock(username);
        show(response);
    }

    @Override
    public void delete() {
        if (showIgnoreStatus(Role.ADMIN, Status.DELETED) == 1) {
            Print.println(Color.RED, "There is no admin");
            return;
        }
        String username = Input.getStr("Enter username: ");
        ResponseEntity<String> response = AdminService.getInstance().delete(username);
        show(response);
    }

    @Override
    public void update() {

    }

    @Override
    public void list() {
        int index = 1;
        ResponseEntity<List<AuthUserDto>> response = AdminService.getInstance().showIgnoreStatus(Role.ADMIN, Status.DELETED);
        if (response.getData().isEmpty()) {
            Print.println(Color.RED, "There is no Admin");
            return;
        }
        for (AuthUserDto adminDto : response.getData()) {
            if (adminDto.getStatus().equals(Status.ACTIVE)) {
                Print.println(Color.GREEN, index++ + ". Full name: " + adminDto.getFullName());
                Print.println(Color.GREEN, "    Phone number: " + adminDto.getPhoneNumber());
                Print.println(Color.GREEN, "    Username: " + adminDto.getUsername());
                Print.println(Color.GREEN, "    Status: " + adminDto.getStatus());
            } else if (adminDto.getStatus().equals(Status.BLOCKED)) {
                Print.println(Color.PURPLE, index++ + ". Full name: " + adminDto.getFullName());
                Print.println(Color.PURPLE, "    Phone number: " + adminDto.getPhoneNumber());
                Print.println(Color.PURPLE, "    Username: " + adminDto.getUsername());
                Print.println(Color.PURPLE, "    Status: " + adminDto.getStatus());
            }
        }
    }

    public int showByStatus(Role role, Status status) {
        int index = 1;
        ResponseEntity<List<AuthUserDto>> response = AdminService.getInstance().showByStatus(role, status);
        if (response.getData().isEmpty()) {
            return index;
        }
        for (AuthUserDto adminDto : response.getData()) {
            Print.println(Color.GREEN, index++ + ". Full name: " + adminDto.getFullName());
            Print.println(Color.GREEN, "    Phone number: " + adminDto.getPhoneNumber());
            Print.println(Color.GREEN, "    Username: " + adminDto.getUsername());
        }
        return index;
    }

    public int showIgnoreStatus(Role role, Status status) {
        int index = 1;
        ResponseEntity<List<AuthUserDto>> response = AdminService.getInstance().showIgnoreStatus(role, status);
        if (response.getData().isEmpty()) return index;
        for (AuthUserDto adminDto : response.getData()) {
            if (adminDto.getStatus().equals(Status.ACTIVE)) {
                Print.println(Color.GREEN, index++ + ". Full name: " + adminDto.getFullName());
                Print.println(Color.GREEN, "    Phone number: " + adminDto.getPhoneNumber());
                Print.println(Color.GREEN, "    Username: " + adminDto.getUsername());
                Print.println(Color.GREEN, "    Status: " + adminDto.getStatus());
            } else if (adminDto.getStatus().equals(Status.BLOCKED)) {
                Print.println(Color.PURPLE, index++ + ". Full name: " + adminDto.getFullName());
                Print.println(Color.PURPLE, "    Phone number: " + adminDto.getPhoneNumber());
                Print.println(Color.PURPLE, "    Username: " + adminDto.getUsername());
                Print.println(Color.PURPLE, "    Status: " + adminDto.getStatus());
            }
        }
        return index;
    }
}
