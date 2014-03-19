/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.bali.log4j;

import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.OptionHandler;
import org.apache.log4j.spi.TriggeringEventEvaluator;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Appender that sends error reports via email.
 *
 * @author abramov
 * @version $Id$
 */
public class SMTPAppender extends org.apache.log4j.net.SMTPAppender {

    private static final Message.RecipientType[] RECIPIENT_TYPES = new Message.RecipientType[]
            {Message.RecipientType.BCC, Message.RecipientType.CC, Message.RecipientType.TO};

    private int interval;
    protected Scheduler scheduler;
    protected long sentTime;
    protected long timeout;
    protected Session session;

    protected ExecutorService senderExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        }
    });

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
        session = createSession();
        msg = new MimeMessage(session);

        // additionally add to the end of message host name and IP address of machine.
        String hostName = null;
        try {
            hostName = determineHostName();
        } catch (Exception ex) {
            LogLog.error("Unable to determine host name", ex);
        }

        try {
            addressMessage(msg);
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

        if (evaluator instanceof OptionHandler) {
            ((OptionHandler) evaluator).activateOptions();
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
        return String.format("%s (%s)", address.getHostName(), address.getHostAddress());
    }

    @Override
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
            senderExecutor.shutdown();
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

    @Override
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

            StringBuilder sb = new StringBuilder();
            String t = layout.getHeader();
            if (t != null) sb.append(t);
            int len = cb.length();
            for (int i = 0; i < len; i++) {
                //sbuf.append(MimeUtility.encodeText(layout.format(cb.get())));
                LoggingEvent event = cb.get();
                sb.append(layout.format(event));
                if (layout.ignoresThrowable()) {
                    String[] s = event.getThrowableStrRep();
                    if (s != null) {
                        for (String value : s) {
                            sb.append(value);
                            sb.append(Layout.LINE_SEP);
                        }
                    }
                }
            }
            t = layout.getFooter();
            if (t != null) sb.append(t);
            part.setContent(sb.toString(), layout.getContentType() + ";charset=UTF-8");

            Multipart mp = new MimeMultipart();
            mp.addBodyPart(part);

            // copy message properties
            Message messageToSend = cloneMessage(msg);

            // prepare message to be sent
            messageToSend.setSentDate(new Date());
            messageToSend.setContent(mp);

            // send email asynchronously
            asyncSendEmail(messageToSend);

            sentTime = System.currentTimeMillis();
        } catch (Exception e) {
            LogLog.error("Error occured while sending e-mail notification.", e);
        }
    }

    protected Message cloneMessage(Message message) throws MessagingException {
        Message messageToSend = new MimeMessage(session);
        for (Address address : message.getFrom()) {
            messageToSend.setFrom(address);
        }

        for (Message.RecipientType recipientType : RECIPIENT_TYPES) {
            messageToSend.setRecipients(recipientType, message.getRecipients(recipientType));
        }

        messageToSend.setSubject(message.getSubject());
        messageToSend.setReplyTo(message.getReplyTo());
        return messageToSend;
    }

    protected void asyncSendEmail(final Message messageToSend) {
        senderExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Transport.send(messageToSend);
                } catch (Exception e) {
                    LogLog.error("Error occured while sending e-mail notification.", e);
                }
            }
        });
    }

    protected class Scheduler extends Thread {

        @Override
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
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
            }
        }
    }
}

class DefaultEvaluator implements TriggeringEventEvaluator {
    @Override
    public boolean isTriggeringEvent(LoggingEvent event) {
        //noinspection deprecation
        return event.level.isGreaterOrEqual(Level.ERROR);
    }
}