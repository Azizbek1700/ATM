package uz.jl.services.filesystems;

import com.google.gson.reflect.TypeToken;
import uz.jl.models.card.Card;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Doston Bokhodirov, Fri 2:39 PM. 12/10/2021
 */
public class CardsDB implements BaseDB {
    private final static String cardsFilePath = pathPre + "cards.json";
    private static List<Card> cards;

    public static List<Card> getCards() {
        if (Objects.isNull(cards)) {
            try (FileReader reader = new FileReader(cardsFilePath);
                 BufferedReader bufferedReader = new BufferedReader(reader)) {
                String jsonDATA = bufferedReader.lines().collect(Collectors.joining());
                cards = GSON.fromJson(jsonDATA, new TypeToken<List<Card>>() {
                }.getType());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cards;
    }

    public static void writeCards(List<Card> cards) {
        try (FileWriter fileWriter = new FileWriter(cardsFilePath);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(GSON.toJson(cards));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
