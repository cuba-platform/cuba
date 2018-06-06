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

package com.haulmont.cuba.desktop.sys;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.desktop.DesktopConfig;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.desktop.sys.vcl.ToolTipButton;
import com.haulmont.cuba.gui.components.KeyCombination;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;

/**
 * Class that encapsulates displaying of tooltips for all components.
 */
public class DesktopToolTipManager extends MouseAdapter {

    protected static final int CLOSE_TIME = 500;
    protected static final int SHOW_TIME = 1000;
    protected static final int DEFAULT_HORIZONTAL_INDENTATION = 15;
    protected static final int DEFAULT_VERTICAL_INDENTATION = 15;

    protected boolean tooltipShowing = false;

    protected JToolTip toolTipWindow;
    protected Popup window;
    protected JComponent component;

    protected Timer showTimer = new Timer(SHOW_TIME, null);
    protected Timer closeTimer;

    protected Configuration configuration = AppBeans.get(Configuration.NAME);

    protected MouseListener componentMouseListener = new ComponentMouseListener();
    protected KeyListener fieldKeyListener = new FieldKeyListener();
    protected ActionListener btnActionListener = new ButtonClickListener();

    private static DesktopToolTipManager instance;

    /**
     * Return singleton instance of DesktopToolTipManager for application.
     *
     * @return instance of DesktopToolTipManager
     */
    public static DesktopToolTipManager getInstance() {
        if (instance == null) {
            instance = new DesktopToolTipManager();
        }
        return instance;
    }

    protected DesktopToolTipManager() {
        closeTimer = new Timer(CLOSE_TIME, null);
        closeTimer.setRepeats(false);
        closeTimer.addActionListener(e -> {
            if (window != null) {
                window.hide();
                window = null;
                tooltipShowing = false;
                toolTipWindow.removeMouseListener(DesktopToolTipManager.this);
                component.removeMouseListener(DesktopToolTipManager.this);
            }
        });

        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            protected MouseEvent event;

            @Override
            public void eventDispatched(AWTEvent e) {
                if (!tooltipShowing) {
                    return;
                }
                event = (MouseEvent) e;
                if (event.getID() == MouseEvent.MOUSE_PRESSED) {
                    if (event.getComponent() != null && event.getComponent().isShowing()) {
                        if (!isPointInComponent(event.getLocationOnScreen(), toolTipWindow))
                            hideTooltip();
                    } else
                        hideTooltip();
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK);
    }

    protected boolean isPointInComponent(Point point, JComponent component) {
        if (!component.isShowing())
            return false;

        Point componentLocation = component.getLocationOnScreen();
        Rectangle bounds = component.getBounds();
        return (((point.x >= componentLocation.x) && (point.x <= componentLocation.x + bounds.width)) &&
                (point.y >= componentLocation.y) && (point.y <= componentLocation.y + bounds.height));
    }

    /**
     * Register tooltip for component.
     * The tooltip is displayed when a user either presses F1 on a focused component or hovers over it.
     * ToolTip text is taken from {@link javax.swing.JComponent#getToolTipText()}.
     *
     * @param component component to register
     */
    public void registerTooltip(final JComponent component) {
        component.removeKeyListener(fieldKeyListener);
        component.addKeyListener(fieldKeyListener);

        component.removeMouseListener(componentMouseListener);
        component.addMouseListener(componentMouseListener);
    }

    /**
     * Register tooltip for {@link javax.swing.AbstractButton}.
     * Tooltip is displayed when the user hovers over a button
     * ToolTip text is taken from {@link javax.swing.JComponent#getToolTipText()}.
     *
     * @param btn Button to register
     */
    public void registerTooltip(final AbstractButton btn) {
        btn.removeMouseListener(componentMouseListener);
        btn.addMouseListener(componentMouseListener);
    }

    /**
     * Register tooltip for {@link javax.swing.JLabel}.
     * Tooltip is displayed when the user hovers over a label
     * ToolTip text is taken from {@link javax.swing.JComponent#getToolTipText()}.
     *
     * @param lbl Label to register
     */
    public void registerTooltip(final JLabel lbl) {
        lbl.removeMouseListener(componentMouseListener);
        lbl.addMouseListener(componentMouseListener);
    }

    /**
     * Register tooltip for ToolTipButton.
     * Tooltip is displayed when the user presses the button
     * ToolTip text is taken from {@link javax.swing.JComponent#getToolTipText()} .
     *
     * @param btn Button to register
     */
    public void registerTooltip(final ToolTipButton btn) {
        btn.removeActionListener(btnActionListener);
        btn.addActionListener(btnActionListener);
    }

    protected void hideTooltip() {
        closeTimer.stop();
        if (window != null) {
            window.hide();
            window = null;
            tooltipShowing = false;
            toolTipWindow.removeMouseListener(DesktopToolTipManager.this);
            component.removeMouseListener(DesktopToolTipManager.this);
        }
    }

    protected void showTooltip(JComponent field, String text) {
        if (!field.isShowing())
            return;

        if (StringUtils.isEmpty(text)) {
            return;
        }

        PointerInfo pointerInfo = MouseInfo.getPointerInfo();
        if (pointerInfo == null) {
            return;
        }

        if (toolTipWindow != null) {
            hideTooltip();
        }

        component = field;

        final JToolTip toolTip = new CubaToolTip();
        toolTip.setTipText(text);

        // Location to display tooltip
        Point location = getToolTipLocation(pointerInfo, toolTip.getTipText());

        final Popup tooltipContainer = PopupFactory.getSharedInstance()
                .getPopup(field, toolTip, location.x, location.y);
        tooltipContainer.show();

        window = tooltipContainer;
        toolTipWindow = toolTip;

        tooltipShowing = true;
        if (!(field instanceof ToolTipButton)) {
            toolTip.addMouseListener(this);
            field.addMouseListener(this);
        }
    }

    protected int getMaxTooltipWidth() {
        UIDefaults lafDefaults = UIManager.getLookAndFeelDefaults();
        int maxTooltipWidth = lafDefaults.getInt("Tooltip.maxWidth");
        if (maxTooltipWidth == 0) {
            maxTooltipWidth = DesktopComponentsHelper.TOOLTIP_WIDTH;
        }
        return maxTooltipWidth;
    }

    protected Point getToolTipLocation(PointerInfo pointerInfo, String text) {
        Point mouseLocation = pointerInfo.getLocation();
        Rectangle bounds = getDeviceBounds(pointerInfo.getDevice());

        // Location on the current screen (suitable if there is more than one screen)
        Point currentScreenMouseLocation = getCurrentScreenMouseLocation(mouseLocation, bounds);

        // Location to display tooltip
        Point location = new Point(mouseLocation);

        Dimension dimension = DesktopComponentsHelper.measureHtmlText(text);

        location.x += getIndentation(bounds.width, dimension.width,
                currentScreenMouseLocation.x, DEFAULT_HORIZONTAL_INDENTATION);
        location.y += getIndentation(bounds.height, dimension.height,
                currentScreenMouseLocation.y, DEFAULT_VERTICAL_INDENTATION);

        return location;
    }

    protected int getIndentation(int screenSize, int textSize, int mouseLocation, int defaultIndentation) {
        if ((mouseLocation + textSize) > screenSize) {
            return -1 * (textSize + defaultIndentation);
        } else {
            return defaultIndentation;
        }
    }

    protected Rectangle getDeviceBounds(GraphicsDevice device) {
        GraphicsConfiguration gc = device.getDefaultConfiguration();
        return gc.getBounds();
    }

    protected Point getCurrentScreenMouseLocation(Point mouseLocation, Rectangle bounds) {
        Point point = new Point(mouseLocation);
        // Subtract the x/y position of the device
        point.x -= bounds.x;
        point.y -= bounds.y;
        // Clip negative values...
        if (point.x < 0) {
            point.x *= -1;
        }
        if (point.y < 0) {
            point.y *= -1;
        }
        return point;
    }

    protected class CubaToolTip extends JToolTip {
        @Override
        public void setTipText(String tipText) {
            int maxTooltipWidth = getMaxTooltipWidth();
            int actualWidth = getActualTextWidth(tipText, getFont());

            if (actualWidth < maxTooltipWidth) {
                tipText = "<html>" + tipText + "</html>";
            } else {
                tipText = "<html><body width=\"" + maxTooltipWidth + "px\">" + tipText + "</body></html>";
            }

            super.setTipText(tipText);
        }

        protected int getActualTextWidth(String tipText, Font font) {
            FontMetrics fontMetrics = StyleContext.getDefaultStyleContext().getFontMetrics(font);
            return SwingUtilities.computeStringWidth(fontMetrics, tipText);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        closeTimer.start();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (closeTimer.isRunning()) {
            closeTimer.stop();
        }
    }

    protected class ComponentMouseListener extends MouseAdapter {

        protected JComponent cmp;

        {
            showTimer.setRepeats(false);
            showTimer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!tooltipShowing)
                        showTooltip(cmp, cmp.getToolTipText());
                }
            });
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (window != null) {
                if (e.getSource() != component && e.getSource() != toolTipWindow) {
                    hideTooltip();
                    cmp = (JComponent) e.getSource();
                    showTimer.start();
                    return;
                }
            }
            if (!tooltipShowing) {
                cmp = (JComponent) e.getSource();
                showTimer.start();
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (!tooltipShowing) {
                if (showTimer.isRunning()) {
                    showTimer.stop();
                }
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            showTimer.stop();
            hideTooltip();
        }
    }

    protected class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            String showTooltipShortcut = configuration.getConfig(DesktopConfig.class).getShowTooltipShortcut();
            KeyStroke keyStroke = DesktopComponentsHelper
                    .convertKeyCombination(KeyCombination.create(showTooltipShortcut));

            if (KeyStroke.getKeyStrokeForEvent(e).equals(keyStroke)) {
                hideTooltip();
                JComponent field = (JComponent) e.getSource();
                showTooltip(field, field.getToolTipText());
            } else {
                if (tooltipShowing) {
                    hideTooltip();
                }
            }
        }
    }

    protected class ButtonClickListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (component == e.getSource() && tooltipShowing) {
                return;
            }
            if (tooltipShowing) {
                hideTooltip();
            }
            showTooltip((JComponent) e.getSource(), ((JButton) e.getSource()).getToolTipText());
        }
    }
}
