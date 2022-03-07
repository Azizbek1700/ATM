package uz.jl.ui.components.hr;

import uz.jl.dtos.auth.AuthUserDto;
import uz.jl.enums.Status;
import uz.jl.enums.auth.Role;
import uz.jl.models.auth.AuthUser;
import uz.jl.repository.auth.AuthUserRepository;
import uz.jl.response.ResponseEntity;
import uz.jl.services.auth.employee.EmployeeService;
import uz.jl.services.auth.hr.HRService;
import uz.jl.ui.BaseAbstractUI;
import uz.jl.ui.BaseUI;
import uz.jl.utils.Color;
import uz.jl.utils.Input;
import uz.jl.utils.Print;

import java.util.List;
import java.util.Objects;

/**
 * @author Elmurodov Javohir, Wed 4:48 PM. 12/8/2021
 */
public class HrUI extends BaseAbstractUI implements BaseUI {

    private static HrUI hrUI;

    public static HrUI getInstance() {
        if (Objects.isNull(hrUI)) {
            return hrUI = new HrUI();
        }
        return hrUI;
    }

    private HrUI() {
    }

    public void create() {
        String pasCode = Input.getStr("Enter passport serial: ");
        AuthUser user = AuthUserRepository.getInstance().findByPassportSerial(pasCode);
        if (Objects.isNull(user)) {
            String fullName = Input.getStr("Enter fullName: ");
            String username = Input.getStr("Enter username: ");
            String password = Input.getStr("Enter password: ");
            String phoneNumber = Input.getStr("Enter phone number: ");
            ResponseEntity<String> response = HRService.getInstance().create(fullName, username, password, phoneNumber, pasCode);
            show(response);
        } else if (!user.getRole().equals(Role.HR)) {
            String username = Input.getStr("Enter username : ");
            String password = Input.getStr("Enter password : ");
            ResponseEntity<String> response = HRService.getInstance().create(pasCode, username, password);
            show(response);
        } else {
            ResponseEntity<String> response = new ResponseEntity<>("User is already HR");
            show(response);
        }

    }

    @Override
    public void block() {
        if (showByStatus(Role.HR, Status.ACTIVE) == 1) {
            Print.println(Color.RED, "There is no active hr");
            return;
        }
        String username = Input.getStr("Enter username: ");
        ResponseEntity<String> response = HRService.getInstance().block(username);
        show(response);
    }

    @Override
    public void unblock() {
        if (showByStatus(Role.HR, Status.BLOCKED) == 1) {
            Print.println(Color.RED, "There is no blocked hr");
            return;
        }
        String username = Input.getStr("Enter username: ");
        ResponseEntity<String> response = HRService.getInstance().unblock(username);
        show(response);
    }

    @Override
    public void delete() {
        if (showIgnoreStatus(Role.HR, Status.DELETED) == 1) {
            Print.println(Color.RED, "There is no active hr");
            return;
        }
        String username = Input.getStr("Enter username: ");
        ResponseEntity<String> response = HRService.getInstance().delete(username);
        show(response);
    }

    @Override
    public void update() {

    }

    @Override
    public void list() {
        int index = 1;
        ResponseEntity<List<AuthUserDto>> response = HRService.getInstance().showIgnoreStatus(Role.HR, Status.DELETED);
        if (response.getData().isEmpty()) {
            Print.println(Color.RED, "There is no hr");
            return;
        }
        for (AuthUserDto hrDto : response.getData()) {
            if (hrDto.getStatus().equals(Status.ACTIVE)) {
                Print.println(Color.GREEN, index++ + ". Full name: " + hrDto.getFullName());
                Print.println(Color.GREEN, "    Phone number: " + hrDto.getPhoneNumber());
                Print.println(Color.GREEN, "    Username: " + hrDto.getUsername());
                Print.println(Color.GREEN, "    Status: " + hrDto.getStatus());
            } else if (hrDto.getStatus().equals(Status.BLOCKED)) {
                Print.println(Color.PURPLE, index++ + ". Full name: " + hrDto.getFullName());
                Print.println(Color.PURPLE, "    Phone number: " + hrDto.getPhoneNumber());
                Print.println(Color.PURPLE, "    Username: " + hrDto.getUsername());
                Print.println(Color.PURPLE, "    Status: " + hrDto.getStatus());
            }
        }
    }

    public int showByStatus(Role role, Status status) {
        int index = 1;
        ResponseEntity<List<AuthUserDto>> response = HRService.getInstance().showByStatus(role, status);
        if (response.getData().isEmpty()) return index;
        for (AuthUserDto hrDto : response.getData()) {
            Print.println(Color.GREEN, index++ + ". Full name: " + hrDto.getFullName());
            Print.println(Color.GREEN, "    Phone number: " + hrDto.getPhoneNumber());
            Print.println(Color.GREEN, "    Username: " + hrDto.getUsername());

        }
        return index;
    }

    public int showIgnoreStatus(Role role, Status status) {
        int index = 1;
        ResponseEntity<List<AuthUserDto>> response = EmployeeService.getInstance().showIgnoreStatus(role, status);
        if (response.getData().isEmpty()) return index;
        for (AuthUserDto hrDto : response.getData()) {
            if (hrDto.getStatus().equals(Status.ACTIVE)) {
                Print.println(Color.GREEN, index++ + ". Full name: " + hrDto.getFullName());
                Print.println(Color.GREEN, "    Phone number: " + hrDto.getPhoneNumber());
                Print.println(Color.GREEN, "    Username: " + hrDto.getUsername());
                Print.println(Color.GREEN, "    Status: " + hrDto.getStatus());
            } else if (hrDto.getStatus().equals(Status.BLOCKED)) {
                Print.println(Color.PURPLE, index++ + ". Full name: " + hrDto.getFullName());
                Print.println(Color.PURPLE, "    Phone number: " + hrDto.getPhoneNumber());
                Print.println(Color.PURPLE, "    Username: " + hrDto.getUsername());
                Print.println(Color.PURPLE, "    Status: " + hrDto.getStatus());
            }
        }
        return index;
    }
}
