package com.kunle.aisle9b.util

import android.net.Uri
import androidx.core.net.toUri
import androidx.room.TypeConverter
import java.util.*

class UUIDConverter {
    @TypeConverter
    fun fromUUID(uuid: UUID): String? {
        return uuid.toString()
    }

    @TypeConverter
    fun toUUID(string: String?) : UUID? {
        return UUID.fromString(string)
    }
}

class UriConverter {
    @TypeConverter
    fun fromUri(uri: Uri?) : String? {
        return uri.toString()
    }

    @TypeConverter
    fun toUri(string: String?) : Uri? {
        return string?.toUri()
    }
}