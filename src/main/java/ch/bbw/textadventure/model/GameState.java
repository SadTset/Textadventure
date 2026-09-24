package ch.bbw.textadventure.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class GameState {
    private String currentRoomId;
    private final Set<String> states = new HashSet<>();

    public GameState(String startRoomId) {
        currentRoomId = Objects.requireNonNull(startRoomId, "Start room ID must not be null");
    }

    public String currentRoomId() {
        return currentRoomId;
    }

    public void moveTo(String roomId) {
        currentRoomId = Objects.requireNonNull(roomId, "Room ID must not be null");
    }

    public boolean hasState(String state) {
        return states.contains(state);
    }

    public void addState(String state) {
        states.add(Objects.requireNonNull(state, "State must not be null"));
    }

    public Set<String> states() {
        return Set.copyOf(states);
    }
}
