/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.report;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.app.FileStorageAPI;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.TimeProvider;
import com.haulmont.cuba.report.exception.ReportFormatterException;
import com.haulmont.cuba.report.formatters.*;
import com.haulmont.cuba.report.formatters.exception.UnsupportedFormatException;
import com.haulmont.cuba.report.loaders.*;
import org.apache.commons.lang.StringUtils;

import javax.annotation.ManagedBean;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
@ManagedBean(ReportingApi.NAME)
public class ReportingBean implements ReportingApi {

    private ThreadLocal<Map<String, Object>> params = new ThreadLocal<Map<String, Object>>();
    private ThreadLocal<Set<String>> bandDefinitionNames = new ThreadLocal<Set<String>>();

    public ReportOutputDocument createReport(Report report, Map<String, Object> params) throws IOException {
        report = reloadEntity(report, "report.edit");
        ReportTemplate reportTemplate = report.getDefaultTemplate();
        return createReportDocument(report, reportTemplate, params);
    }

    public ReportOutputDocument createReport(Report report, String templateCode, Map<String, Object> params) throws IOException {
        report = reloadEntity(report, "report.edit");
        ReportTemplate template = report.getTemplateByCode(templateCode);
        return createReportDocument(report, template, params);
    }

    public ReportOutputDocument createReport(Report report, ReportTemplate template, Map<String, Object> params) throws IOException {
        report = reloadEntity(report, "report.edit");
        return createReportDocument(report, template, params);
    }

    private ReportOutputDocument createReportDocument(Report report, ReportTemplate template, Map<String, Object> params) throws IOException {
        if (template == null)
            throw new NullPointerException("Report template is null");

        try {
            this.params.set(params);
            this.bandDefinitionNames.set(new HashSet<String>());

            if (template.getCustomFlag()) {
                byte[] content = new CustomFormatter(report, template, params).createDocument(null);
                return new ReportOutputDocument(report, template.getReportOutputType(), content);
            }
            BandDefinition rootBandDefinition = report.getRootBandDefinition();

            List<BandDefinition> childrenBandDefinitions = rootBandDefinition.getChildrenBandDefinitions();
            Band rootBand = createRootBand(rootBandDefinition);

            HashMap<String, ReportValueFormat> valuesFormats = new HashMap<String, ReportValueFormat>();
            for (ReportValueFormat valueFormat : report.getValuesFormats()) {
                valuesFormats.put(valueFormat.getValueName(), valueFormat);
            }
            rootBand.setValuesFormats(valuesFormats);

            for (BandDefinition definition : childrenBandDefinitions) {
                bandDefinitionNames.get().add(definition.getName());
                List<Band> bands = createBands(definition, rootBand);
                rootBand.addChildren(bands);
            }
            rootBand.setBandDefinitionNames(bandDefinitionNames.get());

            ByteArrayOutputStream resultStream = new ByteArrayOutputStream();

            ReportEngine reportEngine = getReportEngine(template);
            ReportOutputType format = template.getReportOutputType();

            if (reportEngine.hasSupportReport(
                    template.getTemplateFileDescriptor().getExtension(), format)) {
                reportEngine.setTemplateFile(template.getTemplateFileDescriptor());
                reportEngine.createDocument(rootBand, format, resultStream);
            } else
                throw new UnsupportedFormatException();

            byte[] result = resultStream.toByteArray();
            return new ReportOutputDocument(report, template.getReportOutputType(), result);
        } catch (ReportFormatterException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ReportFormatterException(e);
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

    public FileDescriptor createAndSaveReport(Report report,
                                              Map<String, Object> params, String fileName) throws IOException {
        ReportTemplate template = report.getDefaultTemplate();
        return createAndSaveReport(report, template, params, fileName);
    }

    public FileDescriptor createAndSaveReport(Report report, String templateCode,
                                              Map<String, Object> params, String fileName) throws IOException {
        ReportTemplate template = report.getTemplateByCode(templateCode);
        return createAndSaveReport(report, template, params, fileName);
    }

    public FileDescriptor createAndSaveReport(Report report, ReportTemplate template,
                                              Map<String, Object> params, String fileName) throws IOException {
        report = reloadEntity(report, "_local");

        byte[] reportData = createReport(report, template, params).getContent();

        FileDescriptor file = new FileDescriptor();
        file.setCreateDate(TimeProvider.currentTimestamp());
        String ext = template.getReportOutputType().toString().toLowerCase();
        file.setName(fileName + "." + ext);
        file.setExtension(ext);
        file.setSize(reportData.length);

        try {
            FileStorageAPI mbean = Locator.lookup(FileStorageAPI.NAME);
            mbean.saveFile(file, reportData);
        } catch (FileStorageException e) {
            throw new IOException(e);
        }

        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            em.persist(file);
            tx.commit();
        } finally {
            tx.end();
        }
        return file;
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

    private ReportEngine getReportEngine(ReportTemplate template) {
        ReportEngine reportEngine = null;
        String extension = template.getTemplateFileDescriptor().getExtension();
        if (StringUtils.isNotEmpty(extension)) {
            ReportFileExtension reportExt = ReportFileExtension.fromId(extension.toLowerCase());
            if (reportExt != null) {
                switch (reportExt) {
                    case DOC:
                    case ODT:
                        reportEngine = new DocFormatter();
                        break;

                    case HTML:
                    case HTM:
                        reportEngine = new HtmlFormatter();
                        break;

                    case XLS:
                    case XLT:
                        reportEngine = new XLSFormatter();
                        break;
                }
            } else
                throw new UnsupportedFormatException();
        } else
            throw new UnsupportedFormatException();

        return reportEngine;
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

    /**
     * Create band from band definition
     * Perform query from definition and create band from each result row. Do it recursive down
     *
     * @param definition Band definition
     * @param parentBand Parent band
     * @return Data bands
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
                bandDefinitionNames.get().add(definition.getName());
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
