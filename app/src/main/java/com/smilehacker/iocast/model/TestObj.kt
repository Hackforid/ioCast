package com.smilehacker.iocast.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by kleist on 15/11/8.
 */

public data class Foo(var a : Int = 0) : Parcelable {
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(this.a)
    }

    override fun describeContents(): Int {
        return 0
    }

    protected constructor(p : Parcel) : this() {
        this.a = p.readInt()
    }

    companion object {
        val CREATOR = createParcel { Foo(it) }
    }

}
