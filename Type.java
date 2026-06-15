class Type {

    /*************** 1G *******************/

    public static final String[] espece = {
        "Bulbizarre","Herbizarre","Florizarre","Salamèche","Reptincel","Dracaufeu",
        "Carapuce","Carabaffe","Tortank","Chenipan","Chrysacier","Papilusion",
        "Aspicot","Coconfort","Dardargnan","Roucool","Roucoups","Roucarnage",
        "Rattata","Rattatac","Piafabec","Rapasdepic","Abo","Arbok","Pikachu",
        "Raichu","Sabelette","Sablaireau","Nidoran♀","Nidorina","Nidoqueen",
        "Nidoran♂","Nidorino","Nidoking","Mélofée","Mélodelfe","Goupix","Feunard",
        "Rondoudou","Grodoudou","Nosferapti","Nosferalto","Mystherbe","Ortide",
        "Rafflesia","Paras","Parasect","Mimitoss","Aéromite","Taupiqueur",
        "Triopikeur","Miaouss","Persian","Psykokwak","Akwakwak","Férosinge",
        "Colossinge","Caninos","Arcanin","Ptitard","Têtarte","Tartard","Abra",
        "Kadabra","Alakazam","Machoc","Machopeur","Mackogneur","Chétiflor",
        "Boustiflor","Empiflor","Tentacool","Tentacruel","Racaillou","Gravalanch",
        "Grolem","Ponyta","Galopa","Ramoloss","Flagadoss","Magnéti","Magnéton",
        "Canarticho","Doduo","Dodrio","Otaria","Lamantine","Tadmorv","Grotadmorv",
        "Kokiyas","Crustabri","Fantominus","Spectrum","Ectoplasma","Onix",
        "Soporifik","Hypnomade","Krabby","Krabboss","Voltorbe","Électrode",
        "Noeunoeuf","Noadkoko","Osselait","Ossatueur","Kicklee","Tygnon",
        "Excelangue","Smogo","Smogogo","Rhinocorne","Rhinoféros","Leveinard",
        "Saquedeneu","Kangourex","Hypotrempe","Hypocéan","Poissirène","Poissoroy",
        "Stari","Staross","M. Mime","Insécateur","Lippoutou","Élektek","Magmar",
        "Scarabrute","Tauros","Magicarpe","Léviator","Lokhlass","Métamorph",
        "Évoli","Aquali","Voltali","Pyroli","Porygon","Amonita","Amonistar",
        "Kabuto","Kabutops","Ptéra","Ronflex","Artikodin","Électhor","Sulfura",
        "Minidraco","Draco","Dracolosse","Mewtwo","Mew"
    };

    public static final String[] nomsType = {
        "NORMAL","FEU","EAU","PLANTE","ELECTRIK","GLACE","COMBAT","POISON",
        "SOL","VOL","PSY","INSECTE","ROCHE","SPECTRE","DRAGON","SANS"
    };

    public static final int NORMAL   = 0;
    public static final int FEU      = 1;
    public static final int EAU      = 2;
    public static final int PLANTE   = 3;
    public static final int ELECTRIK = 4;
    public static final int GLACE    = 5;
    public static final int COMBAT   = 6;
    public static final int POISON   = 7;
    public static final int SOL      = 8;
    public static final int VOL      = 9;
    public static final int PSY      = 10;
    public static final int INSECTE  = 11;
    public static final int ROCHE    = 12;
    public static final int SPECTRE  = 13;
    public static final int DRAGON   = 14;
    public static final int SANS     = 15;

    public static final double NEUTRE         = 1.0;
    public static final double INEFFICACE     = 0.0;
    public static final double PAS_EFFICACE   = 0.5;
    public static final double SUPER_EFFICACE = 2.0;

    // efficacite[typeAtt][typeDef]
    private static final double[][] efficacite = {
        {NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, PAS_EFFICACE, INEFFICACE, NEUTRE},       // NORMAL
        {NEUTRE, PAS_EFFICACE, PAS_EFFICACE, SUPER_EFFICACE, NEUTRE, SUPER_EFFICACE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, SUPER_EFFICACE, PAS_EFFICACE, NEUTRE, PAS_EFFICACE}, // FEU
        {NEUTRE, SUPER_EFFICACE, PAS_EFFICACE, PAS_EFFICACE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, SUPER_EFFICACE, NEUTRE, NEUTRE, NEUTRE, SUPER_EFFICACE, NEUTRE, PAS_EFFICACE},       // EAU
        {NEUTRE, PAS_EFFICACE, SUPER_EFFICACE, PAS_EFFICACE, NEUTRE, NEUTRE, NEUTRE, PAS_EFFICACE, SUPER_EFFICACE, PAS_EFFICACE, NEUTRE, PAS_EFFICACE, SUPER_EFFICACE, NEUTRE, PAS_EFFICACE}, // PLANTE
        {NEUTRE, NEUTRE, SUPER_EFFICACE, PAS_EFFICACE, PAS_EFFICACE, NEUTRE, NEUTRE, NEUTRE, INEFFICACE, SUPER_EFFICACE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, PAS_EFFICACE},           // ELECTRIK
        {NEUTRE, NEUTRE, PAS_EFFICACE, SUPER_EFFICACE, NEUTRE, PAS_EFFICACE, NEUTRE, NEUTRE, SUPER_EFFICACE, SUPER_EFFICACE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, SUPER_EFFICACE},     // GLACE
        {SUPER_EFFICACE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, SUPER_EFFICACE, NEUTRE, PAS_EFFICACE, NEUTRE, PAS_EFFICACE, PAS_EFFICACE, PAS_EFFICACE, SUPER_EFFICACE, INEFFICACE, NEUTRE}, // COMBAT
        {NEUTRE, NEUTRE, NEUTRE, SUPER_EFFICACE, NEUTRE, NEUTRE, NEUTRE, PAS_EFFICACE, PAS_EFFICACE, NEUTRE, NEUTRE, SUPER_EFFICACE, PAS_EFFICACE, PAS_EFFICACE, NEUTRE},         // POISON
        {NEUTRE, SUPER_EFFICACE, NEUTRE, PAS_EFFICACE, SUPER_EFFICACE, NEUTRE, NEUTRE, SUPER_EFFICACE, NEUTRE, INEFFICACE, NEUTRE, PAS_EFFICACE, SUPER_EFFICACE, NEUTRE, NEUTRE}, // SOL
        {NEUTRE, NEUTRE, NEUTRE, SUPER_EFFICACE, PAS_EFFICACE, NEUTRE, SUPER_EFFICACE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, SUPER_EFFICACE, PAS_EFFICACE, NEUTRE, NEUTRE},             // VOL
        {NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, SUPER_EFFICACE, SUPER_EFFICACE, NEUTRE, NEUTRE, PAS_EFFICACE, NEUTRE, NEUTRE, NEUTRE, NEUTRE},                           // PSY
        {NEUTRE, PAS_EFFICACE, NEUTRE, SUPER_EFFICACE, NEUTRE, NEUTRE, PAS_EFFICACE, SUPER_EFFICACE, NEUTRE, PAS_EFFICACE, SUPER_EFFICACE, NEUTRE, NEUTRE, PAS_EFFICACE, NEUTRE}, // INSECTE
        {NEUTRE, SUPER_EFFICACE, NEUTRE, NEUTRE, NEUTRE, SUPER_EFFICACE, PAS_EFFICACE, NEUTRE, PAS_EFFICACE, SUPER_EFFICACE, NEUTRE, SUPER_EFFICACE, NEUTRE, NEUTRE, NEUTRE},     // ROCHE
        {INEFFICACE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, INEFFICACE, NEUTRE, NEUTRE, SUPER_EFFICACE, NEUTRE},                                 // SPECTRE
        {NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, SUPER_EFFICACE}                                          // DRAGON
    };

    // Returns the species name for the given pokedex number (1-151)
    public static String getEspece(int numPokedex) {
        return espece[numPokedex - 1];
    }

    // Returns the lowercase type name for the given type constant
    // Example: getNomType(Type.EAU) -> "eau"
    public static String getNomType(int type) {
        return nomsType[type].toLowerCase();
    }

    // Returns the effectiveness multiplier when typeAtt attacks typeDef
    // Example: getEfficacite(Type.EAU, Type.FEU) -> 2.0
    public static double getEfficacite(int typeAtt, int typeDef) {
        if (typeAtt == SANS || typeDef == SANS) return NEUTRE;
        return efficacite[typeAtt][typeDef];
    }

    // Returns the type index for the given type name string (case-insensitive)
    // Example: getIndiceType("spectre") -> 13
    public static int getIndiceType(String type) {
        if (type == null || type.trim().isEmpty()) return SANS;
        for (int i = 0; i < nomsType.length; i++) {
            if (nomsType[i].equalsIgnoreCase(type.trim())) {
                return i;
            }
        }
        return SANS;
    }
}
