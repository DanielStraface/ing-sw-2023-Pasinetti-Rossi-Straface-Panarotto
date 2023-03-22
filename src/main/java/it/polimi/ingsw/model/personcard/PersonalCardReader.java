package it.polimi.ingsw.model.personcard;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

public class PersonalCardReader {

    /* ATTRIBUTES SECTIONS */
    private final String file;

    /* METHOD SECTION */

    /* -- constructors --*/
    public PersonalCardReader(){this.file = "personalObjectiveCards.json";}
    public PersonalCardReader(String file){this.file = file;}

    /**
     * Method readFromFile reads from a Json file the information about all the personalObjCard and returns that as list
     * Must be the caller to select the card randomly for each player
     * @return List<PersonalObjCard> LIST <==>  (forall PersonalObjCard P1 contained in JSON file
     *                                          exists PersonalObjCad P2 contained in LIST) &&
     *                                          P1.equals(P2)
     */
    public List<PersonalObjCard> readFromFile() {
        /* Preliminary operations: gson and reader from Json file creation */
        Gson gson = new Gson();
        Reader input = null;
        try {
            input = new InputStreamReader(
                    getClass().getClassLoader().getResourceAsStream(file)
            );
        } catch (NullPointerException e) {
            System.err.println("ResourceAsStream is null");
        }

        /* Extraction of the information store in Json file and copying into a personalObjCard array */
        PersonalObjCard[] cards = gson.fromJson(input, PersonalObjCard[].class);
        /* Conversion from personalObjCard array to list */
        return Arrays.asList(cards);
    }
}