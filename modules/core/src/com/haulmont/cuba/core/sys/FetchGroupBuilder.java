package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewProperty;
import org.eclipse.persistence.queries.FetchGroup;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class FetchGroupBuilder {
    private final int DEEP_BARRIER = 100; // if user made cycle in view graph, BARRIER will prevent to make infinity cycle

    private final List<View> views;

    public FetchGroupBuilder(List<View> views) {
        requireNonNull(views);

        this.views = views;
    }

    protected FetchGroup build() {
        FetchGroup fg;
        try {
            fg = views.stream().flatMap(v -> v.getProperties().stream()) // What should we do if we have more than one view? Maybe union?
                               .distinct()
                               .flatMap(prop -> extractKeys(prop, 0))
                               .collect(FetchGroup::new, FetchGroup::addAttribute, this::mergeFetchGroups);
        } catch (CycleException ce) {
            throw new CycleException(format("Looks like that union of views (%s) has cycle", views), ce);
        }

        fg.setShouldLoadAll(true);
        return fg;
    }

    private void mergeFetchGroups(FetchGroup first, FetchGroup second) {
        first.addAttributes(second.getAttributeNames());
    }

    private Stream<String> extractKeys(ViewProperty property, int deep) {
        if (deep > DEEP_BARRIER) throw new CycleException("Rises barrier. Looks like that some view has cycle.");

        String prefix = property.getName();

        View view = property.getView();
        if (view != null) {
            return view.getProperties().stream().flatMap(prop -> extractKeys(prop, deep + 1))
                                                .map(k -> new StringJoiner(".").add(prefix).add(k).toString());
        } else {
            return Stream.of(property.getName());
        }
    }
}
