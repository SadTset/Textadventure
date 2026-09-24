package ch.bbw.textadventure.model;

import java.util.List;
import java.util.Map;

public record Room(
        String name,
        String description,
        Map<String, Map<String, List<Action>>> verbs
) {
}
