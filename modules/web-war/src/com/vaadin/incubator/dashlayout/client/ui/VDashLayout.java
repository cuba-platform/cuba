package com.vaadin.incubator.dashlayout.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.DomEvent.Type;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;
import com.vaadin.incubator.dashlayout.client.util.css.CSSRule;
import com.vaadin.incubator.dashlayout.client.util.css.CSSUtil;
import com.vaadin.terminal.gwt.client.*;
import com.vaadin.terminal.gwt.client.ui.LayoutClickEventHandler;
import com.vaadin.terminal.gwt.client.ui.VMarginInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class VDashLayout extends ComplexPanel implements Container {

    public static final String CLASSNAME = "v-dashlayout";

    public static final String CLICK_EVENT_IDENTIFIER = "click";

    private LayoutClickEventHandler clickEventHandler = new LayoutClickEventHandler(
            this, CLICK_EVENT_IDENTIFIER) {

        @Override
        protected Paintable getChildComponent(Element element) {
            return getComponent(element);
        }

        @Override
        protected <H extends EventHandler> HandlerRegistration registerHandler(
                H handler, Type<H> type) {
            return addDomHandler(handler, type);
        }
    };

    private Paintable getComponent(Element element) {
        return Util.getChildPaintableForElement(client, VDashLayout.this,
                element);
    }

    protected boolean horizontal = false;

    /**
     * A logical mapping from widget to its virtual layout cell.
     */
    protected Map<Widget, ChildCell> widgetToCell = new HashMap<Widget, ChildCell>();

    /**
     * All array values are in the following order: top, right, bottom, left.
     */

    /* Current margin values */
    protected int[] margin = { 0, 0, 0, 0 };

    /* Current padding values */
    protected int[] padding = { 0, 0, 0, 0 };

    /* Current border sizes */
    protected int[] border = { 0, 0, 0, 0 };

    /*
     * Current inner size (excluding margins, border and padding) in pixels
     */
    protected int width = -1;
    protected int height = -1;

    protected boolean isRendering;
    protected boolean sizeHasChangedDuringRendering = false;

    // Is the layout size undefined, i.e. defined by the contained components
    protected boolean undefWidth = false;
    protected boolean undefHeight = false;

    protected ApplicationConnection client;

    protected String lastStyleName;

    protected VMarginInfo marginInfo;

    protected float compoundRatio = -1;

    protected int consumedSpace = 0;

    protected boolean useSpacing = true;

    protected HashMap<String, Integer> layoutDetails = new HashMap<String, Integer>();

    public VDashLayout() {
        super();
        setElement(Document.get().createDivElement());
        setStylePrimaryName(CLASSNAME);
        getElement().getStyle().setProperty("float", "left");
        getElement().getStyle().setProperty("cssFloat", "left");
        getElement().getStyle().setProperty("styleFloat", "left");
        getElement().getStyle().setProperty("display", "inline");
        if (BrowserInfo.get().isIE()) {
            getElement().getStyle().setProperty("zoom", "1");
        }
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    @Override
    public WidgetCollection getChildren() {
        return super.getChildren();
    }

    public int getSpacing() {
        if (layoutDetails.containsKey("spacing")) {
            return layoutDetails.get("spacing");
        } else {
            return 0;
        }
    }

    public float getCompoundRatio() {
        return compoundRatio;
    }

    public Map<Widget, ChildCell> getCells() {
        return widgetToCell;
    }

    public int getConsumedSpace() {
        return consumedSpace;
    }

    public void updateActualSize() {
        width = CSSUtil
                .parsePixel(CSSUtil.getStyleValue(getElement(), "width"));
        height = CSSUtil.parsePixel(CSSUtil.getStyleValue(getElement(),
                "height"));
    }

    private void updateMargins() {
        margin = CSSUtil.collectMargin(getElement());
        if (marginInfo.hasTop()) {
            getElement().getStyle().setProperty("marginTop", "");
        } else {
            getElement().getStyle().setProperty("marginTop", "0");
            margin[0] = 0;
        }
        if (marginInfo.hasRight()) {
            getElement().getStyle().setProperty("marginRight", "");
        } else {
            getElement().getStyle().setProperty("marginRight", "0");
            margin[1] = 0;
        }
        if (marginInfo.hasBottom()) {
            getElement().getStyle().setProperty("marginBottom", "");
        } else {
            getElement().getStyle().setProperty("marginBottom", "0");
            margin[2] = 0;
        }
        if (marginInfo.hasLeft()) {
            getElement().getStyle().setProperty("marginLeft", "");
        } else {
            getElement().getStyle().setProperty("marginLeft", "0");
            margin[3] = 0;
        }
    }

    private void updateDynamicSizeInfo(UIDL uidl) {
        String w = uidl.hasAttribute("width") ? uidl
                .getStringAttribute("width") : "";
        undefWidth = w.equals("");
        String h = uidl.hasAttribute("height") ? uidl
                .getStringAttribute("height") : "";
        undefHeight = h.equals("");
    }

    private void measureLayoutDetails() {
        margin = CSSUtil.collectMargin(getElement());
        padding = CSSUtil.collectPadding(getElement());
        border = CSSUtil.collectBorder(getElement());

        final String[] styles = getElement().getClassName().split(" ");
        for (int i = styles.length - 1; i >= 0; i--) {
            String style = styles[i];
            final CSSRule r = new CSSRule("#" + style + "-details", false);
            if (useSpacing) {
                // We misuse "letter-spacing" property for our layout
                // spacing value
                final String spacing = r.getPropertyValue("letterSpacing");
                if (spacing != null && !layoutDetails.containsKey("spacing")) {
                    layoutDetails.put("spacing", CSSUtil.parsePixel(spacing));
                }
            }
            final String minWidth = r.getPropertyValue("minWidth");
            if (minWidth != null && !layoutDetails.containsKey("minWidth")) {
                layoutDetails.put("minWidth", CSSUtil.parsePixel(minWidth));
            }
            final String maxWidth = r.getPropertyValue("maxWidth");
            if (maxWidth != null && !layoutDetails.containsKey("maxWidth")) {
                layoutDetails.put("maxWidth", CSSUtil.parsePixel(maxWidth));
            }
            final String minHeight = r.getPropertyValue("minHeight");
            if (minHeight != null && !layoutDetails.containsKey("minHeight")) {
                layoutDetails.put("minHeight", CSSUtil.parsePixel(minHeight));
            }
            final String maxHeight = r.getPropertyValue("maxHeight");
            if (maxHeight != null && !layoutDetails.containsKey("maxHeight")) {
                layoutDetails.put("maxHeight", CSSUtil.parsePixel(maxHeight));
            }
        }
    }

    @Override
    public void setStyleName(String styleName) {
        super.setStyleName(styleName);

        if (isAttached() && !styleName.equals(lastStyleName)) {
            measureLayoutDetails();
            // updateChildCells(null, false);
            lastStyleName = styleName;
        }

    }

    public RenderSpace getAllocatedSpace(Widget child) {
        ChildCell cell = widgetToCell.get(child);
        if (child instanceof VDashLayout) {
            // Layout handles it's margins internally
            return cell.getRenderSpace();
        }
        return cell.getSpaceSansMargins();
    }

    public boolean hasChildComponent(Widget component) {
        return getChildren().contains(component);
    }

    public void replaceChildComponent(Widget oldComponent, Widget newComponent) {
        if (!hasChildComponent(oldComponent)) {
            return;
        }
        getChildren().insert(newComponent, getChildren().indexOf(oldComponent));
        getChildren().remove(oldComponent);
        getElement().insertBefore(newComponent.getElement(),
                oldComponent.getElement());
        getElement().removeChild(oldComponent.getElement());
        orphan(oldComponent);
        adopt(newComponent);
        widgetToCell.remove(oldComponent);
        ChildCell cell = new ChildCell(newComponent, this);
        widgetToCell.put(newComponent, cell);
    }

    public void updateCaption(Paintable component, UIDL uidl) {
        // TODO Use this same class to display component captions
        // TODO I need to make this class usable without the server-side
    }

    /**
     * Only pixel values are accepted.
     */
    @Override
    public void setWidth(String w) {
        // Assume pixel values are always passed from ApplicationConnection

        int oldW = width;

        String toBeWidth = "";

        if (w != null && w != "") {
            int newWidth = CSSUtil.parsePixel(w) - margin[1] - margin[3]
                    - border[1] - border[3] - padding[1] - padding[3];
            if (newWidth < 0) {
                newWidth = 0;
            }
            width = newWidth;
            toBeWidth = newWidth + "px";
        } else {
            width = -1;
        }

        final Integer minWidth = layoutDetails.get("minWidth");
        if (minWidth != null && width < minWidth) {
            width = minWidth.intValue();
            toBeWidth = width + "px";
        }

        final Integer maxWidth = layoutDetails.get("maxWidth");
        if (maxWidth != null && width > maxWidth) {
            width = maxWidth.intValue();
            toBeWidth = width + "px";
        }

        super.setWidth(toBeWidth);

        updateActualSize();

        if (width != oldW) {
            if (isRendering) {
                sizeHasChangedDuringRendering = true;
            }
            updateLayout(false);
            client.runDescendentsLayout(this);
        }
    }

    /**
     * Only pixel values are accepted.
     */
    @Override
    public void setHeight(String h) {
        // Assume pixel values are always passed from ApplicationConnection

        int oldH = height;

        String toBeHeight = "";

        if (h != null && h != "") {
            int newHeight = CSSUtil.parsePixel(h) - margin[0] - margin[2]
                    - border[0] - border[2] - padding[0] - padding[2];
            if (newHeight < 0) {
                newHeight = 0;
            }
            height = newHeight;
            toBeHeight = newHeight + "px";
        } else {
            height = -1;
        }

        final Integer minHeight = layoutDetails.get("minHeight");
        if (minHeight != null && height < minHeight) {
            height = minHeight.intValue();
            toBeHeight = height + "px";
        }

        final Integer maxHeight = layoutDetails.get("maxHeight");
        if (maxHeight != null && height > maxHeight) {
            height = maxHeight.intValue();
            toBeHeight = height + "px";
        }

        super.setHeight(toBeHeight);

        updateActualSize();

        if (height != oldH) {
            if (isRendering) {
                sizeHasChangedDuringRendering = true;
            }
            updateLayout(false);
            client.runDescendentsLayout(this);
        }
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        this.client = client;

        // Only non-cached UIDL messages can introduce changes
        if (uidl.getBooleanAttribute("cached")) {
            return;
        }

        useSpacing = uidl.hasAttribute("spacing");

        // Margins need to be set before any size calculations
        int bitMask = uidl.getIntAttribute("margins");
        if (marginInfo == null || marginInfo.getBitMask() != bitMask) {
            marginInfo = new VMarginInfo(bitMask);
            updateMargins();
        } else if (marginInfo.getBitMask() == bitMask) {
            // interpret as requestRepaint requested. Could be just an update on
            // margins or expand ratios, but we need to update the margin,
            // border and padding values in this case
            measureLayoutDetails();
            // Otherwise margins and spacing are measured in
            // #setStyleName(String), inside client.updateComponent
        }

        if (client.updateComponent(this, uidl, true)) {
            return;
        }

        clickEventHandler.handleEventHandlerRegistration(client);

        horizontal = uidl.hasAttribute("horizontal");

        isRendering = true;

        updateDynamicSizeInfo(uidl);

        // Iterate through Paintables in UIDL, add new ones and remove any
        // old ones.
        final int uidlCount = uidl.getChildCount();
        final int layoutCount = getChildren().size();
        int uidlPos = 0;
        int layoutPos = 0;

        // Additional info that needs to be passed to components
        final ValueMap alignments = uidl.getMapAttribute("alignments");
        final ValueMap expandRatios = uidl.getMapAttribute("expandRatios");

        if (expandRatios.getKeySet().size() > 0) {
            compoundRatio = 0;
        } else {
            compoundRatio = -1;
        }

        for (; (uidlPos < uidlCount || layoutPos < layoutCount); uidlPos++) {

            final UIDL childUIDL = (uidlPos < uidlCount) ? uidl
                    .getChildUIDL(uidlPos) : null;

            final Widget uidlWidget = childUIDL != null ? (Widget) client
                    .getPaintable(childUIDL) : null;

            final Widget layoutWidget = (layoutPos < layoutCount) ? getChildren()
                    .get(layoutPos)
                    : null;

            // layout position is past UIDL position, no need to continue
            // (remaining old widgets are removed after this loop)
            if (uidlPos >= uidlCount && layoutPos < layoutCount) {
                break;
            }

            // Old widget in old location, all OK
            if (uidlWidget == layoutWidget) {
                layoutPos++;
            }

            // Widget is either new or has changed place

            else if (getChildren().contains(uidlWidget)) {
                // An old component has changed place
                if (getChildren().indexOf(uidlWidget) != layoutPos) {
                    // Detach from old position child.
                    uidlWidget.removeFromParent();

                    // Logical attach.
                    getChildren().insert(uidlWidget, layoutPos);

                    getElement().insertBefore(uidlWidget.getElement(),
                            getElement().getChildNodes().getItem(layoutPos));

                    adopt(uidlWidget);
                    layoutPos++;
                }
            } else {
                // A completely new widget is inserted to this position
                ChildCell cell = new ChildCell(uidlWidget, this);
                widgetToCell.put(uidlWidget, cell);

                // Logical attach
                getChildren().insert(uidlWidget, layoutPos);

                // Avoid inserts (they are slower than appends)
                if (layoutPos < getChildren().size() - 1) {
                    getElement().insertBefore(uidlWidget.getElement(),
                            getElement().getChildNodes().getItem(layoutPos));
                } else {
                    getElement().appendChild(uidlWidget.getElement());
                }
                layoutPos++;
                // Adopt
                adopt(uidlWidget);
            }
            ChildCell cell = widgetToCell.get(uidlWidget);
            cell.updateSizeInfo(childUIDL);
            cell.updateSpace();

            ((Paintable) uidlWidget).updateFromUIDL(childUIDL, client);

            if (alignments.containsKey(childUIDL.getId())) {
                cell.setAlignment(alignments.getInt(childUIDL.getId()));
            }
            if (expandRatios.containsKey(childUIDL.getId())) {
                final float ratio = expandRatios.getInt(childUIDL.getId());
                compoundRatio += ratio;
                cell.setExpandRatio(ratio);
            }

        } // All UIDL widgets painted

        // All remaining widgets are removed
        removeChildrenAfter(layoutPos);

        updateLayout(false);

        isRendering = false;

        if (sizeHasChangedDuringRendering) {
            updateLayout(true);
        }

    }

    protected void removeChildrenAfter(int pos) {
        int toRemove = getChildren().size() - pos;
        while (toRemove-- > 0) {
            Widget child = getChildren().get(pos);
            widgetToCell.remove(child);
            remove(child);
            client.unregisterPaintable((Paintable) child);
        }
    }

    protected void updateLayout(boolean reset) {
        // Hide overflows for the time of layouting
        getElement().getStyle().setProperty("overflow", "hidden");

        if (undefWidth) {
            super.setWidth("");
        }
        if (undefHeight) {
            super.setHeight("");
        }
        if (undefWidth || undefHeight) {
            updateActualSize();
        }

        consumedSpace = 0;
        int totalSize = 0;
        int biggestSize = 0;

        if (useSpacing) {
            totalSize += getSpacing() * (getChildren().size() - 1);
        }

        final ArrayList<ChildCell> updateAfter = new ArrayList<ChildCell>();
        for (Widget w : getChildren()) {
            final ChildCell cell = getCells().get(w);
            if (reset) {
                // Reset widget size as well (true)
                cell.reset(true);
            }
            if (cell.updateAfterOtherCells()) {
                updateAfter.add(cell);
                if (!cell.isRelativeSizeInParentOrientation()) {
                    cell.updateWidgetMarginAndSize();
                    totalSize += isHorizontal() ? cell.getWidgetSize()
                            .getWidth() : cell.getWidgetSize().getHeight();
                }
            } else {
                cell.updateWidgetMarginAndSize();
                cell.updateSpace();
                cell.reAlign();
                totalSize += cell.getMaxSizeInParentOrientation();
                final int size = cell.getMaxSizeInNonParentOrientation();
                if (size > biggestSize) {
                    biggestSize = size;
                }
            }
        }

        if (undefHeight && isHorizontal()) {
            height = biggestSize;
        } else if (undefWidth) {
            width = biggestSize;
        }

        // Update consumed space before calculating expand ratio sizes
        consumedSpace = totalSize;

        for (int i = 0; i < updateAfter.size(); i++) {
            ChildCell cell = updateAfter.get(i);

            // Reclaim previously reserved space (added back later)
            if (!cell.isRelativeSizeInParentOrientation()) {
                totalSize -= isHorizontal() ? cell.getWidgetSize().getWidth()
                        : cell.getWidgetSize().getHeight();
            }

            cell.updateWidgetMargin();
            cell.updateSpace();

            if (cell.hasRelativeSize()) {
                client.handleComponentRelativeSize(cell.getWidget());
                cell.updateWidgetSize();
            }

            cell.reAlign();

            final int size = cell.getMaxSizeInNonParentOrientation();
            if (size > biggestSize) {
                biggestSize = size;
            }
            totalSize += cell.getMaxSizeInParentOrientation();
        }

        if (updateAfter.size() > 1
                && ((isHorizontal() && !undefWidth) || !undefHeight)) {
            ChildCell last = updateAfter.get(updateAfter.size() - 1);
            // Add rounding error pixel to last child (if more than one found,
            // otherwise no rounding errors should exhibit)
            totalSize -= last.getMaxSizeInParentOrientation();
            if (isHorizontal()) {
                last.getRenderSpace().setWidth(width - totalSize);
            } else {
                last.getRenderSpace().setHeight(height - totalSize);
            }
            if (last.hasRelativeSize()) {
                client.handleComponentRelativeSize(last.getWidget());
                last.updateWidgetSize();
            }
            last.reAlign();
            totalSize += last.getMaxSizeInParentOrientation();
        }

        if (undefWidth) {
            width = isHorizontal() ? totalSize : biggestSize;
            super.setWidth(width + "px");

        }
        if (undefHeight) {
            height = isHorizontal() ? biggestSize : totalSize;
            super.setHeight(height + "px");
        }

        getElement().getStyle().setProperty("overflow", "");
    }

    public boolean requestLayout(Set<Paintable> children) {
        int oldWidth = width;
        int oldHeight = height;
        updateLayout(false);
        if (undefWidth && oldWidth != width) {
            return false;
        }
        if (undefHeight && oldHeight != height) {
            return false;
        }
        return true;
        // TODO this could probably be optimized with a code like below
        // for (Paintable child : children) {
        // ChildCell cell = widgetToCell.get(child);
        // cell.reset(false);
        // cell.updateSpace();
        // cell.updateNaturalDimensions();
        // cell.reAlign();
        // }
        // if (undefWidth) {
        // final int oldWidth = width;
        // updateActualSize();
        // if (width != oldWidth) {
        // for (Widget child : getChildren()) {
        // ChildCell cell = widgetToCell.get(child);
        // cell.reset(false);
        // cell.updateSpace();
        // if (cell.isRelativeWidth() || cell.isRelativeHeight()) {
        // client.handleComponentRelativeSize(child);
        // }
        // cell.updateNaturalDimensions();
        // cell.reAlign();
        // }
        // return false;
        // }
        // }
        // return true;
    }

}