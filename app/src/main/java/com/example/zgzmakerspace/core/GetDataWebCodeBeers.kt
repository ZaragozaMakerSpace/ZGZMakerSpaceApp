package com.example.zgzmakerspace.core

import com.example.zgzmakerspace.model.Events
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.text.SimpleDateFormat

class GetDataWebCodeBeers {
    private val classRowEvent = "tribe-events-pro-summary__event-row"
    private val tagDateEvent = "time"
    private val classClassStartTime = "tribe-event-date-start"
    private val classClassEndTime = "tribe-event-date-end"
    private val classWeekdayDayTag="tribe-events-pro-summary__event-date-tag-weekday"
    private val classTitleEvent = "tribe-events-pro-summary__event-title-link"
    private var listEvents= mutableListOf<Events>()

    var doc: Document = Jsoup.connect(Const.URLCODEBEERS)
        .userAgent("Agenda")
        .timeout(30000)
        .referrer("http://google.com")
        .get()

    fun getEvents() {
        val rowsEvents = doc.getElementsByClass(classRowEvent)
        for (row in rowsEvents) {

            var dateEvent: String = row.getElementsByTag(tagDateEvent)[0].attr("datetime")
            val format=SimpleDateFormat("yyyy-MM-dd")
            val formatList=SimpleDateFormat("dd-MM-yyyy")
            val date=format.parse(dateEvent)
            dateEvent=formatList.format(date)
            val timeStartEvent = row.getElementsByClass(classClassStartTime)[0].text()
            val timeEndEvent = row.getElementsByClass(classClassEndTime)[0].text()
            val titleEvent = row.getElementsByClass(classTitleEvent)[0].text()
            val weekDayEvent=row.getElementsByClass(classWeekdayDayTag)[0].text()
            val link = row.getElementsByClass(classTitleEvent)[0].attr("href")
            //val urlImageEvent = row.getElementsByClass(classTitleEvent)[0].text()
            val urlImageEvent = "https://zaragozamakerspace.com/wp-content/uploads/2023/01/CodeBeers-250x250.png"

            val event =
                Events(titleEvent, dateEvent, timeStartEvent, timeEndEvent,weekDayEvent, link, urlImageEvent)
            listEvents.add(event)


        }

    }
    fun returnEvents():MutableList<Events>{
        return listEvents
    }
}