package ch.bbw.textadventure.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.bbw.textadventure.model.GameState;
import ch.bbw.textadventure.model.StoryLoader;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class GameControllerTest {
    @Test
    void describesTheStartingRoomWithoutConsoleInput() throws IOException {
        try (var input = getClass().getResourceAsStream("/tutorial.yaml")) {
            var story = new StoryLoader().load(input);
            var controller = new GameController(story, new GameState(story.startRoom()));

            assertTrue(controller.introduction().startsWith("Welcome to the tutorial!"));
            assertTrue(controller.currentRoomText().contains("Starting room"));
            assertTrue(controller.currentRoomText().contains("A simple light bulb"));
        }
    }

    @Test
    void movesBetweenRoomsButDoesNotIgnoreACondition() throws IOException {
        try (var input = getClass().getResourceAsStream("/tutorial.yaml")) {
            var story = new StoryLoader().load(input);
            var state = new GameState(story.startRoom());
            var controller = new GameController(story, state);

            assertTrue(controller.handleCommand("  go   east  ").contains("Another room"));
            assertTrue(controller.handleCommand("open wardrobe").contains("try to pick up the wardrobe"));
            assertFalse(state.hasState("wardrobeOpen"));
            assertEquals("You can't go east from here!", controller.handleCommand("go east"));
            assertTrue(controller.handleCommand("go west").contains("Starting room"));
        }
    }

    @Test
    void completesTheTutorialPuzzleWithFirstMatchingActions() throws IOException {
        try (var input = getClass().getResourceAsStream("/tutorial.yaml")) {
            var story = new StoryLoader().load(input);
            var state = new GameState(story.startRoom());
            var controller = new GameController(story, state);

            assertTrue(controller.handleCommand("take key").contains("pack it into your pocket"));
            assertTrue(state.hasState("key"));
            assertTrue(controller.handleCommand("take key").contains("already have the key"));

            controller.handleCommand("go east");
            assertFalse(state.hasState("wardrobeOpen"));
            assertEquals("You can't take coat!", controller.handleCommand("take coat"));
            assertTrue(controller.handleCommand("open wardrobe").contains("now unlocked"));
            assertTrue(state.hasState("wardrobeOpen"));
            assertTrue(controller.handleCommand("look wardrobe").contains("open door"));
            assertTrue(controller.handleCommand("take coat").contains("secret passage"));
            assertTrue(state.hasState("coat"));
            assertTrue(controller.handleCommand("go east").contains("A hidden room"));
            assertEquals("secret", state.currentRoomId());
        }
    }

    @Test
    void usesSynonymsHelpAndGlobalErrorMessages() throws IOException {
        try (var input = getClass().getResourceAsStream("/tutorial.yaml")) {
            var story = new StoryLoader().load(input);
            var controller = new GameController(story, new GameState(story.startRoom()));

            assertEquals(controller.handleCommand("look table"), controller.handleCommand("examine table"));
            assertEquals("You can't go north from here!", controller.handleCommand("go north"));
            assertEquals("Nothing to open here!", controller.handleCommand("open key"));
            assertEquals("You cannot do that.", controller.handleCommand("dance"));
            assertTrue(controller.handleCommand("help").contains("typing something like <verb object>"));
            assertEquals(controller.handleCommand("help"), controller.handleCommand("wtf"));
            assertEquals("Possible verbs include: go, take, examine, open,...",
                    controller.handleCommand("help verbs"));
            assertTrue(controller.handleCommand("walk east").contains("Another room"));
        }
    }
}
