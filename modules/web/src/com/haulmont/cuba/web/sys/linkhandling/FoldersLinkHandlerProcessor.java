package com.haulmont.cuba.web.sys.linkhandling;

import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.AbstractSearchFolder;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.web.app.folders.Folders;
import org.slf4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.UUID;

@Component(FoldersLinkHandlerProcessor.NAME)
public class FoldersLinkHandlerProcessor implements LinkHandlerProcessor, Ordered {

    public static final String NAME = "cuba_FoldersLinkHandlerProcessor";

    @Inject
    private Logger log;

    @Inject
    protected DataService dataService;

    @Inject
    protected Folders folders;

    @Override
    public boolean canHandle(ExternalLinkContext linkContext) {
        return linkContext.getRequestParams().containsKey("folder");
    }

    @Override
    public void handle(ExternalLinkContext linkContext) {
        String folderId = linkContext.getRequestParams().get("folder");

        AbstractSearchFolder folder = loadFolder(UUID.fromString(folderId));
        if (folder != null) {
            folders.openFolder(folder);
        } else {
            log.warn("Folder not found: {}", folderId);
        }
    }

    protected AbstractSearchFolder loadFolder(UUID folderId) {
        return dataService.load(new LoadContext<>(AbstractSearchFolder.class).setId(folderId));
    }

    @Override
    public int getOrder() {
        return HIGHEST_PLATFORM_PRECEDENCE + 10;
    }
}
