package com.amperas17.mangaedenapp.model.chapter

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonParseException

import java.lang.reflect.Type


class ChapterDeserializer : JsonDeserializer<Any> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Any {
        val chapterArray = json.asJsonArray

        var name = ""
        if (chapterArray.get(2) !is JsonNull) {
            name = chapterArray.get(2).asString
        }

        return Chapter(
                chapterArray.get(0).asInt,
                chapterArray.get(1).asLong,
                name,
                chapterArray.get(3).asString
        )
    }
}