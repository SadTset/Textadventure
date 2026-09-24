package ch.bbw.textadventure.model;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class StoryLoader {
    private final YAMLMapper mapper = new YAMLMapper();

    public Story load(InputStream input) throws IOException {
        Objects.requireNonNull(input, "Story input must not be null");
        var story = mapper.readValue(input, Story.class);
        validate(story);
        return story;
    }

    private void validate(Story story) {
        if (story == null || story.rooms() == null || story.rooms().isEmpty()) {
            throw new IllegalArgumentException("Story must contain rooms.");
        }
        if (story.startRoom() == null || !story.rooms().containsKey(story.startRoom())) {
            throw new IllegalArgumentException("Start room does not exist: " + story.startRoom());
        }
        if (story.globals() == null) {
            throw new IllegalArgumentException("Story must contain global verb settings.");
        }
        story.rooms().forEach((roomId, room) -> validateRoom(story.rooms(), roomId, room));
    }

    private void validateRoom(Map<String, Room> rooms, String roomId, Room room) {
        if (room == null || room.verbs() == null) {
            throw new IllegalArgumentException("Room has no verbs: " + roomId);
        }
        room.verbs().forEach((verb, objects) -> validateObjects(rooms, roomId, verb, objects));
    }

    private void validateObjects(Map<String, Room> rooms, String roomId, String verb,
                                 Map<String, List<Action>> objects) {
        if (objects == null) {
            throw new IllegalArgumentException("Verb has no objects: " + roomId + "/" + verb);
        }
        objects.forEach((object, actions) -> validateActions(rooms, roomId, verb, object, actions));
    }

    private void validateActions(Map<String, Room> rooms, String roomId, String verb,
                                 String object, List<Action> actions) {
        if (actions == null || actions.isEmpty()) {
            throw new IllegalArgumentException("No actions for: " + roomId + "/" + verb + "/" + object);
        }
        for (var action : actions) {
            if (action == null) {
                throw new IllegalArgumentException("Null action for: " + roomId + "/" + verb + "/" + object);
            }
            if (action.room() != null && !rooms.containsKey(action.room())) {
                throw new IllegalArgumentException("Unknown target room: " + action.room());
            }
        }
    }
}
