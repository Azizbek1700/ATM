package uz.jl.mapper.atm;

import uz.jl.mapper.auth.AuthUserMapper;

/**
 * @author Botirov Najmiddin, Fri 11:47. 10/12/2021
 */
public class AtmMapper {
    private static AtmMapper atmMapper;

    public static AtmMapper getInstance() {
        if ( atmMapper == null )
            atmMapper = new AtmMapper();

        return atmMapper;
    }
}
