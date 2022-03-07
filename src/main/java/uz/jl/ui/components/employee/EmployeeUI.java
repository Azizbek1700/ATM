package uz.jl.ui.components.employee;

import uz.jl.dtos.auth.AuthUserDto;
import uz.jl.enums.Status;
import uz.jl.enums.auth.Role;
import uz.jl.models.auth.AuthUser;
import uz.jl.repository.auth.AuthUserRepository;
import uz.jl.response.ResponseEntity;
import uz.jl.services.auth.employee.EmployeeService;
import uz.jl.ui.BaseAbstractUI;
import uz.jl.ui.BaseUI;
import uz.jl.utils.Color;
import uz.jl.utils.Input;
import uz.jl.utils.Print;

import java.util.List;
import java.util.Objects;

/**
 * @author Elmurodov Javohir, Wed 4:49 PM. 12/8/2021
 */
public class EmployeeUI extends BaseAbstractUI implements BaseUI {

    private static EmployeeUI employeeUI;

    public static EmployeeUI getInstance() {
        if (Objects.isNull(employeeUI)) {
            return employeeUI = new EmployeeUI();
        }
        return employeeUI;
    }

    private EmployeeUI() {
    }

    public void create() {
        String pasCode = Input.getStr("Enter passport serial: ");
        AuthUser user = AuthUserRepository.getInstance().findByPassportSerial(pasCode);

        if (Objects.isNull(user)) {
            String fullName = Input.getStr("Enter fullName: ");
            String username = Input.getStr("Enter username: ");
            String password = Input.getStr("Enter password: ");
            String phoneNumber = Input.getStr("Enter phone number: ");
            ResponseEntity<String> response = EmployeeService.getInstance().create(fullName, username, password, phoneNumber, pasCode);
            show(response);
        } else if (!user.getRole().equals(Role.EMPLOYEE)) {
            String username = Input.getStr("Enter username: ");
            String password = Input.getStr("Enter password: ");
            ResponseEntity<String> response = EmployeeService.getInstance().create(pasCode, username, password);
            show(response);
        } else {
            ResponseEntity<String> response = new ResponseEntity<>("User is already Employee");
            show(response);
        }
    }

    @Override
    public void block() {
        if (showByStatus(Role.EMPLOYEE, Status.ACTIVE) == 1) {
            Print.println(Color.RED, "There is no active employee");
            return;
        }
        String username = Input.getStr("Enter username: ");
        ResponseEntity<String> response = EmployeeService.getInstance().block(username);
        show(response);
    }

    @Override
    public void unblock() {
        if (showByStatus(Role.EMPLOYEE, Status.BLOCKED) == 1) {
            Print.println(Color.RED, "There is no active employee");
            return;
        }
        String username = Input.getStr("Enter username: ");
        ResponseEntity<String> response = EmployeeService.getInstance().unblock(username);
        show(response);
    }

    @Override
    public void delete() {
        if (showIgnoreStatus(Role.EMPLOYEE, Status.DELETED) == 1) {
            Print.println(Color.RED, "There is no employee");
            return;
        }
        String username = Input.getStr("Enter username: ");
        ResponseEntity<String> response = EmployeeService.getInstance().delete(username);
        show(response);
    }

    @Override
    public void update() {

    }

    @Override
    public void list() {
        int index = 1;
        ResponseEntity<List<AuthUserDto>> response = EmployeeService.getInstance().showIgnoreStatus(Role.CLIENT, Status.DELETED);
        if (response.getData().isEmpty()) {
            Print.println(Color.RED, "There is no client");
            return;
        }
        for (AuthUserDto employeeDto : response.getData()) {
            if (employeeDto.getStatus().equals(Status.ACTIVE)) {
                Print.println(Color.GREEN, index++ + ". Full name: " + employeeDto.getFullName());
                Print.println(Color.GREEN, "    Phone number: " + employeeDto.getPhoneNumber());
                Print.println(Color.GREEN, "    Username: " + employeeDto.getUsername());
                Print.println(Color.GREEN, "    Status: " + employeeDto.getStatus());
            } else if (employeeDto.getStatus().equals(Status.BLOCKED)) {
                Print.println(Color.PURPLE, index++ + ". Full name: " + employeeDto.getFullName());
                Print.println(Color.PURPLE, "    Phone number: " + employeeDto.getPhoneNumber());
                Print.println(Color.PURPLE, "    Username: " + employeeDto.getUsername());
                Print.println(Color.PURPLE, "    Status: " + employeeDto.getStatus());
            }
        }
    }

    public int showByStatus(Role role, Status status) {
        int index = 1;
        ResponseEntity<List<AuthUserDto>> response = EmployeeService.getInstance().showByStatus(role, status);
        if (response.getData().isEmpty()) return index;
        for (AuthUserDto userDto : response.getData()) {
            Print.println(Color.GREEN, index++ + ". Full name: " + userDto.getFullName());
            Print.println(Color.GREEN, "    Phone number: " + userDto.getPhoneNumber());
            Print.println(Color.GREEN, "    Username: " + userDto.getUsername());
        }
        return index;
    }

    public int showIgnoreStatus(Role role, Status status) {
        int index = 1;
        ResponseEntity<List<AuthUserDto>> response = EmployeeService.getInstance().showIgnoreStatus(role, status);
        if (response.getData().isEmpty()) {
            return index;
        }
        for (AuthUserDto employeeDto : response.getData()) {
            if (employeeDto.getStatus().equals(Status.ACTIVE)) {
                Print.println(Color.GREEN, index++ + ". Full name: " + employeeDto.getFullName());
                Print.println(Color.GREEN, "    Phone number: " + employeeDto.getPhoneNumber());
                Print.println(Color.GREEN, "    Username: " + employeeDto.getUsername());
                Print.println(Color.GREEN, "    Status: " + employeeDto.getStatus());
            } else if (employeeDto.getStatus().equals(Status.BLOCKED)) {
                Print.println(Color.PURPLE, index++ + ". Full name: " + employeeDto.getFullName());
                Print.println(Color.PURPLE, "    Phone number: " + employeeDto.getPhoneNumber());
                Print.println(Color.PURPLE, "    Username: " + employeeDto.getUsername());
                Print.println(Color.PURPLE, "    Status: " + employeeDto.getStatus());
            }
        }
        return index;
    }
}

