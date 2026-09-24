package ch.bbw.textadventure.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class ConsoleView {
    private final BufferedReader input;
    private final PrintStream output;

    public ConsoleView(InputStream input, PrintStream output) {
        this.input = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(input, "Input must not be null"), StandardCharsets.UTF_8));
        this.output = Objects.requireNonNull(output, "Output must not be null");
    }

    public String readCommand() throws IOException {
        return input.readLine();
    }

    public void show(String text) {
        output.println(text);
    }

    public void showPrompt() {
        output.print("> ");
        output.flush();
    }
}
