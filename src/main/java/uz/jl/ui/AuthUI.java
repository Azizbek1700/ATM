package uz.jl.ui;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import uz.jl.configs.AtmSession;
import uz.jl.configs.CardSession;
import uz.jl.configs.Session;
import uz.jl.enums.settings.Language;
import uz.jl.models.atm.Atm;
import uz.jl.models.auth.AuthUser;
import uz.jl.models.card.Card;
import uz.jl.response.ResponseEntity;
import uz.jl.services.auth.AuthUserService;
import uz.jl.utils.BaseUtils;
import uz.jl.utils.Color;
import uz.jl.utils.Input;
import uz.jl.utils.Print;

import java.util.Objects;
import java.util.Scanner;
import java.util.StringTokenizer;

import static uz.jl.utils.Color.PURPLE;

/**
 * @author Elmurodov Javohir, Wed 4:52 PM. 12/8/2021
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthUI extends BaseAbstractUI /*implements BaseUI*/ {
    private AuthUserService authUserService = AuthUserService.getInstance();
    private static AuthUI authUI;

    public static AuthUI getInstance() {
        if (Objects.isNull(authUI)) {
            return authUI = new AuthUI();
        }
        return authUI;
    }

    public void login() {
        String username = Input.getStr("Username : ");
        String password = Input.getStr("Password : ");
        ResponseEntity<String> response = authUserService.login(username, password);
        show(response);
    }

    public void changeLanguage() {
        for (Language value : Language.values()) {
            Print.println(value);
        }
        String lan = Input.getStr("Enter language type(uz/en/rus): ");
        ResponseEntity<String> response = AuthUserService.getInstance().changeLanguage(lan);
        show(response);
    }

    public void logout() {
        Session.getInstance().setUser(new AuthUser());
        CardSession.getInstance().setSessionCard(null);
        AtmSession.getInstance().setSessionAtm(null);
    }

    public void profile() {
        Print.println(PURPLE, "Welcome Profile 😎😎");
        String choice = Input.getStr("?-> ");
    }

    public static void main(String[] args) {
//       StringBuilder a = new StringBuilder();
//        String name = new Scanner(System.in).nextLine();
//        String pan  = new Scanner(System.in).nextLine();
//        String ex = new Scanner(System.in).nextLine();
//
//
//        a.append(("---------------------------------\n" +
//                  "|                      UZCARD    |\n" +
//                  "|  %s                             \n" +
//                  "|                                |\n" +
//                  "|      %s                         \n" +
//                  "||---|              %s        |\n " +
//                  "||---|                          | \n" +
//                   "--------------------------------\n"));
//
//      String b = String.format(String.valueOf(a),name, pan, ex);
//
//        System.out.println(b);

    }
    public void quit() {
    }
}
