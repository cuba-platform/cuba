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
package com.haulmont.cuba.core.app;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.entity.AppFolder;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Folder;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.SearchFolder;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.ExternalizableConverter;
import com.thoughtworks.xstream.core.DefaultConverterLookup;
import com.thoughtworks.xstream.core.util.ClassLoaderReference;
import com.thoughtworks.xstream.io.xml.XppDriver;
import groovy.lang.Binding;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Service(FoldersService.NAME)
public class FoldersServiceBean implements FoldersService {

    protected Logger log = LoggerFactory.getLogger(getClass());

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

    @Inject
    protected TimeSource timeSource;

    @Override
    public List<AppFolder> loadAppFolders() {
        log.debug("Loading AppFolders");

        StopWatch stopWatch = new Log4JStopWatch("AppFolders");
        stopWatch.start();

        List<AppFolder> result = new ArrayList<>();
        List<AppFolder> list = new ArrayList<>();

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            MetaClass effectiveMetaClass = metadata.getExtendedEntities().getEffectiveMetaClass(AppFolder.class);
            TypedQuery<AppFolder> q = em.createQuery(
                    "select f from " + effectiveMetaClass.getName() + " f order by f.sortOrder, f.name",
                    AppFolder.class);
            list = q.getResultList();

            for (AppFolder folder : list) {
                folder.getParent(); // fetch parent
                result.add(folder);
            }
            tx.commit();
        } finally {
            tx.end();
            stopWatch.stop();
        }

        if (!list.isEmpty()) {
            Binding binding = new Binding();
            binding.setVariable("persistence", persistence);
            binding.setVariable("metadata", metadata);
            binding.setVariable("userSession", userSessionSource.getUserSession());

            for (AppFolder folder : list) {
                Transaction folderTx = persistence.createTransaction();
                try {
                    boolean evaluatedVisibilityScript = true;
                    try {
                        if (!StringUtils.isBlank(folder.getVisibilityScript())) {
                            binding.setVariable("folder", folder);
                            Boolean visible = runScript(folder.getVisibilityScript(), binding);
                            if (BooleanUtils.isFalse(visible)) {
                                continue;
                            }
                        }
                    } catch (Exception e) {
                        log.warn(String.format("Unable to evaluate AppFolder visibility script for folder: id: %s ," +
                                " name: %s", folder.getId(), folder.getName()), e);
                        // because EclipseLink Query marks transaction as rollback-only on JPQL syntax errors
                        evaluatedVisibilityScript = false;
                    }

                    boolean evaluatedQuantityScript = loadFolderQuantity(binding, folder);

                    if (evaluatedVisibilityScript && evaluatedQuantityScript) {
                        folderTx.commit();
                    }
                } finally{
                    folderTx.end();
                }
            }
        }

        return result;
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

        try {
            if (!folders.isEmpty()) {
                Binding binding = new Binding();
                for (AppFolder folder : folders) {
                    Transaction tx = persistence.createTransaction();
                    try {
                        if (loadFolderQuantity(binding, folder)) {
                            tx.commit();
                        }
                    } finally {
                        tx.end();
                    }
                }
            }

            return folders;
        } finally {
            stopWatch.stop();
        }
    }

    protected boolean loadFolderQuantity(Binding binding, AppFolder folder) {
        if (!StringUtils.isBlank(folder.getQuantityScript())) {
            binding.setVariable("persistence", persistence);
            binding.setVariable("metadata", metadata);
            String variable = "style";
            binding.setVariable("folder", folder);
            binding.setVariable(variable, null);

            try {
                Number qty = runScript(folder.getQuantityScript(), binding);
                folder.setItemStyle((String) binding.getVariable(variable));
                folder.setQuantity(qty == null ? null : qty.intValue());
            } catch (Exception e) {
                log.warn(String.format("Unable to evaluate AppFolder quantity script for folder: id: %s ," +
                        " name: %s", folder.getId(), folder.getName()), e);
                return false;
            }
        }

        return true;
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
        zipOutputStream.setEncoding(StandardCharsets.UTF_8.name());
        String xml = createXStream().toXML(folder);
        byte[] xmlBytes = xml.getBytes(StandardCharsets.UTF_8);
        ArchiveEntry zipEntryDesign = newStoredEntry("folder.xml", xmlBytes);
        zipOutputStream.putArchiveEntry(zipEntryDesign);
        zipOutputStream.write(xmlBytes);
        try {
            zipOutputStream.closeArchiveEntry();
        } catch (Exception ex) {
            throw new RuntimeException(String.format("Exception occurred while exporting folder %s.",  folder.getName()));
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
            checkImportPermissions(folder);
            folder.setParent(parentFolder);
            Transaction tx = persistence.createTransaction();
            try {
                EntityManager em = persistence.getEntityManager();
                em.setSoftDeletion(false);
                Folder existingFolder = em.find(Folder.class, folder.getId());
                if (existingFolder != null) {
                    checkImportPermissions(existingFolder);
                    folder.setVersion(existingFolder.getVersion());
                    folder.setCreateTs(existingFolder.getCreateTs());
                    folder.setCreatedBy(existingFolder.getCreatedBy());
                } else {
                    User user = userSessionSource.getUserSession().getUser();
                    folder.setCreatedBy(user.getLoginLowerCase());
                    folder.setCreateTs(timeSource.currentTimestamp());
                    folder.setUpdatedBy(null);
                    folder.setUpdateTs(null);
                    folder.setVersion(0);
                }

                em.merge(folder);
                tx.commit();
            } finally {
                tx.end();
            }
        }
        return folder;
    }

    protected void checkImportPermissions(Folder folder) {
        UserSession userSession = userSessionSource.getUserSession();
        if (folder instanceof SearchFolder) {
            SearchFolder searchFolder = (SearchFolder) folder;
            User currentUser = userSession.getCurrentOrSubstitutedUser();
            if (searchFolder.getUser() != null && !currentUser.equals(searchFolder.getUser())) {
                throw new AccessDeniedException(PermissionType.ENTITY_OP, Folder.class.getSimpleName());
            }
            if (searchFolder.getUser() == null && !userSession.isSpecificPermitted("cuba.gui.searchFolder.global")) {
                throw new AccessDeniedException(PermissionType.ENTITY_OP, Folder.class.getSimpleName());
            }
        }
        if (folder instanceof AppFolder) {
            if (!userSession.isSpecificPermitted("cuba.gui.appFolder.global")) {
                throw new AccessDeniedException(PermissionType.ENTITY_OP, Folder.class.getSimpleName());
            }
        }
    }

    protected XStream createXStream() {
        XStream xStream = new XStream(null, new XppDriver(),
                new ClassLoaderReference(Thread.currentThread().getContextClassLoader()),
                null, new DefaultConverterLookup(), null);
        //createTs and createdBy removed from BaseGenericIdEntity,
        //and import from old versions (platform 6.2) is performed with errors
        //so omit field processing
        xStream.omitField(BaseGenericIdEntity.class, "createTs");
        xStream.omitField(BaseGenericIdEntity.class, "createdBy");
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
}