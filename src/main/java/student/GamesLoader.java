package student;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Loads the games from the csv file into a set of BoardGame objects.
 * This file is stored in the resources folder, and while it is
 * passed in - often tends to be more fixed.
 *
 * It assumes there are no comma's in the data (and does not handle errors if
 * there are extra commas like in the name).
 */
public final class GamesLoader {
    /** Standard csv delim. */
    private static final String DELIMITER = ",";

    /** private constructor to prevent instantiation. */
    private GamesLoader() {
    }

    /**
     * Loads the games from the csv file into a set of BoardGame objects.
     *
     * @param filename the name of the file to load
     * @return a set of BoardGame objects
     */
    public static Set<BoardGame> loadGamesFile(String filename) {

        Set<BoardGame> games = new HashSet<>();

        List<String> lines;
        try {
            // this is so we can store the files in the resources folder
            InputStream is = GamesLoader.class.getResourceAsStream(filename);
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(isr);
            lines = reader.lines().collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error reading file: " + e.getMessage());
            return games;
        }
        if (lines == null || lines.isEmpty()) {
            return games;
        }

        Map<GameData, Integer> columnMap = processHeader(lines.remove(0));

        games = lines.stream()
                .map(line -> toBoardGame(line, columnMap))
                .filter(game -> game != null)
                .collect(Collectors.toSet());

        return games;
    }

    /**
     * Converts a line from the csv file into a BoardGame object.
     *
     * @param line      the line to convert
     * @param columnMap the map of columns to index
     * @return a BoardGame object, or null if conversion fails
     */
    private static BoardGame toBoardGame(String line, Map<GameData, Integer> columnMap) {
        String[] columns = line.split(DELIMITER);
        int maxIndex = columnMap.values().stream().max(Integer::compareTo).orElse(0);
        // Because indices are 0-based, if columns.length is less than or equal to maxIndex, data is missing.
        if (columns.length <= maxIndex) {
            return null;
        }

        try {
            String name = columns[columnMap.get(GameData.NAME)].trim();
            int id = Integer.parseInt(columns[columnMap.get(GameData.ID)].trim());
            int minPlayers = Integer.parseInt(columns[columnMap.get(GameData.MIN_PLAYERS)].trim());
            int maxPlayers = Integer.parseInt(columns[columnMap.get(GameData.MAX_PLAYERS)].trim());
            int minTime = Integer.parseInt(columns[columnMap.get(GameData.MIN_TIME)].trim());
            int maxTime = Integer.parseInt(columns[columnMap.get(GameData.MAX_TIME)].trim());
            double difficulty = Double.parseDouble(columns[columnMap.get(GameData.DIFFICULTY)].trim());
            int rank = Integer.parseInt(columns[columnMap.get(GameData.RANK)].trim());
            double rating = Double.parseDouble(columns[columnMap.get(GameData.RATING)].trim());
            int year = Integer.parseInt(columns[columnMap.get(GameData.YEAR)].trim());
            return new BoardGame(name, id, minPlayers, maxPlayers, minTime, maxTime, difficulty, rank, rating, year);
        } catch (NumberFormatException e) {
            // skip the line if there's a parsing issue.
            return null;
        }
    }

    /**
     * Processes the header line to determine the column mapping.
     *
     * It is common to do this for csv files as the columns can be in any order.
     * This makes it order independent by taking a moment to link the columns
     * with their actual index in the file.
     *
     * @param header the header line
     * @return a map of column to index
     */
    private static Map<GameData, Integer> processHeader(String header) {
        Map<GameData, Integer> columnMap = new HashMap<>();
        String[] columns = header.split(DELIMITER);
        for (int i = 0; i < columns.length; i++) {
            // Trim and convert each token to lower case.
            String token = columns[i].trim().toLowerCase();
            try {
                GameData col = GameData.fromColumnName(token);
                columnMap.put(col, i);
            } catch (IllegalArgumentException e) {
                // Ignoring columns that don't match.
            }
        }
        return columnMap;
    }
}
