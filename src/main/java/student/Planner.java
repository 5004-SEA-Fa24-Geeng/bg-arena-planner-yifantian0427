
package student;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;


/**
 * This class allows multiple filters to be applied sequentially while maintaining the state between calls.
 */
public class Planner implements IPlanner {
    /**
     * The original set of all board games.
     */
    private final Set<BoardGame> games;

    /**
     * List of active filter conditions.
     */
    private List<String> filterConditions;

    /**
     * Constructs a new Planner with the given set of board games.
     * Initializes the current filter to include all games.
     *
     * @param games the set of board games to manage
     */
    public Planner(Set<BoardGame> games) {
        this.games = games;
        this.filterConditions = new ArrayList<>();
    }

    /**
     * Filters the board games by the given filter string. The results are sorted by name in ascending order.
     *
     * @param filter The filter to apply to the board games.
     * @return a stream of board games that match the filter.
     */
    @Override
    public Stream<BoardGame> filter(String filter) {
        return filter(filter, GameData.NAME, true);
    }

    /**
     * Filters the board games by the given filter string
     * and sorts the results by the specified field in ascending order.
     *
     * @param filter The filter to apply to the board games.
     * @param sortOn The column to sort the results on.
     * @return a stream of board games that match the filter.
     */
    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn) {
        return filter(filter, sortOn, true);
    }

    /**
     * Filters the board games by the given filter string
     * and sorts the results by the specified filed in the specified direction.
     *
     * @param filter The filter to apply to the board games.
     * @param sortOn The column to sort the results on.
     * @param ascending Whether to sort the results in ascending order (true) or descending order (false).
     * @return a stream of board games that match the filter.
     */
    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn, boolean ascending) {

        if (filter != null && !filter.isEmpty()) {
            // Split multiple filter conditions, separated by commas)
            String[] filters = filter.split(",");
            for (String singleFilter : filters) {
                filterConditions.add(singleFilter.trim());
            }
        }

        // Apply all stored filters
        Stream<BoardGame> resultStream = games.stream();
        for (String condition : filterConditions) {
            resultStream = filterSingle(condition, resultStream);
        }

        // Sort and return the results
        return resultStream.sorted(GameSorter.getComparator(sortOn, ascending));
    }

    /**
     * Applies a single filter condition to the given stream of board games.
     *
     * @param filter the filter condition to apply.
     * @param filteredGames the stream of games to filter.
     * @return a stream of board games that match the filter condition.
     */
    private Stream<BoardGame> filterSingle(String filter, Stream<BoardGame> filteredGames) {
        Operations operator = Operations.getOperatorFromStr(filter);
        if (operator == null) {
            return filteredGames;
        }

        // Remove the spaces.
        filter = filter.replaceAll("\\s" + operator.getOperator() + "\\s*", operator.getOperator());

        // Split the filter string into column name and value
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

        String value = parts[1];

        return filteredGames.filter(game ->
                Filters.filter(game, column, operator, value));
    }

    /**
     * Resets the filter to include all games. This clears any previously applied filters.
     */
    @Override
    public void reset() {
        this.filterConditions.clear();
    }
}
