# Board Game Arena Planner Design Document


This document is meant to provide a tool for you to demonstrate the design process. You need to work on this before you code, and after have a finished product. That way you can compare the changes, and changes in design are normal as you work through a project. It is contrary to popular belief, but we are not perfect our first attempt. We need to iterate on our designs to make them better. This document is a tool to help you do that.


## (INITIAL DESIGN): Class Diagram 

Place your class diagrams below. Make sure you check the file in the browser on github.com to make sure it is rendering correctly. If it is not, you will need to fix it. As a reminder, here is a link to tools that can help you create a class diagram: [Class Resources: Class Design Tools](https://github.com/CS5004-khoury-lionelle/Resources?tab=readme-ov-file#uml-design-tools)
```mermaid
classDiagram
    direction TB
    
    %% Interfaces
    class IGameList {
        +List<String> getGameNames()
        +void clear()
        +int count()
        +void saveGame(String filename)
        +void addToList(String str, Stream<BoardGame> filtered) throws IllegalArgumentException
        +void removeFromList(String str) throws IllegalArgumentException
    }

    class IPlanner {
        +Stream<BoardGame> filter(String filter)
        +Stream<BoardGame> filter(String filter, GameData sortOn)
        +Stream<BoardGame> filter(String filter, GameData sortOn, boolean ascending)
        +void reset()
    }

    %% Concrete Classes
    class BoardGame {
        +String name
        +int id
        +int minPlayers
        +int maxPlayers
        +int minPlayTime
        +int maxPlayTime
        +double difficulty
        +int rank
        +double averageRating
        +int yearPublished
        +String getName()
        +int getId()
        +int getMinPlayers()
        +int getMaxPlayers()
        +int getMinPlayTime()
        +int getMaxPlayTime()
        +double getDifficulty()
        +int getRank()
        +double getRating()
        +int getYearPublished()
        +String toStringWithInfo(GameData col)
    }

    class GameList {
        +List<String> games
        +GameList()
        +List<String> getGameNames()
        +void clear()
        +int count()
        +void saveGame(String filename)
        +void addToList(String str, Stream<BoardGame> filtered)
        +void removeFromList(String str)
    }

    class Planner {
        +Set<BoardGame> gameCollection
        +Planner(Set<BoardGame> games)
        +Stream<BoardGame> filter(String filter)
        +Stream<BoardGame> filter(String filter, GameData sortOn)
        +Stream<BoardGame> filter(String filter, GameData sortOn, boolean ascending)
        +void reset()
    }

    class GamesLoader {
        +static Set<BoardGame> loadGamesFile(String filename)
    }

    class ConsoleApp {
        -Scanner IN
        -IGameList gameList
        -IPlanner planner
        +ConsoleApp(IGameList gameList, IPlanner planner)
        +void start()
        +void processHelp()
        +void processFilter()
        +void processListCommands()
    }

    class BGArenaPlanner {
        -static final String DEFAULT_COLLECTION
        -BGArenaPlanner()
        +static void main(String[] args)
    }

    class GameData {
        <<enum>>
        +NAME
        +ID
        +RATING
        +DIFFICULTY
        +RANK
        +MIN_PLAYERS
        +MAX_PLAYERS
        +MIN_TIME
        +MAX_TIME
        +YEAR
    }

    class Operations {
        <<enum>>
        +EQUALS
        +NOT_EQUALS
        +GREATER_THAN
        +LESS_THAN
        +GREATER_THAN_EQUALS
        +LESS_THAN_EQUALS
        +CONTAINS
        +static Operations fromOperator(String operator)
    }

    %% Relationships
    IGameList <|.. GameList
    IPlanner <|.. Planner
    GameList --> BoardGame
    Planner --> BoardGame
    GamesLoader --> BoardGame
    ConsoleApp --> IGameList
    ConsoleApp --> IPlanner
    BGArenaPlanner --> ConsoleApp
    BGArenaPlanner --> GamesLoader
    Planner --> GameData
    Planner --> Operations

```
### Provided Code

Provide a class diagram for the provided code as you read through it.  For the classes you are adding, you will create them as a separate diagram, so for now, you can just point towards the interfaces for the provided code diagram.



### Your Plans/Design

Create a class diagram for the classes you plan to create. This is your initial design, and it is okay if it changes. Your starting points are the interfaces. 





## (INITIAL DESIGN): Tests to Write - Brainstorm

Write a test (in english) that you can picture for the class diagram you have created. This is the brainstorming stage in the TDD process. 

> [!TIP]
> As a reminder, this is the TDD process we are following:
> 1. Figure out a number of tests by brainstorming (this step)
> 2. Write **one** test
> 3. Write **just enough** code to make that test pass
> 4. Refactor/update  as you go along
> 5. Repeat steps 2-4 until you have all the tests passing/fully built program

You should feel free to number your brainstorm. 

1. GameList Tests:
    
    1. Create a new GameList and check it initializes empty. 
    
    2. Add a game and verify it appears in the list.
    
    3. Ensure duplicate games are not added. 

    4. Remove a game and check it is removed.

    5. Save and load game lists from a file.

2.  Planner Tests:

    1. Load a set of board games and verify filtering by:
    > 1. minPlayers > 4
    > 2. difficulty < 3.5
    > 3. name ~= Catan
    2. Test sorting options (e.g., rating ascending/descending).
    3. Reset filters and check all games are available again.





## (FINAL DESIGN): Class Diagram

Go through your completed code, and update your class diagram to reflect the final design. Make sure you check the file in the browser on github.com to make sure it is rendering correctly. It is normal that the two diagrams don't match! Rarely (though possible) is your initial design perfect. 

For the final design, you just need to do a single diagram that includes both the original classes and the classes you added. 

> [!WARNING]
> If you resubmit your assignment for manual grading, this is a section that often needs updating. You should double check with every resubmit to make sure it is up to date.

```mermaid
classDiagram
    class BoardGame {
        - String name
        - int id
        - int minPlayers
        - int maxPlayers
        - int minPlayTime
        - int maxPlayTime
        - double difficulty
        - int rank
        - double rating
        - int yearPublished
        + String getName()
        + int getId()
        + int getMinPlayers()
        + int getMaxPlayers()
        + int getMinPlayTime()
        + int getMaxPlayTime()
        + double getDifficulty()
        + int getRank()
        + double getRating()
        + int getYearPublished()
    }

    class IPlanner {
        + Stream<BoardGame> filter(String filter)
        + Stream<BoardGame> filter(String filter, GameData sortOn)
        + Stream<BoardGame> filter(String filter, GameData sortOn, boolean ascending)
        + void reset()
    }

    class Planner {
        - List<BoardGame> originalGames
        + Planner(Set<BoardGame> games)
        + Stream<BoardGame> filter(String filter)
        + Stream<BoardGame> filter(String filter, GameData sortOn)
        + Stream<BoardGame> filter(String filter, GameData sortOn, boolean ascending)
        + void reset()
        - Stream<BoardGame> filterSingle(String filter, Stream<BoardGame> filterGames)
    }

    class GameData {
        <<enumeration>>
        NAME
        RATING
        DIFFICULTY
        RANK
        MIN_PLAYERS
        MAX_PLAYERS
        MIN_TIME
        MAX_TIME
        YEAR
    }

    class GameSorter {
        + static Comparator<BoardGame> getComparator(GameData sortOn, boolean ascending)
    }

    class IGameList {
        + List<String> getGameNames()
        + void clear()
        + int count()
        + void saveGame(String filename)
        + void addToList(String str, Stream<BoardGame> filtered)
        + void removeFromList(String str)
    }

    class GameList {
        - List<BoardGame> selectedGames
        + GameList()
        + List<String> getGameNames()
        + void clear()
        + int count()
        + void saveGame(String filename)
        + void addToList(String str, Stream<BoardGame> filtered)
        + void removeFromList(String str)
    }

    class Operations {
        <<enumeration>>
        EQUALS
        NOT_EQUALS
        GREATER_THAN
        LESS_THAN
        GREATER_THAN_EQUALS
        LESS_THAN_EQUALS
        CONTAINS
    }

    class Filters {
        + static boolean filter(BoardGame game, GameData column, Operations op, String value)
        + static boolean filterString(String gameData, Operations op, String value)
        + static boolean filterInt(int gameData, Operations op, String value)
        + static boolean filterDouble(double gameData, Operations op, String value)
    }

    class GamesLoader {
        + static Set<BoardGame> loadGames(String filename)
    }

    class ConsoleApp {
        + static void main(String[] args)
    }

    class BGArenaPlanner {
        + static void main(String[] args)
    }

    Planner --|> IPlanner
    GameList --|> IGameList
    GameSorter ..> BoardGame
    Planner ..> GameSorter
    Planner ..> Filters
    Filters ..> BoardGame
    Filters ..> GameData
    Filters ..> Operations
    GameSorter ..> GameData
    GamesLoader ..> BoardGame
    ConsoleApp ..> Planner
    ConsoleApp ..> GameList
    BGArenaPlanner ..> Planner

```



## (FINAL DESIGN): Reflection/Retrospective

> [!IMPORTANT]
> The value of reflective writing has been highly researched and documented within computer science, from learning to information to showing higher salaries in the workplace. For this next part, we encourage you to take time, and truly focus on your retrospective.

Take time to reflect on how your design has changed. Write in *prose* (i.e. do not bullet point your answers - it matters in how our brain processes the information). Make sure to include what were some major changes, and why you made them. What did you learn from this process? What would you do differently next time? What was the most challenging part of this process? For most students, it will be a paragraph or two. 

Throughout the design and refinement of the BG Arena Planner, several important changes shaped the final structure of the system. Initially, the architecture lacked clear separation of concerns, which made it more difficult to maintain and extend. As we progressed, one of the most significant shifts was the introduction of interfaces such as IGameList and IPlanner, ensuring flexibility and making it easier to substitute different implementations if needed. This abstraction allowed ConsoleApp to interact with GameList and Planner without being tightly coupled to their specific implementations. Another major change was refining the filtering and sorting mechanisms. The Planner class evolved to rely on explicit sorting strategies through GameSorter, ensuring consistent ordering, particularly when dealing with case-insensitive name comparisons.

One of the most valuable lessons from this process was the importance of structuring data and logic to align with real-world use cases. Early on, some filtering logic was mixed with data management, which created unnecessary complexity. By isolating responsibilities—such as moving filtering logic into Filters and sorting logic into GameSorter—the design became more modular and easier to test. The experience also reinforced the necessity of precise test-driven development. Small inconsistencies in sorting, such as the ordering of "golang" versus "GoRami," highlighted the importance of understanding how string comparisons behave in different contexts. If I were to start over, I would prioritize writing comprehensive tests earlier in the design phase, ensuring that key behaviors are validated from the beginning rather than adjusted later.

The most challenging aspect of this process was debugging subtle issues related to filtering and sorting. The interplay between how Operations parsed conditions, how Filters applied them, and how GameSorter handled comparisons introduced complexities that were difficult to diagnose. It required carefully stepping through test failures and considering multiple edge cases. In the end, this challenge reinforced the value of clear, systematic debugging and the importance of having a well-structured test suite.