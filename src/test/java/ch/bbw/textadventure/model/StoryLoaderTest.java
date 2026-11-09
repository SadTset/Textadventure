package ch.bbw.textadventure.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class StoryLoaderTest {
    private final StoryLoader loader = new StoryLoader();

    @Test
    void loadsAllPartsOfTheTutorial() throws IOException {
        try (var input = getClass().getResourceAsStream("/tutorial.yaml")) {
            var story = loader.load(input);

            assertEquals("room-one", story.startRoom());
            assertEquals(11, story.rooms().size());
            assertTrue(story.description().startsWith("Welcome to the tutorial!"));
            assertEquals("Starting room", story.rooms().get("room-one").name());
            assertEquals("key", story.rooms().get("room-one").verbs().get("take").get("key").getFirst().ifState());
            assertTrue(story.rooms().get("room-two").verbs().get("open").get("wardrobe").stream()
                    .anyMatch(action -> "key".equals(action.ifState())
                            && "wardrobeOpen".equals(action.addState())));
            assertTrue(story.globals().get("go").synonyms().contains("walk"));
            assertEquals("You can't go {} from here!", story.globals().get("go").unknownObject());
        }
    }

    @Test
    void loadsAStoryWithDifferentRoomNames() throws IOException {
        var story = loader.load(yaml("""
                startRoom: forest
                description: A different story
                rooms:
                  forest:
                    name: Forest
                    description: Trees surround you.
                    verbs:
                      go:
                        north:
                          - room: cave
                  cave:
                    name: Cave
                    description: It is dark.
                    verbs: {}
                globals:
                  default:
                    unknownVerb: No.
                    unknownObject: ""
                """));

        assertEquals("forest", story.startRoom());
        assertEquals("cave", story.rooms().get("forest").verbs().get("go").get("north").getFirst().room());
    }

    @Test
    void rejectsAnUnknownTargetRoom() {
        var input = yaml("""
                startRoom: forest
                rooms:
                  forest:
                    name: Forest
                    description: Trees surround you.
                    verbs:
                      go:
                        north:
                          - room: missing
                globals:
                  default:
                    unknownVerb: No.
                    unknownObject: ""
                """);
        var error = assertThrows(IllegalArgumentException.class, () -> loader.load(input));

        assertEquals("Unknown target room: missing", error.getMessage());
    }

    private ByteArrayInputStream yaml(String content) {
        return new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void rejectsInvalidGlobalSettingsBeforeGameplay() throws IOException {
        String tutorial;
        try (var input = getClass().getResourceAsStream("/tutorial.yaml")) {
            tutorial = new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
        for (var globals : new String[] {"{}", "{default: null}",
                "{default: {unknownVerb: ' '}}",
                "{default: {unknownVerb: No.}, go: null}"}) {
            var invalid = tutorial.substring(0, tutorial.indexOf("\nglobals:"))
                    + "\nglobals: " + globals;
            var input = yaml(invalid);
            var error = assertThrows(IllegalArgumentException.class, () -> loader.load(input));
            assertTrue(error.getMessage().contains("global"));
        }
    }
}
