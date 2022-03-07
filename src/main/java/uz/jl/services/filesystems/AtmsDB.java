package uz.jl.services.filesystems;

import com.google.gson.reflect.TypeToken;
import uz.jl.models.atm.Atm;
import uz.jl.models.auth.AuthUser;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Botirov Najmiddin, Fri 11:36. 10/12/2021
 */
public class AtmsDB implements BaseDB{
    private final static String atmsFilePath = pathPre + "atms.json";
    private static List<Atm> atms;

    public static List<Atm> getAtms() {
        if ( Objects.isNull(atms)) {
            try ( FileReader reader = new FileReader(atmsFilePath);
                  BufferedReader bufferedReader = new BufferedReader(reader)) {
                String jsonDATA = bufferedReader.lines().collect(Collectors.joining());
                atms = GSON.fromJson(jsonDATA, new TypeToken<List<Atm>>() {
                }.getType());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return atms;
    }

    public static void writeAtms(List<Atm> atms) {
        try ( FileWriter fileWriter = new FileWriter(atmsFilePath);
              BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(GSON.toJson(atms));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
