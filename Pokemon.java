import java.io.*;
import java.nio.charset.StandardCharsets;

class Pokemon {

    private int    numPokedex;
    private String nom;
    private int    type1;
    private int    type2;
    private int    pvMax;
    private int    pvActuels;
    private int    att;
    private int    def;
    private int    vit;

    // Default constructor — creates Bulbizarre
    public Pokemon() {
        this.numPokedex = 1;
        this.nom        = "Bulbizarre";
        this.type1      = Type.PLANTE;
        this.type2      = Type.POISON;
        this.pvMax      = 45;
        this.pvActuels  = 45;
        this.att        = 49;
        this.def        = 49;
        this.vit        = 45;
    }

    // Full constructor
    public Pokemon(int numPokedex, String nom, int type1, int type2,
                   int pv, int att, int def, int vit) {
        this.numPokedex = numPokedex;
        this.nom        = nom;
        this.type1      = type1;
        this.type2      = type2;
        this.pvMax      = pv;
        this.pvActuels  = pv;
        this.att        = att;
        this.def        = def;
        this.vit        = vit;
    }

    // Simplified constructor — reads stats from pokedex_gen1.csv
    public Pokemon(int numPokedex, String nom) {
        this.numPokedex = numPokedex;
        this.nom        = nom;
        // Safe defaults in case the file read fails
        this.type1     = Type.NORMAL;
        this.type2     = Type.SANS;
        this.pvMax     = 1;
        this.pvActuels = 1;
        this.att       = 1;
        this.def       = 1;
        this.vit       = 1;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("pokedex_gen1.csv"), StandardCharsets.UTF_8))) {

            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; } // skip header
                String[] cols = line.split(";");
                if (cols.length < 8) continue;
                int num = Integer.parseInt(cols[0].trim());
                if (num == numPokedex) {
                    this.type1     = Type.getIndiceType(cols[2].trim());
                    this.type2     = Type.getIndiceType(cols[3].trim()); // returns SANS if empty
                    this.pvMax     = Integer.parseInt(cols[4].trim());
                    this.pvActuels = this.pvMax;
                    this.att       = Integer.parseInt(cols[5].trim());
                    this.def       = Integer.parseInt(cols[6].trim());
                    this.vit       = Integer.parseInt(cols[7].trim());
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du pokedex : " + e.getMessage());
        }
    }

    // --- Getters ---
    public int    getNumPokedex() { return numPokedex; }
    public String getNom()        { return nom; }
    public int    getType1()      { return type1; }
    public int    getType2()      { return type2; }
    public int    getPvMax()      { return pvMax; }
    public int    getPvActuels()  { return pvActuels; }
    public int    getAtt()        { return att; }
    public int    getDef()        { return def; }
    public int    getVit()        { return vit; }

    // --- Setters (only where meaningful) ---
    public void setNom(String nom)       { this.nom = nom; }
    public void setPvActuels(int pv)     { this.pvActuels = Math.max(0, pv); }

    public boolean estVivant() {
        return pvActuels > 0;
    }

    // Calculates damage dealt by this pokemon to the defender, with type effectiveness
    private int calculerDegats(Pokemon defenseur) {
        int degats = this.att - defenseur.def;
        if (degats < 1) degats = 1;

        // Apply type effectiveness: attacker's type vs each of defender's types
        double multiplicateur = Type.getEfficacite(this.type1, defenseur.type1);
        if (defenseur.type2 != Type.SANS) {
            multiplicateur *= Type.getEfficacite(this.type1, defenseur.type2);
        }

        degats = (int)(degats * multiplicateur);
        if (degats < 1) degats = 1;

        // Print effectiveness message
        if (multiplicateur == 0.0) {
            System.out.println("  Ça n'affecte pas " + defenseur.nom + "...");
        } else if (multiplicateur > 1.0) {
            System.out.println("  C'est super efficace !");
        } else if (multiplicateur < 1.0) {
            System.out.println("  Ce n'est pas très efficace...");
        }

        return degats;
    }

    // Single strike used by Pokéchess: this pokemon hits the defender once.
    // Reuses the exact same damage formula (with type effectiveness) as the
    // battle engine, applies the damage to the defender and returns it.
    public int frappe(Pokemon defenseur) {
        int degats = calculerDegats(defenseur);
        defenseur.pvActuels = Math.max(0, defenseur.pvActuels - degats);
        return degats;
    }

    // One round of combat: fastest attacks first, then the other counter-attacks if still alive
    public void attaque(Pokemon adverse) {
        Pokemon premier = (this.vit >= adverse.vit) ? this : adverse;
        Pokemon second  = (this.vit >= adverse.vit) ? adverse : this;

        // First attack
        int degats = premier.calculerDegats(second);
        System.out.println(premier.nom + " attaque " + second.nom + " et inflige " + degats + " dégâts !");
        second.pvActuels = Math.max(0, second.pvActuels - degats);
        System.out.println("  " + second.nom + " : " + second.pvActuels + "/" + second.pvMax + " PV");

        // Counter-attack if still alive
        if (second.estVivant()) {
            System.out.println();
            degats = second.calculerDegats(premier);
            System.out.println(second.nom + " contre-attaque " + premier.nom + " et inflige " + degats + " dégâts !");
            premier.pvActuels = Math.max(0, premier.pvActuels - degats);
            System.out.println("  " + premier.nom + " : " + premier.pvActuels + "/" + premier.pvMax + " PV");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Pokemon)) return false;
        Pokemon other = (Pokemon) obj;
        return this.numPokedex == other.numPokedex && this.nom.equals(other.nom);
    }

    @Override
    public String toString() {
        String espece = Type.getEspece(numPokedex);
        String t1     = Type.getNomType(type1);
        String t2     = (type2 != Type.SANS) ? " / " + Type.getNomType(type2) : "";
        return String.format(
            "[%s] %s (n°%d) | Type : %s%s | PV : %d/%d | Att : %d | Def : %d | Vit : %d",
            nom, espece, numPokedex, t1, t2, pvActuels, pvMax, att, def, vit
        );
    }
}
