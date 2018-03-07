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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.Calendar;
import com.haulmont.cuba.gui.components.calendar.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsHelper;
import com.haulmont.cuba.web.gui.components.calendar.CalendarEventProviderWrapper;
import com.haulmont.cuba.web.gui.components.calendar.CalendarEventWrapper;
import com.haulmont.cuba.web.widgets.CubaCalendar;
import com.vaadin.v7.ui.components.calendar.CalendarComponentEvents;
import org.apache.commons.collections4.CollectionUtils;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class WebCalendar extends WebAbstractComponent<CubaCalendar> implements Calendar {
    private CollectionDatasource datasource;

    protected final String TIME_FORMAT_12H = "12H";
    protected final String TIME_FORMAT_24H = "24H";

    protected CalendarEventProvider calendarEventProvider;
    protected boolean navigationButtonsVisible = false;

    public WebCalendar() {
        component = createComponent();

        Messages messages = AppBeans.get(Messages.NAME);
        String[] monthNamesShort = new String[12];
        monthNamesShort[0] = messages.getMainMessage("calendar.januaryCaption");
        monthNamesShort[1] = messages.getMainMessage("calendar.februaryCaption");
        monthNamesShort[2] = messages.getMainMessage("calendar.marchCaption");
        monthNamesShort[3] = messages.getMainMessage("calendar.aprilCaption");
        monthNamesShort[4] = messages.getMainMessage("calendar.mayCaption");
        monthNamesShort[5] = messages.getMainMessage("calendar.juneCaption");
        monthNamesShort[6] = messages.getMainMessage("calendar.julyCaption");
        monthNamesShort[7] = messages.getMainMessage("calendar.augustCaption");
        monthNamesShort[8] = messages.getMainMessage("calendar.septemberCaption");
        monthNamesShort[9] = messages.getMainMessage("calendar.octoberCaption");
        monthNamesShort[10] = messages.getMainMessage("calendar.novemberCaption");
        monthNamesShort[11] = messages.getMainMessage("calendar.decemberCaption");
        component.setMonthNamesShort(monthNamesShort);

        String[] dayNamesShort = new String[7];
        dayNamesShort[0] = messages.getMainMessage("calendar.sundayCaption");
        dayNamesShort[1] = messages.getMainMessage("calendar.mondayCaption");
        dayNamesShort[2] = messages.getMainMessage("calendar.tuesdayCaption");
        dayNamesShort[3] = messages.getMainMessage("calendar.wednesdayCaption");
        dayNamesShort[4] = messages.getMainMessage("calendar.thursdayCaption");
        dayNamesShort[5] = messages.getMainMessage("calendar.fridayCaption");
        dayNamesShort[6] = messages.getMainMessage("calendar.saturdayCaption");
        component.setDayNamesShort(dayNamesShort);

        if (TIME_FORMAT_12H.equals(messages.getMainMessage("calendar.timeFormat"))) {
            setTimeFormat(TimeFormat.FORMAT_12H);
        } else if (TIME_FORMAT_24H.equals(messages.getMainMessage("calendar.timeFormat"))) {
            setTimeFormat(TimeFormat.FORMAT_24H);
        } else {
            throw new IllegalStateException(
                    String.format("Can't set time format '%s'", messages.getMainMessage("calendar.timeFormat")));
        }

        UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);
        TimeZone userTimeZone = userSessionSource.getUserSession().getTimeZone();
        if (userTimeZone != null) {
            setTimeZone(userTimeZone);
        }

        calendarEventProvider = new ListCalendarEventProvider();
        calendarEventProvider.setCalendar(this);

        component.setEventProvider(new CalendarEventProviderWrapper(calendarEventProvider));
        setNavigationButtonsStyle(navigationButtonsVisible);
    }

    protected CubaCalendar createComponent() {
        return new CubaCalendar();
    }

    @Override
    public void setStartDate(Date date) {
        component.setStartDate(date);
    }

    @Override
    public Date getStartDate() {
        return component.getStartDate();
    }

    @Override
    public void setEndDate(Date date) {
        component.setEndDate(date);
    }

    @Override
    public Date getEndDate() {
        return component.getEndDate();
    }

    @Override
    public void setTimeZone(TimeZone zone) {
        component.setTimeZone(zone);
    }

    @Override
    public TimeZone getTimeZone() {
        return component.getTimeZone();
    }

    @Override
    public void setDatasource(CollectionDatasource datasource) {
        this.datasource = datasource;

        if (datasource == null) {
            setEventProvider(null);
        } else {
            CollectionDsHelper.autoRefreshInvalid(datasource, true);
            setEventProvider(new EntityCalendarEventProvider(datasource));
        }
    }

    @Override
    public CollectionDatasource getDatasource() {
        return datasource;
    }

    @Override
    public void setTimeFormat(TimeFormat format) {
        if (format == TimeFormat.FORMAT_12H) {
            component.setTimeFormat(com.vaadin.v7.ui.Calendar.TimeFormat.Format12H);
        } else {
            component.setTimeFormat(com.vaadin.v7.ui.Calendar.TimeFormat.Format24H);
        }
    }

    @Override
    public TimeFormat getTimeFormat() {
        if (component.getTimeFormat() == com.vaadin.v7.ui.Calendar.TimeFormat.Format12H) {
            return TimeFormat.FORMAT_12H;
        } else {
            return TimeFormat.FORMAT_24H;
        }
    }

    @Override
    public void setFirstVisibleDayOfWeek(int firstDay) {
        component.setFirstVisibleDayOfWeek(firstDay);
    }

    @Override
    public int getFirstVisibleDayOfWeek() {
        return component.getFirstVisibleDayOfWeek();
    }

    @Override
    public void setLastVisibleDayOfWeek(int lastDay) {
        component.setLastVisibleDayOfWeek(lastDay);
    }

    @Override
    public int getLastVisibleDayOfWeek() {
        return component.getLastVisibleDayOfWeek();
    }

    @Override
    public void setFirstVisibleHourOfDay(int firstHour) {
        component.setFirstVisibleHourOfDay(firstHour);
    }

    @Override
    public int getFirstVisibleHourOfDay() {
        return component.getFirstVisibleHourOfDay();
    }

    @Override
    public void setLastVisibleHourOfDay(int lastHour) {
        component.setLastVisibleHourOfDay(lastHour);
    }

    @Override
    public int getLastVisibleHourOfDay() {
        return component.getLastVisibleHourOfDay();
    }

    @Override
    public void setWeeklyCaptionFormat(String dateFormatPattern) {
        component.setWeeklyCaptionFormat(dateFormatPattern);
    }

    @Override
    public String getWeeklyCaptionFormat() {
        return component.getWeeklyCaptionFormat();
    }

    @Override
    public void setEventProvider(CalendarEventProvider calendarEventProvider) {
        if (this.calendarEventProvider instanceof EntityCalendarEventProvider) {
            ((EntityCalendarEventProvider) this.calendarEventProvider).unbind();
        }

        this.calendarEventProvider = calendarEventProvider;
        if (calendarEventProvider != null) {
            calendarEventProvider.setCalendar(this);
            component.setEventProvider(new CalendarEventProviderWrapper(calendarEventProvider));
        } else {
            component.setEventProvider(null);
        }
    }

    @Override
    public void addDateClickListener(CalendarDateClickListener listener) {
        getEventRouter().addListener(CalendarDateClickListener.class, listener);

        component.setHandler((CalendarComponentEvents.DateClickHandler) event -> {
            CalendarDateClickEvent calendarDateClickEvent =
                    new CalendarDateClickEvent(WebCalendar.this, event.getDate());
            getEventRouter().fireEvent(
                    CalendarDateClickListener.class,
                    CalendarDateClickListener::dateClick,
                    calendarDateClickEvent);
        });
    }

    @Override
    public void removeDateClickListener(CalendarDateClickListener listener) {
        getEventRouter().removeListener(CalendarDateClickListener.class, listener);

        if (!getEventRouter().hasListeners(CalendarDateClickListener.class)) {
            component.setHandler((CalendarComponentEvents.DateClickHandler) null);
        }
    }

    @Override
    public void addEventClickListener(CalendarEventClickListener listener) {
        getEventRouter().addListener(CalendarEventClickListener.class, listener);

        component.setHandler((CalendarComponentEvents.EventClickHandler) event -> {
            com.vaadin.v7.ui.components.calendar.event.CalendarEvent calendarEvent = event.getCalendarEvent();
            if (calendarEvent instanceof CalendarEventWrapper) {
                CalendarEvent calendarEventWrapper = ((CalendarEventWrapper) calendarEvent).getCalendarEvent();
                Entity entity = null;
                if (calendarEventWrapper instanceof EntityCalendarEvent) {
                    entity = ((EntityCalendarEvent) ((CalendarEventWrapper) calendarEvent)
                            .getCalendarEvent())
                            .getEntity();
                }

                CalendarEventClickEvent calendarEventClickEvent = new CalendarEventClickEvent(
                        WebCalendar.this,
                        calendarEventWrapper,
                        entity);
                getEventRouter().fireEvent(
                        CalendarEventClickListener.class,
                        CalendarEventClickListener::eventClick,
                        calendarEventClickEvent);
            }
        });
    }

    @Override
    public void removeEventClickListener(CalendarEventClickListener listener) {
        getEventRouter().removeListener(CalendarEventClickListener.class, listener);

        if (!getEventRouter().hasListeners(CalendarEventClickListener.class)) {
            component.setHandler((CalendarComponentEvents.EventClickHandler) null);
        }
    }

    @Override
    public void addEventResizeListener(CalendarEventResizeListener listener) {
        getEventRouter().addListener(CalendarEventResizeListener.class, listener);

        component.setHandler((CalendarComponentEvents.EventResizeHandler) event -> {
            com.vaadin.v7.ui.components.calendar.event.CalendarEvent calendarEvent = event.getCalendarEvent();
            if (calendarEvent instanceof CalendarEventWrapper) {
                CalendarEvent calendarEventWrapper = ((CalendarEventWrapper) calendarEvent).getCalendarEvent();
                Entity entity = null;
                if (calendarEventWrapper instanceof EntityCalendarEvent) {
                    entity = ((EntityCalendarEvent) ((CalendarEventWrapper) calendarEvent)
                            .getCalendarEvent())
                            .getEntity();
                }

                CalendarEventResizeEvent calendarEventResizeEvent = new CalendarEventResizeEvent(
                        WebCalendar.this,
                        ((CalendarEventWrapper) calendarEvent).getCalendarEvent(),
                        event.getNewStart(),
                        event.getNewEnd(),
                        entity);
                getEventRouter().fireEvent(
                        CalendarEventResizeListener.class,
                        CalendarEventResizeListener::eventResize,
                        calendarEventResizeEvent);
            }
        });
    }

    @Override
    public void removeEventResizeListener(CalendarEventResizeListener listener) {
        getEventRouter().removeListener(CalendarEventResizeListener.class, listener);

        if (!getEventRouter().hasListeners(CalendarEventResizeListener.class)) {
            component.setHandler((CalendarComponentEvents.EventResizeHandler) null);
        }
    }

    @Override
    public void addEventMoveListener(CalendarEventMoveListener listener) {
        getEventRouter().addListener(CalendarEventMoveListener.class, listener);

        component.setHandler((CalendarComponentEvents.EventMoveHandler) event -> {
            com.vaadin.v7.ui.components.calendar.event.CalendarEvent calendarEvent = event.getCalendarEvent();
            CalendarEvent calendarEventWrapper = ((CalendarEventWrapper) calendarEvent).getCalendarEvent();

            CalendarEventMoveEvent calendarEventMoveEvent = new CalendarEventMoveEvent(
                    WebCalendar.this,
                    calendarEventWrapper,
                    event.getNewStart());
            getEventRouter().fireEvent(
                    CalendarEventMoveListener.class,
                    CalendarEventMoveListener::eventMove,
                    calendarEventMoveEvent);
        });
    }

    @Override
    public void removeEventMoveListener(CalendarEventMoveListener listener) {
        getEventRouter().removeListener(CalendarEventMoveListener.class, listener);

        if (!getEventRouter().hasListeners(CalendarEventMoveListener.class)) {
            component.setHandler((CalendarComponentEvents.EventMoveHandler) null);
        }
    }

    @Override
    public void addWeekClickListener(CalendarWeekClickListener listener) {
        getEventRouter().addListener(CalendarWeekClickListener.class, listener);

        component.setHandler((CalendarComponentEvents.WeekClickHandler) event -> {
            CalendarWeekClickEvent calendarWeekClickEvent = new CalendarWeekClickEvent(
                    WebCalendar.this,
                    event.getWeek(),
                    event.getYear());
            getEventRouter().fireEvent(
                    CalendarWeekClickListener.class,
                    CalendarWeekClickListener::weekClick,
                    calendarWeekClickEvent);
        });
    }

    @Override
    public void removeWeekClickListener(CalendarWeekClickListener listener) {
        getEventRouter().removeListener(CalendarWeekClickListener.class, listener);

        if (!getEventRouter().hasListeners(CalendarWeekClickListener.class)) {
            component.setHandler((CalendarComponentEvents.WeekClickHandler) null);
        }
    }

    @Override
    public void addForwardClickListener(CalendarForwardClickListener listener) {
        getEventRouter().addListener(CalendarForwardClickListener.class, listener);

        component.setHandler((CalendarComponentEvents.ForwardHandler) event -> {
            CalendarForwardClickEvent calendarForwardClickEvent =
                    new CalendarForwardClickEvent(WebCalendar.this);
            getEventRouter().fireEvent(
                    CalendarForwardClickListener.class,
                    CalendarForwardClickListener::forwardClick,
                    calendarForwardClickEvent);
        });
    }

    @Override
    public void removeForwardClickListener(CalendarForwardClickListener listener) {
        getEventRouter().removeListener(CalendarForwardClickListener.class, listener);

        if (!getEventRouter().hasListeners(CalendarForwardClickListener.class)) {
            component.setHandler((CalendarComponentEvents.ForwardHandler) null);
        }
    }

    @Override
    public void addBackwardClickListener(CalendarBackwardClickListener listener) {
        getEventRouter().addListener(CalendarBackwardClickListener.class, listener);

        component.setHandler((CalendarComponentEvents.BackwardHandler) event -> {
            CalendarBackwardClickEvent calendarBackwardClickEvent =
                    new CalendarBackwardClickEvent(WebCalendar.this);
            getEventRouter().fireEvent(
                    CalendarBackwardClickListener.class,
                    CalendarBackwardClickListener::backwardClick,
                    calendarBackwardClickEvent);
        });
    }

    @Override
    public void removeBackwardClickListener(CalendarBackwardClickListener listener) {
        getEventRouter().removeListener(CalendarBackwardClickListener.class, listener);

        if (!getEventRouter().hasListeners(CalendarBackwardClickListener.class)) {
            component.setHandler((CalendarComponentEvents.BackwardHandler) null);
        }
    }

    @Override
    public void addRangeSelectListener(CalendarRangeSelectListener listener) {
        getEventRouter().addListener(CalendarRangeSelectListener.class, listener);

        component.setHandler((CalendarComponentEvents.RangeSelectHandler) event -> {
            CalendarRangeSelectEvent calendarRangeSelectEvent = new CalendarRangeSelectEvent(
                    WebCalendar.this,
                    event.getStart(),
                    event.getEnd());
            getEventRouter().fireEvent(
                    CalendarRangeSelectListener.class,
                    CalendarRangeSelectListener::rangeSelect,
                    calendarRangeSelectEvent);
        });
    }

    @Override
    public void removeRangeSelectListener(CalendarRangeSelectListener listener) {
        getEventRouter().removeListener(CalendarRangeSelectListener.class, listener);

        if (!getEventRouter().hasListeners(CalendarRangeSelectListener.class)) {
            component.setHandler((CalendarComponentEvents.RangeSelectHandler) null);
        }
    }

    @Override
    public CalendarEventProvider getEventProvider() {
        return ((CalendarEventProviderWrapper) component.getEventProvider()).getCalendarEventProvider();
    }

    @Override
    public void setStyleName(String name) {
        super.setStyleName(name);
        setNavigationButtonsStyle(navigationButtonsVisible);
    }

    @Override
    public void setNavigationButtonsVisible(boolean value) {
        navigationButtonsVisible = value;
        setNavigationButtonsStyle(value);
    }

    @Override
    public boolean isNavigationButtonsVisible() {
        return navigationButtonsVisible;
    }

    protected void setNavigationButtonsStyle(boolean value) {
        if (!value) {
            addStyleName("navbuttons-disabled");
        } else {
            removeStyleName("navbuttons-disabled");
        }
    }

    @Override
    public Map<DayOfWeek, String> getDayNames() {
        List<String> days = Arrays.asList(component.getDayNamesShort().clone());

        int shift = Math.abs(component.getFirstDayOfWeek() - java.util.Calendar.MONDAY) + 1;
        Collections.rotate(days, -shift);

        return days.stream().collect(Collectors.toMap(
                (String d) -> DayOfWeek.of(days.indexOf(d) + 1),
                d -> d
        ));
    }

    @Override
    public void setDayNames(Map<DayOfWeek, String> dayNames) {
        Preconditions.checkNotNullArgument(dayNames);

        if (!CollectionUtils.isEqualCollection(Arrays.asList(DayOfWeek.values()), dayNames.keySet())) {
            throw new IllegalArgumentException("Day names map doesn't contain all required values");
        }

        List<String> daysList = Arrays.stream(DayOfWeek.values())
                .map(dayNames::get)
                .collect(Collectors.toList());

        int shift = Math.abs(component.getFirstDayOfWeek() - java.util.Calendar.MONDAY) + 1;
        Collections.rotate(daysList, shift);

        String[] days = new String[7];
        component.setDayNamesShort(daysList.toArray(days));
    }

    @Override
    public Map<Month, String> getMonthNames() {
        List<String> months = Arrays.asList(component.getMonthNamesShort());

        return months.stream().collect(Collectors.toMap(
                (String m) -> Month.of(months.indexOf(m) + 1),
                m -> m
        ));
    }

    @Override
    public void setMonthNames(Map<Month, String> monthNames) {
        Preconditions.checkNotNullArgument(monthNames);

        if (!CollectionUtils.isEqualCollection(Arrays.asList(Month.values()), monthNames.keySet())) {
            throw new IllegalArgumentException("Month names map doesn't contain all required values");
        }

        String[] months = Arrays.stream(Month.values())
                .map(monthNames::get)
                .toArray(String[]::new);

        component.setMonthNamesShort(months);
    }
}