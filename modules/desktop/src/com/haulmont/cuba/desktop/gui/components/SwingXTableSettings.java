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

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.desktop.DesktopConfig;
import com.haulmont.cuba.gui.components.Table;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.dom4j.Element;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.TableColumnExt;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.*;
import java.util.List;

/**
 */
public class SwingXTableSettings implements TableSettings {

    protected final Logger log = LoggerFactory.getLogger(SwingXTableSettings.class);

    protected JXTable table;
    protected List<Table.Column> columns;

    public SwingXTableSettings(JXTable table, List<Table.Column> columns) {
        this.table = table;
        this.columns = columns;
    }

    @Override
    public boolean saveSettings(Element element) {
        element.addAttribute("horizontalScroll", String.valueOf(table.isHorizontalScrollEnabled()));

        saveFontPreferences(element);

        Element columnsElem = element.element("columns");
        if (columnsElem != null) {
            element.remove(columnsElem);
        }
        columnsElem = element.addElement("columns");

        final List<TableColumn> visibleTableColumns = table.getColumns();
        final List<Table.Column> visibleColumns = new ArrayList<>();
        for (TableColumn tableColumn : visibleTableColumns) {
            visibleColumns.add((Table.Column) tableColumn.getIdentifier());
        }

        List<TableColumn> columns = table.getColumns(true);
        Collections.sort(
                columns,
                new Comparator<TableColumn>() {
                    @SuppressWarnings("SuspiciousMethodCalls")
                    @Override
                    public int compare(TableColumn col1, TableColumn col2) {
                        if (col1 instanceof TableColumnExt && !((TableColumnExt) col1).isVisible()) {
                            return 1;
                        }
                        if (col2 instanceof TableColumnExt && !((TableColumnExt) col2).isVisible()) {
                            return -1;
                        }
                        int i1 = visibleColumns.indexOf(col1.getIdentifier());
                        int i2 = visibleColumns.indexOf(col2.getIdentifier());
                        return Integer.compare(i1, i2);
                    }
                }
        );

        for (TableColumn column : columns) {
            Element colElem = columnsElem.addElement("column");
            colElem.addAttribute("id", column.getIdentifier().toString());

            int width = column.getWidth();
            colElem.addAttribute("width", String.valueOf(width));

            if (column instanceof TableColumnExt) {
                Boolean visible = ((TableColumnExt) column).isVisible();
                colElem.addAttribute("visible", visible.toString());
            }
        }

        if (table.getRowSorter() != null) {
            TableColumn sortedColumn = table.getSortedColumn();
            List<? extends RowSorter.SortKey> sortKeys = table.getRowSorter().getSortKeys();
            if (sortedColumn != null && !sortKeys.isEmpty()) {
                columnsElem.addAttribute("sortColumn", String.valueOf(sortedColumn.getIdentifier()));
                columnsElem.addAttribute("sortOrder", sortKeys.get(0).getSortOrder().toString());
            }
        }

        return true;
    }

    @Override
    public void apply(Element element, boolean sortable) {
        String horizontalScroll = element.attributeValue("horizontalScroll");
        if (!StringUtils.isBlank(horizontalScroll)) {
            table.setHorizontalScrollEnabled(Boolean.valueOf(horizontalScroll));
        }

        loadFontPreferences(element);

        final Element columnsElem = element.element("columns");
        if (columnsElem == null) {
            return;
        }

        Collection<String> modelIds = new LinkedList<>();
        for (TableColumn modelColumn : table.getColumns(true)) {
            modelIds.add(String.valueOf(modelColumn.getIdentifier()));
        }

        Collection<String> loadedIds = new LinkedList<>();
        for (Element colElem : Dom4j.elements(columnsElem, "column")) {
            String id = colElem.attributeValue("id");
            loadedIds.add(id);
        }

        Configuration configuration = AppBeans.get(Configuration.NAME);
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);

        if (clientConfig.getLoadObsoleteSettingsForTable()
                || CollectionUtils.isEqualCollection(modelIds, loadedIds)) {
            applyColumnSettings(element, sortable);
        }
    }

    protected void applyColumnSettings(Element element, boolean sortable) {
        final Element columnsElem = element.element("columns");
        // do not allow duplicates
        Collection<Table.Column> sequence = new LinkedHashSet<>();

        for (Element colElem : Dom4j.elements(columnsElem, "column")) {
            String id = colElem.attributeValue("id");
            Table.Column column = getColumn(id);
            if (column != null) {
                sequence.add(column);

                TableColumnExt tableColumn = table.getColumnExt(column);

                if (tableColumn != null) {
                    String width = colElem.attributeValue("width");
                    if (StringUtils.isNotEmpty(width)) {
                        tableColumn.setPreferredWidth(Integer.parseInt(width));
                    }

                    String visible = colElem.attributeValue("visible");
                    if (StringUtils.isNotEmpty(visible)) {
                        tableColumn.setVisible(Boolean.valueOf(visible));
                    }
                }
            }
        }
        table.setColumnSequence(sequence.toArray(new Object[sequence.size()]));

        if (sortable && table.getRowSorter() != null) {
            String sortColumn = columnsElem.attributeValue("sortColumn");
            if (sortColumn != null) {
                SortOrder sortOrder = SortOrder.valueOf(columnsElem.attributeValue("sortOrder"));
                int sortColumnIndex = -1;

                if (!StringUtils.isNumeric(sortColumn)) {
                    Table.Column column = getColumn(sortColumn);
                    if (column != null) {
                        sortColumnIndex = columns.indexOf(column);
                    }
                } else {
                    // backward compatibility
                    sortColumnIndex = Integer.parseInt(sortColumn);
                }

                if (sortColumnIndex >= 0) {
                    table.getRowSorter().setSortKeys(Collections.singletonList(new RowSorter.SortKey(sortColumnIndex, sortOrder)));
                }
            } else {
                table.getRowSorter().setSortKeys(null);
            }
        }

        table.revalidate();
        table.repaint();
    }

    protected void saveFontPreferences(Element element) {
        if (table.getFont() != null) {
            Font font = table.getFont();
            Map<TextAttribute, ?> attributes = font.getAttributes();
            // save content font
            element.addAttribute("fontFamily", font.getFamily());
            element.addAttribute("fontSize", Integer.toString(font.getSize()));
            element.addAttribute("fontStyle", Integer.toString(font.getStyle()));
            element.addAttribute("fontUnderline",
                    Boolean.toString(attributes.get(TextAttribute.UNDERLINE) == TextAttribute.UNDERLINE_ON));
        }
    }

    protected void loadFontPreferences(Element element) {
        // load font preferences
        String fontFamily = element.attributeValue("fontFamily");
        String fontSize = element.attributeValue("fontSize");
        String fontStyle = element.attributeValue("fontStyle");
        String fontUnderline = element.attributeValue("fontUnderline");
        if (!StringUtils.isBlank(fontFamily) &&
                !StringUtils.isBlank(fontSize) &&
                !StringUtils.isBlank(fontUnderline) &&
                !StringUtils.isBlank(fontStyle)) {

            try {
                int size = Integer.parseInt(fontSize);
                int style = Integer.parseInt(fontStyle);

                String[] availableFonts = GraphicsEnvironment.getLocalGraphicsEnvironment()
                        .getAvailableFontFamilyNames();
                int fontIndex = Arrays.asList(availableFonts).indexOf(fontFamily);
                if (fontIndex < 0) {
                    log.debug("Unsupported font family, font settings not loaded");
                    return;
                }

                Configuration configuration = AppBeans.get(Configuration.NAME);
                DesktopConfig desktopConfig = configuration.getConfig(DesktopConfig.class);
                int sizeIndex = desktopConfig.getAvailableFontSizes().indexOf(size);

                if (sizeIndex < 0) {
                    log.debug("Unsupported font size, font settings not loaded");
                    return;
                }

                Boolean underline = BooleanUtils.toBooleanObject(fontUnderline);
                if (sizeIndex < 0) {
                    log.debug("Broken underline property in font definition, skip");
                }

                @SuppressWarnings("MagicConstant")
                Font font = new Font(fontFamily, style, size);
                if (underline != null && Boolean.TRUE.equals(underline)) {
                    Map<TextAttribute, Integer> attributes = new HashMap<>();
                    attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                    font = font.deriveFont(attributes);
                }
                table.setFont(font);
            } catch (NumberFormatException ex) {
                log.debug("Broken font definition in user setting");
            }
        }
    }

    protected Table.Column getColumn(String id) {
        for (Table.Column column : columns) {
            if (column.getId().toString().equals(id)) {
                return column;
            }
        }
        return null;
    }
}