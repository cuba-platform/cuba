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

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.ScreenProfilerEvent;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.ScreenProfilerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

@Service(ScreenProfilerService.NAME)
public class ScreenProfilerServiceBean implements ScreenProfilerService {

    protected final Logger log = LoggerFactory.getLogger(ScreenProfilerService.class);

    @Inject
    protected Metadata metadata;
    @Inject
    protected Configuration configuration;
    @Inject
    protected ClusterManagerAPI clusterManager;
    protected ScreenProfilerConfig screenProfilerConfig;

    protected volatile boolean enabled;
    protected volatile Set<UUID> userIds;
    protected volatile long timeThreshold;

    protected Queue<ProfilerEventData> events = new LinkedBlockingQueue<>();

    protected static class ProfilerEventData implements Serializable {
        private static final long serialVersionUID = 7129347477902559917L;

        protected Date eventTs;
        protected String screen;
        protected UUID userId;
        protected String user;
        protected Integer clientTime;
        protected Integer serverTime;
        protected Integer networkTime;
        protected Integer totalTime;
    }

    protected static class ProfilingConfigurationMsg implements Serializable {
        private static final long serialVersionUID = -5158096823700332627L;

        protected boolean enabled;
        protected Set<UUID> userIds;
        protected long timeThreshold;

        public ProfilingConfigurationMsg(boolean enabled) {
            this(enabled, null, 0);
        }

        public ProfilingConfigurationMsg(boolean enabled, Set<UUID> userIds, long timeThreshold) {
            this.enabled = enabled;
            this.userIds = userIds;
            this.timeThreshold = timeThreshold;
        }
    }

    protected static class SendEventsDataMsg implements Serializable {
        private static final long serialVersionUID = 8809076288639486951L;

        protected List<ProfilerEventData> eventsData;

        public SendEventsDataMsg(List<ProfilerEventData> eventsData) {
            this.eventsData = eventsData;
        }
    }

    @PostConstruct
    public void init() {
        screenProfilerConfig = configuration.getConfig(ScreenProfilerConfig.class);
        clusterManager.addListener(ProfilingConfigurationMsg.class,  new ProfilingConfigurationClusterListener());
        clusterManager.addListener(SendEventsDataMsg.class, new EventsClusterListener());
    }

    @Override
    public boolean isProfilingEnabled() {
        return enabled;
    }

    @Override
    public boolean isProfilingEnabledForUser(UUID userId) {
        if (isProfilingEnabled()) {
            Set<UUID> localUserIds = userIds;
            return localUserIds == null || localUserIds.size() == 0 || localUserIds.contains(userId);
        }
        return false;
    }

    @Override
    public void enableProfiling(Set<UUID> userIds, long timeThreshold) {
        this.userIds = userIds;
        this.timeThreshold = timeThreshold;
        this.enabled = true;
        clusterManager.send(new ProfilingConfigurationMsg(true, userIds, timeThreshold));
    }

    @Override
    public void disableProfiling() {
        this.enabled = false;
        clusterManager.send(new ProfilingConfigurationMsg(false));
    }

    @Override
    public void saveEvents(List<ScreenProfilerEvent> events) {
        if (events != null) {
            List<ProfilerEventData> eventsData = new ArrayList<>(events.size());
            for (ScreenProfilerEvent event : events) {
                if (needStoreEvent(event)) {
                    ProfilerEventData eventData = extractEventData(event);
                    addEventData(eventData);
                    eventsData.add(eventData);
                }
            }
            if (eventsData.size() != 0) {
                clusterManager.send(new SendEventsDataMsg(eventsData));
            }
        }
    }

    @Override
    public List<ScreenProfilerEvent> getProfilerEvents() {
        Object[] eventsData = events.toArray();
        List<ScreenProfilerEvent> result = new ArrayList<>(eventsData.length);
        for (Object it : events.toArray()) {
            ProfilerEventData eventData = (ProfilerEventData) it;
            ScreenProfilerEvent event = metadata.create(ScreenProfilerEvent.class);
            event.setEventTs(eventData.eventTs);
            event.setScreen(eventData.screen);
            event.setUser(eventData.user);
            event.setClientTime(eventData.clientTime);
            event.setServerTime(eventData.serverTime);
            event.setNetworkTime(eventData.networkTime);
            event.setTotalTime(eventData.totalTime);
            result.add(event);
        }
        return result;
    }

    @Override
    public void clearEvents() {
        events.clear();
    }

    protected ProfilerEventData extractEventData(ScreenProfilerEvent event) {
        ProfilerEventData data = new ProfilerEventData();
        data.eventTs = event.getEventTs();
        data.user = event.getUser();
        data.screen = event.getScreen();
        data.clientTime = event.getClientTime();
        data.serverTime = event.getServerTime();
        data.networkTime = event.getNetworkTime();
        data.totalTime = event.getTotalTime();

        return data;
    }

    protected boolean needStoreEvent(ScreenProfilerEvent event) {
        return event.getTotalTime() > timeThreshold;
    }

    protected void addEventData(ProfilerEventData eventData) {
        if (events.size() >= screenProfilerConfig.getStorageSize()) {
            events.remove();
        }
        events.add(eventData);
    }

    protected class EventsClusterListener implements ClusterListener<SendEventsDataMsg> {
        @Override
        public void receive(SendEventsDataMsg message) {
            if (message.eventsData != null) {
                for (ProfilerEventData eventData : message.eventsData) {
                    addEventData(eventData);
                }
            }
        }

        @Override
        public byte[] getState() {
            if (events.isEmpty())
                return new byte[0];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                Object[] eventsData = events.toArray();
                oos.writeInt(eventsData.length);
                for (Object it : eventsData) {
                    oos.writeObject(it);
                }
            } catch (IOException e) {
                log.error("Error serializing events", e);
            }
            return bos.toByteArray();
        }

        @Override
        public void setState(byte[] state) {
            if (state == null || state.length == 0)
                return;

            ByteArrayInputStream bis = new ByteArrayInputStream(state);
            try {
                ObjectInputStream ois = new ObjectInputStream(bis);
                int size = ois.readInt();
                for (int i = 0; i < size; i++) {
                    ProfilerEventData eventData = (ProfilerEventData) ois.readObject();
                    addEventData(eventData);
                }
            } catch (IOException | ClassNotFoundException e) {
                log.error("Error receiving events state", e);
            }
        }
    }

    protected class ProfilingConfigurationClusterListener implements ClusterListener<ProfilingConfigurationMsg> {
        @Override
        public void receive(ProfilingConfigurationMsg message) {
            if (message.enabled) {
                userIds = message.userIds;
                timeThreshold = message.timeThreshold;
            }
            enabled = message.enabled;
        }

        @Override
        public byte[] getState() {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(new ProfilingConfigurationMsg(enabled, userIds, timeThreshold));
            } catch (IOException e) {
                log.error("Error serializing configuration", e);
            }
            return bos.toByteArray();
        }

        @Override
        public void setState(byte[] state) {
            if (state == null || state.length == 0)
                return;
            ByteArrayInputStream bis = new ByteArrayInputStream(state);
            try {
                ObjectInputStream ois = new ObjectInputStream(bis);
                ProfilingConfigurationMsg msg = (ProfilingConfigurationMsg) ois.readObject();
                receive(msg);
            } catch (IOException | ClassNotFoundException e) {
                log.error("Error receiving configuration state", e);
            }
        }
    }
}
