package com.vaadin.incubator.dashlayout.client.ui;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.incubator.dashlayout.client.util.css.CSSUtil;
import com.vaadin.terminal.gwt.client.BrowserInfo;
import com.vaadin.terminal.gwt.client.RenderInformation.FloatSize;
import com.vaadin.terminal.gwt.client.RenderInformation.Size;
import com.vaadin.terminal.gwt.client.RenderSpace;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.Util;
import com.vaadin.terminal.gwt.client.ui.AlignmentInfo;

/**
 * ChildCell represents one slot in the layout.
 */
public class ChildCell {
    // Size is the allocated space for this widget
    private RenderSpace space = null;
    // Widget size is the natural minimum size of the widget (including margins)
    private final Size widgetSize = new Size(-1, -1);
    // Margins that are specified for the widget in CSS
    private int[] widgetMargin = { 0, 0, 0, 0 };
    private final Widget widget;
    private final VDashLayout parent;
    private FloatSize relativeSize;
    private AlignmentInfo alignment = new AlignmentInfo(1);
    private int alignmentOffsetLeft = 0;
    private int alignmentOffsetTop = 0;
    private float expandRatio = -1;
    private int surplus = 0;

    public ChildCell(Widget child, VDashLayout parent) {
        widget = child;
        this.parent = parent;
        // Floats affect how computedStyle is calculated in browsers
        setFloat("left");
    }

    public void reset(boolean resetSize) {
        if (resetSize) {
            widget.setWidth("");
            widget.setHeight("");
        }
        widget.getElement().getStyle().setPropertyPx("marginTop",
                widgetMargin[0]);
        widget.getElement().getStyle().setPropertyPx("marginRight",
                widgetMargin[1]);
        widget.getElement().getStyle().setPropertyPx("marginBottom",
                widgetMargin[2]);
        widget.getElement().getStyle().setPropertyPx("marginLeft",
                widgetMargin[3]);
        alignmentOffsetLeft = 0;
        alignmentOffsetTop = 0;
        surplus = 0;
    }

    public void reAlign() {
        int left = 0;
        int top = 0;
        if (alignment != null) {
            int horizSpace = space.getWidth() - widgetSize.getWidth();
            if (horizSpace < 0) {
                horizSpace = 0;
            }
            if (alignment.isHorizontalCenter()) {
                left = horizSpace / 2;
            } else if (alignment.isRight()) {
                left = horizSpace;
            }
            int vertSpace = space.getHeight() - widgetSize.getHeight();
            if (vertSpace < 0) {
                vertSpace = 0;
            }
            if (alignment.isVerticalCenter()) {
                top = vertSpace / 2;
            } else if (alignment.isBottom()) {
                top = vertSpace;
            }
        }
        if (parent.getChildren().indexOf(widget) > 0) {
            if (parent.isHorizontal()) {
                left += parent.getSpacing();
            } else {
                top += parent.getSpacing();
            }
        }
        alignmentOffsetLeft = left;
        alignmentOffsetTop = top;

        left += widgetMargin[3];
        widget.getElement().getStyle().setPropertyPx("marginLeft", left);

        top += widgetMargin[0];
        widget.getElement().getStyle().setPropertyPx("marginTop", top);

        // TODO odd fix, try to isolate why IE needs these, why the initial
        // settings aren't enough
        if (BrowserInfo.get().isIE6()) {
            widget.getElement().getStyle().setProperty("display", "inline");
        }

        // Need to remove spacing, it isn't actually reserving space from this
        // layout slot
        if (parent.getChildren().indexOf(widget) > 0) {
            if (parent.isHorizontal()) {
                left -= parent.getSpacing();
            } else {
                top -= parent.getSpacing();
            }
        }

        // Accommodate whole layout cell
        if (parent.isHorizontal()) {
            final int widgetWidth = widgetSize.getWidth() - widgetMargin[1]
                    - widgetMargin[3];
            if (widgetWidth > -1) {
                final int accommodated = widgetWidth + left + widgetMargin[1];
                if (space.getWidth() > accommodated) {
                    surplus = space.getWidth() - accommodated;
                    widget.getElement().getStyle().setPropertyPx("marginRight",
                            surplus + widgetMargin[1]);
                }
            }
        } else {
            final int widgetHeight = widgetSize.getHeight() - widgetMargin[0]
                    - widgetMargin[2];
            if (widgetHeight > -1) {
                final int accommodated = widgetHeight + top + widgetMargin[2];
                if (space.getHeight() > accommodated) {
                    surplus = space.getHeight() - accommodated;
                    widget.getElement().getStyle().setPropertyPx(
                            "marginBottom", surplus + widgetMargin[2]);
                }
            }
        }

        // Needed to shake IE7 a bit, some components might get clipped
        // otherwise (e.g. Panel)
        if (BrowserInfo.get().isIE7()) {
            widget.getElement().getStyle().setProperty("zoom", "");
            DeferredCommand.addCommand(new Command() {
                public void execute() {
                    widget.getElement().getStyle().setProperty("zoom", "1");
                }
            });
        }
    }

    public void setExpandRatio(float i) {
        expandRatio = i;
    }

    public void setAlignment(int i) {
        alignment = new AlignmentInfo(i);
    }

    private void setFloat(String floatValue) {
        Util.setFloat(widget.getElement(), floatValue);
        if (!parent.isHorizontal()) {
            widget.getElement().getStyle().setProperty("clear", floatValue);
        }
        if (BrowserInfo.get().isIE()) {
            widget.getElement().getStyle().setProperty("zoom", "1");
            widget.getElement().getStyle().setProperty("display", "inline");
        }
    }

    public void updateSizeInfo(UIDL uidl) {
        if (uidl.hasAttribute("cached")) {
            return;
        }
        float relativeWidth = -1;
        if (uidl.hasAttribute("width")) {
            final String w = uidl.getStringAttribute("width");
            if (w.endsWith("%")) {
                relativeWidth = Float
                        .parseFloat(w.substring(0, w.length() - 1));
            }
        }
        float relativeHeight = -1;
        if (uidl.hasAttribute("height")) {
            final String h = uidl.getStringAttribute("height");
            if (h.endsWith("%")) {
                relativeHeight = Float.parseFloat(h
                        .substring(0, h.length() - 1));
            }
        }
        relativeSize = new FloatSize(relativeWidth, relativeHeight);
    }

    public void updateSpace() {
        if (widget.isAttached()) {
            int width = -1;
            int height = -1;

            float ratio = 0;
            if ((parent.isHorizontal() && !parent.undefWidth)
                    || !parent.undefHeight) {
                float compoundRatio = parent.getCompoundRatio();
                if (compoundRatio > 0 && expandRatio > 0) {
                    ratio = (expandRatio / compoundRatio);
                } else if (compoundRatio < 0) {
                    // No expand ratios set to any component, set equal
                    // ratios to every widget
                    ratio = 1.0f / parent.getCells().size();
                }
            }

            if (parent.isHorizontal()) {
                height = parent.height;
                if (ratio != 0) {
                    width = parent.width;
                    if (isRelativeWidth()) {
                        width = (int) ((width - parent.getConsumedSpace()) * ratio);
                    } else {
                        width = (int) (widgetSize.getWidth() + (width - parent
                                .getConsumedSpace())
                                * ratio);
                    }
                } else if (isRelativeWidth()) {
                    width = 0;
                }

            } else {
                width = parent.width;
                if (ratio != 0) {
                    height = parent.height;
                    if (isRelativeHeight()) {
                        height = (int) ((height - parent.getConsumedSpace()) * ratio);
                    } else {
                        height = (int) (widgetSize.getHeight() + (height - parent
                                .getConsumedSpace())
                                * ratio);
                    }
                } else if (isRelativeHeight()) {
                    height = 0;
                }
            }

            space = new RenderSpace(width, height);
        }
    }

    public boolean isRelativeWidth() {
        return relativeSize.getWidth() > -1;
    }

    public boolean isRelativeHeight() {
        return relativeSize.getHeight() > -1;
    }

    public boolean hasRelativeSize() {
        return isRelativeHeight() || isRelativeWidth();
    }

    public boolean isRelativeSizeInParentOrientation() {
        return parent.isHorizontal() ? isRelativeWidth() : isRelativeHeight();
    }

    public Widget getWidget() {
        return widget;
    }

    public void updateWidgetMarginAndSize() {
        if (widget.isAttached()) {
            updateWidgetMargin();
            updateWidgetSize();
        }
    }

    public void updateWidgetSize() {
        if (widget.isAttached()) {
            int width = Util.getRequiredWidth(widget.getElement())
                    + widgetMargin[1] + widgetMargin[3];
            int height = Util.getRequiredHeight(widget.getElement())
                    + widgetMargin[0] + widgetMargin[2];

            widgetSize.setWidth(width);
            widgetSize.setHeight(height);
        }
    }

    public void updateWidgetMargin() {
        if (widget.isAttached()) {
            widgetMargin = CSSUtil.collectMargin(widget.getElement());
            widgetMargin[3] -= alignmentOffsetLeft;
            widgetMargin[0] -= alignmentOffsetTop;
            if (parent.isHorizontal()) {
                widgetMargin[1] -= surplus;
            } else {
                widgetMargin[2] -= surplus;
            }
        }
    }

    public RenderSpace getRenderSpace() {
        return space;
    }

    public Size getWidgetSize() {
        return widgetSize;
    }

    public int getMaxSizeInParentOrientation() {
        final int spaceSize = parent.isHorizontal() ? space.getWidth() : space
                .getHeight();
        final int widgetS = parent.isHorizontal() ? widgetSize.getWidth()
                : widgetSize.getHeight();
        return Math.max(spaceSize, widgetS);
    }

    public int getMaxSizeInNonParentOrientation() {
        final int spaceSize = parent.isHorizontal() ? space.getHeight() : space
                .getWidth();
        final int widgetS = parent.isHorizontal() ? widgetSize.getHeight()
                : widgetSize.getWidth();
        return Math.max(spaceSize, widgetS);
    }

    public RenderSpace getSpaceSansMargins() {
        return new RenderSpace(space.getWidth() - widgetMargin[1]
                - widgetMargin[3], space.getHeight() - widgetMargin[0]
                - widgetMargin[2]);
    }

    public boolean updateAfterOtherCells() {
        if (parent.isHorizontal()) {
            return isRelativeHeight()
                    || (!parent.undefWidth && (isRelativeWidth() || expandRatio != -1));
        } else {
            return isRelativeWidth()
                    || (!parent.undefHeight && (isRelativeHeight() || expandRatio != -1));
        }
    }

    public float getExpandRatio() {
        return expandRatio;
    }
}