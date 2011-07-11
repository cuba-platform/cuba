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
import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.uno.XComponentContext;
import ooo.connector.BootstrapSocketConnector;
import ooo.connector.server.OOoServer;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

@ManagedBean(OOOConnector.NAME)
public class OOOConnector {
    public static final String NAME = "report_OOOConnector";

    @Inject
    public void setConfig(ConfigProvider configProvider) {
        String ports = configProvider.doGetConfig(ServerConfig.class).getOpenOfficePorts();
        for (String port : ports.split("\\|")) {
            this.freePorts.add(Integer.valueOf(port));
        }
    }

    private BlockingQueue<Integer> freePorts = new LinkedBlockingDeque<Integer>();
    private ExecutorService executor = Executors.newFixedThreadPool(1);

    //  todo: Think about connection pool - current implementation is too slow
    public OOOConnection createConnection(String openOfficePath) throws BootstrapException {
        List oooOptions = OOoServer.getDefaultOOoOptions();
        oooOptions.add("-nofirststartwizard");
        OOoServer oooServer = new OOoServer(openOfficePath, oooOptions);
        BootstrapSocketConnector bsc = new BootstrapSocketConnector(oooServer);
        Integer port = freePorts.poll();
        if (port != null) {
            XComponentContext xComponentContext = bsc.connect("localhost", port);
            OOOConnection oooConnection = new OOOConnection(xComponentContext, bsc, port);
            return oooConnection;
        } else {
            throw new IllegalStateException("Couldn't get free port from pool");
        }
    }

    public void closeConnection(final OOOConnection connection) {
        if (connection != null) {
            try {
                Future future = executor.submit(new Runnable() {
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

    public ExecutorService getExecutor() {
        return executor;
    }
}
