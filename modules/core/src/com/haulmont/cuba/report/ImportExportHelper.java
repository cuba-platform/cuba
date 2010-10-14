/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Vasiliy Fontanenko
 * Created: 12.07.2010 9:04:06
 *
 * $Id$
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
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.report.app.ReportService;
import com.javamex.arcmexer.ArchiveEntry;
import com.javamex.arcmexer.ArchiveFormatException;
import com.javamex.arcmexer.ArchiveReader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.ExternalizableConverter;
import org.apache.commons.io.IOUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.zip.CRC32;

public class ImportExportHelper {
    private static final String ENCODING = "CP866";

    public static byte[] exportReports(Collection<Report> reports) throws IOException, FileStorageException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
        zipOutputStream.setMethod(ZipEntry.STORED);
        zipOutputStream.setEncoding(ENCODING);
        for (Report report : reports) {
            try {
                byte[] reportBytes = exportReport(report);
                ZipEntry singleReportEntry = newStoredEntry(replaceForbiddenCharacters(report.getName()) + ".zip", reportBytes);
                zipOutputStream.putNextEntry(singleReportEntry);
                zipOutputStream.write(reportBytes);
            } catch (Exception ex) {
                throw new RuntimeException("Exception occured while exporting report\"" + report.getName() + "\".", ex);
            }
        }
        zipOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Exports single report to ZIP archive whith name <report name>.zip. There are 2 files in archive: report.xml and a template file (odt, xls or other..)
     *
     * @param report Report object that must be exported.
     * @return ZIP archive as a byte array.
     * @throws IOException
     * @throws FileStorageException
     */
    private static byte[] exportReport(Report report) throws IOException, FileStorageException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
        zipOutputStream.setMethod(ZipEntry.STORED);
        zipOutputStream.setEncoding(ENCODING);
        report = ((ReportService) Locator.lookup(ReportService.NAME)).reloadReport(report);
        String xml = toXML(report);
        byte[] xmlBytes = xml.getBytes();
        ZipEntry zipEntryReportObject = newStoredEntry("report.xml", xmlBytes);
        zipOutputStream.putNextEntry(zipEntryReportObject);
        zipOutputStream.write(xmlBytes);
        FileDescriptor fd = report.getTemplateFileDescriptor();
        if (fd != null) {
            FileStorageAPI mbean = Locator.lookup(FileStorageAPI.NAME);
            byte[] fileBytes = mbean.loadFile(fd);
            ZipEntry zipEntryTemplate = newStoredEntry(fd.getName(), fileBytes);
            zipOutputStream.putNextEntry(zipEntryTemplate);
            zipOutputStream.write(fileBytes);
        }
        zipOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    private static ZipEntry newStoredEntry(String name, byte[] data) {
        ZipEntry zipEntry = new ZipEntry(name);
        zipEntry.setSize(data.length);
        zipEntry.setCompressedSize(zipEntry.getSize());
        CRC32 crc32 = new CRC32();
        crc32.update(data);
        zipEntry.setCrc(crc32.getValue());
        return zipEntry;
    }

    private static String replaceForbiddenCharacters(String fileName) {
        return fileName.replaceAll("[\\,/,:,\\*,\",<,>,\\|]", "");
    }

    private static String toXML(Object o) {
        XStream xStream = createXStream(o.getClass());
        return xStream.toXML(o);
    }

    private static <T> T fromXML(Class clazz, String xml) {
        XStream xStream = createXStream(clazz);
        Object o = xStream.fromXML(xml);
        return (T) o;
    }

    private static XStream createXStream(Class clazz) {
        XStream xStream = new XStream();
        xStream.getConverterRegistry().removeConverter(ExternalizableConverter.class);
        xStream.alias(clazz.getSimpleName(), clazz);
        // todo: reimplement - use recursion
        for (Field field : clazz.getDeclaredFields()) {
            Class cl = field.getType();
            xStream.alias(cl.getSimpleName(), cl);
        }
        return xStream;
    }

    private static void addAlias(XStream xStream, Class clazz, HashSet<Class> knownClasses) {
        if (!knownClasses.contains(clazz) && Entity.class.isAssignableFrom(clazz)) {
            xStream.alias(clazz.getSimpleName(), clazz);
            knownClasses.add(clazz);
            for (Field field : clazz.getDeclaredFields()) {
                addAlias(xStream, field.getType(), knownClasses);
            }
            for (Field field : clazz.getFields()) {
                addAlias(xStream, field.getType(), knownClasses);
            }
        }
    }

    public static Collection<Report> importReports(byte[] zipBytes) throws IOException, FileStorageException {
        LinkedList<Report> reports = null;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(zipBytes);
        ArchiveReader archiveReader;
        try {
            archiveReader = ArchiveReader.getReader(byteArrayInputStream);
            ArchiveEntry archiveEntry;
            while ((archiveEntry = archiveReader.nextEntry()) != null) {
                if (reports == null) {
                    reports = new LinkedList<Report>();
                }
                Report report = importReport(IOUtils.toByteArray(archiveEntry.getInputStream()));
                reports.add(report);
            }
        } catch (ArchiveFormatException e) {
            throw new IllegalArgumentException("Wrong report archive format. Report not imported.");
        }
        byteArrayInputStream.close();
        return reports;
    }

    private static Report importReport(byte[] zipBytes) throws IOException, FileStorageException {
        Report report = null;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(zipBytes);
        ArchiveReader archiveReader;
        try {
            archiveReader = ArchiveReader.getReader(byteArrayInputStream);
            ArchiveEntry archiveEntry;
            // importing report.xml to report object
            while ((archiveEntry = archiveReader.nextEntry()) != null) {
                if (archiveEntry.getFilename().equals("report.xml")) {
                    String xml = new String(IOUtils.toByteArray(archiveEntry.getInputStream()));
                    report = fromXML(Report.class, xml);
                    break;
                }
            }

            byteArrayInputStream.close();
            // importring template files
            // not using zipInputStream.reset here because marks not supported.
            byteArrayInputStream = new ByteArrayInputStream(zipBytes);
            while ((archiveEntry = archiveReader.nextEntry()) != null) {
                if (!archiveEntry.getFilename().equals("report.xml")) {
                    FileDescriptor fd = report.getTemplateFileDescriptor();
                    FileStorageAPI mbean = Locator.lookup(FileStorageAPI.NAME);
                    try {
                        mbean.removeFile(fd);
                    } catch (FileStorageException e) {/*Do nothing*/}
                    mbean.saveFile(fd, IOUtils.toByteArray(archiveEntry.getInputStream()));
                }
            }
        } catch (ArchiveFormatException e) {
            throw new IllegalArgumentException("Wrong report archive format. Report not imported.");
        }
        byteArrayInputStream.close();

        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            Report exisitngReport = em.find(Report.class, report.getId());
            if (exisitngReport != null) {
                em.remove(exisitngReport);
                em.flush();
            }
            tx.commit();
        } finally {
            tx.end();
        }

        tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            if (PersistenceHelper.isNew(report)) {
                em.persist(report);
            } else {
                em.merge(report);
            }
            tx.commit();
        } finally {
            tx.end();
        }
        return report;
    }
}
