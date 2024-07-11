package com.example.todolist.domain.model

import java.util.UUID

/**
 * Data class representing a todo item.
 *
 * @property id unique identifier of the todo item
 * @property text text description of the todo item
 * @property relevance relevance level of the todo item
 * @property deadline deadline for the todo item (nullable)
 * @property executionFlag flag indicating if the todo item is executed
 * @property dateOfCreating timestamp of when the todo item was created
 * @property dateOfEditing timestamp of when the todo item was last edited (nullable)
 */
data class TodoItem(
    val id: UUID,
    var text: String,
    var relevance: String,
    var deadline: Long? = null,
    var executionFlag: Boolean,
    val dateOfCreating: Long,
    var dateOfEditing: Long? = null,
)/* : Parcelable {
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
        */
{
    constructor() : this(UUID.randomUUID(), "", "", null, false, 0L)

}