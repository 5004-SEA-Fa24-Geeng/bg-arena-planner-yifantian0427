package student;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Planner implements IPlanner {

    /** hold the original board games. */
    private final Set<BoardGame> originalGames;

    /**
     * Constructor for Planner.
     * @param games the board games.
     */
    public Planner(Set<BoardGame> games) {
        this.originalGames = new LinkedHashSet<>(games);
    }

    /**
     * Filters board games based on the string filter.
     * The result stream is sorted by the name of the board game in ascending order.
     * @param filter the filter to apply to the board games.
     * @return a stream of board games that match the filter.
     */
    @Override
    public Stream<BoardGame> filter(String filter) {
        // Always start filtering from the full originalGames set.
        Stream<BoardGame> base = originalGames.stream();
        if (filter == null || filter.trim().isEmpty()) {
            return Sorting.sortOn(base, GameData.NAME, true);
        }
        String[] conditions = filter.split(",");
        for (String condition : conditions) {
            base = filterSingle(condition, base);
        }
        return Sorting.sortOn(base, GameData.NAME, true);
    }

    /**
     * Filters board games based on the string filter,
     * and sorts the stream of board games based on the specific column.
     * @param filter the filter to apply to the board games.
     * @param sortOn the column to sort the results on.
     * @return a stream of board games that match the filter.
     */
    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn) {
        return filter(filter, sortOn, true);
    }

    /**
     * Filters board games based on the string filter,
     * and sorts the stream of board games based on the specific column in the specified order.
     * @param filter the filter to apply to the board games.
     * @param sortOn the column to sort the results on.
     * @param ascending whether to sort the results in ascending or descending order.
     * @return a stream of board games that match the filter.
     */
    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn, boolean ascending) {
        // Always start from the full collection.
        Stream<BoardGame> base = originalGames.stream();
        if (filter != null && !filter.trim().isEmpty()) {
            String[] conditions = filter.split(",");
            for (String condition : conditions) {
                base = filterSingle(condition, base);
            }
        }
        return Sorting.sortOn(base, sortOn, ascending);
    }

    /**
     * Filters games when the filter string contains only one filter condition.
     * @param filter the filter string used for filtering.
     * @param filterGames the stream of board games to be filtered.
     * @return a stream of board games that match the filter condition.
     * @throws IllegalArgumentException if the condition is invalid.
     */
    private Stream<BoardGame> filterSingle(String filter, Stream<BoardGame> filterGames)
            throws IllegalArgumentException {
        Operations operator = Operations.getOperatorFromStr(filter);
        if (operator == null) {
            throw new IllegalArgumentException("Invalid condition.");
        }
        // Remove all spaces.
        filter = filter.replaceAll(" ", "");
        String[] parts = filter.split(operator.getOperator());
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid condition.");
        }
        GameData column;
        try {
            column = GameData.fromString(parts[0]);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid condition.");
        }
        String value = parts[1];
        // For numeric columns, ensure the value is a number.
        if (column != GameData.NAME) {
            try {
                Integer.parseInt(value);
            } catch(NumberFormatException e) {
                throw new IllegalArgumentException("Contains non-numeric values.");
            }
        }
        return filterGames.filter(game -> Filters.filter(game, column, operator, value));
    }

    /**
     * Since filtering is done fresh from the original set every time,
     * reset() does not need to modify any internal state.
     */
    @Override
    public void reset() {
        // No progressive state maintained.
    }
}
