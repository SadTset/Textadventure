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
            assertEquals(3, story.rooms().size());
            assertTrue(story.description().startsWith("Welcome to the tutorial!"));
            assertEquals("Starting room", story.rooms().get("room-one").name());
            assertEquals("key", story.rooms().get("room-one").verbs().get("take").get("key").getFirst().ifState());
            assertEquals("wardrobeOpen", story.rooms().get("room-two").verbs().get("open")
                    .get("wardrobe").getFirst().addState());
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
        var error = assertThrows(IllegalArgumentException.class, () -> loader.load(yaml("""
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
                """)));

        assertEquals("Unknown target room: missing", error.getMessage());
    }

    private ByteArrayInputStream yaml(String content) {
        return new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
    }
}
