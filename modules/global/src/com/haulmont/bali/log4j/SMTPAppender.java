/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 01.09.2009 11:59:38
 * $Id: SMTPAppender.java 3028 2010-11-09 08:12:36Z krivopustov $
 */
package com.haulmont.bali.log4j;

import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.TriggeringEventEvaluator;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class SMTPAppender extends org.apache.log4j.net.SMTPAppender {
    private int interval;
    protected Scheduler scheduler;
    protected long sentTime;
    protected long timeout;

    public SMTPAppender() {
        this(new DefaultEvaluator());
    }

    public SMTPAppender(TriggeringEventEvaluator evaluator) {
        this.evaluator = evaluator;
        scheduler = new Scheduler();
        scheduler.setDaemon(true);
        scheduler.start();
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    @Override
    public void activateOptions() {
        super.activateOptions();

        // additionally add to the end of message host name and IP address of machine.
        String hostName = null;
        try {
            hostName = determineHostName();
        } catch (Exception ex) {
            LogLog.error("Unable to determine host name", ex);
        }

        try {
            String compoundSubject = getCompoundSubject(hostName);
            if (compoundSubject.length() > 0) {
                try {
                    msg.setSubject(MimeUtility.encodeText(compoundSubject, "UTF-8", null));
                } catch (UnsupportedEncodingException ex) {
                    LogLog.error("Unable to encode SMTP subject", ex);
                }
            }
        } catch (MessagingException e) {
            LogLog.error("Could not activate SMTPAppender options.", e);
        }
    }

    private String getCompoundSubject(String hostName) {
        StringBuilder sb = new StringBuilder();
        if (getSubject() != null) {
            sb.append(getSubject());
        }
        if (hostName != null && hostName.length() > 0) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(hostName);
        }

        return sb.toString();
    }

    private String determineHostName() {
        InetAddress address;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            LogLog.error("Unable to get local host IP address", e);
            return "<unknown>";
        }
        StringBuilder sb = new StringBuilder()
                .append(address.getHostName())
                .append(" (")
                .append(address.getHostAddress())
                .append(")");
        String res = sb.toString();
        return res;
    }

    public void close() {
        synchronized (this) {
            if (closed) {
                return;
            }
            closed = true;
            if (cb.length() > 0) {
                sendMail();
                // Or
                // sentTime = 0; sendBuffer();
                // for backwad compatibility
                // because SMTPAppender may have subclasses
                // and sendBuffer() may be overridden?
            }
        }
        try {
            scheduler.interrupt();
        } catch (SecurityException e) {
            LogLog.error("Got a SecurityException while interrupting for the scheduler to finish.", e);
        }

        try {
            scheduler.join();
        } catch (InterruptedException e) {
            LogLog.error("Got an InterruptedException while waiting for the scheduler to finish.", e);
        }
        scheduler = null;
    }

    protected void sendBuffer() {
        try {
            if (cb.length() >= cb.getMaxSize()) {
                if (timeout > 0) {    // scheduler is waiting timeout.
                    scheduler.interrupt();
                }
                sendMail();
            } else {
                if (timeout == 0) {    // scheduler is waiting to be scheduled.
                    scheduler.interrupt();
                }
            }
        } catch (Exception e) {
            LogLog.error("Error occured while sending e-mail notification.", e);
        }
    }

    /**
     * Send the contents of the cyclic buffer as an e-mail message.
     */
    protected void sendMail() {

        // Note: this code already owns the monitor for this
        // appender. This frees us from needing to synchronize on 'cb'.
        try {
            MimeBodyPart part = new MimeBodyPart();

            StringBuffer sbuf = new StringBuffer();
            String t = layout.getHeader();
            if (t != null) sbuf.append(t);
            int len = cb.length();
            for (int i = 0; i < len; i++) {
                //sbuf.append(MimeUtility.encodeText(layout.format(cb.get())));
                LoggingEvent event = cb.get();
                sbuf.append(layout.format(event));
                if (layout.ignoresThrowable()) {
                    String[] s = event.getThrowableStrRep();
                    if (s != null) {
                        for (String value : s) {
                            sbuf.append(value);
                            sbuf.append(Layout.LINE_SEP);
                        }
                    }
                }
            }
            t = layout.getFooter();
            if (t != null) sbuf.append(t);
            part.setContent(sbuf.toString(), layout.getContentType());

            Multipart mp = new MimeMultipart();
            mp.addBodyPart(part);
            msg.setContent(mp);

            msg.setSentDate(new Date());
            Transport.send(msg);

            sentTime = System.currentTimeMillis();
        } catch (Exception e) {
            LogLog.error("Error occured while sending e-mail notification.", e);
        }
    }

    protected class Scheduler extends Thread {

        protected Scheduler() {
        }

        public void run() {
            synchronized (SMTPAppender.this) {
                while (!closed) {
                    timeout = sentTime + interval * 1000 - System.currentTimeMillis();
                    if (cb.length() > 0 && timeout < 0) {
                        SMTPAppender.this.sendMail();
                    } else {
                        try {
                            if (cb.length() <= 0 || timeout < 0) {
                                timeout = 0;
                            }
                            SMTPAppender.this.wait(timeout);
                        } catch (InterruptedException e) {
                            // Do Nothing
                        }
                    }
                }
            }
        }
    }
}

class DefaultEvaluator implements TriggeringEventEvaluator {
    public boolean isTriggeringEvent(LoggingEvent event) {
        //noinspection deprecation
        return event.level.isGreaterOrEqual(Level.ERROR);
    }
}
