package com.amperas17.mangaedenapp.model.page

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException

import java.lang.reflect.Type

class PageDeserializer : JsonDeserializer<Any> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Any {
        val chapterArray = json.asJsonArray
        return Page(
                chapterArray.get(0).asInt,
                chapterArray.get(1).asString,
                chapterArray.get(2).asInt,
                chapterArray.get(3).asInt
        )
    }
}