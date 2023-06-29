# ing-sw-2023-Pasinetti-Rossi-Straface-Panarotto

<img src="https://casualgamerevolution.b-cdn.net/sites/default/files/styles/content/public/field/image/351.png?itok=jmgzZxmE" width=192px height=192 px align="right" />

## MyShelfie
MyShelfie is a board game made for the **Final Test** of the **Software Engineering** course held by the teacher **Gianpaolo Cugola**.


The project includes:
* Phases 1 and 2 of the UML, used in the peer reviews;
* The final UML;
* The test coverage screenshot for model and controller;
* The associated javadocs;
* The sequence diagram of the initial connection of RMI or Socket;
* The sequence diagram of a turn while using RMI or Socket;
* A document explaining how disconnections are handled;
* All the written/received peer reviews and all their documentation;
* The project jar file;
* The project source code.

## Implemented Functionalities
| Functionality            |                                                       Status                                                       |
|:-------------------------|:------------------------------------------------------------------------------------------------------------------:|
| Basic rules              |                                                         ✅                                                          |
| Complete rules           |                                                         ✅                                                          |
| TUI                      |                                                         ✅                                                          |
| GUI                      |                                                         ✅                                                          |
| RMI                      |                                                         ✅                                                          |
| Socket                   |                                                         ✅                                                          |
| Multiple games           |                                                         ✅                                                          |
| Persistence              |                                                         ✅                                                          |
| Disconnection Resilience |                                                         ⛔                                                          |
| Chat                     |                                                         ⛔                                                          |


<!--
[![RED](http://placehold.it/15/f03c15/f03c15)](#)
[![YELLOW](http://placehold.it/15/ffdd00/ffdd00)](#)
[![GREEN](http://placehold.it/15/44bb44/44bb44)](#)
-->

## Requirements
* The latest version of Java and Maven;
* A firewall rule to allow inbound/outgoing connections on the ports 1099/1234.


## Instructions
* After downloading the jar file (a download from the releases section is recommended), open the terminal where the jar file is located and type "java -jar MyShelfie-gc48.jar" to execute the program and finally type and enter:

    1 -> to start a Server 

    2 -> to open the textual user interface (TUI)
  
    3 -> to open the graphical user interface (GUI)

## Notes
* It is recommended to leave the jar file in a folder, since the saved matches (.ser files) are created on the same location as the jar file itself;
* In order to save a match, one turn MUST have passed, this is true also for matches that have been just resumed (example: in an old match that has been just resumed, if the game is closed before the current player makes a move, the match is lost forever); 
* Disconnection notifications may take a while to show up due to client timeout in heartbeat functionality and due to the network's performance;
* Players that lose connection to the network (while using socket technology) have to manually close the game (either by clicking the 'x' in the GUI or Ctrl+c keyboard combination in the CLI);
* Every 75 seconds a check is made to see if a turn has passed (so if it's the same turn since the last check). If not, the match istantly finishes (the game treats it as if the current player has disconnected);
* The match also instantly finishes if the bag runs out of Item tiles (this happens very rarely). If so delete the save file associated to the match in order to avoid any further errors in your next matches.



## Made by
* Matteo Panarotto
* Maria Sole Rossi
* Cristian Pasinetti
* Daniel Straface

