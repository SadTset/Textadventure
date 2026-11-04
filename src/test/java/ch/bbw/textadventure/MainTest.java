package ch.bbw.textadventure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class MainTest {
    @Test
    void reportsAnInvalidStoryPathClearly() {
        var error = assertThrows(IOException.class,
                () -> Main.loadStory(new String[] {"invalid\u0000.yaml"}));

        assertTrue(error.getMessage().startsWith("Could not load story '"));
        assertTrue(error.getCause() instanceof java.nio.file.InvalidPathException);
    }

    @TempDir
    java.nio.file.Path temporaryDirectory;

    @Test
    void usesTheBundledTutorialByDefault() throws IOException {
        var story = Main.loadStory(new String[0]);

        assertEquals("room-one", story.startRoom());
    }

    @Test
    void loadsAStoryFromAFileArgument() throws IOException {
        var storyFile = temporaryDirectory.resolve("forest.yaml");
        try (var input = getClass().getResourceAsStream("/forest.yaml")) {
            Files.copy(input, storyFile);
        }

        var story = Main.loadStory(new String[] {storyFile.toString()});

        assertEquals("clearing", story.startRoom());
        assertEquals("Abandoned cabin", story.rooms().get("cabin").name());
    }

    @Test
    void reportsAMissingStoryFileClearly() {
        var missingFile = temporaryDirectory.resolve("missing.yaml");

        var error = assertThrows(IOException.class,
                () -> Main.loadStory(new String[] {missingFile.toString()}));

        assertTrue(error.getMessage().startsWith("Could not load story '"));
        assertTrue(error.getMessage().contains("missing.yaml"));
    }

    @Test
    void reportsMalformedYamlClearly() throws IOException {
        var malformedFile = temporaryDirectory.resolve("broken.yaml");
        Files.writeString(malformedFile, "rooms: [not finished");

        var error = assertThrows(IOException.class,
                () -> Main.loadStory(new String[] {malformedFile.toString()}));

        assertTrue(error.getMessage().startsWith("Could not load story '"));
        assertTrue(error.getMessage().contains("broken.yaml"));
    }
}
