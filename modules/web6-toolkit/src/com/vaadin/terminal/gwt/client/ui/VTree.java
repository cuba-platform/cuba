/*
 * Copyright 2010 IT Mill Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.vaadin.terminal.gwt.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import com.haulmont.cuba.toolkit.gwt.client.TextSelectionManager;
import com.haulmont.cuba.toolkit.gwt.client.Tools;
import com.haulmont.cuba.toolkit.gwt.client.ui.IScrollablePanel;
import com.haulmont.cuba.toolkit.gwt.client.ui.PopupContainer;
import com.vaadin.terminal.gwt.client.*;
import com.vaadin.terminal.gwt.client.ui.dd.*;
import com.vaadin.terminal.gwt.client.ui.layout.CellBasedLayout;

import java.util.*;

/**
 *
 */
@SuppressWarnings({"GWTStyleCheck"})
public class VTree extends FocusElementPanel implements Paintable, VHasDropHandler, TextSelectionManager, FocusHandler, BlurHandler, KeyPressHandler, KeyDownHandler {

    public static final String CLASSNAME = "v-tree";

    public static final String ITEM_CLICK_EVENT_ID = "itemClick";

    public static final int MULTISELECT_MODE_DEFAULT = 0;
    public static final int MULTISELECT_MODE_SIMPLE = 1;

    protected Set<String> selectedIds = new HashSet<String>();
    protected ApplicationConnection client;
    protected String paintableId;
    protected boolean selectable;
    protected boolean isMultiselect;
    protected String currentMouseOverKey;
    private TreeNode lastSelection;
    private TreeNode focusedNode;
    private int multiSelectMode = MULTISELECT_MODE_DEFAULT;

    protected final HashMap<String, TreeNode> keyToNode = new HashMap<String, TreeNode>();

    protected PopupContainer customContextMenuPopup;

    /**
     * This map contains captions and icon urls for actions like: * "33_c" ->
     * "Edit" * "33_i" -> "http://dom.com/edit.png"
     */
    private final HashMap<String, String> actionMap = new HashMap<String, String>();

    private boolean immediate;

    private boolean isNullSelectionAllowed = true;

    protected boolean disabled = false;

    private boolean readonly;

    protected boolean rendering;

    private VAbstractDropHandler dropHandler;

    private boolean selectionHasChanged = false;

    private int dragMode;

    private boolean doubleClickMode = false;

    private static final int CHARCODE_SPACE = 32;

    private FlowPanel content = new FlowPanel();

    private CellBasedLayout.Spacing borderPaddings = null;

    /*
     * In webkit, focus may have been requested for this component but not yet
     * gained. Use this to trac if tree has gained the focus on webkit. See
     * FocusImplSafari and #6373
     */
    private boolean treeHasFocus;

    public VTree() {
        super();
        setStyleName(CLASSNAME);
        setWidget(content);
        content.setStyleName(CLASSNAME + "-content");

        addFocusHandler(this);
        addBlurHandler(this);

        /*
         * Firefox auto-repeat works correctly only if we use a key press
         * handler, other browsers handle it correctly when using a key down
         * handler
         */
        if (BrowserInfo.get().isGecko() || BrowserInfo.get().isOpera()) {
            addKeyPressHandler(this);
        } else {
            addKeyDownHandler(this);
        }

        /*
         * We need to use the sinkEvents method to catch the keyUp events so we
         * can cache a single shift. KeyUpHandler cannot do this. At the same
         * time we catch the mouse down and up events so we can apply the text
         * selection patch in IE
         */
        sinkEvents(Event.ONMOUSEDOWN | Event.ONMOUSEUP | Event.ONKEYUP);

        /*
         * Re-set the tab index to make sure that the FocusElementPanel's
         * (super) focus element gets the tab index and not the element
         * containing the tree.
         */
        setTabIndex(0);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.google.gwt.user.client.ui.Widget#onBrowserEvent(com.google.gwt.user
     * .client.Event)
     */
    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        if (event.getTypeInt() == Event.ONMOUSEDOWN) {
            // Prevent default text selection in IE
            if (BrowserInfo.get().isIE()) {
                ((Element) event.getEventTarget().cast()).setPropertyJSO(
                        "onselectstart", applyDisableTextSelectionIEHack());
            }
        } else if (event.getTypeInt() == Event.ONMOUSEUP) {
            // Remove IE text selection hack
            if (BrowserInfo.get().isIE()) {
                ((Element) event.getEventTarget().cast()).setPropertyJSO(
                        "onselectstart", null);
            }
        } else if (event.getTypeInt() == Event.ONKEYUP) {
            if (selectionHasChanged) {
                if (event.getKeyCode() == getNavigationDownKey()
                        && !event.getShiftKey()) {
                    sendSelectionToServer();
                    event.preventDefault();
                } else if (event.getKeyCode() == getNavigationUpKey()
                        && !event.getShiftKey()) {
                    sendSelectionToServer();
                    event.preventDefault();
                } else if (event.getKeyCode() == KeyCodes.KEY_SHIFT) {
                    sendSelectionToServer();
                    event.preventDefault();
                } else if (event.getKeyCode() == getNavigationSelectKey()) {
                    sendSelectionToServer();
                    event.preventDefault();
                }
            }
        }
    }

    protected void updateActionMap(UIDL c) {
        final Iterator it = c.getChildIterator();
        while (it.hasNext()) {
            final UIDL action = (UIDL) it.next();
            final String key = action.getStringAttribute("key");
            final String caption = action.getStringAttribute("caption");
            actionMap.put(key + "_c", caption);
            if (action.hasAttribute("icon")) {
                // TODO need some uri handling ??
                actionMap.put(key + "_i", client.translateVaadinUri(action
                        .getStringAttribute("icon")));
            }
        }

    }

    public String getActionCaption(String actionKey) {
        return actionMap.get(actionKey + "_c");
    }

    public String getActionIcon(String actionKey) {
        return actionMap.get(actionKey + "_i");
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        // Ensure correct implementation and let container manage caption
        if (client.updateComponent(this, uidl, true)) {
            return;
        }

        rendering = true;

        this.client = client;

        if (uidl.hasAttribute("partialUpdate")) {
            handleUpdate(uidl);
            rendering = false;
            return;
        }

        paintableId = uidl.getId();

        immediate = uidl.hasAttribute("immediate");

        disabled = uidl.getBooleanAttribute("disabled");
        readonly = uidl.getBooleanAttribute("readonly");

        doubleClickMode = uidl.getBooleanAttribute("doubleClickMode");

        dragMode = uidl.hasAttribute("dragMode") ? uidl
                .getIntAttribute("dragMode") : 0;

        isNullSelectionAllowed = uidl.getBooleanAttribute("nullselect");

        content.clear();

        if (uidl.hasAttribute("hideContextMenu") && customContextMenuPopup != null && customContextMenuPopup.isShowing()) {
            customContextMenuPopup.hide();
        }

        TreeNode childTree = null;
        for (final Iterator i = uidl.getChildIterator(); i.hasNext();) {
            final UIDL childUidl = (UIDL) i.next();
            if ("actions".equals(childUidl.getTag())) {
                updateActionMap(childUidl);
                continue;
            } else if ("-ac".equals(childUidl.getTag())) {
                updateDropHandler(childUidl);
                continue;
            } else if ("cm".equals(childUidl.getTag())) {
                if (customContextMenuPopup == null) {
                    customContextMenuPopup = Tools.createCubaContextMenu();
                }
                customContextMenuPopup.updateFromUIDL(childUidl, client);
                continue;
            }

            childTree = createTreeNode(childUidl);
            content.add(childTree);
            childTree.updateFromUIDL(childUidl, client);
        }

        if (childTree != null) {
            childTree.addStyleName("last");
        }

        final String selectMode = uidl.getStringAttribute("selectmode");
        selectable = !"none".equals(selectMode);
        isMultiselect = "multi".equals(selectMode);

        selectedIds = uidl.getStringArrayVariableAsSet("selected");

        rendering = false;
    }

    @Override
    protected void onDetach() {
        super.onDetach();

        if (customContextMenuPopup != null && customContextMenuPopup.isShowing()) {
            customContextMenuPopup.hide();
        }
    }

     /**
     * Returns the first root node of the tree or null if there are no root
     * nodes.
     *
     * @return The first root {@link TreeNode}
     */
    protected TreeNode getFirstRootNode() {
        if (content.getWidgetCount() == 0) {
            return null;
        }
        return (TreeNode) content.getWidget(0);
    }

    /**
     * Returns the last root node of the tree or null if there are no root
     * nodes.
     *
     * @return The last root {@link TreeNode}
     */
    protected TreeNode getLastRootNode() {
        if (content.getWidgetCount() == 0) {
            return null;
        }
        return (TreeNode) content.getWidget(content.getWidgetCount() - 1);
    }

    /**
     * Returns a list of all root nodes in the Tree in the order they appear in
     * the tree.
     *
     * @return A list of all root {@link TreeNode}s.
     */
    protected List<TreeNode> getRootNodes() {
        ArrayList<TreeNode> rootNodes = new ArrayList<TreeNode>();
        for (int i = 0; i < content.getWidgetCount(); i++) {
            rootNodes.add((TreeNode) content.getWidget(i));
        }
        return rootNodes;
    }

    @Override
    public void setWidth(String width) {
        if (isAttached() && borderPaddings == null) {
            detectBorderPaddings();
        }
        super.setWidth(width);
        if (isAttached()) {
            int w = getOffsetWidth();
            w -= 2 * borderPaddings.hSpacing;
            super.setWidth((w < 0 ? 0 : w)+ "px");
        }
    }

    @Override
    public void setHeight(String height) {
        if (isAttached() && borderPaddings == null) {
            detectBorderPaddings();
        }
        super.setHeight(height);
        if (isAttached()) {
            int h = getOffsetHeight();
            h -= 2 * borderPaddings.vSpacing;
            super.setHeight((h < 0 ? 0 : h) + "px");
        }
    }

    private void detectBorderPaddings() {
        if (borderPaddings == null) {
            Element el = getElement();
            DOM.setStyleAttribute(el, "overflow", "hidden");
            DOM.setStyleAttribute(el, "width", "0px");
            DOM.setStyleAttribute(el, "height", "0px");

            int w = el.getOffsetWidth();
            int h = el.getOffsetHeight();

            borderPaddings = new CellBasedLayout.Spacing(w, h);

            DOM.setStyleAttribute(el, "overflow", "");
            DOM.setStyleAttribute(el, "width", "");
            DOM.setStyleAttribute(el, "height", "");
        }
    }

    protected TreeNode createTreeNode(UIDL childUidl) {
        return new TreeNode();
    }

    private void updateTreeRelatedDragData(VDragEvent drag) {

        currentMouseOverKey = findCurrentMouseOverKey(drag.getElementOver());

        drag.getDropDetails().put("itemIdOver", currentMouseOverKey);
        if (currentMouseOverKey != null) {
            TreeNode treeNode = keyToNode.get(currentMouseOverKey);
            VerticalDropLocation detail = treeNode.getDropDetail(drag
                    .getCurrentGwtEvent());
            Boolean overTreeNode = null;
            if (treeNode != null && !treeNode.isLeaf()
                    && detail == VerticalDropLocation.MIDDLE) {
                overTreeNode = true;
            }
            drag.getDropDetails().put("itemIdOverIsNode", overTreeNode);
            drag.getDropDetails().put("detail", detail);
        } else {
            drag.getDropDetails().put("itemIdOverIsNode", null);
            drag.getDropDetails().put("detail", null);
        }

    }

    private String findCurrentMouseOverKey(Element elementOver) {
        TreeNode treeNode = Util.findWidget(elementOver, TreeNode.class);
        return treeNode == null ? null : treeNode.key;
    }

    private void updateDropHandler(UIDL childUidl) {
        if (dropHandler == null) {
            dropHandler = new VAbstractDropHandler() {

                @Override
                public void dragEnter(VDragEvent drag) {
                }

                @Override
                protected void dragAccepted(final VDragEvent drag) {

                }

                @Override
                public void dragOver(final VDragEvent currentDrag) {
                    final Object oldIdOver = currentDrag.getDropDetails().get(
                            "itemIdOver");
                    final VerticalDropLocation oldDetail = (VerticalDropLocation) currentDrag
                            .getDropDetails().get("detail");

                    updateTreeRelatedDragData(currentDrag);
                    final VerticalDropLocation detail = (VerticalDropLocation) currentDrag
                            .getDropDetails().get("detail");
                    boolean nodeHasChanged = (currentMouseOverKey != null && currentMouseOverKey != oldIdOver)
                            || (currentMouseOverKey == null && oldIdOver != null);
                    boolean detailHasChanded = (detail != null && detail != oldDetail)
                            || (detail == null && oldDetail != null);

                    if (nodeHasChanged || detailHasChanded) {
                        final String newKey = currentMouseOverKey;
                        TreeNode treeNode = keyToNode.get(oldIdOver);
                        if (treeNode != null) {
                            // clear old styles
                            treeNode.emphasis(null);
                        }
                        if (newKey != null) {
                            validate(new VAcceptCallback() {
                                public void accepted(VDragEvent event) {
                                    VerticalDropLocation curDetail = (VerticalDropLocation) event
                                            .getDropDetails().get("detail");
                                    if (curDetail == detail
                                            && newKey
                                                    .equals(currentMouseOverKey)) {
                                        keyToNode.get(newKey).emphasis(detail);
                                    }
                                    /*
                                     * Else drag is already on a different
                                     * node-detail pair, new criteria check is
                                     * going on
                                     */
                                }
                            }, currentDrag);

                        }
                    }

                }

                @Override
                public void dragLeave(VDragEvent drag) {
                    cleanUp();
                }

                private void cleanUp() {
                    if (currentMouseOverKey != null) {
                        keyToNode.get(currentMouseOverKey).emphasis(null);
                        currentMouseOverKey = null;
                    }
                }

                @Override
                public boolean drop(VDragEvent drag) {
                    cleanUp();
                    return super.drop(drag);
                }

                @Override
                public Paintable getPaintable() {
                    return VTree.this;
                }

                @Override
                public ApplicationConnection getApplicationConnection() {
                    return client;
                }

            };
        }
        dropHandler.updateAcceptRules(childUidl);
    }

    protected void handleUpdate(UIDL uidl) {
        final TreeNode rootNode = keyToNode.get(uidl
                .getStringAttribute("rootKey"));
        if (rootNode != null) {
            if (!rootNode.getState()) {
                // expanding node happened server side
                rootNode.setState(true, false);
            }
            rootNode.renderChildNodes(uidl.getChildIterator());
        }
    }

    public void setSelected(TreeNode treeNode, boolean selected) {
        if (selected) {
            if (!isMultiselect) {
                while (selectedIds.size() > 0) {
                    final String id = selectedIds.iterator().next();
                    final TreeNode oldSelection = keyToNode.get(id);
                    if (oldSelection != null) {
                        // can be null if the node is not visible (parent
                        // collapsed)
                        oldSelection.setSelected(false);
                    }
                    selectedIds.remove(id);
                }
            }
            treeNode.setSelected(true);
            selectedIds.add(treeNode.key);
        } else {
            if (!isNullSelectionAllowed) {
                if (!isMultiselect || selectedIds.size() == 1) {
                    return;
                }
            }
            selectedIds.remove(treeNode.key);
            treeNode.setSelected(false);
        }

        client.updateVariable(paintableId, "selected", selectedIds
                .toArray(new String[selectedIds.size()]), immediate);
    }

    public boolean isSelected(TreeNode treeNode) {
        return selectedIds.contains(treeNode.key);
    }

    /**
     * Sends the selection to the server
     */
    private void sendSelectionToServer() {
        client.updateVariable(paintableId, "selected", selectedIds
                .toArray(new String[selectedIds.size()]), immediate);
        selectionHasChanged = false;
    }

    @SuppressWarnings({"GWTStyleCheck"})
    public class TreeNode extends SimplePanel implements ActionOwner {

        public static final String CLASSNAME = "v-tree-node";
        public static final String CLASSNAME_FOCUSED = CLASSNAME + "-focused";

        public String key;

        protected String[] actionKeys = null;

        protected boolean childrenLoaded;

        protected Element nodeCaptionDiv;

        protected Element nodeCaptionSpan;

        protected FlowPanel childNodeContainer;

        private boolean open;

        protected Icon icon;

        protected Element ie6compatnode;

        private Event mouseDownEvent;

        private int cachedHeight = -1;

        private boolean focused = false;

        private boolean blocked = false;

        protected boolean canExpand;

        public TreeNode() {
            constructDom();
            attachEvents();
        }

        protected void attachEvents() {
            sinkEvents(Event.ONCLICK | Event.ONDBLCLICK | Event.MOUSEEVENTS
                    | Event.ONCONTEXTMENU);
        }

        public VerticalDropLocation getDropDetail(NativeEvent currentGwtEvent) {
            if (cachedHeight < 0) {
                /*
                 * Height is cached to avoid flickering (drop hints may change
                 * the reported offsetheight -> would change the drop detail)
                 */
                cachedHeight = nodeCaptionDiv.getOffsetHeight();
            }
            VerticalDropLocation verticalDropLocation = DDUtil
                    .getVerticalDropLocation(nodeCaptionDiv, cachedHeight,
                            currentGwtEvent.getClientY(), 0.15);
            return verticalDropLocation;
        }

        protected void emphasis(VerticalDropLocation detail) {
            String base = "v-tree-node-drag-";
            UIObject.setStyleName(getElement(), base + "top",
                    VerticalDropLocation.TOP == detail);
            UIObject.setStyleName(getElement(), base + "bottom",
                    VerticalDropLocation.BOTTOM == detail);
            UIObject.setStyleName(getElement(), base + "center",
                    VerticalDropLocation.MIDDLE == detail);
            base = "v-tree-node-caption-drag-";
            UIObject.setStyleName(nodeCaptionDiv, base + "top",
                    VerticalDropLocation.TOP == detail);
            UIObject.setStyleName(nodeCaptionDiv, base + "bottom",
                    VerticalDropLocation.BOTTOM == detail);
            UIObject.setStyleName(nodeCaptionDiv, base + "center",
                    VerticalDropLocation.MIDDLE == detail);

            // also add classname to "folder node" into which the drag is
            // targeted

            TreeNode folder;
            /* Possible parent of this TreeNode will be stored here */
            TreeNode parentFolder = getParentNode();

            // TODO fix my bugs
            if (isLeaf()) {
                folder = parentFolder;
                // note, parent folder may be null if this is root node => no
                // folder target exists
            } else {
                if (detail == VerticalDropLocation.TOP) {
                    folder = parentFolder;
                } else {
                    folder = this;
                }
                // ensure we remove the dragfolder classname from the previous
                // folder node
                setDragFolderStyleName(this, false);
                setDragFolderStyleName(parentFolder, false);
            }
            if (folder != null) {
                setDragFolderStyleName(folder, detail != null);
            }

        }

        private TreeNode getParentNode() {
            Widget parent2 = getParent().getParent();
            if (parent2 instanceof TreeNode) {
                return (TreeNode) parent2;
            }
            return null;
        }

        private void setDragFolderStyleName(TreeNode folder, boolean add) {
            if (folder != null) {
                UIObject.setStyleName(folder.getElement(),
                        "v-tree-node-dragfolder", add);
                UIObject.setStyleName(folder.nodeCaptionDiv,
                        "v-tree-node-caption-dragfolder", add);
            }
        }

        /**
         * Handles mouse selection
         *
         * @param ctrl
         *            Was the ctrl-key pressed
         * @param shift
         *            Was the shift-key pressed
         * @return Returns true if event was handled, else false
         */
        private boolean handleClickSelection(final boolean ctrl,
                final boolean shift) {

            // always when clicking an item, focus it
            setFocusedNode(this, false);

            if (!isIE6OrOpera()) {
                /*
                 * Ensure that the tree's focus element also gains focus
                 * (TreeNodes focus is faked using FocusElementPanel in browsers
                 * other than IE6 and Opera).
                 */
                focus();
            }

            Scheduler.ScheduledCommand command = new Scheduler.ScheduledCommand() {
                public void execute() {

                    if (multiSelectMode == MULTISELECT_MODE_SIMPLE
                            || !isMultiselect) {
                        toggleSelection();
                        lastSelection = TreeNode.this;
                    } else if (multiSelectMode == MULTISELECT_MODE_DEFAULT) {
                        // Handle ctrl+click
                        if (isMultiselect && ctrl && !shift) {
                            toggleSelection();
                            lastSelection = TreeNode.this;

                            // Handle shift+click
                        } else if (isMultiselect && !ctrl && shift) {
                            deselectAll();
                            selectNodeRange(lastSelection.key, key);
                            sendSelectionToServer();

                            // Handle ctrl+shift click
                        } else if (isMultiselect && ctrl && shift) {
                            selectNodeRange(lastSelection.key, key);

                            // Handle click
                        } else {
                            // TODO should happen only if this alone not yet
                            // selected,
                            // now sending excess server calls
                            deselectAll();
                            toggleSelection();
                            lastSelection = TreeNode.this;
                        }
                    }
                }
            };

            if ((BrowserInfo.get().isWebkit() ||  BrowserInfo.get().isChrome()) && !treeHasFocus) {
                /*
                 * Safari may need to wait for focus. See FocusImplSafari.
                 */
                // VConsole.log("Deferring click handling to let webkit gain focus...");
                Scheduler.get().scheduleDeferred(command);
            } else {
                command.execute();
            }

            return true;
        }

        @Override
        public void onBrowserEvent(Event event) {
            super.onBrowserEvent(event);
            handleBrowserEvent(event);
        }

        protected void handleBrowserEvent(Event event) {
            if (disabled) {
                return;
            }
            final int type = DOM.eventGetType(event);
            final Element target = DOM.eventGetTarget(event);
            final boolean inCaption = target == nodeCaptionSpan
                    || (icon != null && target == icon.getElement());
            if (inCaption
                    && client
                            .hasEventListeners(VTree.this, ITEM_CLICK_EVENT_ID)

                    && (type == Event.ONDBLCLICK || type == Event.ONMOUSEUP) && !blocked) {

                if (!doubleClickMode && type == Event.ONMOUSEUP) {
                    fireClick(event);
                    blocked = true;
                    Timer t = new Timer() {
                        @Override
                        public void run() {
                            blocked = false;
                        }
                    };
                    t.schedule(1500);
                } else if (doubleClickMode && type == Event.ONDBLCLICK) {
                    fireClick(event);
                }
            }
            switch (type){
                case Event.ONCLICK:
                    if (getElement() == target || ie6compatnode == target) {
                        // state change
                        toggleState();
                    } else if (!readonly && inCaption) {
                        if (selectable) {
                            // caption click = selection change && possible click
                            // event
                            if (handleClickSelection(event.getCtrlKey()
                                    || event.getMetaKey(), event.getShiftKey())) {
                                event.preventDefault();
                            }
                        } else {
                            // Not selectable, only focus the node.
                            setFocusedNode(this);
                        }
                    }
                    event.stopPropagation();
                    break;
                case Event.ONMOUSEUP:
                    if (event.getButton() == NativeEvent.BUTTON_RIGHT){
                        setFocusedNode(this, false);
                        if (selectable){
                            selectNode(this, true);
                            sendSelectionToServer();
                        }
                        event.stopPropagation();
                    }
                    break;
                case Event.ONCONTEXTMENU:
                    showContextMenu(event);
                    break;
            }

            if (dragMode != 0 || dropHandler != null) {
                if (type == Event.ONMOUSEDOWN || type == Event.ONTOUCHSTART) {
                    if (nodeCaptionDiv.isOrHasChild((Node) event
                            .getEventTarget().cast())) {
                        if (dragMode > 0
                                && (type == Event.ONTOUCHSTART || event
                                        .getButton() == NativeEvent.BUTTON_LEFT)) {
                            mouseDownEvent = event; // save event for possible
                            // dd operation
                            if (type == Event.ONMOUSEDOWN) {
                                event.preventDefault(); // prevent text
                                // selection
                            } else {
                                /*
                                 * FIXME We prevent touch start event to be used
                                 * as a scroll start event. Note that we cannot
                                 * easily distinguish whether the user wants to
                                 * drag or scroll. The same issue is in table
                                 * that has scrollable area and has drag and
                                 * drop enable. Some kind of timer might be used
                                 * to resolve the issue.
                                 */
                                event.stopPropagation();
                            }
                        }
                    }
                } else if (type == Event.ONMOUSEMOVE
                        || type == Event.ONMOUSEOUT
                        || type == Event.ONTOUCHMOVE) {

                    if (mouseDownEvent != null) {
                        // start actual drag on slight move when mouse is down
                        VTransferable t = new VTransferable();
                        t.setDragSource(VTree.this);
                        t.setData("itemId", key);
                        VDragEvent drag = VDragAndDropManager.get().startDrag(
                                t, mouseDownEvent, true);

                        drag.createDragImage(nodeCaptionDiv, true);
                        event.stopPropagation();

                        mouseDownEvent = null;
                    }
                } else if (type == Event.ONMOUSEUP) {
                    mouseDownEvent = null;
                }
                if (type == Event.ONMOUSEOVER) {
                    mouseDownEvent = null;
                    currentMouseOverKey = key;
                    event.stopPropagation();
                }

            } else if (type == Event.ONMOUSEDOWN
                    && event.getButton() == NativeEvent.BUTTON_LEFT) {
                event.preventDefault(); // text selection
            }
        }

        protected void fireClick(Event evt) {
            /*
             * Ensure we have focus in tree before sending variables. Otherwise
             * previously modified field may contain dirty variables.
             */
            if (!treeHasFocus) {
                if (BrowserInfo.get().isOpera()) {
                    if (focusedNode == null) {
                        getNodeByKey(key).setFocused(true);
                    } else {
                        focusedNode.setFocused(true);
                    }
                } else {
                    focus();
                }
            }
            final MouseEventDetails details = new MouseEventDetails(evt);
            final int eventType = evt.getTypeInt();

            ScheduledCommand command = new ScheduledCommand() {
                public void execute() {
                    // non-immediate iff an immediate select event is going to
                    // happen
                    boolean imm = (eventType == Event.ONDBLCLICK && doubleClickMode)
                            || !immediate
                            || !selectable
                            || (!isNullSelectionAllowed && isSelected() && selectedIds
                                    .size() == 1);
                    client.updateVariable(paintableId, "clickedKey", key,
                                    false);
                    client.updateVariable(paintableId, "clickEvent", details
                            .toString(), imm);
                }
            };
            if (treeHasFocus) {
                command.execute();
            } else {
                /*
                 * Webkits need a deferring due to FocusImplSafari uses timeout
                 */
                Scheduler.get().scheduleDeferred(command);
            }
        }

        protected void toggleSelection() {
            if (selectable) {
                VTree.this.setSelected(this, !isSelected());
            }
        }

        protected void toggleState() {
            setState(!getState(), true);
        }

        protected void constructDom() {
            // workaround for a very weird IE6 issue #1245
            if (BrowserInfo.get().isIE6()) {
                ie6compatnode = DOM.createDiv();
                setStyleName(ie6compatnode, CLASSNAME + "-ie6compatnode");
                DOM.setInnerText(ie6compatnode, " ");
                DOM.appendChild(getElement(), ie6compatnode);

                DOM.sinkEvents(ie6compatnode, Event.ONCLICK);
            }

            nodeCaptionDiv = DOM.createDiv();
            DOM.setElementProperty(nodeCaptionDiv, "className", CLASSNAME
                    + "-caption");
            Element wrapper = DOM.createDiv();
            nodeCaptionSpan = DOM.createSpan();
            DOM.appendChild(getElement(), nodeCaptionDiv);
            DOM.appendChild(nodeCaptionDiv, wrapper);
            DOM.appendChild(wrapper, nodeCaptionSpan);

            childNodeContainer = new FlowPanel();
            childNodeContainer.setStylePrimaryName(CLASSNAME + "-children");
            setWidget(childNodeContainer);
        }

        public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
            setText(uidl.getStringAttribute("caption"));
            key = uidl.getStringAttribute("key");

            keyToNode.put(key, this);

            if (uidl.hasAttribute("al")) {
                actionKeys = uidl.getStringArrayAttribute("al");
            }

            boolean isNode = uidl.getTag().equals("node");
            if (isNode) {
                renderChildNodes(uidl.getChildIterator());
                childNodeContainer.setVisible(childNodeContainer.getWidgetCount() != 0);
            } else {
                addStyleName(CLASSNAME + "-leaf");
            }
            addStyleName(CLASSNAME);
            if (uidl.hasAttribute("style")) {
                addStyleName(CLASSNAME + "-" + uidl.getStringAttribute("style"));
                Widget.setStyleName(nodeCaptionDiv, CLASSNAME + "-caption-"
                        + uidl.getStringAttribute("style"), true);
                childNodeContainer.addStyleName(CLASSNAME + "-children-"
                        + uidl.getStringAttribute("style"));
            }

            if (uidl.getBooleanAttribute("expanded") && !getState()) {
                setState(true, false);
            }

            if (uidl.getBooleanAttribute("selected")) {
                setSelected(true);
                // ensure that identifier is in selectedIds array (this may be a
                // partial update)
                selectedIds.add(key);
            }

            if (uidl.hasAttribute("icon")) {
                if (icon == null) {
                    icon = new Icon(client);
                    DOM.insertBefore(DOM.getFirstChild(nodeCaptionDiv), icon
                            .getElement(), nodeCaptionSpan);
                }
                icon.setUri(uidl.getStringAttribute("icon"));
            } else {
                if (icon != null) {
                    DOM.removeChild(DOM.getFirstChild(nodeCaptionDiv), icon
                            .getElement());
                    icon = null;
                }
            }

            if (isNode) {
                canExpand = uidl.hasAttribute("hasChildren");
                if (canExpand) {
                    removeStyleName("noChildren");
                } else {
                    addStyleName("noChildren");
                }
            }

            if (BrowserInfo.get().isIE6() && isAttached()) {
                fixWidth();
            }
        }

        public boolean isLeaf() {
            return getStyleName().contains("leaf");
        }

        protected void setState(boolean state, boolean notifyServer) {
            if (open == state) {
                return;
            }
            if (state) {
                if (!childrenLoaded && notifyServer) {
                    client.updateVariable(paintableId, "requestChildTree",
                            true, false);
                }
                if (notifyServer) {
                    client.updateVariable(paintableId, "expand",
                            new String[] { key }, true);
                }
                addStyleName(CLASSNAME + "-expanded");
                childNodeContainer.setVisible(true);

            } else {
                removeStyleName(CLASSNAME + "-expanded");
                childNodeContainer.setVisible(false);
                if (notifyServer) {
                    client.updateVariable(paintableId, "collapse",
                            new String[] { key }, true);
                }
            }
            open = state;

            if (!rendering) {
                Util.notifyParentOfSizeChange(VTree.this, false);
                if (notifyServer) {
                    Container container = Util.getLayout(VTree.this);
                    if (container != null && ((Widget) container).getParent() instanceof IScrollablePanel) {
                        client.runDescendentsLayout((HasWidgets) ((Widget) container).getParent());
                    }
                }
            }
        }

        protected boolean getState() {
            return open;
        }

        protected void setText(String text) {
            DOM.setInnerText(nodeCaptionSpan, text);
        }

        protected void renderChildNodes(Iterator i) {
            childNodeContainer.clear();
            childNodeContainer.setVisible(true);

            TreeNode childTree = null;
            while (i.hasNext()) {
                final UIDL childUidl = (UIDL) i.next();
                // actions are in bit weird place, don't mix them with children,
                // but current node's actions
                if ("actions".equals(childUidl.getTag())) {
                    updateActionMap(childUidl);
                    continue;
                } else if ("cm".equals(childUidl.getTag())) {
                    continue;
                }
                childTree = createTreeNode(childUidl);
                childNodeContainer.add(childTree);
                childTree.updateFromUIDL(childUidl, client);
            }
            if (childTree != null) {
                childTree.addStyleName("last");
            }
            if (childNodeContainer.getWidgetCount() > 0) {
                childrenLoaded = true;
            }
        }

        public boolean isChildrenLoaded() {
            return childrenLoaded;
        }

        /**
         * Returns the children of the node
         *
         * @return A set of tree nodes
         */
        public List<TreeNode> getChildren() {
            List<TreeNode> nodes = new LinkedList<TreeNode>();

            if (!isLeaf() && isChildrenLoaded()) {
                Iterator<Widget> iter = childNodeContainer.iterator();
                while (iter.hasNext()) {
                    TreeNode node = (TreeNode) iter.next();
                    nodes.add(node);
                }
            }
            return nodes;
        }

        public Action[] getActions() {
            if (actionKeys == null) {
                return new Action[] {};
            }
            final Action[] actions = new Action[actionKeys.length];
            for (int i = 0; i < actions.length; i++) {
                final String actionKey = actionKeys[i];
                final TreeAction a = new TreeAction(this, String.valueOf(key),
                        actionKey);
                a.setCaption(getActionCaption(actionKey));
                a.setIconUrl(getActionIcon(actionKey));
                actions[i] = a;
            }
            return actions;
        }

        public ApplicationConnection getClient() {
            return client;
        }

        public String getPaintableId() {
            return paintableId;
        }

        /**
         * Adds/removes Vaadin specific style name. This method ought to be
         * called only from VTree.
         *
         * @param selected
         */
        protected void setSelected(boolean selected) {
            // add style name to caption dom structure only, not to subtree
            setStyleName(nodeCaptionDiv, "v-tree-node-selected", selected);
        }

        protected boolean isSelected() {
            return VTree.this.isSelected(this);
        }

        /**
         * Travels up the hierarchy looking for this node
         *
         * @param child
         *            The child which grandparent this is or is not
         * @return True if this is a grandparent of the child node
         */
        public boolean isGrandParentOf(TreeNode child) {
            TreeNode currentNode = child;
            boolean isGrandParent = false;
            while (currentNode != null) {
                currentNode = currentNode.getParentNode();
                if (currentNode == this) {
                    isGrandParent = true;
                    break;
                }
            }
            return isGrandParent;
        }

        public void showContextMenu(Event event) {
            if (!readonly && !disabled) {
                int left = event.getClientX();
                int top = event.getClientY();
                top += Window.getScrollTop();
                left += Window.getScrollLeft();

                if (actionKeys != null) {
                    client.getContextMenu().showAt(this, left, top);
                } else if (customContextMenuPopup != null) {
                    boolean showMenu = true;
                    if (customContextMenuPopup.getWidget() instanceof VVerticalLayout) {
                        VVerticalLayout layout = (VVerticalLayout) customContextMenuPopup.getWidget();
                        boolean hasVisibleChild = false;
                        for (Widget widget : layout) {
                            if (widget.isVisible()) {
                                hasVisibleChild = true;
                                break;
                            }
                        }

                        if (!hasVisibleChild) {
                            showMenu = false;
                        }
                    }

                    if (showMenu) {
                        customContextMenuPopup.showAt(left, top);
                    }
                }

                if (!isSelected()) {
                    toggleSelection();
                }
                event.cancelBubble(true);
                event.preventDefault();
            }
        }

        /*
         * We need to fix the width of TreeNodes so that the float in
         * ie6compatNode does not wrap (see ticket #1245)
         */
        protected void fixWidth() {
            nodeCaptionDiv.getStyle().setProperty("styleFloat", "left");
            nodeCaptionDiv.getStyle().setProperty("display", "inline");
            nodeCaptionDiv.getStyle().setProperty("marginLeft", "0");
            final int captionWidth = ie6compatnode.getOffsetWidth()
                    + nodeCaptionDiv.getOffsetWidth();
            setWidth(captionWidth + "px");
        }

        @Override
        public void onAttach() {
            super.onAttach();
            if (ie6compatnode != null) {
                fixWidth();
            }
        }

        @Override
        protected void onDetach() {
            super.onDetach();
            client.getContextMenu().ensureHidden(this);
        }

        /**
         * Is the node focused?
         *
         * @param focused
         *            True if focused, false if not
         */
        public void setFocused(boolean focused) {
            if (!this.focused && focused) {
                nodeCaptionDiv.addClassName(CLASSNAME_FOCUSED);
                if (BrowserInfo.get().isIE6()) {
                    ie6compatnode.addClassName(CLASSNAME_FOCUSED);
                }
                this.focused = focused;
                if (isIE6OrOpera()) {
                    nodeCaptionDiv.focus();
                }
            } else if (this.focused && !focused) {
                nodeCaptionDiv.removeClassName(CLASSNAME_FOCUSED);
                if (BrowserInfo.get().isIE6()) {
                    ie6compatnode.removeClassName(CLASSNAME_FOCUSED);
                }
                this.focused = focused;
            }
        }

        /**
         * Scrolls the caption into view
         */
        public void scrollIntoView() {
            nodeCaptionDiv.scrollIntoView();
        }
    }

    public VDropHandler getDropHandler() {
        return dropHandler;
    }

    public TreeNode getNodeByKey(String key) {
        return keyToNode.get(key);
    }

    public boolean allowTextSelection() {
        return false;
    }

    /**
     * Deselects all items in the tree
     */
    public void deselectAll() {
        for (String key : selectedIds) {
            TreeNode node = keyToNode.get(key);
            if (node != null) {
                node.setSelected(false);
            }
        }
        selectedIds.clear();
        selectionHasChanged = true;
    }

    /**
     * Selects a range of nodes
     *
     * @param startNodeKey
     *            The start node key
     * @param endNodeKey
     *            The end node key
     */
    private void selectNodeRange(String startNodeKey, String endNodeKey) {

        TreeNode startNode = keyToNode.get(startNodeKey);
        TreeNode endNode = keyToNode.get(endNodeKey);

        // The nodes have the same parent
        if (startNode.getParent() == endNode.getParent()) {
            doSiblingSelection(startNode, endNode);

            // The start node is a grandparent of the end node
        } else if (startNode.isGrandParentOf(endNode)) {
            doRelationSelection(startNode, endNode);

            // The end node is a grandparent of the start node
        } else if (endNode.isGrandParentOf(startNode)) {
            doRelationSelection(endNode, startNode);

        } else {
            doNoRelationSelection(startNode, endNode);
        }
    }

    /**
     * Selects a node and deselect all other nodes
     *
     * @param node
     *            The node to select
     */
    private void selectNode(TreeNode node, boolean deselectPrevious) {
        if (deselectPrevious) {
            deselectAll();
        }

        if (node != null) {
            node.setSelected(true);
            selectedIds.add(node.key);
            lastSelection = node;
        }
        selectionHasChanged = true;
    }

    /**
     * Deselects a node
     *
     * @param node
     *            The node to deselect
     */
    private void deselectNode(TreeNode node) {
        node.setSelected(false);
        selectedIds.remove(node.key);
        selectionHasChanged = true;
    }

    /**
     * Selects all the open children to a node
     *
     * @param node
     *            The parent node
     */
    private void selectAllChildren(TreeNode node, boolean includeRootNode) {
        if (includeRootNode) {
            node.setSelected(true);
            selectedIds.add(node.key);
        }

        for (TreeNode child : node.getChildren()) {
            if (!child.isLeaf() && child.getState()) {
                selectAllChildren(child, true);
            } else {
                child.setSelected(true);
                selectedIds.add(child.key);
            }
        }
        selectionHasChanged = true;
    }

    /**
     * Selects all children until a stop child is reached
     *
     * @param root
     *            The root not to start from
     * @param stopNode
     *            The node to finish with
     * @param includeRootNode
     *            Should the root node be selected
     * @param includeStopNode
     *            Should the stop node be selected
     *
     * @return Returns false if the stop child was found, else true if all
     *         children was selected
     */
    private boolean selectAllChildrenUntil(TreeNode root, TreeNode stopNode,
            boolean includeRootNode, boolean includeStopNode) {
        if (includeRootNode) {
            root.setSelected(true);
            selectedIds.add(root.key);
        }
        if (root.getState() && root != stopNode) {
            for (TreeNode child : root.getChildren()) {
                if (!child.isLeaf() && child.getState() && child != stopNode) {
                    if (!selectAllChildrenUntil(child, stopNode, true,
                            includeStopNode)) {
                        return false;
                    }
                } else if (child == stopNode) {
                    if (includeStopNode) {
                        child.setSelected(true);
                        selectedIds.add(child.key);
                    }
                    return false;
                } else if (child.isLeaf()) {
                    child.setSelected(true);
                    selectedIds.add(child.key);
                }
            }
        }
        selectionHasChanged = true;

        return true;
    }

    /**
     * Select a range between two nodes which have no relation to each other
     *
     * @param startNode
     *            The start node to start the selection from
     * @param endNode
     *            The end node to end the selection to
     */
    private void doNoRelationSelection(TreeNode startNode, TreeNode endNode) {

        TreeNode commonParent = getCommonGrandParent(startNode, endNode);
        TreeNode startBranch = null, endBranch = null;

        // Find the children of the common parent
        List<TreeNode> children;
        if (commonParent != null) {
            children = commonParent.getChildren();
        } else {
            children = getRootNodes();
        }

        // Find the start and end branches
        for (TreeNode node : children) {
            if (nodeIsInBranch(startNode, node)) {
                startBranch = node;
            }
            if (nodeIsInBranch(endNode, node)) {
                endBranch = node;
            }
        }

        // Swap nodes if necessary
        if (children.indexOf(startBranch) > children.indexOf(endBranch)) {
            TreeNode temp = startBranch;
            startBranch = endBranch;
            endBranch = temp;

            temp = startNode;
            startNode = endNode;
            endNode = temp;
        }

        // Select all children under the start node
        selectAllChildren(startNode, true);
        TreeNode startParent = startNode.getParentNode();
        TreeNode currentNode = startNode;
        while (startParent != null && startParent != commonParent) {
            List<TreeNode> startChildren = startParent.getChildren();
            for (int i = startChildren.indexOf(currentNode) + 1; i < startChildren
                    .size(); i++) {
                selectAllChildren(startChildren.get(i), true);
            }

            currentNode = startParent;
            startParent = startParent.getParentNode();
        }

        // Select nodes until the end node is reached
        for (int i = children.indexOf(startBranch) + 1; i <= children
                .indexOf(endBranch); i++) {
            selectAllChildrenUntil(children.get(i), endNode, true, true);
        }

        // Ensure end node was selected
        endNode.setSelected(true);
        selectedIds.add(endNode.key);
        selectionHasChanged = true;
    }

    /**
     * Examines the children of the branch node and returns true if a node is in
     * that branch
     *
     * @param node
     *            The node to search for
     * @param branch
     *            The branch to search in
     * @return True if found, false if not found
     */
    private boolean nodeIsInBranch(TreeNode node, TreeNode branch) {
        if (node == branch) {
            return true;
        }
        for (TreeNode child : branch.getChildren()) {
            if (child == node) {
                return true;
            }
            if (!child.isLeaf() && child.getState()) {
                if (nodeIsInBranch(node, child)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Selects a range of items which are in direct relation with each other.<br/>
     * NOTE: The start node <b>MUST</b> be before the end node!
     *
     * @param startNode
     *
     * @param endNode
     */
    private void doRelationSelection(TreeNode startNode, TreeNode endNode) {
        TreeNode currentNode = endNode;
        while (currentNode != startNode) {
            currentNode.setSelected(true);
            selectedIds.add(currentNode.key);

            // Traverse children above the selection
            List<TreeNode> subChildren = currentNode.getParentNode()
                    .getChildren();
            if (subChildren.size() > 1) {
                selectNodeRange(subChildren.iterator().next().key,
                        currentNode.key);
            } else if (subChildren.size() == 1) {
                TreeNode n = subChildren.get(0);
                n.setSelected(true);
                selectedIds.add(n.key);
            }

            currentNode = currentNode.getParentNode();
        }
        startNode.setSelected(true);
        selectedIds.add(startNode.key);
        selectionHasChanged = true;
    }

    /**
     * Selects a range of items which have the same parent.
     *
     * @param startNode
     *            The start node
     * @param endNode
     *            The end node
     */
    private void doSiblingSelection(TreeNode startNode, TreeNode endNode) {
        TreeNode parent = startNode.getParentNode();

        List<TreeNode> children;
        if (parent == null) {
            // Topmost parent
            children = getRootNodes();
        } else {
            children = parent.getChildren();
        }

        // Swap start and end point if needed
        if (children.indexOf(startNode) > children.indexOf(endNode)) {
            TreeNode temp = startNode;
            startNode = endNode;
            endNode = temp;
        }

        Iterator<TreeNode> childIter = children.iterator();
        boolean startFound = false;
        while (childIter.hasNext()) {
            TreeNode node = childIter.next();
            if (node == startNode) {
                startFound = true;
            }

            if (startFound && node != endNode && node.getState()) {
                selectAllChildren(node, true);
            } else if (startFound && node != endNode) {
                node.setSelected(true);
                selectedIds.add(node.key);
            }

            if (node == endNode) {
                node.setSelected(true);
                selectedIds.add(node.key);
                break;
            }
        }
        selectionHasChanged = true;
    }

    /**
     * Returns the first common parent of two nodes
     *
     * @param node1
     *            The first node
     * @param node2
     *            The second node
     * @return The common parent or null
     */
    public TreeNode getCommonGrandParent(TreeNode node1, TreeNode node2) {
        // If either one does not have a grand parent then return null
        if (node1.getParentNode() == null || node2.getParentNode() == null) {
            return null;
        }

        // If the nodes are parents of each other then return null
        if (node1.isGrandParentOf(node2) || node2.isGrandParentOf(node1)) {
            return null;
        }

        // Get parents of node1
        List<TreeNode> parents1 = new ArrayList<TreeNode>();
        TreeNode parent1 = node1.getParentNode();
        while (parent1 != null) {
            parents1.add(parent1);
            parent1 = parent1.getParentNode();
        }

        // Get parents of node2
        List<TreeNode> parents2 = new ArrayList<TreeNode>();
        TreeNode parent2 = node2.getParentNode();
        while (parent2 != null) {
            parents2.add(parent2);
            parent2 = parent2.getParentNode();
        }

        // Search the parents for the first common parent
        for (int i = 0; i < parents1.size(); i++) {
            parent1 = parents1.get(i);
            for (int j = 0; j < parents2.size(); j++) {
                parent2 = parents2.get(j);
                if (parent1 == parent2) {
                    return parent1;
                }
            }
        }

        return null;
    }

    /**
     * Sets the node currently in focus
     *
     * @param node
     *            The node to focus or null to remove the focus completely
     * @param scrollIntoView
     *            Scroll the node into view
     */
    public void setFocusedNode(TreeNode node, boolean scrollIntoView) {
        // Unfocus previously focused node
        if (focusedNode != null) {
            focusedNode.setFocused(false);
        }

        if (node != null) {
            node.setFocused(true);
        }

        focusedNode = node;

        if (node != null && scrollIntoView) {
            /*
             * Delay scrolling the focused node into view if we are still
             * rendering. #5396
             */
            if (!rendering) {
                node.scrollIntoView();
            } else {
                Scheduler.get().scheduleDeferred(new Command() {
                    public void execute() {
                        focusedNode.scrollIntoView();
                    }
                });
            }
        }
    }

    /**
     * Focuses a node and scrolls it into view
     *
     * @param node
     *            The node to focus
     */
    public void setFocusedNode(TreeNode node) {
        setFocusedNode(node, true);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.google.gwt.event.dom.client.FocusHandler#onFocus(com.google.gwt.event
     * .dom.client.FocusEvent)
     */
    public void onFocus(FocusEvent event) {
        treeHasFocus = true;
        // If no node has focus, focus the first item in the tree
        if (focusedNode == null && lastSelection == null && selectable) {
            setFocusedNode(getFirstRootNode(), false);
        } else if (focusedNode != null && selectable) {
            setFocusedNode(focusedNode, false);
        } else if (lastSelection != null && selectable) {
            setFocusedNode(lastSelection, false);
        }

        addStyleDependentName("focus");
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.google.gwt.event.dom.client.BlurHandler#onBlur(com.google.gwt.event
     * .dom.client.BlurEvent)
     */
    public void onBlur(BlurEvent event) {
        treeHasFocus = false;
        if (focusedNode != null) {
            focusedNode.setFocused(false);
        }

        removeStyleDependentName("focus");
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.google.gwt.event.dom.client.KeyPressHandler#onKeyPress(com.google
     * .gwt.event.dom.client.KeyPressEvent)
     */
    public void onKeyPress(KeyPressEvent event) {
        NativeEvent nativeEvent = event.getNativeEvent();
        int keyCode = nativeEvent.getKeyCode();
        if (keyCode == 0 && nativeEvent.getCharCode() == ' ') {
            // Provide a keyCode for space to be compatible with FireFox
            // keypress event
            keyCode = CHARCODE_SPACE;
        }
        if (handleKeyNavigation(keyCode, event.isControlKeyDown()
                || event.isMetaKeyDown(), event.isShiftKeyDown())) {
            event.preventDefault();
            event.stopPropagation();
        }
    }

        /*
     * (non-Javadoc)
     *
     * @see
     * com.google.gwt.event.dom.client.KeyDownHandler#onKeyDown(com.google.gwt
     * .event.dom.client.KeyDownEvent)
     */
    public void onKeyDown(KeyDownEvent event) {
        if (handleKeyNavigation(event.getNativeEvent().getKeyCode(), event
                .isControlKeyDown()
                || event.isMetaKeyDown(), event.isShiftKeyDown())) {
            event.preventDefault();
            event.stopPropagation();
        }
    }

    /**
     * Handles the keyboard navigation
     *
     * @param keycode
     *            The keycode of the pressed key
     * @param ctrl
     *            Was ctrl pressed
     * @param shift
     *            Was shift pressed
     * @return Returns true if the key was handled, else false
     */
    protected boolean handleKeyNavigation(int keycode, boolean ctrl,
            boolean shift) {

        // Navigate down
        if (keycode == getNavigationDownKey()) {
            TreeNode node = null;
            // If node is open and has children then move in to the children
            if (!focusedNode.isLeaf() && focusedNode.getState()
                    && focusedNode.getChildren().size() > 0) {
                node = focusedNode.getChildren().get(0);
            }

            // Else move down to the next sibling
            else {
                node = getNextSibling(focusedNode);
                if (node == null) {
                    // Else jump to the parent and try to select the next
                    // sibling there
                    TreeNode current = focusedNode;
                    while (node == null && current.getParentNode() != null) {
                        node = getNextSibling(current.getParentNode());
                        current = current.getParentNode();
                    }
                }
            }

            if (node != null) {
                setFocusedNode(node);
                if (selectable) {
                    if (!ctrl && !shift) {
                        selectNode(node, true);
                    } else if (shift && isMultiselect) {
                        deselectAll();
                        selectNodeRange(lastSelection.key, node.key);
                    } else if (shift) {
                        selectNode(node, true);
                    }
                }
            }
            return true;
        }

        // Navigate up
        if (keycode == getNavigationUpKey()) {
            TreeNode prev = getPreviousSibling(focusedNode);
            TreeNode node = null;
            if (prev != null) {
                node = getLastVisibleChildInTree(prev);
            } else if (focusedNode.getParentNode() != null) {
                node = focusedNode.getParentNode();
            }
            if (node != null) {
                setFocusedNode(node);
                if (selectable) {
                    if (!ctrl && !shift) {
                        selectNode(node, true);
                    } else if (shift && isMultiselect) {
                        deselectAll();
                        selectNodeRange(lastSelection.key, node.key);
                    } else if (shift) {
                        selectNode(node, true);
                    }
                }
            }
            return true;
        }

        // Navigate left (close branch)
        if (keycode == getNavigationLeftKey()) {
            if (!focusedNode.isLeaf() && focusedNode.getState()) {
                focusedNode.setState(false, true);
            } else if (focusedNode.getParentNode() != null
                    && (focusedNode.isLeaf() || !focusedNode.getState())) {

                if (ctrl || !selectable) {
                    setFocusedNode(focusedNode.getParentNode());
                } else if (shift) {
                    doRelationSelection(focusedNode.getParentNode(),
                            focusedNode);
                    setFocusedNode(focusedNode.getParentNode());
                } else {
                    focusAndSelectNode(focusedNode.getParentNode());
                }
            }
            return true;
        }

        // Navigate right (open branch)
        if (keycode == getNavigationRightKey()) {
            if (!focusedNode.isLeaf() && !focusedNode.getState()) {
                focusedNode.setState(true, true);
            } else if (!focusedNode.isLeaf()) {
                if (ctrl || !selectable) {
                    setFocusedNode(focusedNode.getChildren().get(0));
                } else if (shift) {
                    setSelected(focusedNode, true);
                    setFocusedNode(focusedNode.getChildren().get(0));
                    setSelected(focusedNode, true);
                } else {
                    focusAndSelectNode(focusedNode.getChildren().get(0));
                }
            }
            return true;
        }

        // Selection
        if (keycode == getNavigationSelectKey()) {
            if (!focusedNode.isSelected()) {
                selectNode(
                        focusedNode,
                        (!isMultiselect || multiSelectMode == MULTISELECT_MODE_SIMPLE)
                                && selectable);
            } else {
                deselectNode(focusedNode);
            }
            return true;
        }

        // Home selection
        if (keycode == getNavigationStartKey()) {
            TreeNode node = getFirstRootNode();
            if (ctrl || !selectable) {
                setFocusedNode(node);
            } else if (shift) {
                deselectAll();
                selectNodeRange(focusedNode.key, node.key);
            } else {
                selectNode(node, true);
            }
            sendSelectionToServer();
            return true;
        }

        // End selection
        if (keycode == getNavigationEndKey()) {
            TreeNode lastNode = getLastRootNode();
            TreeNode node = getLastVisibleChildInTree(lastNode);
            if (ctrl || !selectable) {
                setFocusedNode(node);
            } else if (shift) {
                deselectAll();
                selectNodeRange(focusedNode.key, node.key);
            } else {
                selectNode(node, true);
            }
            sendSelectionToServer();
            return true;
        }

        return false;
    }

    private void focusAndSelectNode(TreeNode node) {
        /*
         * Keyboard navigation doesn't work reliably if the tree is in
         * multiselect mode as well as isNullSelectionAllowed = false. It first
         * tries to deselect the old focused node, which fails since there must
         * be at least one selection. After this the newly focused node is
         * selected and we've ended up with two selected nodes even though we
         * only navigated with the arrow keys.
         *
         * Because of this, we first select the next node and later de-select
         * the old one.
         */
        TreeNode oldFocusedNode = focusedNode;
        setFocusedNode(node);
        setSelected(focusedNode, true);
        setSelected(oldFocusedNode, false);
    }

    /**
     * Traverses the tree to the bottom most child
     *
     * @param root
     *            The root of the tree
     * @return The bottom most child
     */
    private TreeNode getLastVisibleChildInTree(TreeNode root) {
        if (root.isLeaf() || !root.getState() || root.getChildren().size() == 0) {
            return root;
        }
        List<TreeNode> children = root.getChildren();
        return getLastVisibleChildInTree(children.get(children.size() - 1));
    }


    /**
     * Gets the next sibling in the tree
     *
     * @param node
     *            The node to get the sibling for
     * @return The sibling node or null if the node is the last sibling
     */
    private TreeNode getNextSibling(TreeNode node) {
        TreeNode parent = node.getParentNode();
        List<TreeNode> children;
        if (parent == null) {
            children = getRootNodes();
        } else {
            children = parent.getChildren();
        }

        int idx = children.indexOf(node);
        if (idx < children.size() - 1) {
            return children.get(idx + 1);
        }

        return null;
    }

    /**
     * Returns the previous sibling in the tree
     *
     * @param node
     *            The node to get the sibling for
     * @return The sibling node or null if the node is the first sibling
     */
    private TreeNode getPreviousSibling(TreeNode node) {
        TreeNode parent = node.getParentNode();
        List<TreeNode> children;
        if (parent == null) {
            children = getRootNodes();
        } else {
            children = parent.getChildren();
        }

        int idx = children.indexOf(node);
        if (idx > 0) {
            return children.get(idx - 1);
        }

        return null;
    }

    /**
     * Add this to the element mouse down event by using element.setPropertyJSO
     * ("onselectstart",applyDisableTextSelectionIEHack()); Remove it then again
     * when the mouse is depressed in the mouse up event.
     *
     * @return Returns the JSO preventing text selection
     */
    private native JavaScriptObject applyDisableTextSelectionIEHack()
    /*-{
            return function(){ return false; };
    }-*/;

    /**
     * Get the key that moves the selection head upwards. By default it is the
     * up arrow key but by overriding this you can change the key to whatever
     * you want.
     *
     * @return The keycode of the key
     */
    protected int getNavigationUpKey() {
        return KeyCodes.KEY_UP;
    }

    /**
     * Get the key that moves the selection head downwards. By default it is the
     * down arrow key but by overriding this you can change the key to whatever
     * you want.
     *
     * @return The keycode of the key
     */
    protected int getNavigationDownKey() {
        return KeyCodes.KEY_DOWN;
    }

    /**
     * Get the key that scrolls to the left in the table. By default it is the
     * left arrow key but by overriding this you can change the key to whatever
     * you want.
     *
     * @return The keycode of the key
     */
    protected int getNavigationLeftKey() {
        return KeyCodes.KEY_LEFT;
    }

    /**
     * Get the key that scroll to the right on the table. By default it is the
     * right arrow key but by overriding this you can change the key to whatever
     * you want.
     *
     * @return The keycode of the key
     */
    protected int getNavigationRightKey() {
        return KeyCodes.KEY_RIGHT;
    }

    /**
     * Get the key that selects an item in the table. By default it is the space
     * bar key but by overriding this you can change the key to whatever you
     * want.
     *
     * @return
     */
    protected int getNavigationSelectKey() {
        return CHARCODE_SPACE;
    }

    /**
     * Get the key the moves the selection one page up in the table. By default
     * this is the Page Up key but by overriding this you can change the key to
     * whatever you want.
     *
     * @return
     */
    protected int getNavigationPageUpKey() {
        return KeyCodes.KEY_PAGEUP;
    }

    /**
     * Get the key the moves the selection one page down in the table. By
     * default this is the Page Down key but by overriding this you can change
     * the key to whatever you want.
     *
     * @return
     */
    protected int getNavigationPageDownKey() {
        return KeyCodes.KEY_PAGEDOWN;
    }

    /**
     * Get the key the moves the selection to the beginning of the table. By
     * default this is the Home key but by overriding this you can change the
     * key to whatever you want.
     *
     * @return
     */
    protected int getNavigationStartKey() {
        return KeyCodes.KEY_HOME;
    }

    /**
     * Get the key the moves the selection to the end of the table. By default
     * this is the End key but by overriding this you can change the key to
     * whatever you want.
     *
     * @return
     */
    protected int getNavigationEndKey() {
        return KeyCodes.KEY_END;
    }

    private boolean isIE6OrOpera() {
        return BrowserInfo.get().isIE6() || BrowserInfo.get().isOpera();
    }

}
