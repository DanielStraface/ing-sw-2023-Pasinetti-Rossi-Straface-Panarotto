package it.polimi.ingsw.model.personcard;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

/**
 * This class is a representation of a JSON reader. It stored the personalObjCard information. It has got one attribute
 * that represent the resource file name. There is only one method, readFromFile, that reads collect the twelve
 * personal goal cards.
 * @author Matteo Panarotto
 */
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
     * @author Matteo Panarotto
     */
    public List<PersonalObjCard> readFromFile() {
        /* Preliminary operations: gson and reader from Json file creation */
        Gson gson = new Gson();
        Reader input = null;
        try {
            System.out.println("Read from " + this.file);
            input = new InputStreamReader(
                    getClass().getClassLoader().getResourceAsStream(this.file)
            );
        } catch (NullPointerException e) {
            System.err.println("ResourceAsStream is null " + e.getLocalizedMessage());
        }

        /* Extraction of the information store in Json file and copying into a personalObjCard array */
        PersonalObjCard[] cards = gson.fromJson(input, PersonalObjCard[].class);
        /* Conversion from personalObjCard array to list */
        return Arrays.asList(cards);
    }
}