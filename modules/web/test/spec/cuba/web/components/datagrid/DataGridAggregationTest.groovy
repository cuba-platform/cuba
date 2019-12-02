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

package spec.cuba.web.components.datagrid

import com.haulmont.cuba.gui.components.AggregationInfo
import com.haulmont.cuba.gui.components.DataGrid
import com.haulmont.cuba.gui.components.data.datagrid.ContainerDataGridItems
import com.haulmont.cuba.gui.model.CollectionContainer
import com.haulmont.cuba.gui.screen.OpenMode
import com.haulmont.cuba.web.gui.components.WebDataGrid
import com.haulmont.cuba.web.testmodel.sample.GoodStatistic
import spec.cuba.web.UiScreenSpec
import spec.cuba.web.components.datagrid.screens.DataGridAggregationScreen

@SuppressWarnings(["GroovyAccessibility", "GroovyAssignabilityCheck"])
class DataGridAggregationTest extends UiScreenSpec {

    private CollectionContainer<GoodStatistic> container

    void setup() {
        exportScreensPackages(['spec.cuba.web.components.datagrid.screens'])
    }

    def "add header at index"() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create("mainWindow", OpenMode.ROOT)
        screens.show(mainWindow)

        def dataGridScreen = screens.create(DataGridAggregationScreen)
        dataGridScreen.show()

        def dataGrid = (WebDataGrid) dataGridScreen.getWindow().getComponent("aggregationTopDataGrid")

        when: "getting header aggregation row at 1 position"
        dataGrid.getHeaderRow(1)
        then: "error must occur, because aggregation row not taken into account"
        thrown(IndexOutOfBoundsException)

        when: "adding header at 1 position and getting it from dataGrid"
        def header = (DataGrid.HeaderRow) dataGrid.addHeaderRowAt(1)
        def addedHeader = dataGrid.getHeaderRow(1)
        then: "it should be the same header"
        addedHeader == header

        when: "add header at 3 position"
        dataGrid.addHeaderRowAt(3)
        then: "error must occur, because for now it must contain only two headers"
        thrown(IndexOutOfBoundsException)
    }

    def "append header row"() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create("mainWindow", OpenMode.ROOT)
        screens.show(mainWindow)

        def dataGridScreen = screens.create(DataGridAggregationScreen)
        dataGridScreen.show()

        def dataGrid = (WebDataGrid) dataGridScreen.getWindow().getComponent("aggregationTopDataGrid")

        when: "appending header row and getting it from dataGrid"
        def header = dataGrid.appendHeaderRow()
        def addedHeader = dataGrid.getHeaderRow(1)
        then: "it must be the same row"
        header == addedHeader
    }

    def "header rows size should be independent from aggregation row"() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create("mainWindow", OpenMode.ROOT)
        screens.show(mainWindow)

        def dataGridScreen = screens.create(DataGridAggregationScreen)
        dataGridScreen.show()

        when: "getting a DataGrid with aggregation enabled"
        def dataGrid = (WebDataGrid) dataGridScreen.getWindow().getComponent("aggregationTopDataGrid")
        then: """aggregation row must be added and public API getHeaderRowCount() must return a value without
                 regard to the aggregation row, even though it physically stores it"""
        def internalHeaders = dataGrid.headerRows.size()
        def publicHeaders = dataGrid.getHeaderRowCount()
        internalHeaders == 2
        publicHeaders == 1

        when: "data items are added to a DataGrid with aggregation enabled"
        setDataGridItems(dataGrid)
        then: """aggregation row must be added and public API getHeaderRowCount() must return a value without
                 regard to the aggregation row, even though it physically stores it"""
        dataGrid.headerRows.size() == internalHeaders
        dataGrid.getHeaderRowCount() == publicHeaders

        when: "disabling aggregation in a DataGrid"
        dataGrid.setAggregatable(false)
        then: """aggregation row is removed and public API getHeaderRowCount() must return the same value when the
                 aggregation was enabled"""
        dataGrid.headerRows.size() == 1               // internal headers
        dataGrid.getHeaderRowCount() == publicHeaders // public headers
    }

    def "add footer row at index"() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create("mainWindow", OpenMode.ROOT)
        screens.show(mainWindow)

        def dataGridScreen = screens.create(DataGridAggregationScreen)
        dataGridScreen.show()

        def dataGrid = (WebDataGrid) dataGridScreen.getWindow().getComponent("aggregationBottomDataGrid")

        when: "getting aggregation row at 0 position"
        dataGrid.getFooterRow(0)
        then: "error occurs, because for now it does not contain footers"
        thrown(IndexOutOfBoundsException)

        when: "adding footer row and getting it from dataGrid"
        def footer = dataGrid.addFooterRowAt(0)
        def addedFooter = dataGrid.getFooterRow(0)
        then: "it should be the same footer"
        footer == addedFooter

        when: "getting footer at 2 position"
        dataGrid.addFooterRowAt(2)
        then: "error occurs, because for now it contains only one footer"
        thrown(IndexOutOfBoundsException)
    }

    def "prepend footer row"() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create("mainWindow", OpenMode.ROOT)
        screens.show(mainWindow)

        def dataGridScreen = screens.create(DataGridAggregationScreen)
        dataGridScreen.show()

        def dataGrid = (WebDataGrid) dataGridScreen.getWindow().getComponent("aggregationBottomDataGrid")

        when: "prepending footer and getting it from dataGrid"
        def footer = dataGrid.prependFooterRow()
        def addedFooter = dataGrid.getFooterRow(0)
        then: "it must be the same footer"
        footer == addedFooter
    }

    def "footer rows size should be independent from aggregation row"() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create("mainWindow", OpenMode.ROOT)
        screens.show(mainWindow)

        def dataGridScreen = screens.create(DataGridAggregationScreen)
        dataGridScreen.show()

        when: "getting a DataGrid with aggregation enabled"
        def dataGrid = (WebDataGrid) dataGridScreen.getWindow().getComponent("aggregationBottomDataGrid")
        then: """aggregation row must be added and public API getFooterRowCount() must return a value without regard to
                 the aggregation row, even though it physically stores it"""
        def internalFooters = dataGrid.footerRows.size()
        def publicFooters = dataGrid.getFooterRowCount()
        internalFooters == 1
        publicFooters == 0

        when: "data items are added to a DataGrid with aggregation enabled"
        setDataGridItems(dataGrid)
        then: """aggregation row must be added and public API getFooterRowCount() must return a value without
                 regard to the aggregation row, even though it physically stores it"""
        dataGrid.footerRows.size() == internalFooters // internal footers
        dataGrid.getFooterRowCount() == publicFooters // public footers

        when: "disabling aggregation in a DataGrid"
        dataGrid.setAggregatable(false)
        then: """aggregation row must be removed and public API getFooterRowCount() must return the same value
                 when the aggregation was enabled"""
        dataGrid.footerRows.size() == 0               // internal footers
        dataGrid.getFooterRowCount() == publicFooters // public footers
    }

    def "aggregate values"() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create("mainWindow", OpenMode.ROOT)
        screens.show(mainWindow)

        def dataGridScreen = screens.create(DataGridAggregationScreen)
        dataGridScreen.show()

        def dataGrid = (WebDataGrid) dataGridScreen.getWindow().getComponent("aggregationTopDataGrid")
        setDataGridItems(dataGrid)

        when: "aggregating values"
        def aggregationMap = dataGrid.getAggregationResults()
        then: "aggregation results should be calculated"
        aggregationMap.name == 2      // count
        aggregationMap.count == 20    // min
        aggregationMap.price == 105.5 // avg
        aggregationMap.sales == 20    // max
        aggregationMap.usages == 76.0 // sum
    }

    def "update aggregated values"() {
        def dataGrid = uiComponents.create(DataGrid)
        addAggregatedColumn(dataGrid, "count", AggregationInfo.Type.MIN)
        addAggregatedColumn(dataGrid, "price", AggregationInfo.Type.AVG)
        addAggregatedColumn(dataGrid, "sales", AggregationInfo.Type.MAX)
        addAggregatedColumn(dataGrid, "usages", AggregationInfo.Type.SUM)
        setDataGridItems(dataGrid)

        when: "updating values in container"
        def container = ((ContainerDataGridItems<GoodStatistic>) dataGrid.getItems()).getContainer()
        def entity = container.getMutableItems().find({it.name == "stat1"})
        entity.setCount(5l)
        entity.setSales(10)
        entity.setPrice(105.5)
        entity.setUsages(15.5)

        then: "aggregation values should be updated"
        def aggregationMap = dataGrid.getAggregationResults()
        aggregationMap.count == 5l   // min
        aggregationMap.price == 98d   // avg
        aggregationMap.sales ==  20  // max
        aggregationMap.usages ==  66 // sum
    }

    protected void setDataGridItems(DataGrid dataGrid) {
        container = dataComponents.createCollectionContainer(GoodStatistic)
        container.setItems(createEntities())
        dataGrid.setItems(new ContainerDataGridItems(container))
    }

    protected List<GoodStatistic> createEntities() {
        GoodStatistic statistic1 = new GoodStatistic()
        statistic1.name = "stat1"
        statistic1.count = 20l
        statistic1.sales = 15
        statistic1.price = 120.5
        statistic1.usages = 25.5

        GoodStatistic statistic2 = new GoodStatistic()
        statistic2.name = "stat2"
        statistic2.count = 25l
        statistic2.sales = 20
        statistic2.price = 90.5
        statistic2.usages = 50.5
        return new ArrayList<GoodStatistic>(Arrays.asList(statistic1, statistic2))
    }

    protected void addAggregatedColumn(DataGrid dataGrid, String id, AggregationInfo.Type type) {
        def metaClass = metadata.getClass(GoodStatistic)

        def mmp = metaClass.getPropertyPath(id)
        dataGrid.addColumn(id, mmp)

        def aggregationInfo = new AggregationInfo()
        aggregationInfo.setPropertyPath(mmp)
        aggregationInfo.setType(type)

        def column = dataGrid.getColumn(id)
        column.setAggregation(aggregationInfo)
    }
}
