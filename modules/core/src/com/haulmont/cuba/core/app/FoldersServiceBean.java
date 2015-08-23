/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.entity.AppFolder;
import com.haulmont.cuba.core.entity.Folder;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.SearchFolder;
import com.haulmont.cuba.security.entity.User;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.ExternalizableConverter;
import groovy.lang.Binding;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.zip.CRC32;

/**
 * @author krivopustov
 * @version $Id$
 */
@Service(FoldersService.NAME)
public class FoldersServiceBean implements FoldersService {

    protected Log log = LogFactory.getLog(getClass());

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected Metadata metadata;

    @Inject
    protected Persistence persistence;

    @Inject
    protected Scripting scripting;

    @Inject
    protected Resources resources;

    @Inject
    protected Security security;

    @Override
    public List<AppFolder> loadAppFolders() {
        log.debug("Loading AppFolders");

        StopWatch stopWatch = new Log4JStopWatch("AppFolders");
        stopWatch.start();

        List<AppFolder> result = new ArrayList<>();

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            MetaClass effectiveMetaClass = metadata.getExtendedEntities().getEffectiveMetaClass(AppFolder.class);
            TypedQuery<AppFolder> q = em.createQuery(
                    "select f from " + effectiveMetaClass.getName() + " f order by f.sortOrder, f.name",
                    AppFolder.class);
            List<AppFolder> list = q.getResultList();

            if (!list.isEmpty()) {
                Binding binding = new Binding();
                binding.setVariable("persistence", persistence);
                binding.setVariable("metadata", metadata);
                binding.setVariable("userSession", userSessionSource.getUserSession());
                for (AppFolder folder : list) {
                    try {
                        if (!StringUtils.isBlank(folder.getVisibilityScript())) {
                            binding.setVariable("folder", folder);
                            Boolean visible = runScript(folder.getVisibilityScript(), binding);
                            if (BooleanUtils.isFalse(visible))
                                continue;
                        }
                    } catch (Exception e) {
                        log.warn(String.format("Unable to evaluate AppFolder visibility script for folder: id: %s ," +
                                " name: %s", folder.getId(), folder.getName()), e);
                        //continue;
                    }

                    loadFolderQuantity(binding, folder);

                    folder.getParent(); // fetch parent
                    result.add(folder);
                }
            }

            tx.commit();
            return result;
        } finally {
            tx.end();

            stopWatch.stop();
        }
    }

    protected  <T> T runScript(String script, Binding binding) {
        Object result;
        script = StringUtils.trim(script);
        if (script.endsWith(".groovy")) {
            script = resources.getResourceAsString(script);
        }
        result = scripting.evaluateGroovy(script, binding);
        return (T) result;
    }

    @Override
    public List<AppFolder> reloadAppFolders(List<AppFolder> folders) {
        log.debug("Reloading AppFolders " + folders);

        StopWatch stopWatch = new Log4JStopWatch("AppFolders");
        stopWatch.start();

        Transaction tx = persistence.createTransaction();
        try {
            if (!folders.isEmpty()) {
                Binding binding = new Binding();
                for (AppFolder folder : folders) {
                    loadFolderQuantity(binding, folder);
                }
            }

            tx.commit();
            return folders;
        } finally {
            tx.end();

            stopWatch.stop();
        }
    }

    protected void loadFolderQuantity(Binding binding, AppFolder folder) {
        try {
            if (!StringUtils.isBlank(folder.getQuantityScript())) {
                binding.setVariable("persistence", persistence);
                binding.setVariable("metadata", metadata);
                String variable = "style";
                binding.setVariable("folder", folder);
                binding.setVariable(variable, null);
                Number qty = runScript(folder.getQuantityScript(), binding);
                folder.setItemStyle((String) binding.getVariable(variable));
                folder.setQuantity(qty == null ? null : qty.intValue());
            }
        } catch (Exception e) {
            log.warn(String.format("Unable to evaluate AppFolder quantity script for folder: id: %s ," +
                    " name: %s", folder.getId(), folder.getName()), e);
        }
    }

    @Override
    public List<SearchFolder> loadSearchFolders() {
        log.debug("Loading SearchFolders");

        StopWatch stopWatch = new Log4JStopWatch("SearchFolders");
        stopWatch.start();

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            MetaClass effectiveMetaClass = metadata.getExtendedEntities().getEffectiveMetaClass(SearchFolder.class);
            TypedQuery<SearchFolder> q = em.createQuery("select f from "+ effectiveMetaClass.getName() +" f " +
                    "left join fetch f.user u on u.id = ?1 " +
                    "left join fetch f.presentation " +
                    "order by f.sortOrder, f.name",
                    SearchFolder.class);
            q.setParameter(1, userSessionSource.currentOrSubstitutedUserId());
            List<SearchFolder> list = q.getResultList();
            // fetch parents
            for (SearchFolder folder : list) {
                folder.getParent();
            }

            tx.commit();
            return list;
        } finally {
            tx.end();

            stopWatch.stop();
        }
    }

    @Override
    public byte[] exportFolder(Folder folder) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        ZipArchiveOutputStream zipOutputStream = new ZipArchiveOutputStream(byteArrayOutputStream);
        zipOutputStream.setMethod(ZipArchiveOutputStream.STORED);
        zipOutputStream.setEncoding("UTF-8");
        String xml = createXStream().toXML(folder);
        byte[] xmlBytes = xml.getBytes(StandardCharsets.UTF_8);
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

    @Override
    public Folder importFolder(Folder parentFolder, byte[] bytes) throws IOException {
        if (!security.isEntityOpPermitted(Folder.class, EntityOp.CREATE)) {
            throw new AccessDeniedException(PermissionType.ENTITY_OP, Folder.class.getSimpleName());
        }

        Folder folder = null;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ZipArchiveInputStream archiveReader;
        archiveReader = new ZipArchiveInputStream(byteArrayInputStream);
        ZipArchiveEntry archiveEntry;

        while (((archiveEntry = archiveReader.getNextZipEntry()) != null) && (folder == null)) {
            if (archiveEntry.getName().equals("folder.xml")) {
                String xml = new String(IOUtils.toByteArray(archiveReader), StandardCharsets.UTF_8);
                folder = (Folder) createXStream().fromXML(xml);
            }
        }

        byteArrayInputStream.close();

        if (folder != null) {
            folder = resetAttributes(folder);
            folder.setParent(parentFolder);
            Transaction tx = persistence.createTransaction();
            try {
                EntityManager em = persistence.getEntityManager();
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

    private XStream createXStream() {
        XStream xStream = new XStream();
        xStream.getConverterRegistry().removeConverter(ExternalizableConverter.class);
        return xStream;
    }

    private ArchiveEntry newStoredEntry(String name, byte[] data) {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(name);
        zipEntry.setSize(data.length);
        zipEntry.setCompressedSize(zipEntry.getSize());
        CRC32 crc32 = new CRC32();
        crc32.update(data);
        zipEntry.setCrc(crc32.getValue());
        return zipEntry;
    }

    protected Folder resetAttributes(Folder folder) {
        User user = userSessionSource.getUserSession().getUser();
        folder.setCreatedBy(user.getLoginLowerCase());
        folder.setId(UUID.randomUUID());
        folder.setCreateTs(new Date());
        folder.setUpdatedBy(null);
        folder.setUpdateTs(null);

        return folder;
    }
}
