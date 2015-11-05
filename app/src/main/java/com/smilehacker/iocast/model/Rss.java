package com.smilehacker.iocast.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by kleist on 15/11/5.
 */
@Root(name = "rss")
public class Rss {

    @Element(name = "channel")
    public Channel channel;

    public static class Channel {
        @Element
        public String title;

        @Element
        public String link;

        @Element
        public String description;

        @Element
        public String language;

        @Element(name = "itunes:image")
        public ItunesImage image;

        @Element(name = "itunes:author")
        public String author;

        @Element(name = "itunes:owner")
        public ItunesOwner owner;

        @ElementList(inline = true)
        public List<Item> item;

        public static class ItunesImage {
            @Attribute
            public String href;
        }

        public static class ItunesOwner {
            @Element(name = "itunes:name")
            public String name;

            @Element(name = "itunes:email")
            public String email;
        }

        public static class Item {
            @Element
            public String title;

            @Element
            public String description;

            @Element
            public String link;

            @Element
            public String author;

            @Element
            public String pubDate;

            @Element(name = "itunes:duration")
            public int duration;

            @Element
            public Enclosure enclosure;

            public static class Enclosure {
                @Attribute
                public String type;

                @Attribute
                public String url;
            }
        }

    }

}


