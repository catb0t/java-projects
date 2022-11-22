package org.catb0t;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class GameLogicActionDeserializer implements JsonDeserializer<GameLogicAction> {
    public GameLogicAction deserialize (
            final JsonElement json,
            final Type typeOfT,
            final JsonDeserializationContext context
    )
            throws JsonParseException {
        return GameLogicAction.valueOf(json.getAsJsonPrimitive().getAsString().toUpperCase());
    }
}
