package it.polimi.ingsw.model.personcard;

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
        NullPointerException exception = Assertions.assertThrows(NullPointerException.class, () ->{
            personalCardReader.readFromFile();
        });
        assertEquals("in == null", exception.getMessage());
    }
}
