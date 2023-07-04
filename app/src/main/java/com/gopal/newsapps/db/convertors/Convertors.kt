package com.gopal.newsapps.db.convertors

import androidx.room.TypeConverter
import com.gopal.newsapps.model.Source


class Convertors {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}