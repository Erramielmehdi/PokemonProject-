import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

// Swing front-end for Pokéchess. Shows the turn banner, draws the 9x9 board
// with each pokemon's sprite and current HP, and turns mouse clicks into
// moves / attacks handled by the Game.
public class GameGUI extends JFrame {

    private static final int CELL = 74;

    // Colours
    private static final Color BANNER_BG   = new Color(40, 160, 230);
    private static final Color ROUGE_COLOR = new Color(210, 40,  40);
    private static final Color VERT_COLOR  = new Color(30,  160, 60);
    private static final Color EMPTY_BG    = new Color(230, 230, 230);
    private static final Color GRID_LINE   = new Color(180, 180, 180);
    private static final Color SELECT_BG   = new Color(255, 240, 100);
    private static final Color ROUGE_TINT  = new Color(248, 215, 215);
    private static final Color VERT_TINT   = new Color(214, 240, 220);
    // Circle drawn on valid move/attack targets when a piece is selected
    private static final Color CIRCLE_FILL   = new Color(200, 200, 200, 130);
    private static final Color CIRCLE_STROKE = new Color(80,  80,  80,  200);

    private final Game game = new Game();
    private final JLabel banner = new JLabel("", SwingConstants.CENTER);
    private final JLabel status = new JLabel(" ", SwingConstants.CENTER);
    private final CellButton[][] buttons = new CellButton[Board.SIZE][Board.SIZE];

    // Cache of loaded sprite images, keyed by pokedex number.
    // A null value that is mapped means "file not found" so we skip the disk.
    private final Map<Integer, Image> sprites = new HashMap<>();

    // Currently selected cell (-1 when nothing is selected).
    private int selRow = -1;
    private int selCol = -1;

    public GameGUI() {
        super("Pokéchess");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        banner.setOpaque(true);
        banner.setBackground(BANNER_BG);
        banner.setFont(new Font("SansSerif", Font.BOLD, 24));
        banner.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        add(banner, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(Board.SIZE, Board.SIZE, 1, 1));
        grid.setBackground(GRID_LINE);
        grid.setBorder(BorderFactory.createLineBorder(GRID_LINE, 1));
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                CellButton b = new CellButton(r, c);
                buttons[r][c] = b;
                grid.add(b);
            }
        }
        add(grid, BorderLayout.CENTER);

        status.setFont(new Font("SansSerif", Font.PLAIN, 14));
        status.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        add(status, BorderLayout.SOUTH);

        refresh();
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    // Handle a click on cell (r,c): select, deselect, move or attack.
    private void onCellClicked(int r, int c) {
        if (game.isOver()) return;

        Cell clicked = game.getBoard().get(r, c);

        // Nothing selected yet: pick one of our own pieces.
        if (selRow < 0) {
            if (!clicked.isEmpty() && clicked.getOwner() == game.getCurrent()) {
                selRow = r;
                selCol = c;
                status.setText("Pokémon sélectionné : " + clicked.getOccupant().getNom());
            } else if (!clicked.isEmpty()) {
                status.setText("Ce n'est pas votre pokémon.");
            }
            refresh();
            return;
        }

        // Click the same cell again -> deselect.
        if (r == selRow && c == selCol) {
            clearSelection();
            status.setText(" ");
            refresh();
            return;
        }

        // Click another of our own pieces -> change selection.
        if (!clicked.isEmpty() && clicked.getOwner() == game.getCurrent()) {
            selRow = r;
            selCol = c;
            status.setText("Pokémon sélectionné : " + clicked.getOccupant().getNom());
            refresh();
            return;
        }

        // Otherwise attempt the action towards the target.
        Game.Result result = game.play(selRow, selCol, r, c);
        if (result == Game.Result.INVALID) {
            status.setText(game.getLastMessage());
        } else {
            clearSelection();
            status.setText(game.getLastMessage().isEmpty() ? " " : game.getLastMessage());
        }
        refresh();

        if (game.isOver()) {
            JOptionPane.showMessageDialog(this,
                    "Le joueur " + game.getWinner().getLabel() + " a gagné !",
                    "Fin de la partie",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void clearSelection() {
        selRow = -1;
        selCol = -1;
    }

    // Returns true when (r,c) is a valid target for the currently selected piece.
    private boolean isValidTarget(int r, int c) {
        if (selRow < 0) return false;
        if (!game.getBoard().isAdjacent(selRow, selCol, r, c)) return false;
        Cell target = game.getBoard().get(r, c);
        return target.isEmpty() || target.getOwner() != game.getCurrent();
    }

    // Update the banner and repaint every cell to reflect the model.
    private void refresh() {
        if (game.isOver()) {
            Owner w = game.getWinner();
            banner.setText("Le joueur " + w.getLabel() + " a gagné !");
            banner.setForeground(w == Owner.ROUGE ? ROUGE_COLOR : VERT_COLOR);
        } else {
            Owner cur = game.getCurrent();
            banner.setText("Au joueur " + cur.getLabel() + " de jouer");
            banner.setForeground(cur == Owner.ROUGE ? ROUGE_COLOR : VERT_COLOR);
        }
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                buttons[r][c].repaint();
            }
        }
    }

    // Load images/<num>.png lazily; returns null when the file is absent.
    private Image spriteFor(int num) {
        if (sprites.containsKey(num)) return sprites.get(num); // may be null (known-missing)

        Image img = null;
        File f = new File("images" + File.separator + num + ".png");
        if (f.exists()) {
            ImageIcon icon = new ImageIcon(f.getAbsolutePath());
            if (icon.getIconWidth() > 0) img = icon.getImage();
        }
        sprites.put(num, img);
        return img;
    }

    // One board square, rendered by hand to show sprite + HP number.
    private class CellButton extends JComponent {
        private final int row;
        private final int col;

        CellButton(int row, int col) {
            this.row = row;
            this.col = col;
            setPreferredSize(new Dimension(CELL, CELL));
            addMouseListener(new MouseAdapter() {
                @Override public void mousePressed(MouseEvent e) {
                    onCellClicked(row, col);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            Cell cell = game.getBoard().get(row, col);
            boolean selected    = (row == selRow && col == selCol);
            boolean validTarget = isValidTarget(row, col);

            // --- Background ---
            Color bg = EMPTY_BG;
            if (!cell.isEmpty()) {
                bg = (cell.getOwner() == Owner.ROUGE) ? ROUGE_TINT : VERT_TINT;
            }
            if (selected) bg = SELECT_BG;
            g2.setColor(bg);
            g2.fillRect(0, 0, getWidth(), getHeight());

            // --- Circle hint on valid targets (drawn before the piece) ---
            if (validTarget && cell.isEmpty()) {
                int w = getWidth();
                int h = getHeight();
                int margin = 12;
                g2.setColor(CIRCLE_FILL);
                g2.fillOval(margin, margin, w - 2 * margin, h - 2 * margin);
                g2.setColor(CIRCLE_STROKE);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawOval(margin, margin, w - 2 * margin, h - 2 * margin);
            }

            // --- Piece ---
            if (!cell.isEmpty()) {
                drawPiece(g2, cell, validTarget && !cell.isEmpty());
            }

            g2.dispose();
        }

        private void drawPiece(Graphics2D g2, Cell cell, boolean isAttackTarget) {
            Pokemon p = cell.getOccupant();
            int w = getWidth();
            int h = getHeight();

            // Optional attack-target ring around enemy pieces
            if (isAttackTarget) {
                g2.setColor(new Color(255, 60, 60, 140));
                g2.setStroke(new BasicStroke(3f));
                int m = 3;
                g2.drawOval(m, m, w - 2 * m, h - 2 * m);
            }

            Image sprite = spriteFor(p.getNumPokedex());
            int spriteBottom;
            if (sprite != null) {
                int sw = (int) (w * 0.80);
                int sh = (int) (h * 0.64);
                int sx = (w - sw) / 2;
                int sy = 2;
                g2.drawImage(sprite, sx, sy, sw, sh, null);
                spriteBottom = sy + sh;
            } else {
                // Fallback: coloured disc with abbreviated name / crown for Mewtwo.
                Color disc = (cell.getOwner() == Owner.ROUGE) ? ROUGE_COLOR : VERT_COLOR;
                int d = (int) (Math.min(w, h) * 0.58);
                int x = (w - d) / 2;
                int y = 4;
                g2.setColor(disc);
                g2.fillOval(x, y, d, d);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(x, y, d, d);

                boolean isMewtwo = (p.getNumPokedex() == Game.MEWTWO);
                String abbr = isMewtwo ? "♔"
                        : p.getNom().substring(0, Math.min(3, p.getNom().length()));
                g2.setFont(new Font("SansSerif", Font.BOLD, isMewtwo ? 22 : 13));
                FontMetrics fm = g2.getFontMetrics();
                int tx = x + (d - fm.stringWidth(abbr)) / 2;
                int ty = y + (d + fm.getAscent() - fm.getDescent()) / 2;
                g2.setColor(Color.WHITE);
                g2.drawString(abbr, tx, ty);
                spriteBottom = y + d;
            }

            // HP number — coloured by the piece's owner (not the current player).
            Color hpColor = (cell.getOwner() == Owner.ROUGE) ? ROUGE_COLOR : VERT_COLOR;
            String hp = String.valueOf(p.getPvActuels());
            g2.setFont(new Font("SansSerif", Font.BOLD, 13));
            FontMetrics fm = g2.getFontMetrics();
            int tx = (w - fm.stringWidth(hp)) / 2;
            int ty = Math.min(h - 2, spriteBottom + fm.getAscent() + 1);
            g2.setColor(hpColor);
            g2.drawString(hp, tx, ty);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameGUI().setVisible(true));
    }
}
