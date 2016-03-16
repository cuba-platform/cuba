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

package com.haulmont.cuba.core.app.filestorage.amazon.auth;

import com.haulmont.cuba.core.app.filestorage.amazon.util.BinaryUtils;

import java.net.URL;
import java.util.Date;
import java.util.Map;

/**
 *
 * Sample AWS4 signer demonstrating how to sign 'chunked' uploads
 */
public class AWS4SignerForChunkedUpload extends AWS4SignerBase {

    /**
     * SHA256 substitute marker used in place of x-amz-content-sha256 when
     * employing chunked uploads
     */
    public static final String STREAMING_BODY_SHA256 = "STREAMING-AWS4-HMAC-SHA256-PAYLOAD";
    
    private static final String CLRF = "\r\n";
    private static final String CHUNK_STRING_TO_SIGN_PREFIX = "AWS4-HMAC-SHA256-PAYLOAD";
    private static final String CHUNK_SIGNATURE_HEADER = ";chunk-signature=";
    private static final int SIGNATURE_LENGTH = 64;
    private static final byte[] FINAL_CHUNK = new byte[0];
    
    /**
     * Tracks the previously computed signature value; for chunk 0 this will
     * contain the signature included in the Authorization header. For
     * subsequent chunks it contains the computed signature of the prior chunk.
     */
    private String lastComputedSignature;
    
    /**
     * Date and time of the original signing computation, in ISO 8601 basic
     * format, reused for each chunk
     */
    private String dateTimeStamp;
    
    /**
     * The scope value of the original signing computation, reused for each chunk
     */
    private String scope;
    
    /**
     * The derived signing key used in the original signature computation and
     * re-used for each chunk
     */
    private byte[] signingKey;
    
    public AWS4SignerForChunkedUpload(URL endpointUrl, String httpMethod,
            String serviceName, String regionName) {
        super(endpointUrl, httpMethod, serviceName, regionName);
    }

    /**
     * Computes an AWS4 signature for a request, ready for inclusion as an
     * 'Authorization' header.
     * 
     * @param headers
     *            The request headers; 'Host' and 'X-Amz-Date' will be added to
     *            this set.
     * @param queryParameters
     *            Any query parameters that will be added to the endpoint. The
     *            parameters should be specified in canonical format.
     * @param bodyHash
     *            Precomputed SHA256 hash of the request body content; this
     *            value should also be set as the header 'X-Amz-Content-SHA256'
     *            for non-streaming uploads.
     * @param awsAccessKey
     *            The user's AWS Access Key.
     * @param awsSecretKey
     *            The user's AWS Secret Key.
     * @return The computed authorization string for the request. This value
     *         needs to be set as the header 'Authorization' on the subsequent
     *         HTTP request.
     */
    public String computeSignature(Map<String, String> headers,
                                   Map<String, String> queryParameters,
                                   String bodyHash,
                                   String awsAccessKey,
                                   String awsSecretKey) {
        // first get the date and time for the subsequent request, and convert
        // to ISO 8601 format for use in signature generation
        Date now = new Date();
        this.dateTimeStamp = dateTimeFormat.format(now);

        // update the headers with required 'x-amz-date' and 'host' values
        headers.put("x-amz-date", dateTimeStamp);
        
        String hostHeader = endpointUrl.getHost();
        int port = endpointUrl.getPort();
        if ( port > -1 ) {
            hostHeader = hostHeader.concat(":" + Integer.toString(port));
        }
        headers.put("Host", hostHeader);
        
        // canonicalize the headers; we need the set of header names as well as the
        // names and values to go into the signature process
        String canonicalizedHeaderNames = getCanonicalizeHeaderNames(headers);
        String canonicalizedHeaders = getCanonicalizedHeaderString(headers);
        
        // if any query string parameters have been supplied, canonicalize them
        String canonicalizedQueryParameters = getCanonicalizedQueryString(queryParameters);
        
        // canonicalize the various components of the request
        String canonicalRequest = getCanonicalRequest(endpointUrl, httpMethod,
                canonicalizedQueryParameters, canonicalizedHeaderNames,
                canonicalizedHeaders, bodyHash);
        System.out.println("--------- Canonical request --------");
        System.out.println(canonicalRequest);
        System.out.println("------------------------------------");
        
        // construct the string to be signed
        String dateStamp = dateStampFormat.format(now);
        this.scope =  dateStamp + "/" + regionName + "/" + serviceName + "/" + TERMINATOR;
        String stringToSign = getStringToSign(SCHEME, ALGORITHM, dateTimeStamp, scope, canonicalRequest);
        System.out.println("--------- String to sign -----------");
        System.out.println(stringToSign);
        System.out.println("------------------------------------");
        
        // compute the signing key
        byte[] kSecret = (SCHEME + awsSecretKey).getBytes();
        byte[] kDate = sign(dateStamp, kSecret, "HmacSHA256");
        byte[] kRegion = sign(regionName, kDate, "HmacSHA256");
        byte[] kService = sign(serviceName, kRegion, "HmacSHA256");
        this.signingKey= sign(TERMINATOR, kService, "HmacSHA256");
        byte[] signature = sign(stringToSign, signingKey, "HmacSHA256");
        
        // cache the computed signature ready for chunk 0 upload
        lastComputedSignature = BinaryUtils.toHex(signature);
        
        String credentialsAuthorizationHeader =
                "Credential=" + awsAccessKey + "/" + scope;
        String signedHeadersAuthorizationHeader =
                "SignedHeaders=" + canonicalizedHeaderNames;
        String signatureAuthorizationHeader =
                "Signature=" + lastComputedSignature;

        return String.format("%s-%s %s, %s, %s",
                SCHEME, ALGORITHM, credentialsAuthorizationHeader, signedHeadersAuthorizationHeader, signatureAuthorizationHeader);
    }
    
    /**
     * Calculates the expanded payload size of our data when it is chunked
     * 
     * @param originalLength
     *            The true size of the data payload to be uploaded
     * @param chunkSize
     *            The size of each chunk we intend to send; each chunk will be
     *            prefixed with signed header data, expanding the overall size
     *            by a determinable amount
     * @return The overall payload size to use as content-length on a chunked
     *         upload
     */
    public static long calculateChunkedContentLength(long originalLength, long chunkSize) {
        if (originalLength <= 0) {
            throw new IllegalArgumentException("Nonnegative content length expected.");
        }
        
        long maxSizeChunks = originalLength / chunkSize;
        long remainingBytes =  originalLength % chunkSize;
        return maxSizeChunks * calculateChunkHeaderLength(chunkSize)
                + (remainingBytes > 0? calculateChunkHeaderLength(remainingBytes) : 0)
                + calculateChunkHeaderLength(0);
    }
    
    /**
     * Returns the size of a chunk header, which only varies depending on the
     * selected chunk size
     * 
     * @param chunkDataSize
     *            The intended size of each chunk; this is placed into the chunk
     *            header
     * @return The overall size of the header that will prefix the user data in
     *         each chunk
     */
    private static long calculateChunkHeaderLength(long chunkDataSize) {
        return Long.toHexString(chunkDataSize).length()
                + CHUNK_SIGNATURE_HEADER.length()
                + SIGNATURE_LENGTH
                + CLRF.length()
                + chunkDataSize
                + CLRF.length();
    }
    
    /**
     * Returns a chunk for upload consisting of the signed 'header' or chunk
     * prefix plus the user data. The signature of the chunk incorporates the
     * signature of the previous chunk (or, if the first chunk, the signature of
     * the headers portion of the request).
     * 
     * @param userDataLen
     *            The length of the user data contained in userData
     * @param userData
     *            Contains the user data to be sent in the upload chunk
     * @return A new buffer of data for upload containing the chunk header plus
     *         user data
     */
    public byte[] constructSignedChunk(int userDataLen, byte[] userData) {
        // to keep our computation routine signatures simple, if the userData
        // buffer contains less data than it could, shrink it. Note the special case
        // to handle the requirement that we send an empty chunk to complete
        // our chunked upload.
        byte[] dataToChunk;
        if (userDataLen == 0) {
            dataToChunk = FINAL_CHUNK;
        } else {
            if (userDataLen < userData.length) {
                // shrink the chunkdata to fit
                dataToChunk = new byte[userDataLen];
                System.arraycopy(userData, 0, dataToChunk, 0, userDataLen);
            } else {
                dataToChunk = userData;
            }
        }
        
        StringBuilder chunkHeader = new StringBuilder();
        
        // start with size of user data
        chunkHeader.append(Integer.toHexString(dataToChunk.length));
        
        // nonsig-extension; we have none in these samples
        String nonsigExtension = "";
        
        // if this is the first chunk, we package it with the signing result
        // of the request headers, otherwise we use the cached signature
        // of the previous chunk
        
        // sig-extension
        String chunkStringToSign = 
                CHUNK_STRING_TO_SIGN_PREFIX + "\n" +
                dateTimeStamp + "\n" +
                scope + "\n" +
                lastComputedSignature + "\n" +
                BinaryUtils.toHex(AWS4SignerBase.hash(nonsigExtension)) + "\n" +
                BinaryUtils.toHex(AWS4SignerBase.hash(dataToChunk));
        
        // compute the V4 signature for the chunk
        String chunkSignature = BinaryUtils.toHex(AWS4SignerBase.sign(chunkStringToSign, signingKey, "HmacSHA256"));
        
        // cache the signature to include with the next chunk's signature computation
        lastComputedSignature = chunkSignature;
        
        // construct the actual chunk, comprised of the non-signed extensions, the
        // 'headers' we just signed and their signature, plus a newline then copy
        // that plus the user's data to a payload to be written to the request stream
        chunkHeader.append(nonsigExtension).append(CHUNK_SIGNATURE_HEADER).append(chunkSignature);
        chunkHeader.append(CLRF);
        
        try {
            byte[] header = chunkHeader.toString().getBytes("UTF-8");
            byte[] trailer = CLRF.getBytes("UTF-8");
            byte[] signedChunk = new byte[header.length + dataToChunk.length + trailer.length];
            System.arraycopy(header, 0, signedChunk, 0, header.length);
            System.arraycopy(dataToChunk, 0, signedChunk, header.length, dataToChunk.length);
            System.arraycopy(trailer, 0, 
                    signedChunk, header.length + dataToChunk.length, 
                    trailer.length);
            
            // this is the total data for the chunk that will be sent to the request stream
            return signedChunk;
        } catch (Exception e) {
            throw new RuntimeException("Unable to sign the chunked data. " + e.getMessage(), e);
        }
    }
}