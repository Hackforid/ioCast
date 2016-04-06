package com.smilehacker.iocast.util

/**
 * Created by kleist on 16/4/6.
 */
class HtmlConverter {

    fun fillHtmlDataWithBody(htmlData : String) : String {
        val head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "<link type=\"text/css\" rel=\"stylesheet\" href=\"podcast_item_desc.css\"/>" +
                "</head>"

        return "<html>$head<body>$htmlData</body></html>"
    }
}