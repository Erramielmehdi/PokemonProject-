import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

// The 9x9 playing grid. Holds the cells, performs the initial placement of
// pieces and exposes the geometric helpers (adjacency, bounds) used to
// validate moves and attacks.
public class Board {

    public static final int SIZE = 9;

    private final Cell[][] cells = new Cell[SIZE][SIZE];

    public Board() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                cells[r][c] = new Cell(r, c);
            }
        }
        // A config file, when present, overrides the default line-up (bonus).
        if (!loadFromConfig("board_config.csv")) {
            setupDefault();
        }
    }

    public Cell get(int row, int col) {
        return cells[row][col];
    }

    public boolean inBounds(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    // Two cells are neighbours when they touch on any of the 8 directions,
    // i.e. the Chebyshev distance between them is exactly 1.
    public boolean isAdjacent(int r1, int c1, int r2, int c2) {
        int dr = Math.abs(r1 - r2);
        int dc = Math.abs(c1 - c2);
        return (dr <= 1 && dc <= 1) && (dr + dc) > 0;
    }

    // Convenience: build a pokemon from its pokedex number, naming it after
    // its species, with stats pulled from the CSV by the existing engine.
    private static Pokemon make(int num) {
        return new Pokemon(num, Type.getEspece(num));
    }

    private void place(int row, int col, int num, Owner owner) {
        cells[row][col].set(make(num), owner);
    }

    // --- Default starting line-up -------------------------------------------
    // Each side fills its three closest rows. Back row = strongest pieces with
    // the lone Mewtwo (the "king") in the centre, middle row = evolved forms,
    // front row = basic "pawns". ROUGE sits on the top rows (0-2), VERT on the
    // bottom rows (6-8), mirrored across the board.
    private void setupDefault() {
        // pokedex numbers, column by column (9 wide)
        int[] back   = {142, 144, 145, 149, 150, 149, 145, 144, 142}; // Mewtwo at centre
        int[] middle = {  6,   9,   3,   5,   8,   2,   3,   9,   6};  // final/mid evolutions
        int[] front  = {  4,   7,   1,   4,   7,   1,   4,   7,   1};  // basic pawns

        for (int c = 0; c < SIZE; c++) {
            // ROUGE on the top: row 0 back, row 1 middle, row 2 front
            place(0, c, back[c],   Owner.ROUGE);
            place(1, c, middle[c], Owner.ROUGE);
            place(2, c, front[c],  Owner.ROUGE);

            // VERT on the bottom, mirrored: row 8 back, row 7 middle, row 6 front
            place(8, c, back[c],   Owner.VERT);
            place(7, c, middle[c], Owner.VERT);
            place(6, c, front[c],  Owner.VERT);
        }
    }

    // --- Bonus: load a custom line-up from a config file --------------------
    // Format, one piece per line: player;row;col;numPokedex
    //   player  -> "rouge" or "vert"
    //   row,col -> 0..8
    //   num     -> pokedex number (1..151)
    // Lines starting with '#' and blank lines are ignored. Returns true when a
    // readable config produced at least one placement.
    private boolean loadFromConfig(String path) {
        File f = new File(path);
        if (!f.exists()) return false;

        int placed = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] cols = line.split(";");
                if (cols.length < 4) continue;
                try {
                    Owner owner = "rouge".equalsIgnoreCase(cols[0].trim())
                            ? Owner.ROUGE
                            : "vert".equalsIgnoreCase(cols[0].trim()) ? Owner.VERT : null;
                    int row = Integer.parseInt(cols[1].trim());
                    int col = Integer.parseInt(cols[2].trim());
                    int num = Integer.parseInt(cols[3].trim());
                    if (owner == null || !inBounds(row, col) || num < 1 || num > 151) continue;
                    place(row, col, num, owner);
                    placed++;
                } catch (NumberFormatException ex) {
                    // skip malformed line, keep going
                }
            }
        } catch (IOException e) {
            System.err.println("Impossible de lire " + path + " : " + e.getMessage());
            return false;
        }
        return placed > 0;
    }
}
