package com.example.todolist.domain

import android.os.Parcel
import android.os.Parcelable

data class TodoItem(
    val id: String,
    var text: String,
    var relevance: String,
    var deadline: String? = null,
    var executionFlag: Boolean,
    val dateOfCreating: String,
    var dateOfEditing: String? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(text)
        parcel.writeString(relevance)
        parcel.writeString(deadline)
        parcel.writeByte(if (executionFlag) 1 else 0)
        parcel.writeString(dateOfCreating)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TodoItem> {
        override fun createFromParcel(parcel: Parcel): TodoItem {
            return TodoItem(parcel)
        }

        override fun newArray(size: Int): Array<TodoItem?> {
            return arrayOfNulls(size)
        }
    }
    constructor() : this("", "", "", "", false, "")

}