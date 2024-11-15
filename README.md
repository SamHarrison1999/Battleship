# Battleship Game

This is a Java-based implementation of the classic **Battleship** game. It features a graphical user interface (GUI) powered by JavaFX, and the game logic allows two players (or a player and an AI opponent) to place ships and attempt to sink their opponent's ships.

---

## Features

- **Grid-based Game Board**: 10x10 grid where players can place ships.
- **Ship Placement**: Place ships vertically or horizontally on the board.
- **Game Rules**: Ships can be placed with certain constraints, including boundaries and no overlap with other ships.
- **Real-time Gameplay**: Players take turns firing shots at each other's board, trying to sink ships.
- **Visual Interface**: A clean and intuitive GUI built with JavaFX, dynamic ship placement and hit/miss feedback.

---

## Getting Started

To get started with the Battleship game, follow the instructions below:

### Prerequisites

- **JDK 23 or higher**: Ensure you have installed JDK 23 or later. You can check your JDK version using the following command:

  ```bash
  java -version
  
- Maven: This project uses Maven to manage dependencies and build the application. If you don't already have it, you can install it from [here](https://maven.apache.org/download.cgi).
- JavaFX: Make sure to have JavaFX 23 (or your configured version) in your project. This is handled through Maven dependencies.

---

## Running the Project

1. **Clone the repository**
  ```bash
git clone https://github.com/SamHarrison1999/Battleship.git
cd battleship
```
2. **Build the project**
  ```bash
mvn clean install
```
3. **Run the application**
  ```bash
mvn JavaFX:run
```

---

## Project Structure

- src/main/java/org/com/battleship/: Contains the main Java classes including game logic and GUI components.
- src/main/resources/: Contains the resources, such as images and FXML files for the layout.
- pom.xml: The Maven build configuration that includes project dependencies and plugins.

---

## Dependencies

This project uses the following main dependencies:
- **JavaFX**: For building the GUI.
- **ControlsFX**: For enhanced UI controls.
- **Log4j**: For logging purposes.
- **SLF4J**: For logging abstraction.
- **FxGL**: For additional game development features.


---

## Logging

The project uses Log4j for logging. Logs are printed to the console by default, with configurable logging levels (e.g., INFO, TRACE, ERROR). The logging configuration is defined in the log4j2.xml file.

---

## Exception Handling

Custom exceptions are used to handle errors such as invalid ship placement or board initialization:
- ShipPlacementException: Thrown when ship placement rules are violated.
- BoardInitializationException: Thrown when the game board cannot be initialized correctly.
