package com.haulmont.cuba.web.sys.linkhandling;

/**
 * Interface that is used by {@link com.haulmont.cuba.web.sys.LinkHandler}
 * to handle links from outside of the application.
 * <br> {@link com.haulmont.cuba.web.sys.LinkHandler} traverses processors to find first able to handle link.
 * <br> To set processor priority use {@link org.springframework.core.annotation.Order @Order},
 * {@link org.springframework.core.Ordered} or {@link javax.annotation.Priority @Priority}.
 */
public interface LinkHandlerProcessor {

    /**
     * Defines the highest precedence for {@link org.springframework.core.Ordered} processors of the platform.
     */
    int HIGHEST_PLATFORM_PRECEDENCE = 100;

    /**
     * Defines the lowest precedence for {@link org.springframework.core.Ordered} processors of the platform.
     */
    int LOWEST_PLATFORM_PRECEDENCE = 1000;

    /**
     * @return true if action with such request parameters should be handled by this processor.
     */
    boolean canHandle(ExternalLinkContext linkContext);

    /**
     * Called to handle action.
     */
    void handle(ExternalLinkContext linkContext);
}
