package com.rpgwave.entities;

/**
 * Tipos de personagens disponíveis.
 * Facilita a criação de menus de seleção.
 */
public enum CharacterType {
    WARRIOR("Guerreiro", "Especialista em combate corpo a corpo. Muito HP e resistência."),
    ARCHER("Arqueiro", "Atira flechas à distância. Ágil e preciso."),
    MAGE("Mago", "Lança magias poderosas. Frágil mas destruidor.");

    private final String displayName;
    private final String description;

    CharacterType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
}