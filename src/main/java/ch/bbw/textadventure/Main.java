package ch.bbw.textadventure;

import ch.bbw.textadventure.controller.GameController;
import ch.bbw.textadventure.model.GameState;
import ch.bbw.textadventure.model.Story;
import ch.bbw.textadventure.model.StoryLoader;
import ch.bbw.textadventure.view.ConsoleView;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) throws IOException {
        var story = loadStory(args);
        var state = new GameState(story.startRoom());
        var controller = new GameController(story, state);
        var view = new ConsoleView(System.in, System.out);
        view.show(controller.introduction());
        view.show(controller.currentRoomText());
        runGame(controller, view);
    }

    static Story loadStory(String[] args) throws IOException {
        if (args.length > 1) {
            throw new IOException("Expected at most one story file path.");
        }
        if (args.length == 0) {
            try (var input = Main.class.getResourceAsStream("/tutorial.yaml")) {
                if (input == null) {
                    throw new IOException("The bundled tutorial.yaml story was not found.");
                }
                return new StoryLoader().load(input);
            }
        }

        var storyPath = Path.of(args[0]);
        try (var input = Files.newInputStream(storyPath)) {
            return new StoryLoader().load(input);
        } catch (IOException | IllegalArgumentException error) {
            throw new IOException("Could not load story '" + storyPath + "': " + error.getMessage(), error);
        }
    }

    private static void runGame(GameController controller, ConsoleView view) throws IOException {
        while (true) {
            view.showPrompt();
            var command = view.readCommand();
            if (command == null || command.trim().equalsIgnoreCase("quit")) {
                return;
            }
            if (!command.isBlank()) {
                view.show(controller.handleCommand(command));
            }
        }
    }
}
