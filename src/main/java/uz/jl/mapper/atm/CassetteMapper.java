package uz.jl.mapper.atm;

/**
 * @author Botirov Najmiddin, Fri 12:19. 10/12/2021
 */
public class CassetteMapper {
    private static CassetteMapper cassetteMapper;

    public static CassetteMapper getInstance() {
        if ( cassetteMapper == null )
            cassetteMapper = new CassetteMapper();

        return cassetteMapper;
    }
}
