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
import com.haulmont.cuba.core.sys.CubaXStream;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.SearchFolder;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import com.thoughtworks.xstream.XStream;
import groovy.lang.Binding;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;

@Service(FoldersService.NAME)
public class FoldersServiceBean implements FoldersService {

    private final Logger log = LoggerFactory.getLogger(FoldersServiceBean.class);

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
    @Inject
    protected DataManager dataManager;

    @Override
    public List<AppFolder> loadAppFolders() {
        log.debug("Loading AppFolders");

        StopWatch stopWatch = new Slf4JStopWatch("AppFolders");
        stopWatch.start();

        List<AppFolder> resultList;
        try (Transaction tx = persistence.createTransaction()) {
            String metaClassName = metadata.getExtendedEntities().getEffectiveMetaClass(AppFolder.class).getName();
            TypedQuery<AppFolder> q = persistence.getEntityManager().createQuery(
                    "select f from " + metaClassName + " f order by f.sortOrder, f.name", AppFolder.class);

            resultList = q.getResultList();
            // fetch parent folder
            resultList.forEach(Folder::getParent);

            tx.commit();
        } finally {
            stopWatch.stop();
        }

        if (CollectionUtils.isNotEmpty(resultList)) {
            Binding binding = new Binding();
            binding.setVariable("persistence", persistence);
            binding.setVariable("metadata", metadata);
            binding.setVariable("userSession", userSessionSource.getUserSession());

            Iterator<AppFolder> iterator = resultList.iterator();
            while (iterator.hasNext()) {
                AppFolder folder = iterator.next();
                try (Transaction tx = persistence.createTransaction()) {
                    boolean evaluatedVisibilityScript = true;
                    try {
                        if (!StringUtils.isBlank(folder.getVisibilityScript())) {
                            binding.setVariable("folder", folder);
                            Boolean visible = runScript(folder.getVisibilityScript(), binding);
                            if (BooleanUtils.isFalse(visible)) {
                                iterator.remove();
                                continue;
                            }
                        }
                    } catch (Exception e) {
                        log.warn("Unable to evaluate AppFolder visibility script for folder: id: {}  name: {}",
                                folder.getId(), folder.getName(), e);
                        // because EclipseLink Query marks transaction as rollback-only on JPQL syntax errors
                        evaluatedVisibilityScript = false;
                    }

                    boolean evaluatedQuantityScript = loadFolderQuantity(binding, folder);

                    if (evaluatedVisibilityScript && evaluatedQuantityScript) {
                        tx.commit();
                    }
                }
            }
        }

        return resultList;
    }

    protected <T> T runScript(String script, Binding binding) {
        script = StringUtils.trim(script);
        if (script.endsWith(".groovy")) {
            script = resources.getResourceAsString(script);
        }
        Object result = scripting.evaluateGroovy(script, binding);
        //noinspection unchecked
        return (T) result;
    }

    @Override
    public List<AppFolder> reloadAppFolders(List<AppFolder> folders) {
        log.debug("Reloading AppFolders {}", folders);

        StopWatch stopWatch = new Slf4JStopWatch("AppFolders");
        stopWatch.start();

        try {
            if (!folders.isEmpty()) {
                Binding binding = new Binding();
                binding.setVariable("persistence", persistence);
                binding.setVariable("metadata", metadata);
                binding.setProperty("userSession", userSessionSource.getUserSession());

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
            binding.setVariable("folder", folder);

            String styleVariable = "style";
            binding.setVariable(styleVariable, null);

            try {
                Number qty = runScript(folder.getQuantityScript(), binding);
                folder.setItemStyle((String) binding.getVariable(styleVariable));
                folder.setQuantity(qty == null ? null : qty.intValue());
            } catch (Exception e) {
                log.warn("Unable to evaluate AppFolder quantity script for folder: id: {} , name: {}",
                        folder.getId(), folder.getName(), e);
                return false;
            }
        }

        return true;
    }

    @Override
    public List<SearchFolder> loadSearchFolders() {
        log.debug("Loading SearchFolders");

        StopWatch stopWatch = new Slf4JStopWatch("SearchFolders");
        stopWatch.start();

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            MetaClass effectiveMetaClass = metadata.getExtendedEntities().getEffectiveMetaClass(SearchFolder.class);
            TypedQuery<SearchFolder> q = em.createQuery("select f from "+ effectiveMetaClass.getName() +" f " +
                    "left join f.user u on u.id = ?1 " +
                    "where (u.id = ?1 or u is null) " +
                    "order by f.sortOrder, f.name",
                    SearchFolder.class);
            q.setViewName("searchFolders");
            q.setParameter(1, userSessionSource.currentOrSubstitutedUserId());
            List<SearchFolder> list = q.getResultList();
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
        ZipArchiveEntry zipEntryDesign = newStoredEntry("folder.xml", xmlBytes);
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
            throw new AccessDeniedException(PermissionType.ENTITY_OP, EntityOp.CREATE, Folder.class.getSimpleName());
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
            if (folder.equals(parentFolder)) {
                throw new RuntimeException("Cannot import the folder to itself. Select another parent folder.");
            }

            try (Transaction tx = persistence.createTransaction()) {
                List<Folder> allParentFolders = findAllParentFolders(parentFolder, new ArrayList<>());
                if (allParentFolders.contains(folder)) {
                    throw new RuntimeException("Cannot import the folder. The imported folder is found among ancestors of the target parent folder. " +
                            "Select another parent folder.");
                }
                tx.commit();
            }

            //all parent folders starting with the target parent folder
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

    protected List<Folder> findAllParentFolders(Folder folder, List<Folder> parentFolders) {
        if (folder == null)
            return parentFolders;
        parentFolders.add(folder);
        EntityManager em = persistence.getEntityManager();
        Folder reloadedFolder = em.reload(folder);
        if (reloadedFolder != null && reloadedFolder.getParent() != null) {
            findAllParentFolders(reloadedFolder.getParent(), parentFolders);
        }
        return parentFolders;
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
        XStream xStream = new CubaXStream();
        XStream.setupDefaultSecurity(xStream);
        xStream.allowTypeHierarchy(Serializable.class);
        //createTs and createdBy removed from BaseGenericIdEntity,
        //and import from old versions (platform 6.2) is performed with errors
        //so omit field processing
        xStream.omitField(BaseGenericIdEntity.class, "createTs");
        xStream.omitField(BaseGenericIdEntity.class, "createdBy");
        return xStream;
    }

    protected ZipArchiveEntry newStoredEntry(String name, byte[] data) {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(name);
        zipEntry.setSize(data.length);
        zipEntry.setCompressedSize(zipEntry.getSize());
        CRC32 crc32 = new CRC32();
        crc32.update(data);
        zipEntry.setCrc(crc32.getValue());
        return zipEntry;
    }
}