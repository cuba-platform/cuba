package com.haulmont.cuba.gui.data;

import com.haulmont.cuba.gui.data.CollectionDatasource.Sortable.SortInfo;
import org.junit.jupiter.api.Test;

import static com.haulmont.cuba.gui.data.CollectionDatasource.Sortable.Order.ASC;
import static com.haulmont.cuba.gui.data.CollectionDatasource.Sortable.Order.DESC;
import static org.junit.jupiter.api.Assertions.*;

public class CollectionDsSortInfoTest {

    @Test
    public void twoSortInfoShouldBeEqualsIfThemFieldsTheSame() throws Exception {
        SortInfo<Object> expected = new SortInfo<>();
        expected.setPropertyPath(1);
        expected.setOrder(ASC);

        SortInfo<Object> actual = new SortInfo<>();
        actual.setPropertyPath(1);
        actual.setOrder(ASC);

        assertEquals(expected, actual);
    }

    @Test
    public void inOtherCasesSortInfoShouldBeDifferent() throws Exception {
        SortInfo<Object> expected = new SortInfo<>();
        expected.setPropertyPath(1);
        expected.setOrder(ASC);

        SortInfo<Object> actual = new SortInfo<>();
        actual.setPropertyPath(1);
        actual.setOrder(DESC);

        assertNotEquals(expected, actual);

        actual.setPropertyPath(2);
        actual.setOrder(ASC);

        assertNotEquals(expected, actual);
    }
}