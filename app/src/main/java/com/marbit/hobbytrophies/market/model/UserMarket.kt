package com.marbit.hobbytrophies.market.model

import android.os.Parcel
import android.os.Parcelable

class UserMarket() : Parcelable {
    var id: String = ""
    var name: String = ""
    var chatId: String = ""


    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        name = parcel.readString()
        chatId = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(chatId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserMarket> {
        override fun createFromParcel(parcel: Parcel): UserMarket {
            return UserMarket(parcel)
        }

        override fun newArray(size: Int): Array<UserMarket?> {
            return arrayOfNulls(size)
        }
    }

}
