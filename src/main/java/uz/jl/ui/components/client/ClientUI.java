package uz.jl.ui.components.client;

import uz.jl.dtos.auth.AuthUserDto;
import uz.jl.enums.Status;
import uz.jl.enums.auth.Role;
import uz.jl.models.auth.AuthUser;
import uz.jl.repository.auth.AuthUserRepository;
import uz.jl.response.ResponseEntity;
import uz.jl.services.auth.client.ClientService;
import uz.jl.ui.BaseAbstractUI;
import uz.jl.ui.BaseUI;
import uz.jl.utils.Color;
import uz.jl.utils.Input;
import uz.jl.utils.Print;

import java.util.List;
import java.util.Objects;


/**
 * @author Elmurodov Javohir, Wed 4:50 PM. 12/8/2021
 */
public class ClientUI extends BaseAbstractUI implements BaseUI {

    private static ClientUI clientUI;

    public static ClientUI getInstance() {
        if (Objects.isNull(clientUI)) {
            return clientUI = new ClientUI();
        }
        return clientUI;
    }


    private ClientUI() {
    }

    public void orderCard() {

    }

    public void create() {
        String passportCode = Input.getStr("Enter passportCode: ");
        AuthUser user = AuthUserRepository.getInstance().findByPassportSerial(passportCode);
        if (Objects.isNull(user)) {
            String fullName = Input.getStr("Enter fullName: ");
            String phoneNumber = Input.getStr("Enter phone number: ");
            ResponseEntity<String> response = ClientService.getInstance().create(fullName, phoneNumber, passportCode);
            show(response);
        } else if (!user.getRole().equals(Role.CLIENT)) {
            show(new ResponseEntity<>("You have already registered"));
        } else {
            ResponseEntity<String> response = new ResponseEntity<>("User is already Client");
            show(response);
        }

    }

    @Override
    public void block() {
        if (showByStatus(Role.CLIENT, Status.ACTIVE) == 1) {
            Print.println(Color.RED, "There is no active client");
            return;
        }
        String passportSerial = Input.getStr("Enter passport serial: ");
        ResponseEntity<String> response = ClientService.getInstance().block(passportSerial);
        show(response);
    }

    @Override
    public void unblock() {
        if (showByStatus(Role.CLIENT, Status.BLOCKED) == 1) {
            Print.println(Color.RED, "There is no blocked client");
            return;
        }
        String passportSerial = Input.getStr("Enter passport serial: ");
        ResponseEntity<String> response = ClientService.getInstance().unblock(passportSerial);
        show(response);
    }

    @Override
    public void delete() {
        if (showIgnoreStatus(Role.CLIENT, Status.DELETED) == 1) {
            Print.println(Color.RED, "There is no client");
            return;
        }
        String passportSerial = Input.getStr("Enter passport serial: ");
        ResponseEntity<String> response = ClientService.getInstance().delete(passportSerial);
        show(response);
    }

    @Override
    public void update() {

    }

    @Override
    public void list() {
        int index = 1;
        ResponseEntity<List<AuthUserDto>> response = ClientService.getInstance().showIgnoreStatus(Role.CLIENT, Status.DELETED);
        if (response.getData().isEmpty()) {
            Print.println(Color.RED, "There is no client");
            return;
        }
        for (AuthUserDto clientDto : response.getData()) {
            if (clientDto.getStatus().equals(Status.ACTIVE)) {
                Print.println(Color.GREEN, index++ + ". Full name: " + clientDto.getFullName());
                Print.println(Color.GREEN, "    Phone number: " + clientDto.getPhoneNumber());
                Print.println(Color.GREEN, "    Username: " + clientDto.getUsername());
                Print.println(Color.GREEN, "    Status: " + clientDto.getStatus());
            } else if (clientDto.getStatus().equals(Status.BLOCKED)) {
                Print.println(Color.PURPLE, index++ + ". Full name: " + clientDto.getFullName());
                Print.println(Color.PURPLE, "    Phone number: " + clientDto.getPhoneNumber());
                Print.println(Color.PURPLE, "    Username: " + clientDto.getUsername());
                Print.println(Color.PURPLE, "    Status: " + clientDto.getStatus());
            }
        }
    }

    public int showByStatus(Role role, Status status) {
        int index = 1;
        ResponseEntity<List<AuthUserDto>> response = ClientService.getInstance().showByStatus(role, status);
        if (response.getData().isEmpty()) return index;
        for (AuthUserDto clientDto : response.getData()) {
            Print.println(Color.GREEN, index++ + ". Full name: " + clientDto.getFullName());
            Print.println(Color.GREEN, "    Phone number: " + clientDto.getPhoneNumber());
            Print.println(Color.GREEN, "    Username: " + clientDto.getUsername());
        }
        return index;
    }

    public int showIgnoreStatus(Role role, Status status) {
        int index = 1;
        ResponseEntity<List<AuthUserDto>> response = ClientService.getInstance().showIgnoreStatus(role, status);
        if (response.getData().isEmpty()) {
            return index;
        }
        for (AuthUserDto clientDto : response.getData()) {
            if (clientDto.getStatus().equals(Status.ACTIVE)) {
                Print.println(Color.GREEN, index++ + ". Full name: " + clientDto.getFullName());
                Print.println(Color.GREEN, "    Phone number: " + clientDto.getPhoneNumber());
                Print.println(Color.GREEN, "    Username: " + clientDto.getUsername());
                Print.println(Color.GREEN, "    Status: " + clientDto.getStatus());
            } else if (clientDto.getStatus().equals(Status.BLOCKED)) {
                Print.println(Color.PURPLE, index++ + ". Full name: " + clientDto.getFullName());
                Print.println(Color.PURPLE, "    Phone number: " + clientDto.getPhoneNumber());
                Print.println(Color.PURPLE, "    Username: " + clientDto.getUsername());
                Print.println(Color.PURPLE, "    Status: " + clientDto.getStatus());
            }
        }
        return index;
    }
}

