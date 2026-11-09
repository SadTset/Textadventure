package ch.bbw.textadventure.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.bbw.textadventure.model.GameState;
import ch.bbw.textadventure.model.Action;
import ch.bbw.textadventure.model.Room;
import ch.bbw.textadventure.model.Story;
import ch.bbw.textadventure.model.StoryLoader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StoryConsistencyTest {
    private Story story;
    private GameState state;
    private GameController controller;

    @BeforeEach
    void loadStory() throws IOException {
        try (var input = getClass().getResourceAsStream("/tutorial.yaml")) {
            story = new StoryLoader().load(input);
        }
        state = new GameState(story.startRoom());
        controller = new GameController(story, state);
    }

    @Test
    void wardrobeRemembersTheCoatAndDoesNotRepeatUnlocking() {
        commands("take key", "go east", "open wardrobe", "take coat", "go west", "go east");
        assertTrue(controller.currentRoomText().contains("wearing"));
        assertTrue(controller.handleCommand("look wardrobe").contains("empty"));
        assertTrue(controller.handleCommand("look coat").contains("wearing"));
        assertTrue(controller.handleCommand("take coat").contains("already"));
        assertTrue(controller.handleCommand("open wardrobe").contains("already"));
    }

    @Test
    void collectedItemsDisappearFromTheirOriginalLocations() {
        controller.handleCommand("take key");
        assertTrue(controller.handleCommand("look table").contains("empty"));
        assertFalse(controller.handleCommand("look table").contains("lying on top"));
        commands("go west", "take lantern", "go east", "go west");
        assertTrue(controller.currentRoomText().contains("carrying"));
        assertFalse(controller.currentRoomText().contains("lantern rests"));
        assertTrue(controller.handleCommand("look bench").contains("empty"));
    }

    @Test
    void gateArrivalAndInspectionReflectTheOpenedGate() {
        commands("go west", "take lantern", "go up", "look telescope", "go down", "go east",
                "take key", "go east", "open wardrobe", "take coat", "go east", "go north",
                "go east", "open gate", "go west", "go east");
        assertTrue(controller.currentRoomText().contains("gate stands open"));
        assertFalse(controller.currentRoomText().contains("blocks the road"));
        assertFalse(controller.currentRoomText().contains("enter 'open gate'"));
        assertTrue(controller.handleCommand("look dial").contains("already open"));
        assertTrue(controller.handleCommand("open gate").contains("already open"));
        assertEquals(controller.currentRoomText(), controller.handleCommand("look"));
    }

    @Test
    void fountainDistinguishesTheMechanismsWithoutGivingTheSolution() {
        state.moveTo("courtyard");
        var clue = controller.handleCommand("look fountain");
        assertTrue(clue.contains("counterweight"));
        assertTrue(clue.contains("tower"));
        assertFalse(clue.contains("use gear"));
        assertFalse(clue.contains("go up"));
        assertTrue(controller.handleCommand("pull chain").contains("jammed"));
        assertTrue(controller.handleCommand("use gear").contains("no"));
        assertFalse(state.hasState("gearInstalled"));
    }

    @Test
    void storyCluesDoNotEmbedWalkthroughCommands() {
        for (var room : story.rooms().values()) {
            assertFalse(room.description().contains("'use gear'"));
            for (var objects : room.verbs().values()) {
                for (var actions : objects.values()) {
                    for (var action : actions) {
                        if (action.message() != null) {
                            assertFalse(action.message().matches("(?s).*'(?:go|take|look|open|pull|use|ring) [^']+'.*"),
                                    action.message());
                        }
                    }
                }
            }
        }
    }

    @Test
    void archiveStopsAdvertisingTheGearAfterItHasBeenTakenOrInstalled() {
        state.moveTo("archive");
        commands("look journal", "open chest", "take gear");
        assertTrue(controller.currentRoomText().contains("chest is empty"));
        assertTrue(controller.handleCommand("look chest").contains("empty"));
        assertTrue(controller.handleCommand("open chest").contains("empty"));
        assertFalse(controller.handleCommand("open chest").contains("take gear"));
        state.addState("gearInstalled");
        assertTrue(controller.handleCommand("take gear").contains("installed"));
        assertFalse(controller.handleCommand("take gear").contains("have the sun gear"));
    }

    @Test
    void fountainAndTowerRememberEveryRepairStage() {
        state.moveTo("bell-tower");
        state.addState("sunGear");
        controller.handleCommand("use gear");
        assertFalse(controller.currentRoomText().contains("sun-shaped gap"));
        assertTrue(controller.handleCommand("look mechanism").contains("socket"));
        state.moveTo("courtyard");
        controller.handleCommand("pull chain");
        assertTrue(controller.handleCommand("look fountain").contains("recovered"));
        assertTrue(controller.currentRoomText().contains("recovered"));
        state.moveTo("bell-tower");
        controller.handleCommand("use crank");
        assertTrue(controller.currentRoomText().contains("wound"));
        assertFalse(controller.currentRoomText().contains("empty crank socket"));
        assertTrue(controller.handleCommand("use crank").contains("already"));
        controller.handleCommand("ring bell");
        assertTrue(controller.currentRoomText().contains("Time is moving again"));
        assertFalse(controller.currentRoomText().contains("motionless"));
        assertTrue(controller.handleCommand("look mechanism").contains("running"));
        assertTrue(controller.handleCommand("use crank").contains("running"));
    }

    @Test
    void restoredValleyDoesNotReturnToFrozenDescriptionsOnRevisits() {
        state.addState("spring");
        for (var roomId : new String[] {"secret", "frozen-wood", "iron-gate", "archive", "courtyard", "bell-tower"}) {
            state.moveTo(roomId);
            assertTrue(controller.currentRoomText().contains("thaw"), roomId);
        }
        state.moveTo("secret");
        assertTrue(controller.handleCommand("look snow").contains("melting"));
        state.moveTo("frozen-wood");
        assertTrue(controller.handleCommand("look fox").contains("alive"));
    }

    @Test
    void conditionalRoomTextIsReadOnlyAndFallsBackToTheDescription() {
        var action = new Action("seen", "shouldNotBeAdded", "Changed room", "other");
        var room = new Room("Room", "Original room", Map.of("look", Map.of("", List.of(action))));
        var other = new Room("Other", "Other room", Map.of());
        var sample = new Story("here", "Intro", Map.of("here", room, "other", other), story.globals());
        var sampleState = new GameState("here");
        var sampleController = new GameController(sample, sampleState);
        assertTrue(sampleController.currentRoomText().contains("Original room"));
        sampleState.addState("seen");
        assertTrue(sampleController.handleCommand("examine").contains("Changed room"));
        assertEquals("here", sampleState.currentRoomId());
        assertFalse(sampleState.hasState("shouldNotBeAdded"));
    }

    @Test
    void roomInspectionDoesNotChangePuzzleState() {
        var before = state.states();
        var room = state.currentRoomId();
        assertTrue(controller.handleCommand("look").contains("Starting room"));
        assertEquals(before, state.states());
        assertEquals(room, state.currentRoomId());
        try (var input = getClass().getResourceAsStream("/forest.yaml")) {
            var forest = new StoryLoader().load(input);
            var forestController = new GameController(forest, new GameState(forest.startRoom()));
            assertEquals(forestController.currentRoomText(), forestController.handleCommand("look"));
        } catch (IOException error) {
            throw new AssertionError(error);
        }
    }

    private void commands(String... inputs) {
        for (var input : inputs) {
            controller.handleCommand(input);
        }
    }
}
