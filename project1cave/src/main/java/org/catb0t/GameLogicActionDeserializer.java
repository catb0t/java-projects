package org.catb0t;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Locale;

/**
 * Implement deserialisation of members of the {@link GameLogicAction} enum from JSON values.
 */
public class GameLogicActionDeserializer implements JsonDeserializer<GameLogicAction> {
    @Override
    public GameLogicAction deserialize (
            final JsonElement json,
            final Type typeOfT,
            final JsonDeserializationContext context
    )
            throws JsonParseException {
        return GameLogicAction.valueOf(
                json.getAsJsonPrimitive().getAsString().toUpperCase(Locale.ROOT)
        );
    }
}
