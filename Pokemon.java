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

    public boolean estVivant() {
        return pvActuels > 0;
    }
}
