package it.polimi.ingsw.model.personcard;

import it.polimi.ingsw.exceptions.InvalidPointerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonalObjCardReaderTest {
    PersonalCardReader personalCardReader;

    @BeforeAll
    public void setup(){
        final String fileToRead;
        fileToRead = "mustFail.json";
        personalCardReader = new PersonalCardReader(fileToRead);
    }

    @Test
    public void invalidResourceAsStreamTest(){
        InvalidPointerException exception = Assertions.assertThrows(InvalidPointerException.class, () ->{
            personalCardReader.readFromFile();
        });
        assertEquals("in == null", exception.getMessage());
    }
}
