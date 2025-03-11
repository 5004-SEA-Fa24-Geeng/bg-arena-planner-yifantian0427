package student;

public final class Filters {
    private Filters() {}

    public static boolean filter(BoardGame game, GameData column,
                                 Operations op, String value) {

        switch (column) {
            case NAME:
                return filterString(game.getName(), op, value);
            case MAX_PLAYERS:
                return filterNum(game.getMaxPlayers(), op, value);
            case MIN_PLAYERS:
                return filterNum(game.getMinPlayers(), op, value);
            case MIN_TIME:
                return filterNum(game.getMinPlayTime(), op, value);
            case MAX_TIME:
                return filterNum(game.getMaxPlayTime(), op, value);
            case RANK:
                return filterNum(game.getRank(), op, value);
            case YEAR:
                return filterNum(game.getYearPublished(), op, value);
            // Extend with additional cases if needed.
            default:
                return false;
        }
    }

    public static boolean filterString(String gameData, Operations op, String value) {
        // Trim both strings before comparing.
        String trimmedData = gameData.trim();
        String trimmedValue = value.trim();
        switch (op) {
            case EQUALS:
                return trimmedData.equalsIgnoreCase(trimmedValue);
            case NOT_EQUALS:
                return !trimmedData.equalsIgnoreCase(trimmedValue);
            case CONTAINS:
                return trimmedData.toLowerCase().contains(trimmedValue.toLowerCase());
            default:
                return false;
        }
    }

    public static boolean filterNum(int gameData, Operations op, String value) {
        int parsedValue;
        try {
            parsedValue = Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return false;
        }
        switch (op) {
            case EQUALS:
                return gameData == parsedValue;
            case NOT_EQUALS:
                return gameData != parsedValue;
            case GREATER_THAN:
                return gameData > parsedValue;
            case LESS_THAN:
                return gameData < parsedValue;
            case GREATER_THAN_EQUALS:
                return gameData >= parsedValue;
            case LESS_THAN_EQUALS:
                return gameData <= parsedValue;
            default:
                return false;
        }
    }
}
