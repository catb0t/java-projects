package org.catb0t;

import com.google.gson.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * Implement deserialisation of members of the {@link GameRules} record from JSON values.
 */
public class GameRulesDeserializer implements JsonDeserializer<GameRules> {
    @Override
    public GameRules deserialize (
        final JsonElement json,
        final Type typeOfT,
        final JsonDeserializationContext context
    )
        throws JsonParseException {
        return GameRules.valueOf(
            json.getAsJsonPrimitive().getAsString().toUpperCase(Locale.ROOT)
        );
    }
}
