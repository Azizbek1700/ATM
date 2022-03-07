package uz.jl.ui;

import uz.jl.configs.AtmSession;
import uz.jl.configs.CardSession;
import uz.jl.configs.LangConfig;
import uz.jl.configs.Session;
import uz.jl.enums.auth.Role;
import uz.jl.enums.settings.Language;
import uz.jl.models.auth.AuthUser;
import uz.jl.ui.components.MenuVal;
import uz.jl.utils.Color;
import uz.jl.utils.Input;
import uz.jl.utils.Print;

import java.util.*;

import static uz.jl.utils.Color.YELLOW;

/**
 * @author Elmurodov Javohir, Wed 3:24 PM. 12/8/2021
 */
public class MenuUI {

    private static Map<String, String> menus() {
        LinkedHashMap<String, String> menus = new LinkedHashMap<>();
        AuthUser user = Session.getInstance().getUser();
        Role role = user.getRole();
        Language language = user.getLanguage();


        if ( Role.SUPER_ADMIN.equals(role) ) {
            // TODO: 12/8/2021 need to translation
            menus.put(LangConfig.get("atm.create"), MenuVal.ATM_CREATE);
            menus.put(LangConfig.get("atm.delete"), MenuVal.ATM_DELETE);
            menus.put(LangConfig.get("atm.block"), MenuVal.ATM_BLOCK);
            menus.put(LangConfig.get("atm.unblock"), MenuVal.ATM_UNBLOCK);
            menus.put(LangConfig.get("atm.list"), MenuVal.ATM_LIST);

            menus.put(LangConfig.get("admin.create"), MenuVal.ADMIN_CREATE);
            menus.put(LangConfig.get("admin.delete"), MenuVal.ADMIN_DELETE);
            menus.put(LangConfig.get("admin.block"), MenuVal.ADMIN_BLOCK);
            menus.put(LangConfig.get("admin.unblock"), MenuVal.ADMIN_UNBLOCK);
            menus.put(LangConfig.get("admin.list"), MenuVal.ADMIN_LIST);

            menus.put(LangConfig.get("language.change"), MenuVal.LANGUAGE_CHANGE);
            menus.put(LangConfig.get("logout"), MenuVal.LOGOUT);
            menus.put(LangConfig.get("profile"), MenuVal.PROFILE);

        } else if ( Role.ADMIN.equals(role) ) {

            menus.put(LangConfig.get("hr.create"), MenuVal.HR_CREATE);
            menus.put(LangConfig.get("hr.delete"), MenuVal.HR_DELETE);
            menus.put(LangConfig.get("hr.block"), MenuVal.HR_BLOCK);
            menus.put(LangConfig.get("hr.unblock"), MenuVal.HR_UNBLOCK);
            menus.put(LangConfig.get("hr.list"), MenuVal.HR_LIST);

            menus.put(LangConfig.get("atm.create"), MenuVal.ATM_CREATE);
            menus.put(LangConfig.get("atm.block"), MenuVal.ATM_DELETE);
            menus.put(LangConfig.get("atm.unblock"), MenuVal.ATM_BLOCK);
            menus.put(LangConfig.get("atm.delete"), MenuVal.ATM_UNBLOCK);
            menus.put(LangConfig.get("atm.list"), MenuVal.ATM_LIST);

            menus.put(LangConfig.get("card.create"), MenuVal.CARD_CREATE);
            menus.put(LangConfig.get("card.delete"), MenuVal.CARD_DELETE);
            menus.put(LangConfig.get("card.block"), MenuVal.CARD_BLOCK);
            menus.put(LangConfig.get("card.unblock"), MenuVal.CARD_UNBLOCK);
            menus.put(LangConfig.get("card.list"), MenuVal.CARD_LIST);

            menus.put(LangConfig.get("language.change"), MenuVal.LANGUAGE_CHANGE);
            menus.put(LangConfig.get("logout"), MenuVal.LOGOUT);
            menus.put(LangConfig.get("block.cassette"), MenuVal.BLOCK_ATM_CASSETTE);
            menus.put(LangConfig.get("unblock.cassette"), MenuVal.UNBLOCK_ATM_CASSETTE);
        } else if ( Role.HR.equals(role) ) {

            menus.put(LangConfig.get("employee.create"), MenuVal.EMPLOYEE_CREATE);
            menus.put(LangConfig.get("employee.block"), MenuVal.EMPLOYEE_DELETE);
            menus.put(LangConfig.get("employee.unblock"), MenuVal.EMPLOYEE_BLOCK);
            menus.put(LangConfig.get("employee.delete"), MenuVal.EMPLOYEE_UNBLOCK);
            menus.put(LangConfig.get("employee.list"), MenuVal.EMPLOYEE_LIST);

            menus.put(LangConfig.get("language.change"), MenuVal.LANGUAGE_CHANGE);
            menus.put(LangConfig.get("logout"), MenuVal.LOGOUT);
            menus.put(LangConfig.get("profile"), MenuVal.PROFILE);
        } else if ( Role.EMPLOYEE.equals(role) ) {

            menus.put(LangConfig.get("client.create"), MenuVal.CLIENT_CREATE);
            menus.put(LangConfig.get("client.delete"), MenuVal.CLIENT_DELETE);
            menus.put(LangConfig.get("client.block"), MenuVal.CLIENT_BLOCK);
            menus.put(LangConfig.get("client.unblock"), MenuVal.CLIENT_UNBLOCK);
            menus.put(LangConfig.get("client.list"), MenuVal.CLIENT_LIST);

            menus.put(LangConfig.get("give.cards"), MenuVal.GIVE_CARDS);
            menus.put(LangConfig.get("atm.update"), MenuVal.UPDATE_ATM);
            menus.put(LangConfig.get("atm.cassette.block"), MenuVal.BLOCK_ATM_CASSETTE);
            menus.put(LangConfig.get("atm.cassette.unblock"), MenuVal.UNBLOCK_ATM_CASSETTE);

            menus.put(LangConfig.get("language.change"), MenuVal.LANGUAGE_CHANGE);
            menus.put(LangConfig.get("logout"), MenuVal.LOGOUT);
            menus.put(LangConfig.get("profile"), MenuVal.PROFILE);
        } else if ( role.equals(Role.ANONYMOUS) && Objects.nonNull(CardSession.getInstance().getSessionCard()) ) {
            menus.put(LangConfig.get("withdraw.money"), MenuVal.WITHDRAW_MONEY);
            menus.put(LangConfig.get("replenish.card"), MenuVal.REPLENISH_CARD);
            menus.put(LangConfig.get("card.block"), MenuVal.CARD_BLOCK);
            menus.put(LangConfig.get("language.change"), MenuVal.LANGUAGE_CHANGE);
            menus.put(LangConfig.get("logout"), MenuVal.LOGOUT);
        }
//        menus.put(LangConfig.get("atm.service"), MenuVal.ATM_SERVICES);
        return menus;
    }

    public static void show() {

//        menus().forEach((k, v) -> Print.println(Color.GREEN, (k + " - > " + v)));

        if ( Role.ANONYMOUS.equals(Session.getInstance().getUser().getRole()) && Objects.isNull(CardSession.getInstance().getSessionCard()) ) {
            Print.println("1. BANK");
            Print.println("2. ATM");
        } else {
            int index = 1;
            for (String key : menus().keySet()) {
                Print.println((index++) + ". " + key);
            }
        }
        Print.println("QUIT: q...");

    }
}
