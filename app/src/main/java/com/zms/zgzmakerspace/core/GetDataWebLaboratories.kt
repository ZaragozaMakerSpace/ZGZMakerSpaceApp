package com.zms.zgzmakerspace.core

import com.zms.zgzmakerspace.model.Events
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.text.SimpleDateFormat

class GetDataWebLaboratories {
    val classRowEvent = "ect-list-post"
    val tagDayEvent = "ev-day"
    val tagMonthEvent = "ev-mo"
    val tagYearEvent = "ev-yr"
    val classClassStartTime = "tribe-events-abbr tribe-events-start-time published dtstart"
    val classClassEndTime = "tribe-event-date-end"
    val classTitleEvent = "ect-event-url"
    val classImageurl = "tribe-events-event-image"
    val classWeekdayDayTag = "tribe-events-pro-summary__event-date-tag-weekday"
    var listEvents = mutableListOf<Events>()

    var doc: Document = Jsoup.connect(Const.URLTALLERES)
        .userAgent("Agenda")
        .timeout(30000)
        .referrer("https://google.com")
        .get()

    fun getEvents() {
        val rowsEvents = doc.getElementsByClass(classRowEvent)
        for (row in rowsEvents) {

//            var dateEvent: String = row.getElementsByTag(tagDateEvent)[0].attr("datetime")
//            val date=format.parse(dateEvent)
            //dateEvent=formatList.format(date)
            var day = row.getElementsByClass(tagDayEvent).text()
            var month = row.getElementsByClass(tagMonthEvent).text()
            var year = row.getElementsByClass(tagYearEvent).text()
            var dateEvent: String = "$day/$month/$year"
            var timeStartEvent = "0" //row.getElementsByClass(classClassStartTime)[0].text()
            val timeEndEvent = "0" //row.getElementsByClass(classClassEndTime)[0].text()
            val titleEvent = row.getElementsByClass(classTitleEvent)[0].text()
            val weekDayEvent = " " //row.getElementsByClass(classWeekdayDayTag)[0].text()
            val link = row.getElementsByClass(classTitleEvent)[0].attr("href")


            var lab: Document = Jsoup.connect(link).userAgent("Agenda").timeout(30000)
                .referrer("https://google.com")
                .get()
            val urlImageEvent =
                lab.getElementsByClass(classImageurl)[0].getElementsByTag("img").attr("src")
            timeStartEvent = lab.getElementsByClass(classClassStartTime)[0].text()
            dateEvent = lab.getElementsByClass(classClassStartTime)[0].attr("title")
            var format = SimpleDateFormat("yyyy-MM-dd")
            var formatList = SimpleDateFormat("dd-MM-yyyy")
            val date = format.parse(dateEvent)
            dateEvent = formatList.format(date)


            val event =
                Events(
                    titleEvent,
                    dateEvent,
                    timeStartEvent,
                    timeEndEvent,
                    weekDayEvent,
                    link,
                    urlImageEvent
                )
            listEvents.add(event)


        }

    }

    fun returnEvents(): MutableList<Events> {
        return listEvents
    }
}