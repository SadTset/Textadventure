package ch.bbw.textadventure.model;

public record Action(
        String ifState,
        String addState,
        String message,
        String room
) {
}
