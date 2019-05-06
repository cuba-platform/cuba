/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.core.sys.environmentcheck;

import com.haulmont.cuba.core.sys.AppContext;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class DirectoriesCheck implements EnvironmentCheck {

    @Override
    public List<CheckFailedResult> doCheck() {
        List<CheckFailedResult> result = new ArrayList<>();
        CheckFailedResult dataResult = checkDirectory("cuba.dataDir");
        CheckFailedResult tempResult = checkDirectory("cuba.tempDir");
        if (dataResult != null) {
            result.add(dataResult);
        }
        if (tempResult != null) {
            result.add(tempResult);
        }
        return result;
    }

    protected CheckFailedResult checkDirectory(String dirKey) {
        String dir = AppContext.getProperty(dirKey);
        if (dir != null) {
            File dirFile = new File(dir);
            boolean readable = Files.isReadable(dirFile.toPath());
            boolean writable = Files.isWritable(dirFile.toPath());
            boolean isDir = dirFile.isDirectory();
            if (!writable && !isDir) {
                try {
                    isDir = dirFile.mkdirs();
                    readable = Files.isReadable(dirFile.toPath());
                    writable = Files.isWritable(dirFile.toPath());
                } catch (SecurityException e) {
                    return new CheckFailedResult(
                            String.format("Directory \'%s\' must have read and write permissions." +
                                            " Current permissions: Readable: %b, Writable: %b",
                                    dirKey, readable, writable), e);
                }
            }
            if (!writable || !readable || !isDir) {
                return new CheckFailedResult(
                        String.format("Directory \'%s\' must have read and write permissions. " +
                                        "Current permissions: Readable: %b, Writable: %b, Is directory: %b",
                                dirKey, readable, writable, isDir), null);
            }
        } else {
            return new CheckFailedResult(String.format("Unable to get directory path from \'%s\' property", dirKey),
                    null);
        }
        return null;
    }
}
