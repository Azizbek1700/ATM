package uz.jl.services.filesystems;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Elmurodov Javohir, Thu 3:45 PM. 12/9/2021
 */
public interface BaseDB {
    String pathPre = "src/main/resources/db/";
    Gson GSON = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

}
