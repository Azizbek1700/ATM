package uz.jl.services.filesystems;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import uz.jl.models.auth.AuthUser;
import uz.jl.utils.Print;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Elmurodov Javohir, Wed 5:52 PM. 12/8/2021
 */
public class UsersDB implements BaseDB {
    private final static String usersFilePath = pathPre + "users.json";
    private static List<AuthUser> users;

    public static List<AuthUser> getUsers() {
        if (Objects.isNull(users)) {
            try (FileReader reader = new FileReader(usersFilePath);
                 BufferedReader bufferedReader = new BufferedReader(reader)) {
                String jsonDATA = bufferedReader.lines().collect(Collectors.joining());
                users = GSON.fromJson(jsonDATA, new TypeToken<List<AuthUser>>() {
                }.getType());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    public static void writeUsers(List<AuthUser> users) {
        try (FileWriter fileWriter = new FileWriter(usersFilePath);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(GSON.toJson(users));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
