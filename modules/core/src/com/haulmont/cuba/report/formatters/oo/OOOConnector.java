/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Vasiliy Fontanenko
 * Created: 12.10.2010 19:21:08
 *
 * $Id$
 */
package com.haulmont.cuba.report.formatters.oo;

import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.Configuration;
import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.uno.XComponentContext;
import ooo.connector.BootstrapSocketConnector;
import ooo.connector.server.OOoServer;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.*;

@ManagedBean(OOOConnectorAPI.NAME)
public class OOOConnector implements OOOConnectorAPI, OOOConnectorMBean {

    @Inject
    public void setConfig(Configuration configuration) {
        String ports = configuration.getConfig(ServerConfig.class).getOpenOfficePorts();
        for (String port : ports.split("\\|")) {
            this.freePorts.add(Integer.valueOf(port));
        }
    }

    private final BlockingQueue<Integer> freePorts = new LinkedBlockingDeque<Integer>();
    private ExecutorService executor = Executors.newFixedThreadPool(1);

    //  todo: Think about connection pool - current implementation is too slow
    @Override
    public OOOConnection createConnection(String openOfficePath) throws BootstrapException {
        List oooOptions = OOoServer.getDefaultOOoOptions();
        oooOptions.add("-nofirststartwizard");
        OOoServer oooServer = new OOoServer(openOfficePath, oooOptions);
        BootstrapSocketConnector bsc = new BootstrapSocketConnector(oooServer);

        Integer port;
        synchronized (freePorts) {
            port = freePorts.poll();
        }

        if (port != null) {

            XComponentContext xComponentContext;
            try {
                xComponentContext = bsc.connect("localhost", port);
            } catch (BootstrapException ex) {
                // put port back to queue
                freePorts.add(port);
                throw ex;
            }

            return new OOOConnection(xComponentContext, bsc, port);
        } else {
            throw new IllegalStateException("Couldn't get free port from pool");
        }
    }

    @Override
    public void closeConnection(final OOOConnection connection) {
        if (connection != null) {
            try {
                Future future = executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        BootstrapSocketConnector bsc = connection.getBsc();
                        if (bsc != null) {
                            bsc.disconnect();
                        }
                    }
                });

                future.get(20, TimeUnit.SECONDS);
            } catch (Exception e) {
                //do nothing
            } finally {
                freePorts.add(connection.getPort());
            }
        }
    }

    @Override
    public ExecutorService getExecutor() {
        return executor;
    }

    @Override
    public String getAvailablePorts() {
        StringBuilder builder = new StringBuilder();

        Integer[] ports;
        synchronized (freePorts) {
            ports = freePorts.toArray(new Integer[freePorts.size()]);
        }

        if ((ports != null) && (ports.length > 0)) {
            for (Integer port : ports) {
                if (port != null)
                    builder.append(Integer.toString(port)).append(" ");
            }
        } else
            builder.append("No available ports");

        return builder.toString();
    }

    @Override
    public void hardReloadAccessPorts() {
        String ports = ConfigProvider.getConfig(ServerConfig.class).getOpenOfficePorts();
        synchronized (freePorts) {
            freePorts.clear();
            for (String port : ports.split("\\|")) {
                this.freePorts.add(Integer.valueOf(port));
            }
        }
    }
}
