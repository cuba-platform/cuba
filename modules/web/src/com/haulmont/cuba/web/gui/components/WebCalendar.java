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
import com.haulmont.cuba.web.toolkit.ui.CubaCalendar;
import com.vaadin.ui.components.calendar.CalendarComponentEvents;

import java.util.Date;
import java.util.TimeZone;

public class WebCalendar extends WebAbstractComponent<CubaCalendar> implements Calendar {
    private CollectionDatasource datasource;

    protected final String TIME_FORMAT_12H = "12H";
    protected final String TIME_FORMAT_24H = "24H";

    protected CalendarEventProvider calendarEventProvider;
    protected boolean navigationButtonsVisible = false;

    public WebCalendar() {
        component = createComponent();

        Messages messages = AppBeans.get(Messages.NAME);
        String [] monthNamesShort = new String[12];
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

        String [] dayNamesShort = new String[7];
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
            component.setTimeFormat(com.vaadin.ui.Calendar.TimeFormat.Format12H);
        } else {
            component.setTimeFormat(com.vaadin.ui.Calendar.TimeFormat.Format24H);
        }
    }

    @Override
    public TimeFormat getTimeFormat() {
        if (component.getTimeFormat() == com.vaadin.ui.Calendar.TimeFormat.Format12H) {
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
        if (listener != null) {
            getEventRouter().addListener(CalendarDateClickListener.class, listener);

            component.setHandler(new CalendarComponentEvents.DateClickHandler() {
                @Override
                public void dateClick(CalendarComponentEvents.DateClickEvent event) {
                    if (getEventRouter().hasListeners(CalendarDateClickListener.class)) {
                        getEventRouter().fireEvent(
                                CalendarDateClickListener.class,
                                CalendarDateClickListener::dateClick,
                                new CalendarDateClickEvent(WebCalendar.this, event.getDate())
                        );
                    }
                }
            });
        }
    }

    @Override
    public void removeDateClickListener(CalendarDateClickListener listener) {
        getEventRouter().removeListener(CalendarDateClickListener.class, listener);

        if (getEventRouter().hasListeners(CalendarDateClickListener.class)) {
            component.setHandler((CalendarComponentEvents.DateClickHandler) null);
        }
    }

    @Override
    public void addEventClickListener(CalendarEventClickListener listener) {
        if (listener != null) {
            getEventRouter().addListener(CalendarEventClickListener.class, listener);

            component.setHandler(new CalendarComponentEvents.EventClickHandler() {
                @Override
                public void eventClick(CalendarComponentEvents.EventClick event) {
                    com.vaadin.ui.components.calendar.event.CalendarEvent calendarEvent = event.getCalendarEvent();
                    if (calendarEvent instanceof CalendarEventWrapper) {
                        Entity entity = null;
                        if (((CalendarEventWrapper) calendarEvent).getCalendarEvent() instanceof EntityCalendarEvent) {
                            entity = ((EntityCalendarEvent) ((CalendarEventWrapper) calendarEvent)
                                    .getCalendarEvent())
                                    .getEntity();
                        }

                        CalendarEvent calendarEventWrapper = ((CalendarEventWrapper) calendarEvent).getCalendarEvent();
                        getEventRouter().fireEvent(
                                CalendarEventClickListener.class,
                                CalendarEventClickListener::eventClick,
                                new CalendarEventClickEvent(
                                        WebCalendar.this,
                                        calendarEventWrapper,
                                        entity)
                        );
                    }
                }
            });
        }
    }

    @Override
    public void removeEventClickListener(CalendarEventClickListener listener) {
        getEventRouter().removeListener(CalendarEventClickListener.class, listener);

        if (getEventRouter().hasListeners(CalendarEventClickListener.class)) {
            component.setHandler((CalendarComponentEvents.EventClickHandler) null);
        }
    }

    @Override
    public void addEventResizeListener(CalendarEventResizeListener listener) {
        if (listener != null) {
            getEventRouter().addListener(CalendarEventResizeListener.class, listener);

            component.setHandler(new CalendarComponentEvents.EventResizeHandler() {
                @Override
                public void eventResize(CalendarComponentEvents.EventResize event) {
                    com.vaadin.ui.components.calendar.event.CalendarEvent calendarEvent = event.getCalendarEvent();
                    if (calendarEvent instanceof CalendarEventWrapper) {
                        Entity entity = null;
                        if (((CalendarEventWrapper) calendarEvent).getCalendarEvent() instanceof EntityCalendarEvent) {
                            entity = ((EntityCalendarEvent) ((CalendarEventWrapper) calendarEvent)
                                    .getCalendarEvent())
                                    .getEntity();
                        }

                        getEventRouter().fireEvent(
                                CalendarEventResizeListener.class,
                                CalendarEventResizeListener::eventResize,
                                new CalendarEventResizeEvent(
                                        WebCalendar.this,
                                        ((CalendarEventWrapper) calendarEvent).getCalendarEvent(),
                                        event.getNewStart(),
                                        event.getNewEnd(),
                                        entity)
                        );
                    }
                }
            });
        }
    }

    @Override
    public void removeEventResizeListener(CalendarEventResizeListener listener) {
        getEventRouter().removeListener(CalendarEventResizeListener.class, listener);

        if (getEventRouter().hasListeners(CalendarEventResizeListener.class)) {
            component.setHandler((CalendarComponentEvents.EventResizeHandler) null);
        }
    }

    @Override
    public void addEventMoveListener(CalendarEventMoveListener listener) {
        if (listener != null) {
            getEventRouter().addListener(CalendarEventMoveListener.class, listener);

            component.setHandler(new CalendarComponentEvents.EventMoveHandler() {
                @Override
                public void eventMove(CalendarComponentEvents.MoveEvent event) {
                    com.vaadin.ui.components.calendar.event.CalendarEvent calendarEvent = event.getCalendarEvent();
                    getEventRouter().fireEvent(CalendarEventMoveListener.class, CalendarEventMoveListener::eventMove, new CalendarEventMoveEvent(
                            WebCalendar.this,
                            ((CalendarEventWrapper) calendarEvent).getCalendarEvent(),
                            event.getNewStart()
                    ));
                }
            });
        }
    }

    @Override
    public void removeEventMoveListener(CalendarEventMoveListener listener) {
        getEventRouter().removeListener(CalendarEventMoveListener.class, listener);

        if (getEventRouter().hasListeners(CalendarEventMoveListener.class)) {
            component.setHandler((CalendarComponentEvents.EventMoveHandler) null);
        }
    }

    @Override
    public void addWeekClickListener(CalendarWeekClickListener listener) {
        if (listener != null) {
            getEventRouter().addListener(CalendarWeekClickListener.class, listener);

            component.setHandler(new CalendarComponentEvents.WeekClickHandler() {
                @Override
                public void weekClick(CalendarComponentEvents.WeekClick event) {
                    getEventRouter().fireEvent(
                            CalendarWeekClickListener.class,
                            CalendarWeekClickListener::weekClick,
                            new CalendarWeekClickEvent(
                                    WebCalendar.this,
                                    event.getWeek(),
                                    event.getYear())
                    );
                }
            });
        }
    }

    @Override
    public void removeWeekClickListener(CalendarWeekClickListener listener) {
        getEventRouter().removeListener(CalendarWeekClickListener.class, listener);

        if (getEventRouter().hasListeners(CalendarWeekClickListener.class)) {
            component.setHandler((CalendarComponentEvents.WeekClickHandler) null);
        }
    }

    @Override
    public void addForwardClickListener(CalendarForwardClickListener listener) {
        if (listener != null) {
            getEventRouter().addListener(CalendarForwardClickListener.class, listener);

            component.setHandler(new CalendarComponentEvents.ForwardHandler() {
                @Override
                public void forward(CalendarComponentEvents.ForwardEvent event) {
                    getEventRouter().fireEvent(
                            CalendarForwardClickListener.class,
                            CalendarForwardClickListener::forwardClick,
                            new CalendarForwardClickEvent(WebCalendar.this)
                    );
                }
            });
        }
    }

    @Override
    public void removeForwardClickListener(CalendarForwardClickListener listener) {
        getEventRouter().removeListener(CalendarForwardClickListener.class, listener);

        if (getEventRouter().hasListeners(CalendarForwardClickListener.class)) {
            component.setHandler((CalendarComponentEvents.ForwardHandler) null);
        }
    }

    @Override
    public void addBackwardClickListener(CalendarBackwardClickListener listener) {
        if (listener != null) {
            getEventRouter().addListener(CalendarBackwardClickListener.class, listener);

            component.setHandler(new CalendarComponentEvents.BackwardHandler() {
                @Override
                public void backward(CalendarComponentEvents.BackwardEvent event) {
                    getEventRouter().fireEvent(
                            CalendarBackwardClickListener.class,
                            CalendarBackwardClickListener::backwardClick,
                            new CalendarBackwardClickEvent(WebCalendar.this)
                    );
                }
            });
        }
    }

    @Override
    public void removeBackwardClickListener(CalendarBackwardClickListener listener) {
        getEventRouter().removeListener(CalendarBackwardClickListener.class, listener);

        if (getEventRouter().hasListeners(CalendarBackwardClickListener.class)) {
            component.setHandler((CalendarComponentEvents.BackwardHandler) null);
        }
    }

    @Override
    public void addRangeSelectListener(CalendarRangeSelectListener listener) {
        if (listener != null) {
            getEventRouter().addListener(CalendarRangeSelectListener.class, listener);

            component.setHandler(new CalendarComponentEvents.RangeSelectHandler() {
                @Override
                public void rangeSelect(CalendarComponentEvents.RangeSelectEvent event) {
                    getEventRouter().fireEvent(
                            CalendarRangeSelectListener.class,
                            CalendarRangeSelectListener::rangeSelect,
                            new CalendarRangeSelectEvent(WebCalendar.this,
                                    event.getStart(),
                                    event.getEnd())
                    );
                }
            });
        }
    }

    @Override
    public void removeRangeSelectListener(CalendarRangeSelectListener listener) {
        getEventRouter().removeListener(CalendarRangeSelectListener.class, listener);

        if (getEventRouter().hasListeners(CalendarRangeSelectListener.class)) {
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
}
