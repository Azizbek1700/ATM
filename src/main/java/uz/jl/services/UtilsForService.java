package uz.jl.services;

import lombok.Getter;
import lombok.Setter;


/**
 * @author Botirov Najmiddin, Fri 15:15. 10/12/2021
 */

@Getter
@Setter
public class UtilsForService {
    private static UtilsForService utilsForService;

    private UtilsForService(){}

    public static UtilsForService getInstance() {
        if ( utilsForService == null )
            utilsForService = new UtilsForService();
        return utilsForService;
    }

    public boolean isInteger(Object number) {
        try {
            Long intNumber = Long.parseLong((String) number);
            return false;
        } catch (Exception e) {
            return true;
        }
    }
}
