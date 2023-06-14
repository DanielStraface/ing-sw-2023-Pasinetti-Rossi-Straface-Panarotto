package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;

public interface GUIController {
    /**
     * method to set gui for the application
     * @param gui GUI
     */
    void setGUI(GUI gui);

    /**
     * plays a sound using the specific file path
     * @param filePath String
     */
    void playSound(String filePath);
}
