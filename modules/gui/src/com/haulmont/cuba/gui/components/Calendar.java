/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.calendar.CalendarEvent;
import com.haulmont.cuba.gui.components.calendar.CalendarEventProvider;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.TimeZone;

public interface Calendar extends Component.BelongToFrame, Component.HasCaption, Component.HasIcon {
    String NAME = "calendar";

    /**
     * Set start date for the calendar range.
     */
    void setStartDate(Date date);
    /**
     * @return the start date for the calendar range.
     */
    Date getStartDate();

    /**
     * Set end date for the calendar's range.
     */
    void setEndDate(Date date);
    /**
     * @return the last date for the calendar range.
     */
    Date getEndDate();

    /**
     * Set timezone.
     */
    void setTimeZone(TimeZone zone);
    /**
     * @return timezone.
     */
    TimeZone getTimeZone();

    /**
     * Set collection datasource for the calendar component with a collection of events.
     */
    void setDatasource(CollectionDatasource datasource);
    CollectionDatasource getDatasource();

    /**
     * Set format for time. 12H/24H.
     */
    void setTimeFormat(TimeFormat format);
    /**
     * @return enumeration of ite format.
     */
    TimeFormat getTimeFormat();

    /**
     * Set first day of the week to show.
     */
    void setFirstVisibleDayOfWeek(int firstDay);
    /**
     * @return first showed day of the week.
     */
    int getFirstVisibleDayOfWeek();

    /**
     * Set last day of the week to show.
     */
    void setLastVisibleDayOfWeek(int lastDay);
    /**
     * @return last showed day of the week.
     */
    int getLastVisibleDayOfWeek();

    /**
     * Set first hour of the day to show.
     */
    void setFirstVisibleHourOfDay(int firstHour);
    /**
     * @return first showed hour of the day.
     */
    int getFirstVisibleHourOfDay();

    /**
     * Set last hour of the day to show.
     */
    void setLastVisibleHourOfDay(int lastHour);
    /**
     * @return last showed hour of the day.
     */
    int getLastVisibleHourOfDay();

    /**
     * Set date caption format for the weekly view.
     */
    void setWeeklyCaptionFormat(String dateFormatPattern);
    /**
     * @return date pattern of captions.
     */
    String getWeeklyCaptionFormat();

    /**
     * Set the calendar event provider. Provider can contain calendar events.
     */
    void setEventProvider(CalendarEventProvider calendarEventProvider);
    /**
     * @return calendar event provider.
     */
    CalendarEventProvider getEventProvider();

    /**
     * Set visibility for the backward and forward buttons.
     */
    void setNavigationButtonsVisible(boolean value);
    /**
     * @return backward and forward buttons visibility.
     */
    boolean isNavigationButtonsVisible();


    void addDateClickListener(CalendarDateClickListener listener);
    void removeDateClickListener(CalendarDateClickListener listener);

    void addEventClickListener(CalendarEventClickListener listener);
    void removeEventClickListener(CalendarEventClickListener listener);

    void addEventResizeListener(CalendarEventResizeListener listener);
    void removeEventResizeListener(CalendarEventResizeListener listener);

    void addEventMoveListener(CalendarEventMoveListener listener);
    void removeEventMoveListener(CalendarEventMoveListener listener);

    void addWeekClickListener(CalendarWeekClickListener listener);
    void removeWeekClickListener(CalendarWeekClickListener listener);

    void addForwardClickListener(CalendarForwardClickListener listener);
    void removeForwardClickListener(CalendarForwardClickListener listener);

    void addBackwardClickListener(CalendarBackwardClickListener listener);
    void removeBackwardClickListener(CalendarBackwardClickListener listener);

    void addRangeSelectListener(CalendarRangeSelectListener listener);
    void removeRangeSelectListener(CalendarRangeSelectListener listener);

    enum TimeFormat {
        FORMAT_12H, FORMAT_24H
    }

    /**
     * Event mouse drag listener.
     */
    interface CalendarEventMoveListener {
        void eventMove(CalendarEventMoveEvent event);
    }

    class CalendarEventMoveEvent {
        protected Calendar calendar;
        protected CalendarEvent calendarEvent;
        protected Date newStart;

        public CalendarEventMoveEvent(Calendar calendar, CalendarEvent calendarEvent, Date newStart) {
            this.calendar = calendar;
            this.calendarEvent = calendarEvent;
            this.newStart = newStart;
        }

        public Calendar getCalendar() {
            return calendar;
        }

        public CalendarEvent getCalendarEvent() {
            return calendarEvent;
        }

        public Date getNewStart() {
            return newStart;
        }
    }

    /**
     * Backward icon click listener.
     */
    interface CalendarBackwardClickListener {
        void backwardClick(CalendarBackwardClickEvent event);
    }

    class CalendarBackwardClickEvent {
        protected Calendar calendar;

        public CalendarBackwardClickEvent(Calendar calendar) {
            this.calendar = calendar;
        }

        public Calendar getCalendar() {
            return calendar;
        }
    }

    /**
     * Date labels click listener of the component.
     */
    interface CalendarDateClickListener {
        void dateClick(CalendarDateClickEvent event);
    }

    class CalendarDateClickEvent {
        protected Calendar calendar;
        protected Date date;

        public CalendarDateClickEvent(Calendar calendar, Date date) {
            this.calendar = calendar;
            this.date = date;
        }

        public Date getDate() {
            return date;
        }

        public Calendar getCalendar() {
            return calendar;
        }
    }

    /**
     * Event labels click listener of the component.
     */
    interface CalendarEventClickListener {
        void eventClick(CalendarEventClickEvent event);
    }

    class CalendarEventClickEvent {
        protected Calendar calendar;
        protected CalendarEvent calendarEvent;
        protected Entity entity;

        public CalendarEventClickEvent(Calendar calendar, CalendarEvent calendarEvent, @Nullable Entity entity) {
            this.calendar = calendar;
            this.calendarEvent = calendarEvent;
            this.entity = entity;
        }

        public Entity getEntity() {
            return entity;
        }

        public CalendarEvent getCalendarEvent() {
            return calendarEvent;
        }

        public Calendar getCalendar() {
            return calendar;
        }
    }

    /**
     * Forward icon click listener.
     */
    interface CalendarForwardClickListener {
        void forwardClick(CalendarForwardClickEvent event);
    }

    class CalendarForwardClickEvent {
        protected Calendar calendar;

        public CalendarForwardClickEvent(Calendar calendar) {
            this.calendar = calendar;
        }

        public Calendar getCalendar() {
            return calendar;
        }
    }

    /**
     * Event range change listener.
     */
    interface CalendarEventResizeListener {
        void eventResize(CalendarEventResizeEvent event);
    }

    class CalendarEventResizeEvent {
        protected Calendar calendar;
        protected CalendarEvent calendarEvent;
        protected Date newStart;
        protected Date newEnd;
        protected Entity entity;

        public CalendarEventResizeEvent(Calendar calendar, CalendarEvent calendarEvent, Date newStart,
                                        Date newEnd, @Nullable Entity entity) {
            this.calendar = calendar;
            this.calendarEvent = calendarEvent;
            this.newEnd = newEnd;
            this.newStart = newStart;
            this.entity = entity;
        }

        public Entity getEntity() {
            return entity;
        }

        public CalendarEvent getCalendarEvent() {
            return calendarEvent;
        }

        public Calendar getCalendar() {
            return calendar;
        }

        public Date getNewStart() {
            return newStart;
        }

        public Date getNewEnd() {
            return newEnd;
        }
    }

    /**
     * Week labels click listener.
     */
    interface CalendarWeekClickListener {
        void weekClick(CalendarWeekClickEvent event);
    }

    class CalendarWeekClickEvent {
        protected Calendar calendar;
        protected int week;
        protected int year;

        public CalendarWeekClickEvent(Calendar calendar, int week, int year) {
            this.calendar = calendar;
            this.week = week;
            this.year = year;
        }

        public Calendar getCalendar() {
            return calendar;
        }

        public int getWeek() {
            return week;
        }

        public int getYear() {
            return year;
        }
    }

    /**
     * Time range mouse select listener.
     */
    interface CalendarRangeSelectListener {
        void rangeSelect(CalendarRangeSelectEvent event);
    }

    class CalendarRangeSelectEvent {
        protected Calendar calendar;
        protected Date start;
        protected Date end;

        public CalendarRangeSelectEvent(Calendar calendar, Date start, Date end) {
            this.calendar = calendar;
            this.start = start;
            this.end = end;
        }

        public Calendar getCalendar() {
            return calendar;
        }

        public Date getStart() {
            return start;
        }

        public Date getEnd() {
            return end;
        }
    }
}