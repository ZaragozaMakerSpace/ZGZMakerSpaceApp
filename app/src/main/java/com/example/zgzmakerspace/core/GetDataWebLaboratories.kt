package com.example.zgzmakerspace.core

import android.util.Log
import com.example.zgzmakerspace.model.Events
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.text.SimpleDateFormat

class GetDataWebLaboratories {
    val classRowEvent = "tribe-events-pro-summary__event-row"
    val tagDateEvent = "time"
    val classClassStartTime = "tribe-event-date-start"
    val classClassEndTime = "tribe-event-date-end"
    val classTitleEvent = "tribe-events-pro-summary__event-title-link"
    val classWeekdayDayTag="tribe-events-pro-summary__event-date-tag-weekday"
    var listEvents= mutableListOf<Events>()

    var doc: Document = Jsoup.connect(Const.URLTALLERES)
        .userAgent("Agenda")
        .timeout(30000)
        .referrer("http://google.com")
        .get()

    fun getEvents() {
        val rowsEvents = doc.getElementsByClass(classRowEvent)
        for (row in rowsEvents) {

            var dateEvent: String = row.getElementsByTag(tagDateEvent)[0].attr("datetime")
            var format=SimpleDateFormat("yyyy-MM-dd")
            var formatList=SimpleDateFormat("dd-MM-yyyy")
            val date=format.parse(dateEvent)
            dateEvent=formatList.format(date)
            val timeStartEvent = row.getElementsByClass(classClassStartTime)[0].text()
            val timeEndEvent = row.getElementsByClass(classClassEndTime)[0].text()
            val titleEvent = row.getElementsByClass(classTitleEvent)[0].text()
            val weekDayEvent=row.getElementsByClass(classWeekdayDayTag)[0].text()
            val link = row.getElementsByClass(classTitleEvent)[0].attr("href")
            val urlImageEvent = row.getElementsByClass(classTitleEvent)[0].text()

            val event =
                Events(titleEvent, dateEvent, timeStartEvent, timeEndEvent,weekDayEvent, link, urlImageEvent)
            listEvents.add(event)


        }

    }
    fun returnEvents():MutableList<Events>{
        return listEvents
    }
}