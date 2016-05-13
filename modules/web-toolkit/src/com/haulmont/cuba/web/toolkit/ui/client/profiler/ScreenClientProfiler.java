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

package com.haulmont.cuba.web.toolkit.ui.client.profiler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.vaadin.client.ValueMap;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ScreenClientProfiler {

    protected int flushEventsCount;
    protected int flushTimeout;
    protected boolean enabled;
    protected String profilerMarker;
    protected long lastFlushTime = System.currentTimeMillis();
    protected FlushEventsListener flushEventsListener;
    protected Map<String, ScreenProfilerClientEvent> events = new HashMap<String, ScreenProfilerClientEvent>();
    protected Timer timer = new Timer() {
        @Override
        public void run() {
            scheduleFlush();
        }
    };

    public interface FlushEventsListener {
        void flush(ScreenProfilerClientEvent[] events);
    }

    private static ScreenClientProfiler instance = GWT.create(ScreenClientProfiler.class);

    public static ScreenClientProfiler getInstance() {
        return instance;
    }

    public static String getProfilerMarkerFromJson(ValueMap json) {
        if (json.containsKey("profilerMarker")) {
            return json.getString("profilerMarker");
        } else {
            return null;
        }
    }

    public static long getEventTsFromJson(ValueMap json) {
        if (json.containsKey("profilerEventTs")) {
            try {
                return Long.valueOf(json.getString("profilerEventTs"));
            } catch (NumberFormatException e) {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public static int getServerTimeFromJson(ValueMap json) {
        if (json.containsKey("profilerServerTime")) {
            return json.getInt("profilerServerTime");
        } else {
            return 0;
        }
    }

    public String getProfilerMarker() {
        return profilerMarker;
    }

    public void setProfilerMarker(String profilerMarker) {
        this.profilerMarker = profilerMarker;
    }

    public void clearProfilerMarker() {
        profilerMarker = null;
    }

    public void registerClientTime(String profilerMarker, int time) {
        ScreenProfilerClientEvent event = getEvent(profilerMarker);
        event.setClientTime(event.getClientTime() + time);
    }

    public void registerServerTime(String profilerMarker, int time) {
        ScreenProfilerClientEvent event = getEvent(profilerMarker);
        event.setServerTime(event.getServerTime() + time);
    }

    public void registerNetworkTime(String profilerMarker, int time) {
        ScreenProfilerClientEvent event = getEvent(profilerMarker);
        event.setNetworkTime(event.getNetworkTime() + time);
    }

    public void registerEventTs(String profilerMarker, long ts) {
        ScreenProfilerClientEvent event = getEvent(profilerMarker);
        if (event.getEventTs() == 0) {
            event.setEventTs(ts);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        boolean oldEnabled = this.enabled;
        this.enabled = enabled;
        if (!oldEnabled && enabled) {
            timer.scheduleRepeating(flushTimeout / 2 * 1000);
        }
    }

    public void setFlushEventsCount(int flushEventsCount) {
        this.flushEventsCount = flushEventsCount;
    }

    public void setFlushTimeout(int flushTimeout) {
        this.flushTimeout = flushTimeout;
    }

    public void addListener(FlushEventsListener flushListener) {
        this.flushEventsListener = flushListener;
    }

    protected ScreenProfilerClientEvent getEvent(String profilerMarker) {
        ScreenProfilerClientEvent event = events.get(profilerMarker);
        if (event == null) {
            event = new ScreenProfilerClientEvent();
            event.setProfilerMarker(profilerMarker);
            events.put(profilerMarker, event);
        }
        return event;
    }

    protected void scheduleFlush() {
        if (isEnabled()) {
            if (events.size() > 0) {
                if (needFlush()) {
                    flushEvents();
                    lastFlushTime = System.currentTimeMillis();
                    events.clear();
                    timer.cancel();
                    setEnabled(false);
                }
            }
        }
    }

    protected boolean needFlush() {
        if (events.size() > flushEventsCount) {
            return true;
        } else {
            int wait = ((int) (System.currentTimeMillis() - lastFlushTime)) / 1000;
            return wait >= flushTimeout;
        }
    }

    protected void flushEvents() {
        if (flushEventsListener != null) {
            flushEventsListener.flush(events.values().toArray(new ScreenProfilerClientEvent[events.size()]));
        }
    }
}
