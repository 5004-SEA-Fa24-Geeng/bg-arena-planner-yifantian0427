import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import student.BoardGame;
import student.GameData;
import student.GameList;
import student.IPlanner;
import student.IGameList;
import student.Planner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class TestPlanner {

    static Set<BoardGame> games;

    @BeforeAll
    public static void setup() {
        games = new HashSet<>();
        // Sample data
        games.add(new BoardGame("17 days", 6, 1, 8, 70, 70, 9.0, 600, 9.0, 2005));
        games.add(new BoardGame("Chess", 7, 2, 2, 10, 20, 10.0, 700, 10.0, 2006));
        games.add(new BoardGame("Go", 1, 2, 5, 30, 30, 8.0, 100, 7.5, 2000));
        games.add(new BoardGame("Go Fish", 2, 2, 10, 20, 120, 3.0, 200, 6.5, 2001));
        games.add(new BoardGame("golang", 4, 2, 7, 50, 55, 7.0, 400, 9.5, 2003));
        games.add(new BoardGame("GoRami", 3, 6, 6, 40, 42, 5.0, 300, 8.5, 2002));
        games.add(new BoardGame("Monopoly", 8, 6, 10, 20, 1000, 1.0, 800, 5.0, 2007));
        games.add(new BoardGame("Tucano", 5, 10, 20, 60, 90, 6.0, 500, 8.0, 2004));
    }

    // Test 1: Filter by name contains operator.
    // We'll use filter "name~=go" which should match: "Go", "Go Fish", "GoRami", "golang"
    @Test
    public void testFilterByNameContains() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("name~=go").toList();
        // Expected names sorted by name ascending (case-insensitive):
        // "Go", "Go Fish", "GoRami", "golang"
        assertEquals(4, filtered.size(), "Expected 4 games matching 'go'");
        assertEquals("Go", filtered.get(0).getName());
        assertEquals("Go Fish", filtered.get(1).getName());
        assertEquals("GoRami", filtered.get(2).getName());
        assertEquals("golang", filtered.get(3).getName());
    }

    // Test 2: Filter numeric comparisons. Use filter "minPlayers>=6"
    // Expected games: "GoRami" (minPlayers 6), "Monopoly" (minPlayers 6), "Tucano" (minPlayers 10)
    @Test
    public void testFilterNumericComparison() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("minPlayers>=6").toList();
        assertEquals(3, filtered.size(), "Expected 3 games with minPlayers >= 6");
        // Sorted by name ascending:
        assertEquals("GoRami", filtered.get(0).getName());
        assertEquals("Monopoly", filtered.get(1).getName());
        assertEquals("Tucano", filtered.get(2).getName());
    }

    // Test 3: Filter with sorting in ascending order.
    // With an empty filter and sort on YEAR ascending, expect games sorted by year.
    @Test
    public void testSortAscending() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("", GameData.YEAR, true).toList();
        // Expected order by year (oldest first): "Go" (2000), "Go Fish" (2001), "GoRami" (2002),
        // "golang" (2003), "Tucano" (2004), "17 days" (2005), "Chess" (2006), "Monopoly" (2007)
        assertEquals(8, filtered.size(), "Expected 8 games with no filter");
        assertEquals("Go", filtered.get(0).getName());
        assertEquals("Go Fish", filtered.get(1).getName());
        assertEquals("GoRami", filtered.get(2).getName());
        assertEquals("golang", filtered.get(3).getName());
        assertEquals("Tucano", filtered.get(4).getName());
        assertEquals("17 days", filtered.get(5).getName());
        assertEquals("Chess", filtered.get(6).getName());
        assertEquals("Monopoly", filtered.get(7).getName());
    }

    // Test 4: Test that reset() properly clears all filters.
    // Since reset() does nothing and filter() always starts with full list,
    // filter("") should return all games.
    @Test
    public void testReset() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("").toList();
        assertEquals(games.size(), filtered.size(), "Reset should show all games");
    }

    // Test 5: BoardGame getName() returns correct name.
    @Test
    public void testBoardGameGetName() {
        BoardGame bg = new BoardGame("Test Game", 99, 2, 4, 30, 60, 5.0, 100, 7.5, 2010);
        assertEquals("Test Game", bg.getName(), "getName() should return 'Test Game'");
    }

    // Test 6: BoardGame hashCode() is consistent with equals().
    @Test
    public void testBoardGameHashCodeEquals() {
        BoardGame bg1 = new BoardGame("Test Game", 99, 2, 4, 30, 60, 5.0, 100, 7.5, 2010);
        BoardGame bg2 = new BoardGame("Test Game", 99, 3, 5, 35, 70, 6.0, 101, 7.5, 2010);
        // Even if other fields differ, equals and hashCode use name and id.
        assertTrue(bg1.equals(bg2), "BoardGame objects with same name and id should be equal");
        assertEquals(bg1.hashCode(), bg2.hashCode(), "Hash codes should be equal for equal objects");
    }

    // Test 7: BoardGame toStringWithInfo() formats output properly for different GameData types.
    @Test
    public void testBoardGameToStringWithInfo() {
        BoardGame bg = new BoardGame("Test Game", 99, 2, 4, 30, 60, 5.0, 100, 7.5, 2010);
        // For NAME, should return just the name.
        assertEquals("Test Game", bg.toStringWithInfo(GameData.NAME));
        // For RATING, expect format "Test Game (7.50)" (formatted to 2 decimals).
        assertEquals("Test Game (7.50)", bg.toStringWithInfo(GameData.RATING));
        // For DIFFICULTY, expect "Test Game (5.00)".
        assertEquals("Test Game (5.00)", bg.toStringWithInfo(GameData.DIFFICULTY));
        // For RANK, expect "Test Game (100)".
        assertEquals("Test Game (100)", bg.toStringWithInfo(GameData.RANK));
    }

    // Test 8: GameList count() returns 0 when newly created.
    @Test
    public void testGameListCount() {
        IGameList list = new GameList();
        assertEquals(0, list.count(), "Newly created GameList should have count 0");
    }

    // Test 9: GameList addToList() properly adds games.
    @Test
    public void testAddToList() {
        IGameList list = new GameList();
        // We'll filter to get a stream of games with name containing "Go"
        IPlanner planner = new Planner(games);
        Stream<BoardGame> filtered = planner.filter("name~=go");
        // Use "1" to add the first game from the filtered stream.
        list.addToList("1", filtered);
        // After adding, count should be 1.
        assertEquals(1, list.count(), "GameList should contain 1 game after addToList");
        // The added game should be the first in sorted order of the filtered results.
        List<String> names = list.getGameNames();
        // Expected order from testFilterByNameContains: "Go", "Go Fish", "GoRami", "golang"
        assertEquals("Go", names.get(0));
    }
}
