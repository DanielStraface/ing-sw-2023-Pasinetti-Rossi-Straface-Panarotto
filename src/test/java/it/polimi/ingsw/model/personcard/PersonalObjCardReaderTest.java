package it.polimi.ingsw.model.personcard;

import it.polimi.ingsw.controller.Controller;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class PersonalObjCardReaderTest tests PersonalObjCardReader class
 * @see PersonalCardReader
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonalObjCardReaderTest {
    PersonalCardReader personalCardReader;

    /**
     * Setup method for all classes
     */
    @BeforeAll
    public void setup(){
        final String fileToRead;
        fileToRead = "mustFail.json";
        personalCardReader = new PersonalCardReader(fileToRead);
    }

    /**
     * Tests if the NullPointerException is successfully triggered
     */
    @Test
    public void invalidResourceAsStreamTest(){
        NullPointerException exception = Assertions.assertThrows(NullPointerException.class, () ->{
            personalCardReader.readFromFile();
        });
        assertEquals("in == null", exception.getMessage());
    }
}
