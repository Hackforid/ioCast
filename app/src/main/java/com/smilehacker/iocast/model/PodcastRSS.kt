package com.smilehacker.iocast.model

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.StringReader
import java.util.*

/**
 * Created by kleist on 15/11/5.
 */
public class PodcastRSS {

    public var title  = ""
    public var link = ""
    public var description = ""
    public var language = ""
    public var image = ""
    public var author = ""
    public var category = ""
    public var items : ArrayList<Item>? = null

    class Item {
        public var title = ""
        public var description = ""
        public var link = ""
        public var author = ""
        public var pubData = ""
        public var duration = 0
        public var fileType = ""
        public var fileUrl = ""
    }

}


