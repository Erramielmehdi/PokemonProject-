// Headless sanity check of the Pokéchess rules (not part of the deliverable).
public class SmokeTest {
    static int checks = 0, fails = 0;
    static void check(boolean cond, String label) {
        checks++;
        if (!cond) { fails++; System.out.println("FAIL: " + label); }
        else System.out.println("ok  : " + label);
    }

    public static void main(String[] args) {
        Game g = new Game();
        Board b = g.getBoard();

        check(g.getCurrent() == Owner.ROUGE, "rouge starts");

        // Rows 0-2 belong to ROUGE, 6-8 to VERT, 3-5 empty.
        check(b.get(0,4).getOccupant().getNumPokedex() == 150, "rouge mewtwo at (0,4)");
        check(b.get(8,4).getOccupant().getNumPokedex() == 150, "vert mewtwo at (8,4)");
        check(b.get(4,4).isEmpty(), "centre empty");

        // Illegal: acting out of turn (select a VERT piece).
        check(g.play(6,0, 5,0) == Game.Result.INVALID, "cannot move enemy piece");
        // Illegal: move two squares.
        check(g.play(2,0, 4,0) == Game.Result.INVALID, "cannot move 2 squares");
        // Illegal: move onto own piece.
        check(g.play(2,0, 1,0) == Game.Result.INVALID, "cannot move onto own piece");

        // Legal move: rouge front pawn (2,0) -> empty (3,0).
        check(g.play(2,0, 3,0) == Game.Result.MOVED, "rouge moves forward");
        check(b.get(3,0).getOccupant() != null && b.get(2,0).isEmpty(), "piece relocated");
        check(g.getCurrent() == Owner.VERT, "turn passed to vert");

        // Vert moves a pawn too.
        check(g.play(6,0, 5,0) == Game.Result.MOVED, "vert moves forward");
        check(g.getCurrent() == Owner.ROUGE, "turn back to rouge");

        // --- Win condition: force-kill vert's Mewtwo via repeated adjacency attacks. ---
        Game g2 = new Game();
        Board b2 = g2.getBoard();
        // Put a rouge attacker next to vert mewtwo and damage it until it dies.
        // Mewtwo at (8,4); place a strong rouge piece at (7,4) by clearing first.
        b2.get(7,4).clear();
        b2.get(7,4).set(new Pokemon(150, "Mewtwo"), Owner.ROUGE);
        int guard = 0;
        boolean vertDown = false; // toggle vert's filler pawn between (6,0)/(5,0)
        while (!g2.isOver() && guard++ < 200) {
            g2.play(7,4, 8,4);                 // rouge attacks vert's mewtwo
            if (g2.isOver()) break;
            if (!vertDown) { g2.play(6,0, 5,0); vertDown = true; }
            else           { g2.play(5,0, 6,0); vertDown = false; }
        }
        check(g2.isOver() && g2.getWinner() == Owner.ROUGE, "rouge wins when vert mewtwo dies");
        check(b2.get(8,4).isEmpty(), "dead mewtwo removed from board");

        System.out.println("\n" + (checks - fails) + "/" + checks + " checks passed.");
        if (fails > 0) System.exit(1);
    }
}
