/**
 * The class used to create new renderers
 */
public class PlayerFactory {
    /**
     * A constructor for the renderer factory
     */
    public PlayerFactory() {}

    /**
     * The factory to create the players
     * @param type The type of the player(human/whatever/clever/genius)
     * @return The player according to the type given
     */
    public Player buildPlayer(String type) {
        return switch (type) {
            case "human" -> new HumanPlayer();
            case "whatever" -> new WhateverPlayer();
            case "clever" -> new CleverPlayer();
            case "genius" -> new GeniusPlayer();
            default -> null;
        };
    }
}
