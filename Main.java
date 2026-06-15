class Main {

    public static void main(String[] args) {

        // Create the two example pokemons from the subject using the CSV constructor
        Pokemon p1 = new Pokemon(57, "Robert");   // Colossinge
        Pokemon p2 = new Pokemon(94, "Gustave");  // Ectoplasma

        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║           *** COMBAT POKEMON ***             ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.println();
        System.out.println(p1);
        System.out.println("                    VS");
        System.out.println(p2);
        System.out.println();
        System.out.println("══════════════════════════════════════════════");
        System.out.println();

        int tour = 1;
        while (p1.estVivant() && p2.estVivant()) {
            System.out.println("--- Tour " + tour + " ---");
            p1.attaque(p2);
            System.out.println();
            tour++;
        }

        System.out.println("══════════════════════════════════════════════");
        System.out.println("               *** FIN DU COMBAT ***");
        System.out.println();

        if (p1.estVivant()) {
            System.out.println(p1.getNom() + " (" + Type.getEspece(p1.getNumPokedex()) + ") remporte le combat !");
            System.out.println(p2.getNom() + " (" + Type.getEspece(p2.getNumPokedex()) + ") retourne dans sa Pokéball...");
        } else {
            System.out.println(p2.getNom() + " (" + Type.getEspece(p2.getNumPokedex()) + ") remporte le combat !");
            System.out.println(p1.getNom() + " (" + Type.getEspece(p1.getNumPokedex()) + ") retourne dans sa Pokéball...");
        }
        System.out.println();
    }
}
