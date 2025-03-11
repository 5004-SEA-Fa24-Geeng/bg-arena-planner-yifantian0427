package student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Planner implements IPlanner {

    private final List<BoardGame> originalGames;

    public Planner(Set<BoardGame> games) {
        // Store the full collection in a sorted list (by name, case-insensitive).
        this.originalGames = games.stream()
                .sorted(Comparator.comparing(game -> game.getName().toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Stream<BoardGame> filter(String filter) {
        Stream<BoardGame> base = originalGames.stream();
        if (filter == null || filter.trim().isEmpty()) {
            return base;
        }
        // Trim the filter string
        filter = filter.trim();
        // Apply each condition (separated by commas) to the full set.
        String[] conditions = filter.split(",");
        for (String condition : conditions) {
            base = filterSingle(condition, base);
        }
        return base;
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
        // No progressive filtering state is maintained.
    }

    /**
     * Filters games for a single condition.
     *
     * @param filter the filter condition (e.g., "name~=o")
     * @param filterGames the stream of games to filter
     * @return a stream of games matching the condition
     */
    private Stream<BoardGame> filterSingle(String filter, Stream<BoardGame> filterGames) {
        // Force CONTAINS operator if the filter contains "~="
        Operations operator;
        if (filter.contains("~=")) {
            operator = Operations.CONTAINS;
        } else {
            operator = Operations.getOperatorFromStr(filter);
        }
        if (operator == null) {
            return filterGames;
        }
        filter = filter.trim();
        // Use indexOf and substring to reliably extract parts.
        int opIndex = filter.indexOf(operator.getOperator());
        if (opIndex < 0) {
            return filterGames;
        }
        String columnStr = filter.substring(0, opIndex).trim().toLowerCase();
        String value = filter.substring(opIndex + operator.getOperator().length()).trim();
        GameData column;
        try {
            column = GameData.fromString(columnStr);
        } catch (IllegalArgumentException e) {
            return filterGames;
        }
        // Let Filters.filter perform a case-insensitive comparison.
        return filterGames.filter(game -> Filters.filter(game, column, operator, value));
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
