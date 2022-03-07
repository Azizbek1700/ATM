package uz.jl.services.filesystems;

import com.google.gson.reflect.TypeToken;
import uz.jl.models.atm.Atm;
import uz.jl.models.atm.Cassette;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Doston Bokhodirov, Fri 2:36 PM. 12/10/2021
 */
public class CassettesDB implements BaseDB{
    private final static String cassettesFilePath = pathPre + "cassettes.json";
    private static List<Cassette> cassettes;

    public static List<Cassette> getCassettes() {
        if ( Objects.isNull(cassettes)) {
            try (FileReader reader = new FileReader(cassettesFilePath);
                 BufferedReader bufferedReader = new BufferedReader(reader)) {
                String jsonDATA = bufferedReader.lines().collect(Collectors.joining());
                cassettes = GSON.fromJson(jsonDATA, new TypeToken<List<Cassette>>() {
                }.getType());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cassettes;
    }

    public static void writeCassettes(List<Cassette> cassettes) {
        try (FileWriter fileWriter = new FileWriter(cassettesFilePath);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(GSON.toJson(cassettes));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
