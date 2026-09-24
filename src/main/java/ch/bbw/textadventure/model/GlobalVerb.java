package ch.bbw.textadventure.model;

import java.util.List;

public record GlobalVerb(
        List<String> synonyms,
        String unknownVerb,
        String unknownObject
) {
}
