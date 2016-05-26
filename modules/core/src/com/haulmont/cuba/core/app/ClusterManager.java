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
 *
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.serialization.SerializationSupport;
import org.apache.commons.io.IOUtils;
import org.jgroups.*;
import org.jgroups.conf.XmlConfigurator;
import org.jgroups.jmx.JmxConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.management.MBeanServer;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Standard implementation of middleware clustering based on JGroups.
 *
 */
@Component(ClusterManagerAPI.NAME)
public class ClusterManager implements ClusterManagerAPI, AppContext.Listener {

    protected Logger log = LoggerFactory.getLogger(ClusterManager.class);

    protected Map<String, ClusterListener> listeners = new HashMap<>();

    protected JChannel channel;

    protected View currentView;

    protected ThreadPoolExecutor executor;

    @Inject
    protected Resources resources;

    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected ClusterConfig clusterConfig;

    protected ThreadLocal<Boolean> forceSyncSending = new ThreadLocal<>();

    protected static final String STATE_MAGIC = "CUBA_STATE";

    public ClusterManager() {
        AppContext.addListener(this);
    }

    @PostConstruct
    public void init() {
        int nThreads = clusterConfig.getClusterMessageSendingThreadPoolSize();
        executor = new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(clusterConfig.getClusterMessageSendingQueueCapacity()),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        SendMessageRunnable sendMessageRunnable = (SendMessageRunnable) r;
                        log.info("Queue capacity is exceeded. Message {}, {}", sendMessageRunnable.message, sendMessageRunnable.message.getClass());
                    }
                });
    }

    @Override
    public void send(final Serializable message) {
        if (channel == null)
            return;

        Boolean sync = forceSyncSending.get();
        if (sync != null && sync) {
            internalSend(message, true);
        } else {
            log.trace("Submitting message {}, {} to send asynchronously", message, message.getClass());
            executor.execute(new SendMessageRunnable(message));
        }
    }

    @Override
    public void sendSync(Serializable message) {
        if (channel == null)
            return;

        internalSend(message, true);
    }

    protected void internalSend(Serializable message, boolean sync) {
        log.debug("Sending message " + message.getClass() + ": " + message);
        byte[] bytes = SerializationSupport.serialize(message);
        Message msg = new Message(null, null, bytes);
        if (sync) {
            msg.setFlag(Message.Flag.RSVP);
        }
        try {
            channel.send(msg);
        } catch (Exception e) {
            log.error("Error sending message", e);
        }
    }

    @Override
    public boolean getSyncSendingForCurrentThread() {
        return forceSyncSending.get() == null ? false : forceSyncSending.get();
    }

    @Override
    public void setSyncSendingForCurrentThread(boolean sync) {
        if (sync) {
            forceSyncSending.set(true);
        } else {
            forceSyncSending.remove();
        }
    }

    @Override
    public synchronized void addListener(Class messageClass, ClusterListener listener) {
        this.listeners.put(messageClass.getName(), listener);
    }

    @Override
    public synchronized void removeListener(Class messageClass, ClusterListener listener) {
        listeners.remove(messageClass.getName());
    }

    @Override
    public void applicationStarted() {
        // Cluster starts in AppContextLoader.afterInitAppContext()
    }

    @Override
    public void applicationStopped() {
        executor.shutdown();
        stop();
    }

    @Override
    public void start() {
        log.info("Starting cluster");

        InputStream stream = null;
        try {
            String configName = AppContext.getProperty("cuba.cluster.jgroupsConfig");
            if (configName == null) {
                log.error("No property 'cuba.cluster.jgroupsConfig' specified");
                return;
            }
            stream = resources.getResource(configName).getInputStream();

            channel = new JChannel(XmlConfigurator.getInstance(stream));
            channel.setDiscardOwnMessages(true); // do not receive a copy of our own messages
            channel.setReceiver(new ClusterReceiver());
            channel.connect(getClusterName());
            channel.getState(null, clusterConfig.getStateReceiveTimeout());
            registerJmxBeans();
        } catch (Exception e) {
            channel = null;
            throw new RuntimeException("Error starting cluster", e);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    protected void registerJmxBeans() {
        try {
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            JmxConfigurator.registerChannel(channel, mBeanServer, getMBeanDomain(), getClusterName(), true);
        } catch (Exception e) {
            log.error("Failed to register channel in jmx", e);
        }
    }

    protected void unregisterJmxBeans() {
        try {
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            JmxConfigurator.unregisterChannel(channel, mBeanServer, getMBeanDomain(), getClusterName());
        } catch (Exception e) {
            log.error("Failed to unregister channel in jmx", e);
        }
    }

    protected String getClusterName() {
        return "cubaCluster";
    }

    protected String getMBeanDomain() {
        return globalConfig.getWebContextName() + ".jgroups";
    }

    public int getActiveThreadsCount() {
        return executor.getActiveCount();
    }

    @Override
    public int getMessagesCount() {
        return executor.getQueue().size();
    }

    @Override
    public void stop() {
        if (channel == null)
            return;

        log.info("Stopping cluster");
        unregisterJmxBeans();
        try {
            channel.close();
        } catch (Exception e) {
            log.warn("Error stopping cluster", e);
        }
        channel = null;
        currentView = null;
    }

    @Override
    public boolean isStarted() {
        return channel != null;
    }

    @Override
    public boolean isMaster() {
        if (currentView == null || channel == null)
            return true;

        List<Address> members = currentView.getMembers();
        if (members.size() == 0)
            return true;

        Address coordinator = members.get(0);
        return coordinator.equals(channel.getAddress());
    }

    @Override
    public String getCurrentView() {
        return currentView == null ? "" : currentView.toString();
    }

    protected class ClusterReceiver implements Receiver {

        @Override
        public void receive(Message msg) {
            byte[] bytes = msg.getBuffer();
            if (bytes == null) {
                log.debug("Null buffer received");
                return;
            }

            Serializable data = (Serializable) SerializationSupport.deserialize(bytes);
            log.debug("Received message " + data.getClass() + ": " + data);
            ClusterListener listener = listeners.get(data.getClass().getName());
            if (listener != null)
                listener.receive(data);
        }

        @Override
        public void viewAccepted(View new_view) {
            log.info("New cluster view: " + new_view);
            currentView = new_view;
        }

        @Override
        public void getState(OutputStream output) {
            log.debug("Sending state");
            try (DataOutputStream out = new DataOutputStream(output)) {
                Map<String, byte[]> state = new HashMap<>();
                for (Map.Entry<String, ClusterListener> entry : listeners.entrySet()) {
                    byte[] data = entry.getValue().getState();
                    if (data != null && data.length > 0) {
                        state.put(entry.getKey(), data);
                    }
                }

                if (state.size() > 0) {
                    out.writeUTF(STATE_MAGIC);
                    out.writeInt(state.size());
                    for (Map.Entry<String, byte[]> entry : state.entrySet()) {
                        log.debug("Sending state: " + entry.getKey() + " (" + entry.getValue().length + " bytes)");
                        out.writeUTF(entry.getKey());
                        out.writeInt(entry.getValue().length);
                        out.write(entry.getValue());
                    }
                }
            } catch (Exception e) {
                log.error("Error sending state", e);
            }
        }

        @Override
        public void suspect(Address suspected_mbr) {
            log.info("Suspected member: " + suspected_mbr);
        }

        @Override
        public void setState(InputStream input) {
            log.debug("Receiving state");

            try (DataInputStream in = new DataInputStream(input)) {
                if (input.available() == 0)
                    return;

                String magic = in.readUTF();
                if (!STATE_MAGIC.equals(magic)) {
                    log.debug("Invalid magic in state received");
                    return;
                }
                int count = in.readInt();
                for (int i = 0; i < count; i++) {
                    String name = in.readUTF();
                    int len = in.readInt();
                    log.debug("Receiving state: " + name + " (" + len + " bytes)");
                    byte[] data = new byte[len];
                    int c = in.read(data);
                    if (c != len) {
                        log.error("Error receiving state: invalid data length");
                        return;
                    }

                    ClusterListener listener = listeners.get(name);
                    if (listener != null)
                        listener.setState(data);
                }
            } catch (Exception e) {
                log.error("Error receiving state", e);
            }
        }

        @Override
        public void block() {
        }

        @Override
        public void unblock() {
        }
    }

    protected class SendMessageRunnable implements Runnable {
        protected Serializable message;

        public SendMessageRunnable(Serializable message) {
            this.message = message;
        }

        @Override
        public void run() {
            internalSend(message, false);
        }
    }
}