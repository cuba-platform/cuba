package com.haulmont.cuba.testmodel.embeddedwithinheritance;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.cuba.core.entity.EmbeddableEntity;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import java.util.Date;

@MetaClass(name = "test_EmbeddedVerificationInfo")
@Embeddable
public class VerificationInfo extends EmbeddableEntity {
    private static final long serialVersionUID = -4704716822439223150L;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_")
    protected Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    protected User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}