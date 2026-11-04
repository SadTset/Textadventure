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
    void exploresTheWorkshopAndObservatoryUsingALantern() throws IOException {
        try (var input = getClass().getResourceAsStream("/tutorial.yaml")) {
            var story = new StoryLoader().load(input);
            var state = new GameState(story.startRoom());
            var controller = new GameController(story, state);

            assertTrue(controller.currentRoomText().contains("west"));
            assertTrue(controller.handleCommand("go west").contains("Clockmaker's workshop"));
            assertTrue(controller.handleCommand("go up").contains("too dark"));
            assertEquals("workshop", state.currentRoomId());
            assertTrue(controller.handleCommand("look bench").contains("lantern"));
            assertTrue(controller.handleCommand("take lantern").contains("light the lantern"));
            assertTrue(controller.handleCommand("take lantern").contains("already carrying"));
            assertTrue(controller.handleCommand("go up").contains("Rooftop observatory"));
            assertTrue(controller.handleCommand("look telescope").contains("silver constellation"));
            controller.handleCommand("go down");
            assertTrue(controller.handleCommand("go east").contains("Starting room"));
            assertTrue(state.hasState("lantern"));
        }
    }

    @Test
    void showsEarlierCommandsInOrderWithoutRecordingHistoryItself() throws IOException {
        try (var input = getClass().getResourceAsStream("/tutorial.yaml")) {
            var story = new StoryLoader().load(input);
            var state = new GameState(story.startRoom());
            var controller = new GameController(story, state);

            assertEquals("No commands yet.", controller.handleCommand("history"));
            assertEquals("", controller.handleCommand("   "));
            assertEquals("No commands yet.", controller.handleCommand("history"));
            assertTrue(controller.introduction().contains("'history'"));
            controller.handleCommand("  examine table  ");
            controller.handleCommand("dance");
            controller.handleCommand("walk east");

            var expected = String.join(System.lineSeparator(),
                    "Command history:", "1. examine table", "2. dance", "3. walk east");
            assertEquals(expected, controller.handleCommand("history"));
            assertEquals(expected, controller.handleCommand("  HISTORY  "));
            assertEquals("room-two", state.currentRoomId());
            assertTrue(state.states().isEmpty());
        }
    }

    @Test
    void supportsHistoryWithAnExternalStory() throws IOException {
        try (var input = getClass().getResourceAsStream("/forest.yaml")) {
            var story = new StoryLoader().load(input);
            var controller = new GameController(story, new GameState(story.startRoom()));

            assertEquals("No commands yet.", controller.handleCommand("history"));
            controller.handleCommand("look sign");
            assertEquals("Command history:" + System.lineSeparator() + "1. look sign",
                    controller.handleCommand("history"));
        }
    }

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
