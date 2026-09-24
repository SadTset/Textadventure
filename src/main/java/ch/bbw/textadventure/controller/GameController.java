package ch.bbw.textadventure.controller;

import ch.bbw.textadventure.model.GameState;
import ch.bbw.textadventure.model.Story;
import java.util.Locale;
import java.util.Objects;

public final class GameController {
    private final Story story;
    private final GameState state;

    public GameController(Story story, GameState state) {
        this.story = Objects.requireNonNull(story, "Story must not be null");
        this.state = Objects.requireNonNull(state, "Game state must not be null");
        if (!story.rooms().containsKey(state.currentRoomId())) {
            throw new IllegalArgumentException("Current room does not exist: " + state.currentRoomId());
        }
    }

    public String introduction() {
        return story.description();
    }

    public String currentRoomText() {
        var room = story.rooms().get(state.currentRoomId());
        return room.name() + System.lineSeparator() + room.description();
    }

    public String handleCommand(String input) {
        var parts = input.trim().split("\\s+", 2);
        var verb = parts[0].toLowerCase(Locale.ROOT);
        var object = parts.length > 1 ? parts[1].toLowerCase(Locale.ROOT) : "";
        var room = story.rooms().get(state.currentRoomId());
        var objects = room.verbs().get(verb);
        var actions = objects == null ? null : objects.get(object);
        if (actions == null) {
            return "That action is not available here yet.";
        }

        for (var action : actions) {
            if (action.ifState() == null && action.room() != null) {
                state.moveTo(action.room());
                return currentRoomText();
            }
        }
        return "That action is not available here yet.";
    }
}
