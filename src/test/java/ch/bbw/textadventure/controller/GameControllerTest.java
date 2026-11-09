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
    void restoresTimeOnlyAfterFindingTheGearAndCrank() throws IOException {
        try (var input = getClass().getResourceAsStream("/tutorial.yaml")) {
            var story = new StoryLoader().load(input);
            var state = new GameState(story.startRoom());
            var controller = new GameController(story, state);
            for (var command : new String[] {"go west", "take lantern", "go up", "look telescope",
                    "go down", "go east"}) {
                controller.handleCommand(command);
            }
            enterNarnia(controller);
            for (var command : new String[] {"go north", "go east", "open gate", "go north", "go up"}) {
                controller.handleCommand(command);
            }
            assertTrue(controller.handleCommand("ring bell").contains("gear"));
            controller.handleCommand("use crank");
            controller.handleCommand("go down");
            assertTrue(controller.handleCommand("pull chain").contains("jammed"));
            assertFalse(state.hasState("crank"));
            controller.handleCommand("go up");
            controller.handleCommand("go east");
            assertFalse(state.hasState("spring"));
            assertEquals("bell-tower", state.currentRoomId());

            for (var command : new String[] {"go down", "go south", "go west", "go west"}) {
                controller.handleCommand(command);
            }
            assertTrue(controller.handleCommand("take gear").contains("sealed chest"));
            assertTrue(controller.handleCommand("open chest").contains("handwriting"));
            controller.handleCommand("look journal");
            controller.handleCommand("open chest");
            controller.handleCommand("take gear");
            assertTrue(controller.handleCommand("take gear").contains("already"));
            for (var command : new String[] {"go east", "go east", "go north", "go up", "use gear"}) {
                controller.handleCommand(command);
            }
            assertTrue(controller.handleCommand("ring bell").contains("spring is slack"));
            assertFalse(state.hasState("spring"));
            controller.handleCommand("go down");
            controller.handleCommand("pull chain");
            assertTrue(controller.handleCommand("pull chain").contains("already"));
            controller.handleCommand("go up");
            controller.handleCommand("use crank");
            assertTrue(controller.handleCommand("ring bell").contains("Time is moving again"));
            assertTrue(controller.handleCommand("ring bell").contains("rings freely"));
            assertTrue(controller.handleCommand("go east").contains("THE END"));
            assertEquals("sunrise", state.currentRoomId());
        }
    }

    @Test
    void opensTheWinterGateUsingTheObservatoryClue() throws IOException {
        try (var input = getClass().getResourceAsStream("/tutorial.yaml")) {
            var story = new StoryLoader().load(input);
            var state = new GameState(story.startRoom());
            var controller = new GameController(story, state);

            enterNarnia(controller);
            assertTrue(controller.currentRoomText().contains("north"));
            assertTrue(controller.handleCommand("go north").contains("cannot see"));
            assertEquals("secret", state.currentRoomId());
            controller.handleCommand("go west");
            controller.handleCommand("go west");
            controller.handleCommand("go west");
            controller.handleCommand("take lantern");
            controller.handleCommand("go east");
            controller.handleCommand("go east");
            controller.handleCommand("go east");
            assertTrue(controller.handleCommand("go north").contains("Frozen wood"));
            controller.handleCommand("go east");
            assertTrue(controller.handleCommand("open gate").contains("watcher"));
            controller.handleCommand("go north");
            assertEquals("iron-gate", state.currentRoomId());

            for (var command : new String[] {"go west", "go south", "go west", "go west",
                    "go west", "go up", "look telescope", "go down", "go east", "go east",
                    "go east", "go north", "go east"}) {
                controller.handleCommand(command);
            }
            assertTrue(state.hasState("starMap"));
            assertTrue(controller.handleCommand("open gate").contains("gate swings open"));
            assertTrue(controller.handleCommand("go north").contains("Silent courtyard"));
            assertEquals("courtyard", state.currentRoomId());
        }
    }

    private void enterNarnia(GameController controller) {
        for (var command : new String[] {"take key", "go east", "open wardrobe", "take coat", "go east"}) {
            controller.handleCommand(command);
        }
    }

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
            assertTrue(controller.handleCommand("open wardrobe").contains("locked"));
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
            assertTrue(controller.handleCommand("help verbs").contains("pull, use, ring"));
            assertTrue(controller.handleCommand("walk east").contains("Another room"));
        }
    }
}
