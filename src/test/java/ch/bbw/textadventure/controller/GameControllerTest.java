package ch.bbw.textadventure.controller;

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
            assertTrue(controller.handleCommand("go east").contains("not available"));
            assertTrue(controller.handleCommand("go west").contains("Starting room"));
        }
    }
}
