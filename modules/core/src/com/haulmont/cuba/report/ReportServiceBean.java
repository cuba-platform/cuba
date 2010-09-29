/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 07.05.2010 15:12:05
 *
 * $Id$
 */
package com.haulmont.cuba.report;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.report.app.ReportService;
import com.haulmont.cuba.report.formatters.CustomFormatter;
import com.haulmont.cuba.report.formatters.DocFormatter;
import com.haulmont.cuba.report.formatters.Formatter;
import com.haulmont.cuba.report.formatters.XLSFormatter;
import com.haulmont.cuba.report.loaders.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service(ReportService.NAME)
public class ReportServiceBean implements ReportService {

    private ThreadLocal<Map<String, Object>> params = new ThreadLocal<Map<String, Object>>();

    /*
    *  Simple algorithm for band tree creation
    */

    public byte[] createReport(Report report, ReportOutputType format, Map<String, Object> params) throws IOException {
        try {
            this.params.set(params);
            report = reloadEntity(report, "report.edit");

            if (report.getIsCustom()) {
                return new CustomFormatter(report, params).createDocument(null);
            }

            BandDefinition rootBandDefinition = report.getRootBandDefinition();

            List<BandDefinition> childrenBandDefinitions = rootBandDefinition.getChildrenBandDefinitions();
            Band rootBand = createRootBand(rootBandDefinition);

            for (BandDefinition definition : childrenBandDefinitions) {
                List<Band> bands = createBands(definition, rootBand);
                rootBand.addChildren(bands);
            }
            System.out.println(rootBand);

            Formatter formatter = createFormatter(report, format);
            return formatter.createDocument(rootBand);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Report reloadReport(Report report) {
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            em.setView(MetadataProvider.getViewRepository().getView(report.getClass(), "report.export"));
            report = em.find(Report.class, report.getId());
            reloadBandDefinitions(report.getRootBandDefinition());
            tx.commit();
            return report;
        } finally {
            tx.end();
        }
    }

    public byte[] exportReports(Collection<Report> reports) throws IOException, FileStorageException {
        return ImportExportHelper.exportReports(reports);
    }

    public Collection<Report> importReports(byte[] zipBytes) throws IOException, FileStorageException {
        return ImportExportHelper.importReports(zipBytes);
    }

    private void reloadBandDefinitions(BandDefinition bd) {
        if (bd.getChildrenBandDefinitions() != null) {
            for (BandDefinition d : bd.getChildrenBandDefinitions()) {
                reloadBandDefinitions(d);
            }
        }
    }

    //todo: move to another service

    private Formatter createFormatter(Report report, ReportOutputType format) throws IOException {
        if (ReportOutputType.XLS.equals(format)) {
            return new XLSFormatter(report.getTemplateFileDescriptor());
        } else return new DocFormatter(report.getTemplateFileDescriptor(), format);
    }

    private Band createRootBand(BandDefinition rootBandDefinition) {
        Band rootBand = new Band(rootBandDefinition.getName(), 1, null, rootBandDefinition.getOrientation());
        List<Map<String, Object>> data = getBandData(rootBandDefinition, null);
        if (data.size() > 0)
            rootBand.setData(data.get(0));
        return rootBand;
    }

    private List<Map<String, Object>> getBandData(BandDefinition definition, Band parentBand) {
        List<DataSet> dataSets = definition.getDataSets();
        if (dataSets == null || dataSets.size() == 0)
            return Collections.singletonList(params.get());//add input params to band

        DataSet firstDataSet = dataSets.get(0);

        Map<String, Object> paramsMap = params.get();

        List<Map<String, Object>> result;

        result = getDataSetData(parentBand, firstDataSet, paramsMap);//gets data from first dataset

        for (int i = 1; i < dataSets.size(); i++) {//adds data from second and following datasets to result
            List<Map<String, Object>> dataSetData = getDataSetData(parentBand, dataSets.get(i), paramsMap);
            for (int j = 0; (j < result.size()) && (j < dataSetData.size()); j++) {
                result.get(j).putAll(dataSetData.get(j));
            }
        }
        for (Map<String, Object> map : result) {
            map.putAll(params.get());//add input params to band
        }

        return result;
    }

    private List<Map<String, Object>> getDataSetData(Band parentBand, DataSet dataSet, Map<String, Object> paramsMap) {
        List<Map<String, Object>> result = null;
        DataSetType dataSetType = dataSet.getType();

        if (DataSetType.SQL.equals(dataSetType)) {
            result = new SqlDataDataLoader(paramsMap).loadData(dataSet, parentBand);
        } else if (DataSetType.GROOVY.equals(dataSetType)) {
            result = new GroovyDataLoader(paramsMap).loadData(dataSet, parentBand);
        } else if (DataSetType.JPQL.equals(dataSetType)) {
            result = new JpqlDataDataLoader(paramsMap).loadData(dataSet, parentBand);
        } else if (DataSetType.SINGLE.equals(dataSetType)) {
            result = new SingleEntityDataLoader(paramsMap).loadData(dataSet, parentBand);
        } else if (DataSetType.MULTI.equals(dataSetType)) {
            result = new MultiEntityDataLoader(paramsMap).loadData(dataSet, parentBand);
        }
        return result;
    }

    /*
    *   Create band from band definition
    *   Perform query from definition and create band from each result row. Do it recursive down
    */

    private List<Band> createBands(BandDefinition definition, Band parentBand) {
        definition = reloadEntity(definition, "report.edit");
        List<Map<String, Object>> outputData = getBandData(definition, parentBand);
        List<Band> bandsList = createBandsList(definition, parentBand, outputData);
        return bandsList;
    }

    private List<Band> createBandsList(BandDefinition definition, Band parentBand, List<Map<String, Object>> outputData) {
        List<Band> bandsList = new ArrayList<Band>();
        for (Map<String, Object> data : outputData) {
            Band band = new Band(definition.getName(), parentBand.getLevel() + 1, parentBand, definition.getOrientation());
            band.setData(data);
            List<BandDefinition> childrenBandDefinitions = definition.getChildrenBandDefinitions();
            for (BandDefinition childDefinition : childrenBandDefinitions) {
                List<Band> childBands = createBands(childDefinition, band);
                band.addChildren(childBands);
            }
            bandsList.add(band);
        }
        return bandsList;
    }

    private <T extends Entity> T reloadEntity(T entity, String viewName) {
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            em.setView(MetadataProvider.getViewRepository().getView(entity.getClass(), viewName));
            entity = (T) em.find(entity.getClass(), entity.getId());
            tx.commit();
            return entity;
        } finally {
            tx.end();
        }
    }
}
