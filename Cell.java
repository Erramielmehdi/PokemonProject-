// A single square of the 9x9 board. It knows its coordinates, the pokemon
// currently standing on it (if any) and which player owns that pokemon.
public class Cell {

    private final int row;
    private final int col;
    private Pokemon   occupant; // null when the cell is empty
    private Owner     owner;     // null when the cell is empty

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }

    public Pokemon getOccupant() { return occupant; }
    public Owner   getOwner()    { return owner; }

    public boolean isEmpty() { return occupant == null; }

    // Place a pokemon on this cell (or clear it by passing null).
    public void set(Pokemon p, Owner o) {
        this.occupant = p;
        this.owner    = (p == null) ? null : o;
    }

    public void clear() {
        set(null, null);
    }
}
