package com.thexm.todoquest.data.db

import androidx.room.TypeConverter
import com.thexm.todoquest.data.model.RecurrenceType
import com.thexm.todoquest.data.model.XPTier

class Converters {
    @TypeConverter fun fromXPTier(value: XPTier): String = value.name
    @TypeConverter fun toXPTier(value: String): XPTier = XPTier.valueOf(value)
    @TypeConverter fun fromRecurrenceType(value: RecurrenceType): String = value.name
    @TypeConverter fun toRecurrenceType(value: String): RecurrenceType = RecurrenceType.valueOf(value)
}
