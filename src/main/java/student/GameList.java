package student;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameList implements IGameList {

    // Internal storage for selected games.
    private final List<BoardGame> selectedGames;

    /**
     * Constructor for the GameList.
     */
    public GameList() {
        this.selectedGames = new ArrayList<>();
    }

    @Override
    public List<String> getGameNames() {
        // Return game names in case-insensitive ascending order.
        return selectedGames.stream()
                .sorted(Comparator.comparing(game -> game.getName().toLowerCase()))
                .map(BoardGame::getName)
                .collect(Collectors.toList());
    }

    @Override
    public void clear() {
        selectedGames.clear();
    }

    @Override
    public int count() {
        return selectedGames.size();
    }

    @Override
    public void saveGame(String filename) {
        List<String> names = getGameNames();
        try {
            java.io.File file = new java.io.File(filename);
            java.io.File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(file))) {
                for (String name : names) {
                    writer.println(name);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving file: " + e.getMessage(), e);
        }
    }


    @Override
    public void addToList(String str, Stream<BoardGame> filtered) throws IllegalArgumentException {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Input is empty");
        }
        String input = str.trim();
        List<BoardGame> filteredList = filtered.collect(Collectors.toList());

        // If "all" is specified, add every game from the filtered list.
        if (input.equalsIgnoreCase(ADD_ALL)) {
            for (BoardGame game : filteredList) {
                addGame(game);
            }
            return;
        }

        // If a range is specified (e.g., "1-5")
        if (input.contains("-")) {
            String[] parts = input.split("-");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid range format");
            }
            try {
                int start = Integer.parseInt(parts[0]);
                int end = Integer.parseInt(parts[1]);
                if (start < 1 || end < start || end > filteredList.size()) {
                    throw new IllegalArgumentException("Range out of bounds");
                }
                for (int i = start; i <= end; i++) {
                    addGame(filteredList.get(i - 1));
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid number in range");
            }
            return;
        }

        // If input is a single number (e.g., "1")
        try {
            int index = Integer.parseInt(input);
            if (index < 1 || index > filteredList.size()) {
                throw new IllegalArgumentException("Index out of bounds");
            }
            addGame(filteredList.get(index - 1));
            return;
        } catch (NumberFormatException e) {
            // Not a number—treat input as a game name.
        }

        // Otherwise, treat the input as a game name (case-insensitive).
        boolean found = false;
        for (BoardGame game : filteredList) {
            if (game.getName().equalsIgnoreCase(input)) {
                addGame(game);
                found = true;
                // If multiple matches occur, add them all.
            }
        }
        if (!found) {
            throw new IllegalArgumentException("No game found with name: " + input);
        }
    }

    @Override
    public void removeFromList(String str) throws IllegalArgumentException {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Input is empty");
        }
        String input = str.trim();
        // Get the currently selected games in sorted order.
        List<BoardGame> sortedList = selectedGames.stream()
                .sorted(Comparator.comparing(game -> game.getName().toLowerCase()))
                .collect(Collectors.toList());

        // If "all" is specified, clear the list.
        if (input.equalsIgnoreCase(ADD_ALL)) {
            clear();
            return;
        }

        // If a range is specified.
        if (input.contains("-")) {
            String[] parts = input.split("-");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid range format");
            }
            try {
                int start = Integer.parseInt(parts[0]);
                int end = Integer.parseInt(parts[1]);
                if (start < 1 || end < start || end > sortedList.size()) {
                    throw new IllegalArgumentException("Range out of bounds");
                }
                // Remove games from the sorted list by first collecting them.
                List<BoardGame> toRemove = new ArrayList<>();
                for (int i = start; i <= end; i++) {
                    toRemove.add(sortedList.get(i - 1));
                }
                if (toRemove.isEmpty()) {
                    throw new IllegalArgumentException("No games to remove in range");
                }
                selectedGames.removeAll(toRemove);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid number in range");
            }
            return;
        }

        // If input is a single number.
        try {
            int index = Integer.parseInt(input);
            if (index < 1 || index > sortedList.size()) {
                throw new IllegalArgumentException("Index out of bounds");
            }
            BoardGame gameToRemove = sortedList.get(index - 1);
            selectedGames.remove(gameToRemove);
            return;
        } catch (NumberFormatException e) {
            // Not a number—treat as a game name.
        }

        // Remove by game name (case-insensitive).
        boolean removed = selectedGames.removeIf(game -> game.getName().equalsIgnoreCase(input));
        if (!removed) {
            throw new IllegalArgumentException("No game found with name: " + input);
        }
    }

    // Helper method to add a game if it isn't already in the list.
    private void addGame(BoardGame game) {
        if (!selectedGames.contains(game)) {
            selectedGames.add(game);
        }
    }
}
