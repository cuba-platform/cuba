/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.script;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.entity.Entity;
import org.springframework.stereotype.Service;

/**
 * @author degtyarjov
 * @version $Id$
 */
@Service(ScriptGenerationService.NAME)
public class ScriptGenerationServiceBean implements ScriptGenerationService {
    @Override
    public String generateInsertScript(Entity entity) {
        Preconditions.checkNotNullArgument(entity);
        return new ScriptGeneratorImpl(entity.getClass()).generateInsertScript(entity);
    }

    @Override
    public String generateUpdateScript(Entity entity) {
        Preconditions.checkNotNullArgument(entity);
        return new ScriptGeneratorImpl(entity.getClass()).generateUpdateScript(entity);
    }
}
