package it.polimi.ingsw.client.GUI.controllers;

import it.polimi.ingsw.client.GUI.GUI;

/**
 * The GUIController interface defines methods for controlling the graphical user interface (GUI) of an application.
 * Implementations of this interface are responsible for setting up the GUI and playing sounds.
 */
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
