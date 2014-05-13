/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys.vcl;

import com.haulmont.cuba.desktop.App;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Panel with border and collapse/expand button
 *
 * @author artamonov
 * @version $Id$
 */
public class CollapsiblePanel extends JPanel {

    private static final int COLLAPSED_HEIGHT = 10;

    private boolean expanded = true;
    private boolean collapsable = false;

    private JComponent composition;
    private JButton titleBtn;

    private boolean borderVisible = true;

    private Dimension preferredSize;

    private java.util.List<Runnable> postPaintActions = new LinkedList<>();

    public interface CollapseListener extends java.util.EventListener {

        public void collapsed();

        public void expanded();
    }

    private java.util.List<CollapseListener> collapseListeners;

    private Icon expandedIcon;
    private Icon collapsedIcon;

    public CollapsiblePanel(JComponent composition) {
        this.composition = composition;

        titleBtn = new JButton();
        titleBtn.setBorder(BorderFactory.createEmptyBorder(0, 3, 5, 3));
        titleBtn.setVerticalTextPosition(AbstractButton.CENTER);
        titleBtn.setHorizontalTextPosition(AbstractButton.RIGHT);
        titleBtn.setMargin(new Insets(0, 0, 3, 0));

        titleBtn.setFont(getTitleFont());
        titleBtn.setFocusable(false);
        titleBtn.setContentAreaFilled(false);
        titleBtn.setVisible(false);

        titleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isCollapsable())
                    setExpanded(!isExpanded());
            }
        });

        // Add icons
        loadIcons();
        refreshTitleIcon();

        setLayout(new BorderLayout());
        add(titleBtn, BorderLayout.CENTER);
        add(composition, BorderLayout.CENTER);

        setBorder(createBorderImplementation());

        preferredSize = getPreferredSize();

        placeTitleComponent();
    }

    private Font getTitleFont() {
        UIDefaults lafDefaults = UIManager.getLookAndFeelDefaults();
        if (lafDefaults.getFont("CollapsiblePanel.font") != null) { // take it from desktop theme
            return lafDefaults.getFont("CollapsiblePanel.font");
        }
        return lafDefaults.getFont("Panel.font");
    }

    private void refreshTitleIcon() {
        if (collapsable) {
            if (expanded) {
                titleBtn.setIcon(expandedIcon);
            } else {
                titleBtn.setIcon(collapsedIcon);
            }
        } else {
            titleBtn.setIcon(null);
        }
    }

    private void placeTitleComponent() {
        Insets insets = getInsets();
        Rectangle containerRectangle = getBounds();
        if (borderVisible) {
            Rectangle componentRectangle = ((CollapsibleTitledBorder) getBorder()).getComponentRect(containerRectangle, insets);
            titleBtn.setBounds(componentRectangle);
        } else {
            Dimension compD = titleBtn.getPreferredSize();
            Rectangle compR = new Rectangle(20 - insets.left, 0, compD.width, compD.height);
            titleBtn.setBounds(compR);
        }

        int preferredHeight = getPreferredSize().height;
        int preferredWidth = titleBtn.getPreferredSize().width + titleBtn.getBounds().x * 2;

        if (preferredWidth > getPreferredSize().width) {
            setPreferredSize(new Dimension(preferredWidth, preferredHeight));
        }
    }

    private void loadIcons() {
        expandedIcon = App.getInstance().getResources().getIcon("components/groupbox/item-expanded.png");
        collapsedIcon = App.getInstance().getResources().getIcon("components/groupbox/item-collapsed.png");
    }

    private void expandPanel() {
        postPaintActions.add(new Runnable() {
            @Override
            public void run() {
                add(composition, BorderLayout.CENTER);

                titleBtn.setIcon(expandedIcon);
                setPreferredSize(preferredSize);
                updateUI();
            }
        });

        fireExpandListeners();

        repaint();
    }

    private void collapsePanel() {
        postPaintActions.add(new Runnable() {
            @Override
            public void run() {
                int collapsedWidth = getWidth();
                int collapsedHeight = COLLAPSED_HEIGHT;

                preferredSize = getPreferredSize();

                titleBtn.setIcon(collapsedIcon);
                remove(composition);
                setPreferredSize(new Dimension(collapsedWidth, collapsedHeight));
                updateUI();
            }
        });

        fireCollapseListeners();

        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                for (Runnable action : postPaintActions) {
                    action.run();
                }

                postPaintActions.clear();
            }
        });
    }

    public boolean isExpanded() {
        return !collapsable || expanded;
    }

    public void setExpanded(boolean expanded) {
        if (this.expanded != expanded) {
            this.expanded = expanded;
            if (collapsable) {
                if (expanded) {
                    expandPanel();
                } else {
                    collapsePanel();
                }
            }
        }
    }

    public boolean isCollapsable() {
        return collapsable;
    }

    public void setCollapsible(boolean collapsable) {
        if (this.collapsable != collapsable) {
            if (!collapsable && !expanded) {
                setExpanded(true);
            }
            this.collapsable = collapsable;

            revalidate();
            repaint();

            postPaintActions.add(new Runnable() {
                @Override
                public void run() {
                    refreshTitleIcon();
                    placeTitleComponent();

                    titleBtn.validate();
                    titleBtn.repaint();
                }
            });
        }
    }

    public boolean isBorderVisible() {
        return borderVisible;
    }

    public void setBorderVisible(boolean borderVisible) {
        this.borderVisible = borderVisible;
        if (borderVisible) {
            setBorder(createBorderImplementation());
            placeTitleComponent();
        } else {
            if (titleBtn.isVisible()) {
                this.setBorder(new EmptyBorder(12, 2, 2, 2));
            } else {
                this.setBorder(new EmptyBorder(0, 0, 0, 0));
            }
            placeTitleComponent();
        }
        this.repaint();
    }

    private CollapsibleTitledBorder createBorderImplementation() {
        Border border = LineBorder.createGrayLineBorder();
        return new CollapsibleTitledBorder(border, titleBtn);
    }

    public JComponent getComposition() {
        return composition;
    }

    public void setComposition(JComponent composition) {
        this.composition = composition;
        updateUI();
        placeTitleComponent();
    }

    public String getCaption() {
        return titleBtn.getText();
    }

    public void setCaption(String caption) {
        if (StringUtils.isEmpty(caption))
            titleBtn.setVisible(false);
        else
            titleBtn.setVisible(true);

        titleBtn.setText(caption);
        setBorderVisible(borderVisible);
    }

    public void addCollapseListener(CollapseListener collapseListener) {
        if (collapseListeners == null)
            collapseListeners = new ArrayList<>();
        collapseListeners.add(collapseListener);
    }

    public void removeCollapseListener(CollapseListener collapseListener) {
        if (collapseListeners != null) {
            collapseListeners.remove(collapseListener);
            if (collapseListeners.isEmpty())
                collapseListeners = null;
        }
    }

    private void fireExpandListeners() {
        if (collapseListeners != null) {
            for (final CollapsiblePanel.CollapseListener collapseListener : collapseListeners) {
                collapseListener.expanded();
            }
        }
    }

    private void fireCollapseListeners() {
        if (collapseListeners != null) {
            for (final CollapsiblePanel.CollapseListener collapseListener : collapseListeners) {
                collapseListener.collapsed();
            }
        }
    }

    private class CollapsibleTitledBorder extends TitledBorder {

        private JButton titleComponent;

        public CollapsibleTitledBorder(Border border) {
            this(border, null, LEFT, TOP);
        }

        public CollapsibleTitledBorder(@Nullable Border border, JButton titleButton) {
            this(border, titleButton, LEFT, TOP);
        }

        public CollapsibleTitledBorder(@Nullable Border border, @Nullable JButton titleButton, int titleJustification, int titlePosition) {
            super(border, null, titleJustification, titlePosition, null, null);
            this.titleComponent = titleButton;
            if (border == null) {
                this.border = super.getBorder();
            }
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            if (!borderVisible)
                return;

            if (StringUtils.isEmpty(getCaption())) {
                super.paintBorder(c, g, x, y, width, height);
                return;
            }

            Rectangle borderR = new Rectangle(x + EDGE_SPACING, y + EDGE_SPACING, width - (EDGE_SPACING * 2), height - (EDGE_SPACING * 2));
            Insets borderInsets = new Insets(3, 3, 3, 3);

            Rectangle rect = new Rectangle(x, y, width, height);
            Insets insets = getBorderInsets(c);
            Rectangle compR = getComponentRect(rect, insets);
            int diff;
            switch (titlePosition) {
                case ABOVE_TOP:
                    diff = compR.height + TEXT_SPACING;
                    borderR.y += diff;
                    borderR.height -= diff;
                    break;
                case TOP:
                case DEFAULT_POSITION:
                    diff = insets.top / 2 - borderInsets.top - EDGE_SPACING;
                    borderR.y += diff;
                    borderR.height -= diff;
                    break;
                case BELOW_TOP:
                case ABOVE_BOTTOM:
                    break;
                case BOTTOM:
                    diff = insets.bottom / 2 - borderInsets.bottom - EDGE_SPACING;
                    borderR.height -= diff;
                    break;
                case BELOW_BOTTOM:
                    diff = compR.height + TEXT_SPACING;
                    borderR.height -= diff;
                    break;
            }
            border.paintBorder(c, g, borderR.x, borderR.y, borderR.width, borderR.height);
            Color col = g.getColor();
            g.setColor(c.getBackground());
            g.fillRect(compR.x, compR.y, compR.width, compR.height);
            g.setColor(col);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            Insets borderInsets = (Insets) UIManager.getLookAndFeelDefaults().get("CollapsiblePanel.borderInsets");
            if (borderInsets == null) {
                borderInsets = new Insets(10, 3, 3, 3);
            }
            insets.top = EDGE_SPACING + TEXT_SPACING + borderInsets.top;
            insets.right = EDGE_SPACING + TEXT_SPACING + borderInsets.right;
            insets.bottom = EDGE_SPACING + TEXT_SPACING + borderInsets.bottom;
            insets.left = EDGE_SPACING + TEXT_SPACING + borderInsets.left;

            if (c == null || titleComponent == null) {
                return insets;
            }

            int compHeight = titleComponent.getPreferredSize().height;

            switch (titlePosition) {
                case ABOVE_TOP:
                    insets.top += compHeight + TEXT_SPACING;
                    break;
                case TOP:
                case DEFAULT_POSITION:
                    insets.top += (compHeight / 2 - TEXT_SPACING);
                    break;
                case BELOW_TOP:
                    insets.top += compHeight + TEXT_SPACING;
                    break;
                case ABOVE_BOTTOM:
                    insets.bottom += compHeight + TEXT_SPACING;
                    break;
                case BOTTOM:
                    insets.bottom += Math.max(compHeight, borderInsets.bottom) - borderInsets.bottom;
                    break;
                case BELOW_BOTTOM:
                    insets.bottom += compHeight + TEXT_SPACING;
                    break;
            }
            return insets;
        }

        public Rectangle getComponentRect(Rectangle rect, Insets borderInsets) {
            Dimension compD = titleComponent.getPreferredSize();
            Rectangle compR = new Rectangle(0, 0, compD.width, compD.height);
            switch (titlePosition) {
                case ABOVE_TOP:
                    compR.y = EDGE_SPACING;
                    break;
                case TOP:
                case DEFAULT_POSITION:
                    compR.y = EDGE_SPACING + (borderInsets.top - EDGE_SPACING - TEXT_SPACING * 0 - compD.height) / 2;
                    break;
                case BELOW_TOP:
                    compR.y = borderInsets.top - compD.height - TEXT_SPACING;
                    break;
                case ABOVE_BOTTOM:
                    compR.y = rect.height - borderInsets.bottom + TEXT_SPACING;
                    break;
                case BOTTOM:
                    compR.y = rect.height - borderInsets.bottom + TEXT_SPACING + (borderInsets.bottom - EDGE_SPACING - TEXT_SPACING - compD.height) / 2;
                    break;
                case BELOW_BOTTOM:
                    compR.y = rect.height - compD.height - EDGE_SPACING;
                    break;
            }
            switch (titleJustification) {
                case LEFT:
                case DEFAULT_JUSTIFICATION:
                    compR.x = TEXT_INSET_H + borderInsets.left - EDGE_SPACING;
                    break;
                case RIGHT:
                    compR.x = rect.width - borderInsets.right - TEXT_INSET_H - compR.width;
                    break;
                case CENTER:
                    compR.x = (rect.width - compR.width) / 2;
                    break;
            }
            return compR;
        }
    }
}