package student;

import java.util.Comparator;

/**
 * Utility class for creating comparators to sort BoardGame objects.
 */
public final class GameSorter {

    // Private constructor prevents instantiation.
    private GameSorter() {}

    /**
     * Returns a comparator for BoardGame objects based on the specified column and order.
     *
     * @param sortOn the GameData column to sort on.
     * @param ascending true for ascending order; false for descending.
     * @return a Comparator for BoardGame.
     */
    public static Comparator<BoardGame> getComparator(GameData sortOn, boolean ascending) {
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
        return ascending ? comparator : comparator.reversed();
    }
}
