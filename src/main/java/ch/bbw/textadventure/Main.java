package ch.bbw.textadventure;

import ch.bbw.textadventure.controller.GameController;
import ch.bbw.textadventure.model.GameState;
import ch.bbw.textadventure.model.StoryLoader;
import ch.bbw.textadventure.view.ConsoleView;
import java.io.IOException;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) throws IOException {
        try (var input = Main.class.getResourceAsStream("/tutorial.yaml")) {
            if (input == null) {
                throw new IOException("The bundled tutorial.yaml story was not found.");
            }

            var story = new StoryLoader().load(input);
            var state = new GameState(story.startRoom());
            var controller = new GameController(story, state);
            var view = new ConsoleView(System.in, System.out);
            view.show(controller.introduction());
            view.show(controller.currentRoomText());
            runGame(controller, view);
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
