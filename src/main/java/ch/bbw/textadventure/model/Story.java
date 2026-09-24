package ch.bbw.textadventure.model;

import java.util.Map;

public record Story(
        String startRoom,
        String description,
        Map<String, Room> rooms,
        Map<String, GlobalVerb> globals
) {
}
