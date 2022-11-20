package com.example.chatapp.util.CalendarDecorator

import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class MemoDecorator(private val string: String, dates: Collection<CalendarDay>) :

    DayViewDecorator {
    private val dates: HashSet<CalendarDay> = HashSet(dates)
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(AddTextToDates(string))
        view.setDaysDisabled(true)
    }

}