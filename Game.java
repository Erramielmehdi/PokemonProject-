// Orchestrates the match: holds the board, tracks whose turn it is, applies a
// move or an attack and detects the end of the game (a Mewtwo dying).
public class Game {

    // Outcome of attempting an action, so the GUI can react and show feedback.
    public enum Result { MOVED, ATTACKED, INVALID }

    public static final int MEWTWO = 150; // pokedex number of the "king" piece

    private final Board board = new Board();
    private Owner   current = Owner.ROUGE; // ROUGE always starts
    private Owner   winner;                 // null until someone wins
    private boolean over;
    private String  lastMessage = "";

    public Board getBoard()      { return board; }
    public Owner getCurrent()    { return current; }
    public Owner getWinner()     { return winner; }
    public boolean isOver()      { return over; }
    public String getLastMessage() { return lastMessage; }

    // Try to act with the piece on (selRow,selCol) towards (tgtRow,tgtCol).
    // The selected cell must hold a piece belonging to the current player.
    // Depending on the target it resolves to a move (empty cell) or an attack
    // (enemy piece). Anything illegal returns INVALID and changes nothing.
    public Result play(int selRow, int selCol, int tgtRow, int tgtCol) {
        if (over) {
            lastMessage = "La partie est terminée.";
            return Result.INVALID;
        }

        Cell from = board.get(selRow, selCol);

        // Must select one of your own pieces.
        if (from.isEmpty() || from.getOwner() != current) {
            lastMessage = "Sélectionnez d'abord un de vos pokémons.";
            return Result.INVALID;
        }

        // Target must be a neighbouring cell (one square, 8 directions).
        if (!board.isAdjacent(selRow, selCol, tgtRow, tgtCol)) {
            lastMessage = "Action impossible : la case n'est pas adjacente.";
            return Result.INVALID;
        }

        Cell to = board.get(tgtRow, tgtCol);

        if (to.isEmpty()) {
            // --- Move onto an empty adjacent cell ---
            to.set(from.getOccupant(), from.getOwner());
            from.clear();
            lastMessage = "";
            switchTurn();
            return Result.MOVED;
        }

        // Occupied target.
        if (to.getOwner() == current) {
            lastMessage = "Action impossible : votre pokémon occupe déjà cette case.";
            return Result.INVALID;
        }

        // --- Attack an adjacent enemy piece (attacker stays in place) ---
        Pokemon attaquant = from.getOccupant();
        Pokemon defenseur = to.getOccupant();
        int degats = attaquant.frappe(defenseur);

        StringBuilder sb = new StringBuilder();
        sb.append(attaquant.getNom()).append(" inflige ").append(degats)
          .append(" dégâts à ").append(defenseur.getNom()).append(" !");

        if (!defenseur.estVivant()) {
            boolean estMewtwo = defenseur.getNumPokedex() == MEWTWO;
            Owner proprietaireMort = to.getOwner();
            to.clear();
            sb.append(" ").append(defenseur.getNom()).append(" est K.O. !");
            if (estMewtwo) {
                over   = true;
                winner = proprietaireMort.adverse();
                lastMessage = "Le joueur " + winner.getLabel() + " a gagné !";
                return Result.ATTACKED;
            }
        }

        lastMessage = sb.toString();
        switchTurn();
        return Result.ATTACKED;
    }

    private void switchTurn() {
        current = current.adverse();
    }
}
