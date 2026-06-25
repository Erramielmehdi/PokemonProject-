// Identifies which of the two players owns a piece / whose turn it is.
public enum Owner {
    ROUGE("rouge"),
    VERT("vert");

    private final String label;

    Owner(String label) { this.label = label; }

    public String getLabel() { return label; }

    // The opposing player.
    public Owner adverse() {
        return (this == ROUGE) ? VERT : ROUGE;
    }
}
