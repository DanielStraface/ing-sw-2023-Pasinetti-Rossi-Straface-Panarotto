package it.polimi.ingsw.model;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PersonalCardReader {

    /* METHOD SECTION */

    /**
     * Method readFromFile reads from a Json file the information about all the personalObjCard and returns that as list
     * Must be the caller to select the card randomly for each player
     */
    public List<PersonalObjCard> readFromFile(){
        /* Preliminary operations: gson and reader from Json file creation */
        Gson gson = new Gson();
        Reader input = new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("personalObjectiveCards.json")
        );
        /* Extraction of the information store in Json file and copying into a personalObjCard array */
        PersonalObjCard[] cards = gson.fromJson(input, PersonalObjCard[].class);
        /* Conversion from personalObjCard array to list */
        List<PersonalObjCard> list = Arrays.asList(cards);
        return list;
    }
}