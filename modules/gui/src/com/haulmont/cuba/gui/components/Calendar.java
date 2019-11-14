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
import java.util.EventObject;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Consumer;

/**
 * Calendar is used for visualizing events in a calendar using week or month view.
 *
 * @param <V> type of value
 */
public interface Calendar<V> extends Component.BelongToFrame, Component.HasCaption, Component.HasIcon,
        HasContextHelp, HasHtmlCaption, HasHtmlDescription, HasDatatype<V> {

    String NAME = "calendar";

    /**
     * Set start date for the calendar range.
     */
    void setStartDate(V date);
    /**
     * @return the start date for the calendar range.
     */
    V getStartDate();

    /**
     * Set end date for the calendar's range.
     */
    void setEndDate(V date);
    /**
     * @return the last date for the calendar range.
     */
    V getEndDate();

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
     * Allows setting first day of week independent of Locale.
     * <p>
     * Pass {@code null} to use a day of week defined by current locale.
     * </p>
     *
     * @param dayOfWeek
     *            any of java.util.Calendar.SUNDAY ... java.util.Calendar.SATURDAY
     *            or null to revert to default first day of week by locale
     */
    void setFirstDayOfWeek(Integer dayOfWeek);

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

    Subscription addDateClickListener(Consumer<CalendarDateClickEvent<V>> listener);

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeDateClickListener(Consumer<CalendarDateClickEvent<V>> listener);

    Subscription addEventClickListener(Consumer<CalendarEventClickEvent<V>> listener);

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeEventClickListener(Consumer<CalendarEventClickEvent<V>> listener);

    Subscription addEventResizeListener(Consumer<CalendarEventResizeEvent<V>> listener);

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeEventResizeListener(Consumer<CalendarEventResizeEvent<V>> listener);

    Subscription addEventMoveListener(Consumer<CalendarEventMoveEvent<V>> listener);

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeEventMoveListener(Consumer<CalendarEventMoveEvent<V>> listener);

    Subscription addWeekClickListener(Consumer<CalendarWeekClickEvent<V>> listener);

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeWeekClickListener(Consumer<CalendarWeekClickEvent<V>> listener);

    Subscription addForwardClickListener(Consumer<CalendarForwardClickEvent<V>> listener);

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeForwardClickListener(Consumer<CalendarForwardClickEvent<V>> listener);

    Subscription addBackwardClickListener(Consumer<CalendarBackwardClickEvent<V>> listener);

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeBackwardClickListener(Consumer<CalendarBackwardClickEvent<V>> listener);

    Subscription addRangeSelectListener(Consumer<CalendarRangeSelectEvent<V>> listener);

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} instead
     */
    @Deprecated
    void removeRangeSelectListener(Consumer<CalendarRangeSelectEvent<V>> listener);

    enum TimeFormat {
        FORMAT_12H, FORMAT_24H
    }

    class CalendarEventMoveEvent<V> extends EventObject {
        protected CalendarEvent calendarEvent;
        protected V newStart;
        protected V newEnd;
        protected Entity entity;

        @Deprecated
        public CalendarEventMoveEvent(Calendar<V> calendar, CalendarEvent calendarEvent, V newStart) {
            super(calendar);

            this.calendarEvent = calendarEvent;
            this.newStart = newStart;
        }

        public CalendarEventMoveEvent(Calendar<V> calendar, CalendarEvent calendarEvent, V newStart, @Nullable Entity entity) {
            super(calendar);

            this.calendarEvent = calendarEvent;
            this.newStart = newStart;
            this.entity = entity;
        }

        public CalendarEventMoveEvent(Calendar<V> calendar, CalendarEvent calendarEvent, V newStart, V newEnd,
                                      @Nullable Entity entity) {
            super(calendar);

            this.calendarEvent = calendarEvent;
            this.newStart = newStart;
            this.newEnd = newEnd;
            this.entity = entity;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Calendar<V> getSource() {
            return (Calendar<V>) super.getSource();
        }

        /**
         * @return an event source
         * @deprecated Use {@link #getSource()} instead
         */
        @Deprecated
        public Calendar<V> getCalendar() {
            return getSource();
        }

        public CalendarEvent getCalendarEvent() {
            return calendarEvent;
        }

        public V getNewStart() {
            return newStart;
        }

        public V getNewEnd() {
            return newEnd;
        }

        /**
         * @return moved event entity or null if it is not entity based event
         */
        @Nullable
        public Entity getEntity() {
            return entity;
        }
    }

    class CalendarBackwardClickEvent<V> extends EventObject {

        public CalendarBackwardClickEvent(Calendar<V> calendar) {
            super(calendar);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Calendar<V> getSource() {
            return (Calendar<V>) super.getSource();
        }

        /**
         * @return an event source
         * @deprecated Use {@link #getSource()} instead
         */
        @Deprecated
        public Calendar<V> getCalendar() {
            return getSource();
        }
    }

    class CalendarDateClickEvent<V> extends EventObject {
        protected V date;

        public CalendarDateClickEvent(Calendar<V> calendar, V date) {
            super(calendar);

            this.date = date;
        }

        public V getDate() {
            return date;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Calendar<V> getSource() {
            return (Calendar<V>) super.getSource();
        }

        /**
         * @return an event source
         * @deprecated Use {@link #getSource()} instead
         */
        @Deprecated
        public Calendar<V> getCalendar() {
            return getSource();
        }
    }

    class CalendarEventClickEvent<V> extends EventObject {
        protected CalendarEvent calendarEvent;
        protected Entity entity;

        public CalendarEventClickEvent(Calendar<V> calendar, CalendarEvent calendarEvent, @Nullable Entity entity) {
            super(calendar);

            this.calendarEvent = calendarEvent;
            this.entity = entity;
        }

        /**
         * @return event entity that was clicked or null if it is not entity based event
         */
        @Nullable
        public Entity getEntity() {
            return entity;
        }

        public CalendarEvent getCalendarEvent() {
            return calendarEvent;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Calendar<V> getSource() {
            return (Calendar<V>) super.getSource();
        }

        /**
         * @return an event source
         * @deprecated Use {@link #getSource()} instead
         */
        @Deprecated
        public Calendar<V> getCalendar() {
            return getSource();
        }
    }

    class CalendarForwardClickEvent<V> extends EventObject {
        public CalendarForwardClickEvent(Calendar<V> calendar) {
            super(calendar);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Calendar<V> getSource() {
            return (Calendar<V>) super.getSource();
        }

        /**
         * @return an event source
         * @deprecated Use {@link #getSource()} instead
         */
        @Deprecated
        public Calendar<V> getCalendar() {
            return getSource();
        }
    }

    class CalendarEventResizeEvent<V> extends EventObject {
        protected CalendarEvent calendarEvent;
        protected V newStart;
        protected V newEnd;
        protected Entity entity;

        public CalendarEventResizeEvent(Calendar<V> calendar, CalendarEvent calendarEvent, V newStart,
                                        V newEnd, @Nullable Entity entity) {
            super(calendar);

            this.calendarEvent = calendarEvent;
            this.newEnd = newEnd;
            this.newStart = newStart;
            this.entity = entity;
        }

        /**
         * @return event entity that was resized or null if it is not entity based event
         */
        @Nullable
        public Entity getEntity() {
            return entity;
        }

        public CalendarEvent getCalendarEvent() {
            return calendarEvent;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Calendar<V> getSource() {
            return (Calendar<V>) super.getSource();
        }

        /**
         * @return an event source
         * @deprecated Use {@link #getSource()} instead
         */
        @Deprecated
        public Calendar<V> getCalendar() {
            return getSource();
        }

        public V getNewStart() {
            return newStart;
        }

        public V getNewEnd() {
            return newEnd;
        }
    }

    class CalendarWeekClickEvent<V> extends EventObject {
        protected int week;
        protected int year;

        public CalendarWeekClickEvent(Calendar<V> calendar, int week, int year) {
            super(calendar);

            this.week = week;
            this.year = year;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Calendar<V> getSource() {
            return (Calendar<V>) super.getSource();
        }

        /**
         * @return an event source
         * @deprecated Use {@link #getSource()} instead
         */
        @Deprecated
        public Calendar<V> getCalendar() {
            return getSource();
        }

        public int getWeek() {
            return week;
        }

        public int getYear() {
            return year;
        }
    }

    class CalendarRangeSelectEvent<V> extends EventObject {
        protected V start;
        protected V end;

        public CalendarRangeSelectEvent(Calendar<V> calendar, V start, V end) {
            super(calendar);

            this.start = start;
            this.end = end;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Calendar<V> getSource() {
            return (Calendar<V>) super.getSource();
        }

        /**
         * @return an event source
         * @deprecated Use {@link #getSource()} instead
         */
        @Deprecated
        public Calendar<V> getCalendar() {
            return getSource();
        }

        public V getStart() {
            return start;
        }

        public V getEnd() {
            return end;
        }
    }
}