package ch.bbw.textadventure.controller;

import ch.bbw.textadventure.model.Action;
import ch.bbw.textadventure.model.GameState;
import ch.bbw.textadventure.model.Story;
import java.util.List;
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
        var verb = canonicalVerb(parts[0].toLowerCase(Locale.ROOT));
        var object = parts.length > 1 ? parts[1].toLowerCase(Locale.ROOT) : "";
        var room = story.rooms().get(state.currentRoomId());
        if ("help".equals(verb) && !room.verbs().containsKey(verb)) {
            return object.isEmpty() ? unknownVerb(verb) : unknownObject(verb, object);
        }

        var objects = room.verbs().get(verb);
        if (objects == null) {
            return unknownVerb(verb);
        }

        var actions = objects.get(object);
        if (actions == null) {
            return unknownObject(verb, object);
        }
        var action = firstMatchingAction(actions);
        return action == null ? unknownObject(verb, object) : execute(action);
    }

    private String canonicalVerb(String inputVerb) {
        if (story.globals().containsKey(inputVerb) && !"default".equals(inputVerb)) {
            return inputVerb;
        }
        for (var entry : story.globals().entrySet()) {
            var synonyms = entry.getValue().synonyms();
            if (synonyms != null && synonyms.contains(inputVerb)) {
                return entry.getKey();
            }
        }
        return inputVerb;
    }

    private String unknownVerb(String verb) {
        var settings = story.globals().get(verb);
        var message = settings == null ? null : settings.unknownVerb();
        return message == null ? story.globals().get("default").unknownVerb() : message;
    }

    private String unknownObject(String verb, String object) {
        var settings = story.globals().get(verb);
        var message = settings == null ? null : settings.unknownObject();
        if (message == null) {
            message = story.globals().get("default").unknownObject();
        }
        return message == null ? "" : message.replace("{}", object);
    }

    private Action firstMatchingAction(List<Action> actions) {
        for (var action : actions) {
            if (action.ifState() == null || state.hasState(action.ifState())) {
                return action;
            }
        }
        return null;
    }

    private String execute(Action action) {
        if (action.addState() != null) {
            state.addState(action.addState());
        }
        if (action.room() != null) {
            state.moveTo(action.room());
            return action.message() == null
                    ? currentRoomText()
                    : action.message() + System.lineSeparator() + currentRoomText();
        }
        return action.message() == null ? "" : action.message();
    }
}
