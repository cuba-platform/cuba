/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.client.sys;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.client.sys.fileupload.InputStreamProgressEntity;
import com.haulmont.cuba.core.app.FileStorageService;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.remoting.LocalFileExchangeService;
import com.haulmont.cuba.core.sys.remoting.discovery.ServerSelector;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.util.function.Supplier;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

@Component(FileLoader.NAME)
public class FileLoaderClientImpl implements FileLoader {

    private static final Logger log = LoggerFactory.getLogger(FileLoaderClientImpl.class);

    @Resource(name = ServerSelector.NAME)
    protected ServerSelector serverSelector;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected Configuration configuration;

    @Inject
    protected FileStorageService fileStorageService;

    @Override
    public void saveStream(FileDescriptor fd, Supplier<InputStream> inputStreamSupplier) throws FileStorageException {
        try {
            saveStream(fd, inputStreamSupplier, null);
        } catch (InterruptedException e) {
            // should never happen
            throw new RuntimeException("Interrupted exception on stream saving");
        }
    }

    @Override
    public void saveStream(FileDescriptor fd, Supplier<InputStream> inputStreamSupplier,
                           @Nullable StreamingProgressListener streamingListener)
            throws FileStorageException, InterruptedException {
        checkNotNullArgument(fd);
        checkNotNullArgument(inputStreamSupplier);

        String useLocalInvocation = AppContext.getProperty("cuba.useLocalServiceInvocation");
        if (Boolean.parseBoolean(useLocalInvocation)) {
            saveStreamLocally(fd, inputStreamSupplier);
        } else {
            saveStreamWithServlet(fd, inputStreamSupplier, streamingListener);
        }
    }

    @Override
    public InputStream openStream(FileDescriptor fd) throws FileStorageException {
        checkNotNullArgument(fd);

        String useLocalInvocation = AppContext.getProperty("cuba.useLocalServiceInvocation");
        if (Boolean.parseBoolean(useLocalInvocation)) {
            return openStreamLocally(fd);
        } else {
            return openStreamWithServlet(fd);
        }
    }

    @Override
    public void removeFile(FileDescriptor fd) throws FileStorageException {
        checkNotNullArgument(fd);

        fileStorageService.removeFile(fd);
    }

    @Override
    public boolean fileExists(FileDescriptor fd) throws FileStorageException {
        checkNotNullArgument(fd);

        return fileStorageService.fileExists(fd);
    }

    protected void saveStreamWithServlet(FileDescriptor fd, Supplier<InputStream> inputStreamSupplier,
                                         @Nullable StreamingProgressListener streamingListener)
            throws FileStorageException, InterruptedException {

        Object context = serverSelector.initContext();
        String selectedUrl = serverSelector.getUrl(context);
        if (selectedUrl == null) {
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, fd.getName());
        }

        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        String fileUploadContext = clientConfig.getFileUploadContext();

        while (true) {
            String url = selectedUrl
                    + fileUploadContext
                    + "?s=" + userSessionSource.getUserSession().getId()
                    + "&f=" + fd.toUrlParam();

            try (InputStream inputStream = inputStreamSupplier.get()) {
                InputStreamProgressEntity.UploadProgressListener progressListener = null;
                if (streamingListener != null) {
                    progressListener = streamingListener::onStreamingProgressChanged;
                }

                HttpPost method = new HttpPost(url);
                method.setEntity(new InputStreamProgressEntity(inputStream,
                        ContentType.APPLICATION_OCTET_STREAM, progressListener));

                HttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager();
                HttpClient client = HttpClientBuilder.create()
                        .setConnectionManager(connectionManager)
                        .build();
                try {
                    HttpResponse response = client.execute(method);

                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == HttpStatus.SC_OK) {
                        break;
                    } else {
                        log.debug("Unable to upload file to {}\n{}", url, response.getStatusLine());
                        selectedUrl = failAndGetNextUrl(context);
                        if (selectedUrl == null) {
                            throw new FileStorageException(FileStorageException.Type.fromHttpStatus(statusCode), fd.getName());
                        }
                    }
                } catch (InterruptedIOException e) {
                    log.trace("Uploading has been interrupted");
                    throw new InterruptedException("File uploading is interrupted");
                } catch (IOException e) {
                    log.debug("Unable to upload file to {}\n{}", url, e);
                    selectedUrl = failAndGetNextUrl(context);
                    if (selectedUrl == null) {
                        throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, fd.getName(), e);
                    }
                } finally {
                    connectionManager.shutdown();
                }
            } catch (IOException | RetryUnsupportedException e) {
                throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, fd.getName(), e);
            }
        }
    }

    protected void saveStreamLocally(FileDescriptor fd, Supplier<InputStream> inputStream) throws FileStorageException {
        // cannot be injected since there is no implementation for Desktop
        LocalFileExchangeService localFileExchangeService = AppBeans.get(LocalFileExchangeService.NAME);
        localFileExchangeService.uploadFile(inputStream.get(), fd);
    }

    protected InputStream openStreamLocally(FileDescriptor fd) throws FileStorageException {
        // cannot be injected since there is no implementation for Desktop
        LocalFileExchangeService localFileExchangeService = AppBeans.get(LocalFileExchangeService.NAME);
        return localFileExchangeService.downloadFile(fd);
    }

    protected InputStream openStreamWithServlet(FileDescriptor fd) throws FileStorageException {
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        String fileDownloadContext = clientConfig.getFileDownloadContext();

        Object context = serverSelector.initContext();
        String selectedUrl = serverSelector.getUrl(context);
        if (selectedUrl == null) {
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, fd.getName());
        }
        while (true) {
            String url = selectedUrl + fileDownloadContext +
                    "?s=" + userSessionSource.getUserSession().getId() +
                    "&f=" + fd.getId().toString();

            HttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager();
            HttpClient httpClient = HttpClientBuilder.create()
                    .setConnectionManager(connectionManager)
                    .build();

            HttpGet httpGet = new HttpGet(url);

            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                int httpStatus = httpResponse.getStatusLine().getStatusCode();
                if (httpStatus == HttpStatus.SC_OK) {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    if (httpEntity != null) {
                        return httpEntity.getContent();
                    } else {
                        log.debug("Unable to download file from {}\nHttpEntity is null", url);
                        selectedUrl = failAndGetNextUrl(context);
                        if (selectedUrl == null) {
                            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, fd.getName());
                        }
                    }
                } else {
                    log.debug("Unable to download file from {}\n{}", url, httpResponse.getStatusLine());
                    selectedUrl = failAndGetNextUrl(context);
                    if (selectedUrl == null) {
                        throw new FileStorageException(FileStorageException.Type.fromHttpStatus(httpStatus), fd.getName());
                    }
                }
            } catch (InterruptedIOException e) {
                log.trace("Downloading has been interrupted");
                throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, fd.getName(), e);
            } catch (IOException ex) {
                log.debug("Unable to download file from {}\n{}", url, ex);
                selectedUrl = failAndGetNextUrl(context);
                if (selectedUrl == null) {
                    throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, fd.getName(), ex);
                }
            }
        }
    }

    @Nullable
    protected String failAndGetNextUrl(Object context) {
        serverSelector.fail(context);
        String url = serverSelector.getUrl(context);
        if (url != null) {
            log.debug("Trying next URL");
        }
        return url;
    }
}