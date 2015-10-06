/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.filestorage.amazon;

import com.haulmont.cuba.core.app.FileStorageAPI;
import com.haulmont.cuba.core.app.filestorage.amazon.auth.AWS4SignerBase;
import com.haulmont.cuba.core.app.filestorage.amazon.auth.AWS4SignerForAuthorizationHeader;
import com.haulmont.cuba.core.app.filestorage.amazon.auth.AWS4SignerForChunkedUpload;
import com.haulmont.cuba.core.app.filestorage.amazon.util.HttpUtils;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.FileStorageException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class AmazonS3FileStorage implements FileStorageAPI {
    @Inject
    protected Configuration configuration;

    @Override
    public long saveStream(FileDescriptor fileDescr, InputStream inputStream) throws FileStorageException {
        AmazonConfiguration amazonConfiguration = configuration.getConfig(AmazonConfiguration.class);
        URL amazonUrl = getAmazonUrl(fileDescr);

        // set the markers indicating we're going to send the upload as a series
        // of chunks:
        //   -- 'x-amz-content-sha256' is the fixed marker indicating chunked
        //      upload
        //   -- 'content-length' becomes the total size in bytes of the upload
        //      (including chunk headers),
        //   -- 'x-amz-decoded-content-length' is used to transmit the actual
        //      length of the data payload, less chunk headers
        Map<String, String> headers = new HashMap<>();
        headers.put("x-amz-storage-class", "REDUCED_REDUNDANCY");
        headers.put("x-amz-content-sha256", AWS4SignerForChunkedUpload.STREAMING_BODY_SHA256);
        headers.put("content-encoding", "aws-chunked");
        headers.put("x-amz-decoded-content-length", "" + fileDescr.getSize());

        AWS4SignerForChunkedUpload signer = new AWS4SignerForChunkedUpload(
                amazonUrl, "PUT", "s3", amazonConfiguration.getRegionName());

        // how big is the overall request stream going to be once we add the signature
        // 'headers' to each chunk?
        long totalLength = AWS4SignerForChunkedUpload.calculateChunkedContentLength(
                fileDescr.getSize(), amazonConfiguration.getChunkSize());
        headers.put("content-length", "" + totalLength);

        String authorization = signer.computeSignature(headers,
                null, // no query parameters
                AWS4SignerForChunkedUpload.STREAMING_BODY_SHA256,
                amazonConfiguration.getAccessKey(),
                amazonConfiguration.getSecretAccessKey());

        // place the computed signature into a formatted 'Authorization' header
        // and call S3
        headers.put("Authorization", authorization);

        // start consuming the data payload in blocks which we subsequently chunk; this prefixes
        // the data with a 'chunk header' containing signature data from the prior chunk (or header
        // signing, if the first chunk) plus length and other data. Each completed chunk is
        // written to the request stream and to complete the upload, we send a final chunk with
        // a zero-length data payload.

        try {
            // first set up the connection
            HttpURLConnection connection = HttpUtils.createHttpConnection(amazonUrl, "PUT", headers);

            // get the request stream and start writing the user data as chunks, as outlined
            // above;
            byte[] buffer = new byte[4096];
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

            int bytesRead;
            while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) != -1) {
                // process into a chunk
                byte[] chunk = signer.constructSignedChunk(bytesRead, buffer);

                // send the chunk
                outputStream.write(chunk);
                outputStream.flush();
            }

            // last step is to send a signed zero-length chunk to complete the upload
            byte[] finalChunk = signer.constructSignedChunk(0, buffer);
            outputStream.write(finalChunk);
            outputStream.flush();
            outputStream.close();

            // make the call to Amazon S3
            HttpUtils.HttpResponse httpResponse = HttpUtils.executeHttpRequest(connection);
            if (!httpResponse.isStatusOk()) {
                throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION,
                        "Could not save file " + getFileName(fileDescr));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error when sending chunked upload request", e);
        }

        return fileDescr.getSize();
    }

    @Override
    public void saveFile(FileDescriptor fileDescr, byte[] data) throws FileStorageException {
        checkNotNullArgument(data, "File content is null");
        saveStream(fileDescr, new ByteArrayInputStream(data));
    }

    @Override
    public void removeFile(FileDescriptor fileDescr) throws FileStorageException {
        URL amazonUrl = getAmazonUrl(fileDescr);

        // for a simple DELETE, we have no body so supply the precomputed 'empty' hash
        Map<String, String> headers = new HashMap<>();
        headers.put("x-amz-content-sha256", AWS4SignerBase.EMPTY_BODY_SHA256);

        String authorization = createAuthorizationHeader(amazonUrl, "DELETE", headers);

        headers.put("Authorization", authorization);
        HttpUtils.HttpResponse httpResponse = HttpUtils.invokeHttpRequest(amazonUrl, "DELETE", headers, null);
        if (!httpResponse.isStatusOk()) {
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION,
                    "Could not remove file " + getFileName(fileDescr));
        }
    }

    @Override
    public InputStream openStream(FileDescriptor fileDescr) throws FileStorageException {
        URL amazonUrl = getAmazonUrl(fileDescr);

        // for a simple GET, we have no body so supply the precomputed 'empty' hash
        Map<String, String> headers = new HashMap<>();
        headers.put("x-amz-content-sha256", AWS4SignerBase.EMPTY_BODY_SHA256);

        String authorization = createAuthorizationHeader(amazonUrl, "GET", headers);

        headers.put("Authorization", authorization);
        HttpUtils.HttpResponse httpResponse = HttpUtils.invokeHttpRequest(amazonUrl, "GET", headers, null);

        if (httpResponse.isStatusOk()) {
            return httpResponse.getInputStream();
        } else if (httpResponse.isStatusNotFound()) {
            throw new FileStorageException(FileStorageException.Type.FILE_NOT_FOUND,
                    "File not found" + getFileName(fileDescr));
        } else {
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION,
                    "Could not get file " + getFileName(fileDescr));
        }
    }

    @Override
    public byte[] loadFile(FileDescriptor fileDescr) throws FileStorageException {
        InputStream inputStream = openStream(fileDescr);
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, fileDescr.getId().toString(), e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    @Override
    public boolean fileExists(FileDescriptor fileDescr) {
        URL amazonUrl = getAmazonUrl(fileDescr);

        // for a simple HEAD, we have no body so supply the precomputed 'empty' hash
        Map<String, String> headers = new HashMap<>();
        headers.put("x-amz-content-sha256", AWS4SignerBase.EMPTY_BODY_SHA256);

        String authorization = createAuthorizationHeader(amazonUrl, "HEAD", headers);

        headers.put("Authorization", authorization);
        HttpUtils.HttpResponse httpResponse = HttpUtils.invokeHttpRequest(amazonUrl, "HEAD", headers, null);
        return httpResponse.isStatusOk();
    }

    protected String resolveFileName(FileDescriptor fileDescr) {
        return getStorageDir(fileDescr.getCreateDate()) + "/" + getFileName(fileDescr);
    }

    /**
     * INTERNAL. Don't use in application code.
     */
    protected String getStorageDir(Date createDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(createDate);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return String.format("%d/%s/%s",
                year, StringUtils.leftPad(String.valueOf(month), 2, '0'), StringUtils.leftPad(String.valueOf(day), 2, '0'));
    }

    protected URL getAmazonUrl(FileDescriptor fileDescr) {
        // the region-specific endpoint to the target object expressed in path style
        try {
            AmazonConfiguration amazonConfiguration = configuration.getConfig(AmazonConfiguration.class);
            return new URL(String.format("https://%s.s3.amazonaws.com/%s",
                    amazonConfiguration.getBucket(), resolveFileName(fileDescr)));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Unable to parse service endpoint: " + e.getMessage());
        }
    }

    protected String getFileName(FileDescriptor fileDescriptor) {
        return fileDescriptor.getId().toString() + "." + fileDescriptor.getExtension();
    }

    protected String createAuthorizationHeader(URL endpointUrl, String method, Map<String, String> headers) {
        AmazonConfiguration amazonConfiguration = configuration.getConfig(AmazonConfiguration.class);
        AWS4SignerForAuthorizationHeader signer = new AWS4SignerForAuthorizationHeader(
                endpointUrl, method, "s3", amazonConfiguration.getRegionName());
        return signer.computeSignature(headers,
                null, // no query parameters
                AWS4SignerBase.EMPTY_BODY_SHA256,
                amazonConfiguration.getAccessKey(),
                amazonConfiguration.getSecretAccessKey());
    }
}
