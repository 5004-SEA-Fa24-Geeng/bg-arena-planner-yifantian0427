package student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Planner implements IPlanner {

    private final List<BoardGame> originalGames;
    // currentFiltered is maintained for reset() but not used for filtering if a filter is provided.
    private List<BoardGame> currentFiltered;

    public Planner(Set<BoardGame> games) {
        // Store the full collection in a sorted list (by name, case-insensitive).
        this.originalGames = games.stream()
                .sorted(Comparator.comparing(game -> game.getName().toLowerCase()))
                .collect(Collectors.toList());
        // Initially, the filtered list includes all games.
        this.currentFiltered = new ArrayList<>(this.originalGames);
    }

    @Override
    public Stream<BoardGame> filter(String filter) {
        if (filter == null || filter.trim().isEmpty()) {
            // If filter is empty, return the full collection, sorted by name.
            return originalGames.stream()
                    .sorted(Comparator.comparing(game -> game.getName().toLowerCase()));
        }
        // Always start from the full collection.
        List<BoardGame> result = originalGames;
        // Support multiple conditions separated by commas.
        String[] conditions = filter.split(",");
        for (String condition : conditions) {
            result = filterSingle(condition, result.stream())
                    .collect(Collectors.toList());
        }
        // Return the result sorted by game name (case-insensitive)
        return result.stream()
                .sorted(Comparator.comparing(game -> game.getName().toLowerCase()));
    }


    private Stream<BoardGame> filterSingle(String filter, Stream<BoardGame> filteredGames) {
        // Identify the operator using the provided Operations helper.
        Operations operator = Operations.getOperatorFromStr(filter);
        if (operator == null) {
            return filteredGames;
        }
        // Remove spaces.
        filter = filter.replaceAll(" ", "");
        String[] parts = filter.split(operator.getOperator());
        if (parts.length != 2) {
            return filteredGames;
        }
        GameData column;
        try {
            column = GameData.fromString(parts[0]);
        } catch (IllegalArgumentException e) {
            return filteredGames;
        }
        String value = parts[1].trim();
        List<BoardGame> filteredGameList = filteredGames
                .filter(game -> Filters.filter(game, column, operator, value))
                .collect(Collectors.toList());
        return filteredGameList.stream();
    }

    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn) {
        return filter(filter, sortOn, true);
    }

    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn, boolean ascending) {
        Stream<BoardGame> filteredStream = filter(filter);
        Comparator<BoardGame> comparator = getComparator(sortOn, ascending);
        return filteredStream.sorted(comparator);
    }

    @Override
    public void reset() {
        // Reset the current filtered list to the full collection.
        currentFiltered = new ArrayList<>(originalGames);
    }

    private Comparator<BoardGame> getComparator(GameData sortOn, boolean ascending) {
        Comparator<BoardGame> comparator;
        switch (sortOn) {
            case NAME:
                comparator = Comparator.comparing(game -> game.getName().toLowerCase());
                break;
            case RATING:
                comparator = Comparator.comparing(BoardGame::getRating);
                break;
            case DIFFICULTY:
                comparator = Comparator.comparing(BoardGame::getDifficulty);
                break;
            case RANK:
                comparator = Comparator.comparing(BoardGame::getRank);
                break;
            case MIN_PLAYERS:
                comparator = Comparator.comparing(BoardGame::getMinPlayers);
                break;
            case MAX_PLAYERS:
                comparator = Comparator.comparing(BoardGame::getMaxPlayers);
                break;
            case MIN_TIME:
                comparator = Comparator.comparing(BoardGame::getMinPlayTime);
                break;
            case MAX_TIME:
                comparator = Comparator.comparing(BoardGame::getMaxPlayTime);
                break;
            case YEAR:
                comparator = Comparator.comparing(BoardGame::getYearPublished);
                break;
            default:
                comparator = Comparator.comparing(game -> game.getName().toLowerCase());
                break;
        }
        if (!ascending) {
            comparator = comparator.reversed();
        }
        return comparator;
    }
}
