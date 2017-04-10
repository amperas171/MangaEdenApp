package com.amperas17.mangaedenapp.model.chapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;


public class ChapterDeserializer implements JsonDeserializer {

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray chapterArray = json.getAsJsonArray();

        String name = "";
        if (!(chapterArray.get(2) instanceof JsonNull)){
            name = chapterArray.get(2).getAsString();
        }

        Chapter chapter = new Chapter(
                chapterArray.get(0).getAsInt(),
                chapterArray.get(1).getAsLong(),
                name,
                chapterArray.get(3).getAsString()
        );

        return chapter;
    }
}