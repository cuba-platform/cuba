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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class WebCalendar extends WebAbstractComponent<CubaCalendar> implements Calendar {
    private CollectionDatasource datasource;

    protected final String TIME_FORMAT_12H = "12H";
    protected final String TIME_FORMAT_24H = "24H";

    protected CalendarEventProvider calendarEventProvider;
    protected List<CalendarDateClickListener> dateClickListeners;
    protected List<CalendarWeekClickListener> weekClickListeners;
    protected List<CalendarEventClickListener> eventClickListeners;
    protected List<CalendarEventResizeListener> eventResizeListeners;
    protected List<CalendarForwardClickListener> forwardClickListeners;
    protected List<CalendarBackwardClickListener> backwardClickListeners;
    protected List<CalendarEventMoveListener> eventMoveListeners;
    protected List<CalendarRangeSelectListener> rangeSelectListeners;

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

        component.setHandler(new CalendarComponentEvents.DateClickHandler() {
            @Override
            public void dateClick(CalendarComponentEvents.DateClickEvent event) {
                if (dateClickListeners != null) {
                    for (CalendarDateClickListener calendarDateClickListener : dateClickListeners) {
                        calendarDateClickListener.dateClick(new CalendarDateClickEvent(WebCalendar.this, event.getDate()));
                    }
                }
            }
        });

        component.setHandler(new CalendarComponentEvents.RangeSelectHandler() {
            @Override
            public void rangeSelect(CalendarComponentEvents.RangeSelectEvent event) {
                if (rangeSelectListeners != null) {
                    for (CalendarRangeSelectListener calendarRangeSelectListener : rangeSelectListeners) {
                        calendarRangeSelectListener.rangeSelect(new CalendarRangeSelectEvent(WebCalendar.this,
                                event.getStart(),
                                event.getEnd()));
                    }
                }
            }
        });

        component.setHandler(new CalendarComponentEvents.EventClickHandler() {
            @Override
            public void eventClick(CalendarComponentEvents.EventClick event) {
                if (eventClickListeners != null) {
                    com.vaadin.ui.components.calendar.event.CalendarEvent calendarEvent = event.getCalendarEvent();
                    if (calendarEvent instanceof CalendarEventWrapper) {
                        Entity entity = null;
                        if (((CalendarEventWrapper) calendarEvent).getCalendarEvent() instanceof EntityCalendarEvent) {
                            entity = ((EntityCalendarEvent) ((CalendarEventWrapper) calendarEvent)
                                    .getCalendarEvent())
                                    .getEntity();
                        }

                        CalendarEvent calendarEventWrapper = ((CalendarEventWrapper) calendarEvent).getCalendarEvent();
                        for (CalendarEventClickListener calendarEventClickListener : eventClickListeners) {
                            calendarEventClickListener.eventClick(new CalendarEventClickEvent(
                                    WebCalendar.this,
                                    calendarEventWrapper,
                                    entity
                            ));
                        }
                    }
                }
            }
        });

        component.setHandler(new CalendarComponentEvents.WeekClickHandler() {
            @Override
            public void weekClick(CalendarComponentEvents.WeekClick event) {
                if (weekClickListeners != null) {
                    for (CalendarWeekClickListener calendarWeekClickListener : weekClickListeners) {
                        calendarWeekClickListener.weekClick(new CalendarWeekClickEvent(
                                WebCalendar.this,
                                event.getWeek(),
                                event.getYear()
                        ));
                    }
                }
            }
        });

        component.setHandler(new CalendarComponentEvents.EventResizeHandler() {
            @Override
            public void eventResize(CalendarComponentEvents.EventResize event) {
                if (eventResizeListeners != null) {
                    com.vaadin.ui.components.calendar.event.CalendarEvent calendarEvent = event.getCalendarEvent();
                    if (calendarEvent instanceof CalendarEventWrapper) {
                        Entity entity = null;
                        if (((CalendarEventWrapper) calendarEvent).getCalendarEvent() instanceof EntityCalendarEvent) {
                            entity = ((EntityCalendarEvent) ((CalendarEventWrapper) calendarEvent)
                                    .getCalendarEvent())
                                    .getEntity();
                        }

                        for (CalendarEventResizeListener calendarEventResizeListener : eventResizeListeners) {
                            calendarEventResizeListener.eventResize(new CalendarEventResizeEvent(
                                    WebCalendar.this,
                                    ((CalendarEventWrapper) calendarEvent).getCalendarEvent(),
                                    event.getNewStart(),
                                    event.getNewEnd(),
                                    entity
                            ));
                        }
                    }
                }
            }
        });

        component.setHandler(new CalendarComponentEvents.ForwardHandler() {
            @Override
            public void forward(CalendarComponentEvents.ForwardEvent event) {
                if (forwardClickListeners != null) {
                    for (CalendarForwardClickListener calendarForwardClickListener : forwardClickListeners) {
                        calendarForwardClickListener.forwardClick(new CalendarForwardClickEvent(WebCalendar.this));
                    }
                }
            }
        });

        component.setHandler(new CalendarComponentEvents.BackwardHandler() {
            @Override
            public void backward(CalendarComponentEvents.BackwardEvent event) {
                if (backwardClickListeners != null) {
                    for (CalendarBackwardClickListener calendarBackwardClickListener : backwardClickListeners) {
                        calendarBackwardClickListener.backwardClick(new CalendarBackwardClickEvent(WebCalendar.this));
                    }
                }
            }
        });

        component.setHandler(new CalendarComponentEvents.EventMoveHandler() {
            @Override
            public void eventMove(CalendarComponentEvents.MoveEvent event) {
                if (eventMoveListeners != null) {
                    com.vaadin.ui.components.calendar.event.CalendarEvent calendarEvent = event.getCalendarEvent();
                    for (CalendarEventMoveListener calendarEventMoveListener : eventMoveListeners) {
                        calendarEventMoveListener.eventMove(new CalendarEventMoveEvent(
                                WebCalendar.this,
                                ((CalendarEventWrapper) calendarEvent).getCalendarEvent(),
                                event.getNewStart()
                        ));
                    }
                }
            }
        });
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
        if (dateClickListeners == null) {
            dateClickListeners = new ArrayList<>();
        }
        if (!dateClickListeners.contains(listener)) {
            dateClickListeners.add(listener);
        }
    }

    @Override
    public void removeDateClickListener(CalendarDateClickListener listener) {
        if (dateClickListeners != null) {
            dateClickListeners.remove(listener);
        }
    }

    @Override
    public void addEventClickListener(CalendarEventClickListener listener) {
        if (eventClickListeners == null) {
            eventClickListeners = new ArrayList<>();
        }
        if (!eventClickListeners.contains(listener)) {
            eventClickListeners.add(listener);
        }
    }

    @Override
    public void removeEventClickListener(CalendarEventClickListener listener) {
        if (!eventClickListeners.isEmpty()) {
            eventClickListeners.remove(listener);
        }
    }

    @Override
    public void addEventResizeListener(CalendarEventResizeListener listener) {
        if (eventResizeListeners == null) {
            eventResizeListeners = new ArrayList<>();
        }
        if (!eventResizeListeners.contains(listener)) {
            eventResizeListeners.add(listener);
        }
    }

    @Override
    public void removeEventResizeListener(CalendarEventResizeListener listener) {
        if (eventResizeListeners != null) {
            eventResizeListeners.remove(listener);
        }
    }

    @Override
    public void addEventMoveListener(CalendarEventMoveListener listener) {
        if (eventMoveListeners == null) {
            eventMoveListeners = new ArrayList<>();
        }
        if (!eventMoveListeners.contains(listener)) {
            eventMoveListeners.add(listener);
        }
    }

    @Override
    public void removeEventMoveListener(CalendarEventMoveListener listener) {
        if (!eventMoveListeners.contains(listener)) {
            eventMoveListeners.remove(listener);
        }
    }

    @Override
    public void addWeekClickListener(CalendarWeekClickListener listener) {
        if (weekClickListeners == null) {
            weekClickListeners = new ArrayList<>();
        }
        if (!weekClickListeners.contains(listener)) {
            weekClickListeners.add(listener);
        }
    }

    @Override
    public void removeWeekClickListener(CalendarWeekClickListener listener) {
        if (weekClickListeners != null) {
            weekClickListeners.remove(listener);
        }
    }

    @Override
    public void addForwardClickListener(CalendarForwardClickListener listener) {
        if (forwardClickListeners == null) {
            forwardClickListeners = new ArrayList<>();
        }
        if (!forwardClickListeners.contains(listener)) {
            forwardClickListeners.add(listener);
        }
    }

    @Override
    public void removeForwardClickListener(CalendarForwardClickListener listener) {
        if (forwardClickListeners != null) {
            forwardClickListeners.remove(listener);
        }
    }

    @Override
    public void addBackwardClickListener(CalendarBackwardClickListener listener) {
        if (backwardClickListeners == null) {
            backwardClickListeners = new ArrayList<>();
        }
        if (!backwardClickListeners.contains(listener)) {
            backwardClickListeners.add(listener);
        }
    }

    @Override
    public void removeBackwardClickListener(CalendarBackwardClickListener listener) {
        if (backwardClickListeners != null) {
            backwardClickListeners.remove(listener);
        }
    }

    @Override
    public void addRangeSelectListener(CalendarRangeSelectListener listener) {
        if (rangeSelectListeners == null) {
            rangeSelectListeners = new ArrayList<>();
        }
        if (!rangeSelectListeners.contains(listener)) {
            rangeSelectListeners.add(listener);
        }
    }

    @Override
    public void removeRangeSelectListener(CalendarRangeSelectListener listener) {
        if (rangeSelectListeners != null) {
            rangeSelectListeners.remove(listener);
        }
    }

    @Override
    public CalendarEventProvider getEventProvider() {
        return ((CalendarEventProviderWrapper) component.getEventProvider()).getCalendarEventProvider();
    }
}
