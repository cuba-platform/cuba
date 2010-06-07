/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 02.06.2010 19:06:02
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jgroups.*;

import javax.annotation.ManagedBean;
import java.io.*;
import java.util.*;

@ManagedBean(ClusterManagerAPI.NAME)
public class ClusterManager implements ClusterManagerAPI, ClusterManagerMBean, AppContext.Listener {

    private static Log log = LogFactory.getLog(ClusterManager.class);

    private Map<String, ClusterListener> listeners = new HashMap<String, ClusterListener>();

    private JChannel channel;

    private View currentView;

    private static final String STATE_MAGIC = "CUBA_STATE";

    public ClusterManager() {
        AppContext.addListener(this);
    }

    public void send(Serializable message) {
        if (channel == null)
            return;

        log.debug("Sending message " + message.getClass() + ": " + message);
        Message msg = new Message(null, null, message);
        try {
            channel.send(msg);
        } catch (ChannelNotConnectedException e) {
            log.error("Send error", e);
        } catch (ChannelClosedException e) {
            log.error("Send error", e);
        }
    }

    public synchronized void addListener(Class messageClass, ClusterListener listener) {
        this.listeners.put(messageClass.getName(), listener);
    }

    public synchronized void removeListener(Class messageClass, ClusterListener listener) {
        listeners.remove(messageClass.getName());
    }

    public void applicationStarted() {
        String enabled = AppContext.getProperty("cuba.cluster.enabled");
        if (Boolean.valueOf(enabled))
            start();
    }

    public void applicationStopped() {
        stop();
    }

    public void start() {
        log.info("Starting cluster");

        try {
            String configName = AppContext.getProperty("cuba.cluster.jgroupsConfig");
            if (configName == null) {
                log.error("No property 'cuba.cluster.jgroupsConfig' specified");
                return;
            }
            File configFile = new File(configName);
            if (!configFile.exists()) {
                log.error("JGroups config file doesn't exist: " + configName);
                return;
            }

            channel = new JChannel(configFile);
            channel.setOpt(Channel.LOCAL, false); // do not receive a copy of our own messages
            channel.setReceiver(new ClusterReceiver());
            channel.connect("cubaCluster");
            channel.getState(null, 5000);
        } catch (Exception e) {
            log.error("Unable to start cluster", e);
            channel = null;
        }
    }

    public void stop() {
        if (channel == null)
            return;

        log.info("Stopping cluster");
        try {
            channel.close();
        } catch (Exception e) {
            log.warn("Error stopping cluster", e);
        }
        channel = null;
        currentView = null;
    }

    public boolean isStarted() {
        return channel != null;
    }

    public boolean isMaster() {
        if (currentView == null || channel == null)
            return true;

        Vector<Address> members = currentView.getMembers();
        if (members.size() == 0)
            return true;

        Address coordinator = members.get(0);
        return coordinator.equals(channel.getAddress());
    }

    public String getCurrentView() {
        return currentView == null ? "" : currentView.toString();
    }

    private class ClusterReceiver implements Receiver {

        public void receive(Message msg) {
            Serializable data = (Serializable) msg.getObject();
            if (data == null)
                return;

            log.debug("Received message " + data.getClass() + ": " + data);
            ClusterListener listener = listeners.get(data.getClass().getName());
            if (listener != null)
                listener.receive(data);
        }

        public void viewAccepted(View new_view) {
            log.info("New cluster view: " + new_view);
            currentView = new_view;
        }

        public byte[] getState() {
            log.debug("Sending state");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(outputStream);
            try {
                Map<String, byte[]> state = new HashMap<String, byte[]>();
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
            } catch (IOException e) {
                log.error("Error sending state", e);
            }

            return outputStream.toByteArray();
        }

        public void suspect(Address suspected_mbr) {
            log.info("Suspected member: " + suspected_mbr);
        }

        public void setState(byte[] state) {
            log.debug("Receiving state");
            if (state.length == 0)
                return;
            
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(state));
            try {
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
            } catch (IOException e) {
                log.error("Error receiving state", e);
            }
        }

        public void block() {
        }
    }
}
