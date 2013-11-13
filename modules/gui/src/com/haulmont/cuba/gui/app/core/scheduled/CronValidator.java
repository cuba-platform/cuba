/**
 *
 * @author degtyarjov
 * @version $Id$
 */
package com.haulmont.cuba.gui.app.core.scheduled;

import com.haulmont.cuba.core.app.ServerInfoService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ValidationException;
import org.springframework.scheduling.support.CronSequenceGenerator;

public class CronValidator implements Field.Validator {

    @Override
    public void validate(Object value) throws ValidationException {
        if (value != null) {
            ServerInfoService serverInfoService = AppBeans.get(ServerInfoService.NAME);
            Messages messages = AppBeans.get(Messages.NAME);
            try {
                CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(value.toString(), serverInfoService.getTimeZone());
            } catch (Exception e) {
                throw new ValidationException(messages.getMessage(getClass(), "validation.cronInvalid"));
            }
        }
    }
}
