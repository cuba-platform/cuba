/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package spec.cuba.web.components.composite.components.comments;

import com.google.common.base.Strings;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.ComponentContainer;
import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.VBoxLayout;
import com.haulmont.cuba.gui.components.data.DataGridItems;
import com.haulmont.cuba.gui.components.data.datagrid.ContainerDataGridItems;
import com.haulmont.cuba.gui.components.data.meta.ContainerDataUnit;
import com.haulmont.cuba.gui.components.sys.ShowInfoAction;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.web.gui.components.CompositeComponent;
import com.haulmont.cuba.web.gui.components.CompositeDescriptor;
import com.haulmont.cuba.web.gui.components.CompositeWithCaption;
import com.haulmont.cuba.web.testmodel.compositecomponent.Comment;

import javax.inject.Inject;
import java.util.function.Function;

@CompositeDescriptor("/spec/cuba/web/components/composite/components/comments/commentary-panel.xml")
public class TestCommentaryPanel extends CompositeComponent<VBoxLayout> implements CompositeWithCaption {

    public static final String NAME = "testCommentaryPanel";

    /* Beans */
    @Inject
    private MetadataTools metadataTools;

    /* Nested Components */
    private DataGrid<Comment> commentsDataGrid;
    private TextField<String> messageField;
    private Button sendBtn;

    private CollectionContainer<Comment> collectionContainer;
    private Function<String, Comment> commentProvider;

    public TestCommentaryPanel() {
        getEventHub().subscribe(CreateEvent.class, event ->
                initComponent(getCompositionNN()));
    }

    @Override
    protected void setComposition(VBoxLayout composition) {
        super.setComposition(composition);

        commentsDataGrid = getInnerComponent("commentsDataGrid");
        messageField = getInnerComponent("messageField");
        sendBtn = getInnerComponent("sendBtn");
    }

    private void initComponent(ComponentContainer composition) {
        commentsDataGrid.addGeneratedColumn("comment", new DataGrid.ColumnGenerator<Comment, String>() {
            @Override
            public String getValue(DataGrid.ColumnGeneratorEvent<Comment> event) {
                Comment item = event.getItem();

                StringBuilder sb = new StringBuilder();
                if (item.getCreatedBy() != null || item.getCreateTs() != null) {
                    sb.append("<p class=\"message-info\">");
                    if (item.getCreatedBy() != null) {
                        sb.append("<span>").append(item.getCreatedBy()).append("</span>");
                    }

                    if (item.getCreateTs() != null) {
                        sb.append("<span style=\"float: right;\">")
                                .append(metadataTools.format(item.getCreateTs()))
                                .append("</span>");
                    }
                    sb.append("</p>");
                }

                sb.append("<p class=\"message-text\">").append(item.getText()).append("</p>");

                return sb.toString();
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }
        });
        commentsDataGrid.setRowDescriptionProvider(Comment::getText);

        sendBtn.addClickListener(clickEvent ->
                sendMessage());
        messageField.addEnterPressListener(enterPressEvent ->
                sendMessage());
    }

    private void sendMessage() {
        String messageText = messageField.getValue();
        if (!Strings.isNullOrEmpty(messageText)) {
            addMessage(messageText);
            messageField.clear();
        }
    }

    private void addMessage(String text) {
        if (getCommentProvider() == null) {
            return;
        }

        Comment comment = getCommentProvider().apply(text);

        DataGridItems<Comment> items = commentsDataGrid.getItems();
        if (items instanceof ContainerDataUnit) {
            //noinspection unchecked
            CollectionContainer<Comment> container = ((ContainerDataUnit<Comment>) items).getContainer();
            container.getMutableItems().add(comment);
        } else {
            throw new IllegalStateException("Items must implement com.haulmont.cuba.gui.components.data.meta.ContainerDataUnit");
        }
    }

    public Function<String, Comment> getCommentProvider() {
        return commentProvider;
    }

    public void setCreateCommentProvider(Function<String, Comment> commentProvider) {
        this.commentProvider = commentProvider;
    }

    public CollectionContainer<Comment> getCollectionContainer() {
        return collectionContainer;
    }

    public void setDataContainer(CollectionContainer<Comment> container) {
        this.collectionContainer = container;

        commentsDataGrid.setItems(new ContainerDataGridItems<>(container));
        commentsDataGrid.getColumnNN("comment")
                .setRenderer(commentsDataGrid.createRenderer(DataGrid.HtmlRenderer.class));
        commentsDataGrid.removeAction(ShowInfoAction.ACTION_ID);

        container.addCollectionChangeListener(this::onCollectionChanged);
    }

    private void onCollectionChanged(CollectionContainer.CollectionChangeEvent<Comment> event) {
        commentsDataGrid.scrollToEnd();
    }
}
