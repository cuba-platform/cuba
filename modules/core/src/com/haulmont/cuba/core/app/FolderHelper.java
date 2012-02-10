/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.Folder;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.security.entity.User;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.ExternalizableConverter;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import java.util.zip.CRC32;

/**
 * <p>$Id$</p>
 *
 * @author pavlov
 */

public class FolderHelper {
    protected static final String ENCODING = "UTF-8";

    public static byte[] exportFolder(Folder folder) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        ZipArchiveOutputStream zipOutputStream = new ZipArchiveOutputStream(byteArrayOutputStream);
        zipOutputStream.setMethod(ZipArchiveOutputStream.STORED);
        zipOutputStream.setEncoding(ENCODING);
        String xml = createXStream().toXML(folder);
        byte[] xmlBytes = xml.getBytes();
        ArchiveEntry zipEntryDesign = newStoredEntry("folder.xml", xmlBytes);
        zipOutputStream.putArchiveEntry(zipEntryDesign);
        zipOutputStream.write(xmlBytes);
        try {
            zipOutputStream.closeArchiveEntry();
        } catch (Exception ex) {
            throw new RuntimeException("Exception occured while exporting folder\"" + folder.getName() + "\".", ex);
        }

        zipOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    private static XStream createXStream() {
        XStream xStream = new XStream();
        xStream.getConverterRegistry().removeConverter(ExternalizableConverter.class);
        return xStream;
    }

    private static ArchiveEntry newStoredEntry(String name, byte[] data) {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(name);
        zipEntry.setSize(data.length);
        zipEntry.setCompressedSize(zipEntry.getSize());
        CRC32 crc32 = new CRC32();
        crc32.update(data);
        zipEntry.setCrc(crc32.getValue());
        return zipEntry;
    }

    public static Folder importFolder(Folder parentFolder, byte[] zipBytes) throws IOException {
        Folder folder = null;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(zipBytes);
        ZipArchiveInputStream archiveReader;
        archiveReader = new ZipArchiveInputStream(byteArrayInputStream);
        ZipArchiveEntry archiveEntry;

        while (((archiveEntry = archiveReader.getNextZipEntry()) != null) && (folder == null)) {
            if (archiveEntry.getName().equals("folder.xml")) {
                String xml = new String(IOUtils.toByteArray(archiveReader));
                folder = (Folder) createXStream().fromXML(xml);
            }
        }

        byteArrayInputStream.close();

        if (folder != null) {
            folder = resetAttributes(folder);
            folder.setParent(parentFolder);
            Transaction tx = Locator.createTransaction();
            try {
                EntityManager em = PersistenceProvider.getEntityManager();
                if (PersistenceHelper.isNew(folder)) {
                    em.persist(folder);
                } else {
                    em.merge(folder);
                }
                tx.commit();
            } finally {
                tx.end();
            }
        }
        return folder;
    }

    private static Folder resetAttributes(Folder folder) {
        User user = UserSessionProvider.getUserSession().getUser();
        folder.setCreatedBy(user.getLoginLowerCase());
        folder.setUuid(UUID.randomUUID());
        folder.setCreateTs(new Date());
        folder.setUpdatedBy(null);
        folder.setUpdateTs(null);

        return folder;
    }
}
