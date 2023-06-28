# ing-sw-2023-Pasinetti-Rossi-Straface-Panarotto

The project includes:
* Phases 1 and 2 of the UML, used in the peer reviews;
* The final UML;
* The test coverage screenshot for model and controller;
* The associated javadocs;
* The sequence diagram of the initial connection of RMI or Socket;
* The sequence diagram of a turn while using RMI or Socket;
* All the written/received peer reviews and all their documentation;
* The project jar file;
* The project source code.

## Implemented Functionalities
| Functionality            |                                                                 Status                                                                 |
|:-------------------------|:--------------------------------------------------------------------------------------------------------------------------------------:|
| Basic rules              |                                                                  [✅]                                                                   |
| Complete rules           |                                                                  [✅]                                                                   |
| Socket                   |                                                                  [✅]                                                                   |
| RMI                      |                                                                  [✅]                                                                   |
| GUI                      |                                                                  [✅]                                                                   |
| TUI                      |                                                                  [✅]                                                                   |
| Multiple games           |                                                                  [✅]                                                                   |
| Persistence              |                                                                  [✅]                                                                   |
| Disconnection Resilience |                                                                  [⛔]                                                                   |
| Chat                     |                                                                  [⛔]                                                                   |


<!--
[![RED](http://placehold.it/15/f03c15/f03c15)](#)
[![YELLOW](http://placehold.it/15/ffdd00/ffdd00)](#)
[![GREEN](http://placehold.it/15/44bb44/44bb44)](#)
-->

## Requirements
* The latest version of Java and Maven;
* A rule for inbound/outgoing connections on the ports 1099/1234.


## Instructions
* After downloading the jar file (a download from the releases section is recommended), open the Windows terminal where the jar file is located and type "java -jar MyShelfie-gc48.jar" and type:

    1 -> to start a Server 

    2 -> to open the CLI client version 
  
    3 -> to open the GUI client version

## Notes
* It is recommended to leave the jar file in a folder, since the saved matches (.ser files) are created on the same location as the jar file itself;
* In order to save a match, one turn MUST have passed, this is true also for matches that have been just resumed.
* Disconnection notifications may take a while to show up;


## Made by
* Matteo Panarotto
* Maria Sole Rossi
* Cristian Pasinetti
* Daniel Straface

