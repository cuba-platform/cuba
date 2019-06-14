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

package com.haulmont.cuba.web.testmodel.beanvalidation;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import java.util.Date;

@Entity(name = "test_DateValidationEntity")
public class DateValidationEntity extends StandardEntity {

    @Future
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FUTURE_DATE_PICKER")
    protected Date futureDatePicker;

    @Past
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PAST_DATE_PICKER")
    protected Date pastDatePicker;

    @FutureOrPresent
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FUTURE_OR_PRESENT_DATE_PICKER")
    protected Date futureOrPresentDatePicker;

    @PastOrPresent
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PAST_OR_PRESENT_DATE_PICKER")
    protected Date pastOrPresentDatePicker;

    @Future
    @Temporal(TemporalType.DATE)
    @Column(name = "SPECIFIC_FUTURE_DATE_PICKER")
    protected Date specificFutureDatePicker;

    @Future
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FUTURE_DATE_FIELD")
    protected Date futureDateField;

    @Past
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PAST_DATE_FIELD")
    protected Date pastDateField;

    @FutureOrPresent
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FUTURE_OR_PRESENT_DATE_FIELD")
    protected Date futureOrPresentDateField;

    @PastOrPresent
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PAST_OR_PRESENT_DATE_FIELD")
    protected Date pastOrPresentDateField;

    @Future
    @Temporal(TemporalType.DATE)
    @Column(name = "SPECIFIC_FUTURE_DATE_Field")
    protected Date specificFutureDateField;

    public void setFutureDatePicker(Date futureDatePicker) {
        this.futureDatePicker = futureDatePicker;
    }

    public Date getFutureDatePicker() {
        return futureDatePicker;
    }

    public void setPastDatePicker(Date pastDatePicker) {
        this.pastDatePicker = pastDatePicker;
    }

    public Date getPastDatePicker() {
        return pastDatePicker;
    }

    public void setFutureOrPresentDatePicker(Date futureOrPresentDatePicker) {
        this.futureOrPresentDatePicker = futureOrPresentDatePicker;
    }

    public Date getFutureOrPresentDatePicker() {
        return futureOrPresentDatePicker;
    }

    public void setPastOrPresentDatePicker(Date pastOrPresentDatePicker) {
        this.pastOrPresentDatePicker = pastOrPresentDatePicker;
    }

    public Date getPastOrPresentDatePicker() {
        return pastOrPresentDatePicker;
    }

    public void setSpecificFutureDatePicker(Date specificFutureDatePicker) {
        this.specificFutureDatePicker = specificFutureDatePicker;
    }

    public Date getSpecificFutureDatePicker() {
        return specificFutureDatePicker;
    }

    public void setFutureDateField(Date futureDateField) {
        this.futureDateField = futureDateField;
    }

    public Date getFutureDateField() {
        return futureDateField;
    }

    public void setPastDateField(Date pastDateField) {
        this.pastDateField = pastDateField;
    }

    public Date getPastDateField() {
        return pastDateField;
    }

    public void setFutureOrPresentDateField(Date futureOrPresentDateField) {
        this.futureOrPresentDateField = futureOrPresentDateField;
    }

    public Date getFutureOrPresentDateField() {
        return futureOrPresentDateField;
    }

    public void setPastOrPresentDateField(Date pastOrPresentDateField) {
        this.pastOrPresentDateField = pastOrPresentDateField;
    }

    public Date getPastOrPresentDateField() {
        return pastOrPresentDateField;
    }

    public void setSpecificFutureDateField(Date specificFutureDateField) {
        this.specificFutureDateField = specificFutureDateField;
    }

    public Date getSpecificFutureDateField() {
        return specificFutureDateField;
    }
}

