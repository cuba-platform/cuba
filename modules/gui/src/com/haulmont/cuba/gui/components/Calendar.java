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

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.calendar.CalendarEvent;
import com.haulmont.cuba.gui.components.calendar.CalendarEventProvider;
import com.haulmont.cuba.gui.components.calendar.ContainerCalendarEventProvider;
import com.haulmont.cuba.gui.components.calendar.EntityCalendarEventProvider;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import javax.annotation.Nullable;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.Date;
import java.util.EventObject;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Consumer;

public interface Calendar extends Component.BelongToFrame, Component.HasCaption, Component.HasIcon,
        HasContextHelp, HasHtmlCaption, HasHtmlDescription {

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
     *
     * @param datasource a datasource to set
     * @deprecated @deprecated Use {@link #setEventProvider(CalendarEventProvider)}
     * with {@link EntityCalendarEventProvider} instead
     */
    @Deprecated
    void setDatasource(CollectionDatasource datasource);

    /**
     * @return a datasource
     * @deprecated Use {@link #getEventProvider()} instead
     */
    @Nullable
    @Deprecated
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
     *
     * @param calendarEventProvider an event provider with events
     * @see ContainerCalendarEventProvider
     * @see EntityCalendarEventProvider
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

    /**
     * @return {@link DayOfWeek} values matched to localized day names
     */
    Map<DayOfWeek, String> getDayNames();

    /**
     * Sets localized Calendar day names.
     *
     * @param dayNames {@link DayOfWeek} values matched to localized day names
     */
    void setDayNames(Map<DayOfWeek, String> dayNames);

    /**
     * @return {@link Month} values matched to localized month names
     */
    Map<Month, String> getMonthNames();

    /**
     * Sets localized Calendar month names.
     *
     * @param monthNames {@link Month} values matched to localized month names
     */
    void setMonthNames(Map<Month, String> monthNames);

    Subscription addDateClickListener(Consumer<CalendarDateClickEvent> listener);

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeDateClickListener(Consumer<CalendarDateClickEvent> listener);

    Subscription addEventClickListener(Consumer<CalendarEventClickEvent> listener);

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeEventClickListener(Consumer<CalendarEventClickEvent> listener);

    Subscription addEventResizeListener(Consumer<CalendarEventResizeEvent> listener);

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeEventResizeListener(Consumer<CalendarEventResizeEvent> listener);

    Subscription addEventMoveListener(Consumer<CalendarEventMoveEvent> listener);

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeEventMoveListener(Consumer<CalendarEventMoveEvent> listener);

    Subscription addWeekClickListener(Consumer<CalendarWeekClickEvent> listener);

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeWeekClickListener(Consumer<CalendarWeekClickEvent> listener);

    Subscription addForwardClickListener(Consumer<CalendarForwardClickEvent> listener);

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeForwardClickListener(Consumer<CalendarForwardClickEvent> listener);

    Subscription addBackwardClickListener(Consumer<CalendarBackwardClickEvent> listener);

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeBackwardClickListener(Consumer<CalendarBackwardClickEvent> listener);

    Subscription addRangeSelectListener(Consumer<CalendarRangeSelectEvent> listener);

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeRangeSelectListener(Consumer<CalendarRangeSelectEvent> listener);

    enum TimeFormat {
        FORMAT_12H, FORMAT_24H
    }

    class CalendarEventMoveEvent extends EventObject {
        protected CalendarEvent calendarEvent;
        protected Date newStart;

        public CalendarEventMoveEvent(Calendar calendar, CalendarEvent calendarEvent, Date newStart) {
            super(calendar);

            this.calendarEvent = calendarEvent;
            this.newStart = newStart;
        }

        @Override
        public Calendar getSource() {
            return (Calendar) super.getSource();
        }

        /**
         * @return an event source
         * @deprecated Use {@link #getSource()} instead
         */
        @Deprecated
        public Calendar getCalendar() {
            return getSource();
        }

        public CalendarEvent getCalendarEvent() {
            return calendarEvent;
        }

        public Date getNewStart() {
            return newStart;
        }
    }

    class CalendarBackwardClickEvent extends EventObject {

        public CalendarBackwardClickEvent(Calendar calendar) {
            super(calendar);
        }

        @Override
        public Calendar getSource() {
            return (Calendar) super.getSource();
        }

        /**
         * @return an event source
         * @deprecated Use {@link #getSource()} instead
         */
        @Deprecated
        public Calendar getCalendar() {
            return getSource();
        }
    }

    class CalendarDateClickEvent extends EventObject {
        protected Date date;

        public CalendarDateClickEvent(Calendar calendar, Date date) {
            super(calendar);

            this.date = date;
        }

        public Date getDate() {
            return date;
        }

        @Override
        public Calendar getSource() {
            return (Calendar) super.getSource();
        }

        /**
         * @return an event source
         * @deprecated Use {@link #getSource()} instead
         */
        @Deprecated
        public Calendar getCalendar() {
            return getSource();
        }
    }

    class CalendarEventClickEvent extends EventObject {
        protected CalendarEvent calendarEvent;
        protected Entity entity;

        public CalendarEventClickEvent(Calendar calendar, CalendarEvent calendarEvent, @Nullable Entity entity) {
            super(calendar);

            this.calendarEvent = calendarEvent;
            this.entity = entity;
        }

        public Entity getEntity() {
            return entity;
        }

        public CalendarEvent getCalendarEvent() {
            return calendarEvent;
        }

        @Override
        public Calendar getSource() {
            return (Calendar) super.getSource();
        }

        /**
         * @return an event source
         * @deprecated Use {@link #getSource()} instead
         */
        @Deprecated
        public Calendar getCalendar() {
            return getSource();
        }
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

    class CalendarEventResizeEvent extends EventObject {
        protected CalendarEvent calendarEvent;
        protected Date newStart;
        protected Date newEnd;
        protected Entity entity;

        public CalendarEventResizeEvent(Calendar calendar, CalendarEvent calendarEvent, Date newStart,
                                        Date newEnd, @Nullable Entity entity) {
            super(calendar);

            this.calendarEvent = calendarEvent;
            this.newEnd = newEnd;
            this.newStart = newStart;
            this.entity = entity;
        }

        @Nullable
        public Entity getEntity() {
            return entity;
        }

        public CalendarEvent getCalendarEvent() {
            return calendarEvent;
        }

        @Override
        public Calendar getSource() {
            return (Calendar) super.getSource();
        }

        /**
         * @return an event source
         * @deprecated Use {@link #getSource()} instead
         */
        @Deprecated
        public Calendar getCalendar() {
            return getSource();
        }

        public Date getNewStart() {
            return newStart;
        }

        public Date getNewEnd() {
            return newEnd;
        }
    }

    class CalendarWeekClickEvent extends EventObject {
        protected int week;
        protected int year;

        public CalendarWeekClickEvent(Calendar calendar, int week, int year) {
            super(calendar);

            this.week = week;
            this.year = year;
        }

        @Override
        public Calendar getSource() {
            return (Calendar) super.getSource();
        }

        /**
         * @return an event source
         * @deprecated Use {@link #getSource()} instead
         */
        @Deprecated
        public Calendar getCalendar() {
            return getSource();
        }

        public int getWeek() {
            return week;
        }

        public int getYear() {
            return year;
        }
    }

    class CalendarRangeSelectEvent extends EventObject {
        protected Date start;
        protected Date end;

        public CalendarRangeSelectEvent(Calendar calendar, Date start, Date end) {
            super(calendar);

            this.start = start;
            this.end = end;
        }

        @Override
        public Calendar getSource() {
            return (Calendar) super.getSource();
        }

        /**
         * @return an event source
         * @deprecated Use {@link #getSource()} instead
         */
        @Deprecated
        public Calendar getCalendar() {
            return getSource();
        }

        public Date getStart() {
            return start;
        }

        public Date getEnd() {
            return end;
        }
    }
}