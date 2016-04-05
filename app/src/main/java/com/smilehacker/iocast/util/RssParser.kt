package com.smilehacker.iocast.util

import android.util.Xml
import com.smilehacker.iocast.model.PodcastItem
import com.smilehacker.iocast.model.Podcast
import org.xmlpull.v1.XmlPullParser
import java.io.Reader
import java.io.StringReader
import java.util.*

/**
 * Created by kleist on 15/11/5.
 */
public class RssParser {

    public fun parse(rss : Reader) : Podcast? {
        val parser = Xml.newPullParser()
        parser.setInput(rss)
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.next()
        var eventType = parser.eventType
        while(eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && parser.name.equals("channel")) {
                return readChannel(parser)
            } else{
                parser.nextTag()
            }
        }
        return null
    }

    public fun parse(rss : String) : Podcast? {
        val parser = Xml.newPullParser()
        parser.setInput(StringReader(rss))
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.nextTag()
        var eventType = parser.eventType

        while(eventType != XmlPullParser.END_TAG) {
            if (eventType == XmlPullParser.START_TAG && "channel".equals(parser.name)) {
                return readChannel(parser)
            } else{
                parser.next()
                eventType = parser.eventType
            }
        }
        return null
    }

    fun readChannel(parser : XmlPullParser) : Podcast {
        parser.require(XmlPullParser.START_TAG, null, "channel")

        val podcast = Podcast()
        podcast.items = ArrayList<PodcastItem>()
        while(parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            val name = parser.name
            when(name) {
                "title" -> podcast.title = parser.nextText()
                "link" -> podcast.link = parser.nextText()
                "description" -> podcast.description = parser.nextText()
                "language" -> podcast.language = parser.nextText()
                "itunes:image" -> {
                    podcast.image = parser.getAttributeValue(null, "href")
                    parser.nextTag()
                }
                "itunes:author" -> podcast.author = parser.nextText()
                "itunes:category" -> {
                    podcast.category = parser.getAttributeValue(null, "text")
                    parser.nextTag()
                }
                "item" -> podcast.items?.add(readItem(parser))
                else -> {
                    skip(parser)
                }
            }
        }

        return podcast
    }

    fun readItem(parser : XmlPullParser) : PodcastItem {
        val item = PodcastItem()
        parser.require(XmlPullParser.START_TAG, null, "item")
        while(parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when(parser.name) {
                "title" -> item.title = parser.nextText()
                "description" -> item.description = parser.nextText()
                "link" -> item.link = parser.nextText()
                "author" -> item.author = parser.nextText()
                "pubDate" -> item.setPubDateByRssDate(parser.nextText())
                "itunes:duration" -> item.duration = parser.nextText().toLong()
                "enclosure" -> {
                    item.fileType = parser.getAttributeValue(null, "type")
                    item.fileUrl = parser.getAttributeValue(null, "url")
                    parser.nextTag()
                }
                else -> {
                    skip(parser)
                }
            }
        }

        return item
    }


    fun skip(parser : XmlPullParser) {
        var depth = 1;
        while (depth != 0) {
            when(parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}