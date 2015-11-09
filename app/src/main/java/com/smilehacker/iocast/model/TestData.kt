package com.smilehacker.iocast.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * Created by kleist on 15/11/9.
 */
class TestData : Parcelable {
    var a: Int = 0
    var datas: ArrayList<NestData>? = null

    class NestData : Parcelable {
        var c: Int = 0
        var d: String? = ""

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(this.c)
            dest.writeString(this.d)
        }

        constructor() {
        }

        protected constructor(`in`: Parcel) {
            this.c = `in`.readInt()
            this.d = `in`.readString()
        }

        companion object {

            val CREATOR: Parcelable.Creator<NestData> = object : Parcelable.Creator<NestData> {
                override fun createFromParcel(source: Parcel): NestData {
                    return NestData(source)
                }

                override fun newArray(size: Int): Array<NestData?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(this.a)
        dest.writeTypedList(datas)
    }

    constructor() {
    }

    protected constructor(`in`: Parcel) {
        this.a = `in`.readInt()
        this.datas = `in`.createTypedArrayList(NestData.CREATOR)
    }

    companion object {

        val CREATOR: Parcelable.Creator<TestData> = object : Parcelable.Creator<TestData> {
            override fun createFromParcel(source: Parcel): TestData {
                return TestData(source)
            }

            override fun newArray(size: Int): Array<TestData?> {
                return arrayOfNulls(size)
            }
        }
    }
}
