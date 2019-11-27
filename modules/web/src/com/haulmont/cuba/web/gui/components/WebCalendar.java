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

import com.haulmont.bali.events.Subscription;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.DatatypeRegistry;
import com.haulmont.chile.core.datatypes.impl.AbstractTemporalDatatype;
import com.haulmont.chile.core.datatypes.impl.DateDatatype;
import com.haulmont.chile.core.datatypes.impl.DateTimeDatatype;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DateTimeTransformations;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.Calendar;
import com.haulmont.cuba.gui.components.calendar.CalendarEvent;
import com.haulmont.cuba.gui.components.calendar.CalendarEventProvider;
import com.haulmont.cuba.gui.components.calendar.ContainerCalendarEventProvider;
import com.haulmont.cuba.gui.components.calendar.EntityCalendarEvent;
import com.haulmont.cuba.gui.components.calendar.ListCalendarEventProvider;
import com.haulmont.cuba.gui.components.data.calendar.EntityCalendarEventProvider;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsHelper;
import com.haulmont.cuba.web.gui.components.calendar.CalendarEventProviderWrapper;
import com.haulmont.cuba.web.gui.components.calendar.CalendarEventWrapper;
import com.haulmont.cuba.web.widgets.CubaCalendar;
import com.vaadin.v7.ui.components.calendar.CalendarComponentEvents;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class WebCalendar<V> extends WebAbstractComponent<CubaCalendar>
        implements Calendar<V>, InitializingBean {

    protected final String TIME_FORMAT_12H = "12H";
    protected final String TIME_FORMAT_24H = "24H";

    protected CalendarEventProvider calendarEventProvider;
    protected boolean navigationButtonsVisible = false;

    protected DateTimeTransformations dateTimeTransformations;
    protected DatatypeRegistry datatypeRegistry;

    protected Datatype<V> datatype;

    public WebCalendar() {
        component = createComponent();
    }

    protected CubaCalendar createComponent() {
        return new CubaCalendar();
    }

    @Override
    public void afterPropertiesSet() {
        initComponent(component);
        initDefaultEventProvider(component);
    }

    protected void initComponent(CubaCalendar component) {
        Messages messages = beanLocator.get(Messages.NAME);
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

        UserSessionSource userSessionSource = beanLocator.get(UserSessionSource.NAME);
        TimeZone userTimeZone = userSessionSource.getUserSession().getTimeZone();
        if (userTimeZone != null) {
            setTimeZone(userTimeZone);
        }

        setNavigationButtonsStyle(navigationButtonsVisible);
    }

    protected void initDefaultEventProvider(CubaCalendar component) {
        calendarEventProvider = new ListCalendarEventProvider();

        component.setEventProvider(new CalendarEventProviderWrapper<>(calendarEventProvider, this::convertToPresentation));
    }

    @Inject
    public void setDateTimeTransformations(DateTimeTransformations dateTimeTransformations) {
        this.dateTimeTransformations = dateTimeTransformations;
    }

    @Inject
    public void setDatatypeRegistry(DatatypeRegistry datatypeRegistry) {
        this.datatypeRegistry = datatypeRegistry;
    }

    @Override
    public void setDatatype(Datatype<V> datatype) {
        checkDatatypeMismatch(datatype);

        if (!(datatype instanceof DateDatatype)
                && !(datatype instanceof DateTimeDatatype)
                && !(datatype instanceof AbstractTemporalDatatype)) {
            throw new IllegalArgumentException("Calendar supports only temporal datatype");
        }
        this.datatype = datatype;
    }

    @Override
    public Datatype<V> getDatatype() {
        if (datatype == null) {
            datatype = getDefaultDatatype();
        }
        return datatype;
    }

    protected Datatype<V> getDefaultDatatype() {
        MetaProperty metaProperty = getMetaProperty();
        Class datatypeClass = metaProperty != null
                ? metaProperty.getJavaType()
                : Date.class;
        return (Datatype<V>) datatypeRegistry.get(datatypeClass);
    }

    @Nullable
    protected MetaProperty getMetaProperty() {
        if (getEventProvider() instanceof EntityCalendarEventProvider) {
            EntityCalendarEventProvider eventProvider = (EntityCalendarEventProvider) getEventProvider();
            String property = eventProvider.getStartDateProperty().isEmpty()
                    ? eventProvider.getEndDateProperty()
                    : eventProvider.getStartDateProperty();

            if (!property.isEmpty()) {
                CollectionDatasource datasource = getDatasource();
                MetaClass metaClass = datasource != null
                        ? datasource.getMetaClass()
                        : ((ContainerCalendarEventProvider) eventProvider).getContainer().getEntityMetaClass();

                return metaClass.getProperty(property);
            }
        }
        return null;
    }

    protected void checkDatatypeMismatch(Datatype datatype) {
        MetaProperty metaProperty = getMetaProperty();
        if (datatype != null
                && metaProperty != null
                && !metaProperty.getJavaType().equals(datatype.getJavaClass())) {
            throw new IllegalArgumentException(String.format("Property '%s' and passed Datatype have different types. " +
                    "Property: '%s'; Datatype: '%s'", metaProperty.getName(), metaProperty.getJavaType(), datatype.getJavaClass()));
        }
    }

    @Override
    public void setStartDate(V date) {
        component.setStartDate(convertToPresentation(date));
    }

    @Override
    public V getStartDate() {
        return convertToModel(component.getStartDate());
    }

    @Override
    public void setEndDate(V date) {
        component.setEndDate(convertToPresentation(date));
    }

    @Override
    public V getEndDate() {
        return convertToModel(component.getEndDate());
    }

    protected Date convertToPresentation(V date) {
        if (date == null) {
            return null;
        }

        Class datatypeClass = getDatatype().getJavaClass();

        return (Date) (Date.class == datatypeClass
                ? date
                : dateTimeTransformations.transformToType(date, Date.class, null));
    }

    @SuppressWarnings("unchecked")
    protected V convertToModel(Date date) {
        if (date == null) {
            return null;
        }

        Class datatypeClass = getDatatype().getJavaClass();

        return (V) dateTimeTransformations.transformToType(date, datatypeClass, null);
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
        if (datasource == null) {
            setEventProvider(null);
        } else {
            CollectionDsHelper.autoRefreshInvalid(datasource, true);
            setEventProvider(new com.haulmont.cuba.gui.components.calendar.EntityCalendarEventProvider(datasource));
        }
    }

    @Nullable
    @Override
    public CollectionDatasource getDatasource() {
        return (calendarEventProvider instanceof com.haulmont.cuba.gui.components.calendar.EntityCalendarEventProvider)
                ? ((com.haulmont.cuba.gui.components.calendar.EntityCalendarEventProvider) calendarEventProvider)
                .getDatasource()
                : null;
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
    public void setFirstDayOfWeek(Integer dayOfWeek) {
        component.setFirstDayOfWeek(dayOfWeek);
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
            component.setEventProvider(new CalendarEventProviderWrapper<>(calendarEventProvider, this::convertToPresentation));
        } else {
            component.setEventProvider(new CalendarEventProviderWrapper<>(new ListCalendarEventProvider(), this::convertToPresentation));
        }
    }

    @Override
    public Subscription addDateClickListener(Consumer<CalendarDateClickEvent<V>> listener) {
        component.setHandler(this::onDateClick);

        return getEventHub().subscribe(CalendarDateClickEvent.class, (Consumer) listener);
    }

    protected void onDateClick(CalendarComponentEvents.DateClickEvent event) {
        CalendarDateClickEvent<V> calendarDateClickEvent = new CalendarDateClickEvent<>(
                WebCalendar.this,
                convertToModel(event.getDate()));
        publish(CalendarDateClickEvent.class, calendarDateClickEvent);
    }

    @Override
    public void removeDateClickListener(Consumer<CalendarDateClickEvent<V>> listener) {
        unsubscribe(CalendarDateClickEvent.class, (Consumer) listener);

        if (!hasSubscriptions(CalendarDateClickEvent.class)) {
            component.setHandler((CalendarComponentEvents.DateClickHandler) null);
        }
    }

    @Override
    public Subscription addEventClickListener(Consumer<CalendarEventClickEvent<V>> listener) {
        component.setHandler(this::onEventClick);

        return getEventHub().subscribe(CalendarEventClickEvent.class, (Consumer) listener);
    }

    protected void onEventClick(CalendarComponentEvents.EventClick event) {
        com.vaadin.v7.ui.components.calendar.event.CalendarEvent calendarEvent = event.getCalendarEvent();
        if (calendarEvent instanceof CalendarEventWrapper) {
            CalendarEvent calendarEventWrapper = ((CalendarEventWrapper) calendarEvent).getCalendarEvent();
            Entity entity = null;
            if (calendarEventWrapper instanceof EntityCalendarEvent) {
                entity = ((EntityCalendarEvent) calendarEventWrapper).getEntity();
            }

            CalendarEventClickEvent<V> calendarEventClickEvent = new CalendarEventClickEvent<>(
                    WebCalendar.this,
                    calendarEventWrapper,
                    entity);
            publish(CalendarEventClickEvent.class, calendarEventClickEvent);
        }
    }

    @Override
    public void removeEventClickListener(Consumer<CalendarEventClickEvent<V>> listener) {
        unsubscribe(CalendarEventClickEvent.class, (Consumer) listener);

        if (!hasSubscriptions(CalendarEventClickEvent.class)) {
            component.setHandler((CalendarComponentEvents.EventClickHandler) null);
        }
    }

    @Override
    public Subscription addEventResizeListener(Consumer<CalendarEventResizeEvent<V>> listener) {
        component.setHandler(this::onEventResize);

        return getEventHub().subscribe(CalendarEventResizeEvent.class, (Consumer) listener);
    }

    protected void onEventResize(CalendarComponentEvents.EventResize event) {
        com.vaadin.v7.ui.components.calendar.event.CalendarEvent calendarEvent = event.getCalendarEvent();
        if (calendarEvent instanceof CalendarEventWrapper) {
            CalendarEvent calendarEventWrapper = ((CalendarEventWrapper) calendarEvent).getCalendarEvent();
            Entity entity = null;
            if (calendarEventWrapper instanceof EntityCalendarEvent) {
                entity = ((EntityCalendarEvent) calendarEventWrapper).getEntity();
            }

            CalendarEventResizeEvent<V> calendarEventResizeEvent = new CalendarEventResizeEvent<>(
                    WebCalendar.this,
                    ((CalendarEventWrapper) calendarEvent).getCalendarEvent(),
                    convertToModel(event.getNewStart()),
                    convertToModel(event.getNewEnd()),
                    entity);
            publish(CalendarEventResizeEvent.class, calendarEventResizeEvent);
        }
    }

    @Override
    public void removeEventResizeListener(Consumer<CalendarEventResizeEvent<V>> listener) {
        unsubscribe(CalendarEventResizeEvent.class, (Consumer) listener);

        if (!hasSubscriptions(CalendarEventResizeEvent.class)) {
            component.setHandler((CalendarComponentEvents.EventResizeHandler) null);
        }
    }

    @Override
    public Subscription addEventMoveListener(Consumer<CalendarEventMoveEvent<V>> listener) {
        component.setHandler(this::onEventMove);

        return getEventHub().subscribe(CalendarEventMoveEvent.class, (Consumer) listener);
    }

    protected void onEventMove(CalendarComponentEvents.MoveEvent event) {
        com.vaadin.v7.ui.components.calendar.event.CalendarEvent calendarEvent = event.getCalendarEvent();
        CalendarEvent calendarEventWrapper = ((CalendarEventWrapper) calendarEvent).getCalendarEvent();

        Entity entity = null;
        if (calendarEventWrapper instanceof EntityCalendarEvent) {
            entity = ((EntityCalendarEvent) calendarEventWrapper).getEntity();
        }

        CalendarEventMoveEvent<V> calendarEventMoveEvent = new CalendarEventMoveEvent<>(
                WebCalendar.this,
                calendarEventWrapper,
                convertToModel(event.getNewStart()),
                convertToModel(calculateNewEnd(calendarEvent, event.getNewStart())),
                entity);
        publish(CalendarEventMoveEvent.class, calendarEventMoveEvent);
    }

    @Nullable
    protected Date calculateNewEnd(com.vaadin.v7.ui.components.calendar.event.CalendarEvent calendarEvent, Date newStart) {
        Date start = calendarEvent.getStart();
        Date end = calendarEvent.getEnd();
        if (start != null
                && end != null
                && newStart != null) {
            long duration = calendarEvent.getEnd().getTime() - calendarEvent.getStart().getTime();
            return new Date(newStart.getTime() + duration);
        }
        return null;
    }

    @Override
    public void removeEventMoveListener(Consumer<CalendarEventMoveEvent<V>> listener) {
        unsubscribe(CalendarEventMoveEvent.class, (Consumer) listener);

        if (!hasSubscriptions(CalendarEventMoveEvent.class)) {
            component.setHandler((CalendarComponentEvents.EventMoveHandler) null);
        }
    }

    @Override
    public Subscription addWeekClickListener(Consumer<CalendarWeekClickEvent<V>> listener) {
        component.setHandler(this::onWeekClick);

        return getEventHub().subscribe(CalendarWeekClickEvent.class, (Consumer) listener);
    }

    protected void onWeekClick(CalendarComponentEvents.WeekClick event) {
        CalendarWeekClickEvent<V> calendarWeekClickEvent = new CalendarWeekClickEvent<>(
                WebCalendar.this,
                event.getWeek(),
                event.getYear());
        publish(CalendarWeekClickEvent.class, calendarWeekClickEvent);
    }

    @Override
    public void removeWeekClickListener(Consumer<CalendarWeekClickEvent<V>> listener) {
        unsubscribe(CalendarWeekClickEvent.class, (Consumer) listener);

        if (!hasSubscriptions(CalendarWeekClickEvent.class)) {
            component.setHandler((CalendarComponentEvents.WeekClickHandler) null);
        }
    }

    @Override
    public Subscription addForwardClickListener(Consumer<CalendarForwardClickEvent<V>> listener) {
        component.setHandler(this::onForward);

        return getEventHub().subscribe(CalendarForwardClickEvent.class, (Consumer) listener);
    }

    protected void onForward(CalendarComponentEvents.ForwardEvent event) {
        CalendarForwardClickEvent<V> calendarForwardClickEvent =
                new CalendarForwardClickEvent<>(WebCalendar.this);
        publish(CalendarForwardClickEvent.class, calendarForwardClickEvent);
    }

    @Override
    public void removeForwardClickListener(Consumer<CalendarForwardClickEvent<V>> listener) {
        unsubscribe(CalendarForwardClickEvent.class, (Consumer) listener);

        if (!hasSubscriptions(CalendarForwardClickEvent.class)) {
            component.setHandler((CalendarComponentEvents.ForwardHandler) null);
        }
    }

    @Override
    public Subscription addBackwardClickListener(Consumer<CalendarBackwardClickEvent<V>> listener) {
        component.setHandler(this::onBackward);

        return getEventHub().subscribe(CalendarBackwardClickEvent.class, (Consumer) listener);
    }

    protected void onBackward(CalendarComponentEvents.BackwardEvent event) {
        CalendarBackwardClickEvent<V> calendarBackwardClickEvent = new CalendarBackwardClickEvent<>(this);
        publish(CalendarBackwardClickEvent.class, calendarBackwardClickEvent);
    }

    @Override
    public void removeBackwardClickListener(Consumer<CalendarBackwardClickEvent<V>> listener) {
        unsubscribe(CalendarBackwardClickEvent.class, (Consumer) listener);

        if (!hasSubscriptions(CalendarBackwardClickEvent.class)) {
            component.setHandler((CalendarComponentEvents.BackwardHandler) null);
        }
    }

    @Override
    public Subscription addRangeSelectListener(Consumer<CalendarRangeSelectEvent<V>> listener) {
        component.setHandler(this::onRangeSelect);

        return getEventHub().subscribe(CalendarRangeSelectEvent.class, (Consumer) listener);
    }

    protected void onRangeSelect(CalendarComponentEvents.RangeSelectEvent event) {
        CalendarRangeSelectEvent<V> calendarRangeSelectEvent = new CalendarRangeSelectEvent<>(
                this,
                convertToModel(event.getStart()),
                convertToModel(event.getEnd()));
        publish(CalendarRangeSelectEvent.class, calendarRangeSelectEvent);
    }

    @Override
    public void removeRangeSelectListener(Consumer<CalendarRangeSelectEvent<V>> listener) {
        unsubscribe(CalendarRangeSelectEvent.class, (Consumer) listener);

        if (!hasSubscriptions(CalendarRangeSelectEvent.class)) {
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