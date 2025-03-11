package student;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Comparator;

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
        // Trim the filter string.
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
        // Delegate sorting to the GameSorter class.
        return filteredStream.sorted(GameSorter.getComparator(sortOn, ascending));
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
        filter = filter.trim();
        Operations operator;
        String columnStr;
        String value;

        // If the condition uses the CONTAINS operator, use a regex to allow spaces around "~="
        if (filter.contains("~=")) {
            operator = Operations.CONTAINS;
            // Pattern: one or more non-space characters for the column, optional spaces, "~=", optional spaces, then the value.
            Pattern pattern = Pattern.compile("(\\S+)\\s*~=\\s*(.+)");
            Matcher matcher = pattern.matcher(filter);
            if (matcher.matches()) {
                columnStr = matcher.group(1).trim().toLowerCase();
                value = matcher.group(2).trim();
            } else {
                return filterGames;
            }
        } else {
            operator = Operations.getOperatorFromStr(filter);
            if (operator == null) {
                return filterGames;
            }
            int opIndex = filter.indexOf(operator.getOperator());
            if (opIndex < 0) {
                return filterGames;
            }
            columnStr = filter.substring(0, opIndex).trim().toLowerCase();
            value = filter.substring(opIndex + operator.getOperator().length()).trim();
        }
        GameData column;
        try {
            column = GameData.fromString(columnStr);
        } catch (IllegalArgumentException e) {
            return filterGames;
        }
        // Delegate the filtering to the Filters utility.
        return filterGames.filter(game -> Filters.filter(game, column, operator, value));
    }
}
