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
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.app.TestingService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.concurrent.*;

@Service("cuba_ServiceInterceptorTestService")
public class ServiceInterceptorTestServiceBean implements ServiceInterceptorTestService {

    @Inject
    private TestingService testingService;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public void declarativeTransaction() {
        testingService.declarativeTransaction();
    }

    @Override
    public void declarativeTransactionNewThread() {
        SecurityContext securityContext = AppContext.getSecurityContextNN();
        Future future = executor.submit(() -> {
            AppContext.setSecurityContext(securityContext);
            declarativeTransaction();
            AppContext.setSecurityContext(null);
        });
        try {
            future.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof RuntimeException)
                throw (RuntimeException) e.getCause();
            else
                throw new RuntimeException(e.getCause());
        }
    }

    @Override
    public String executeWithException() throws TestingService.TestException {
        return testingService.executeWithException();
    }

    @Override
    public String executeWithExceptionNewThread() throws TestingService.TestException {
        Future<String> future = executor.submit(new SecurityContextAwareCallable<>(this::executeWithException));
        try {
            return future.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof TestingService.TestException)
                throw (TestingService.TestException) e.getCause();
            else
                throw new RuntimeException(e.getCause());
        }
    }
}
