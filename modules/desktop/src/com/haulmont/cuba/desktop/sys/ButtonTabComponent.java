/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;

/**
 * Component to be used as tabComponent. <br/>
 * Contains a JLabel to show the text and a JButton to close the tab it belongs to.
 *
 * @author krivopustov
 * @version $Id$
 */
public class ButtonTabComponent extends JPanel {

    protected JButton tabButton;
    protected JButton detachButton;

    protected Messages messages = AppBeans.get(Messages.NAME);

    public interface CloseListener {
        void onTabClose(int tabIndex);
    }

    public interface DetachListener {
        void onDetach(int tabIndex);
    }

    private final JTabbedPane pane;
    private CloseListener closeListener;
    private DetachListener detachListener;
    private JLabel titleLabel;
    private boolean closeable;
    private boolean detachable;

    public ButtonTabComponent(final JTabbedPane pane, boolean closeable, boolean detachable,
                              CloseListener closeListener, DetachListener detachListener) {
        //unset default FlowLayout' gaps
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        if (pane == null) {
            throw new NullPointerException("TabbedPane is null");
        }
        this.closeable = closeable;
        this.detachable = detachable;
        this.detachListener = detachListener;
        this.closeListener = closeListener;

        this.pane = pane;
        setOpaque(false);

        //make JLabel read titles from JTabbedPane
        titleLabel = new JLabel() {
            public String getText() {
                int i = pane.indexOfTabComponent(ButtonTabComponent.this);
                if (i != -1) {
                    return pane.getTitleAt(i);
                }
                return null;
            }
        };

        add(titleLabel);
        //add more space between the label and the button
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        detachButton = new DetachButton();
        if (detachable) {
            add(detachButton);
        }
        //tab button
        tabButton = new TabButton();
        if (closeable) {
            add(tabButton);
        }
        //add more space to the top of the component
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));

        this.closeListener = closeListener;
    }

    public String getCaption() {
        return titleLabel.getText();
    }

    public void setCaption(String caption) {
        int i = pane.indexOfTabComponent(this);
        if (i != -1) {
            pane.setTitleAt(i, caption);
        }
        titleLabel.setText(caption);
        tabButton.revalidate();
        tabButton.repaint();
        detachButton.revalidate();
        detachButton.repaint();
    }

    public boolean isCloseable() {
        return closeable;
    }

    public void setCloseable(boolean closeable) {
        if (this.closeable != closeable) {
            this.closeable = closeable;
            if (closeable) {
                add(tabButton, detachable ? 2 : 1);
            } else {
                remove(tabButton);
            }
        }
    }

    public boolean isDetachable() {
        return detachable;
    }

    public void setDetachable(boolean detachable) {
        if (this.detachable != detachable) {
            this.detachable = detachable;
            if (detachable) {
                add(detachButton, 1);
            } else {
                remove(detachButton);
            }
        }
    }

    public JLabel getTitleLabel() {
        return titleLabel;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        detachButton.setEnabled(enabled);
        tabButton.setEnabled(enabled);
    }

    private class TabButton extends JButton implements ActionListener {
        public TabButton() {
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText(messages.getMainMessage("closeTabToolTip"));
            //Make the button looks the same for all Laf's
            setUI(new BasicButtonUI());
            //Make it transparent
            setContentAreaFilled(false);
            //No need to be focusable
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            //Making nice rollover effect
            //we use the same listener for all buttons
            addMouseListener(buttonMouseListener);
            addAncestorListener(ancestorListener);

            setRolloverEnabled(true);
            //Close the proper tab by clicking the button
            addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            int i = pane.indexOfTabComponent(ButtonTabComponent.this);
            if (i != -1) {
                if (closeListener != null) {
                    closeListener.onTabClose(i);
                } else
                    pane.remove(i);
            }
        }

        //we don't want to update UI for this button
        public void updateUI() {
        }

        //paint the cross
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            doPaintContent(g);
        }

        protected void doPaintContent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            //shift the image for pressed buttons
            if (isEnabled() && getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            if (getModel().isRollover()) {
                g2.setColor(Color.RED);
            }
            int delta = 6;
            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
            g2.dispose();
        }
    }

    private class DetachButton extends TabButton {

        public DetachButton() {
            setToolTipText(messages.getMainMessage("detach"));
        }

        public void actionPerformed(ActionEvent e) {
            int i = pane.indexOfTabComponent(ButtonTabComponent.this);
            if (i != -1) {
                if (detachListener != null) {
                    detachListener.onDetach(i);
                } else
                    pane.remove(i);
            }
        }

        protected void doPaintContent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            //shift the image for pressed buttons
            if (isEnabled() && getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(1));
            g2.setColor(Color.BLACK);
            if (getModel().isRollover()) {
                g2.setColor(Color.RED);
            }
            int delta = 6;
            //back
            g2.drawLine(delta, delta - 2, delta + delta, delta - 2);
            g2.drawLine(delta + delta, delta - 2, delta + delta, delta - 2 + delta);
            //front
            g2.drawRect(delta - 2, delta, delta, delta);
            g2.dispose();
        }
    }

    private final static MouseListener buttonMouseListener = new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton && component.isEnabled()) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(true);
            }
        }

        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(false);
            }
        }
    };

    private final AncestorListener ancestorListener = new AncestorListener() {
        @Override
        public void ancestorAdded(AncestorEvent event) {
            detachButton.setBorderPainted(false);
            tabButton.setBorderPainted(false);
        }

        @Override
        public void ancestorRemoved(AncestorEvent event) {
        }

        @Override
        public void ancestorMoved(AncestorEvent event) {
        }
    };
}
