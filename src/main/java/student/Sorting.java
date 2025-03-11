package student;

import java.util.Comparator;
import java.util.stream.Stream;

public class Sorting {
    /**
     * Sorts a stream of BoardGame objects based on the specified GameData column and order.
     *
     * @param stream   the stream of BoardGame objects to sort
     * @param sortOn   the GameData column to sort on
     * @param ascending if true, sorts in ascending order; if false, in descending order
     * @return a sorted stream of BoardGame objects
     */
    public static Stream<BoardGame> sortOn(Stream<BoardGame> stream, GameData sortOn, boolean ascending) {
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
        return stream.sorted(comparator);
    }
}
