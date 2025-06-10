package com.on_class.bootcamp.infrastructure.utils;

public class Constants {
    private Constants() {
        throw new UnsupportedOperationException(UTILITY_CLASS_SHOULD_NOT_BE_INSTANTIATED);
    }

    public static final String UTILITY_CLASS_SHOULD_NOT_BE_INSTANTIATED = "Utility class should not be instantiated";

    //Routes
    public static final String ROUTE_BOOTCAMP = "/bootcamp";
    public static final String ROUTE_EMPTY = "";
    //Capability-Micro
    public static final String PROPERTIES_PREFIX_TECHNOLOGY = "capability";
    public static final String BOOTCAMP_CAPABILITIES_PATH = "/bootcamps/{bootcampId}/capabilities";
    public static final String BOOTCAMP_BY_IDS_PATH = "/bootcamps/byIds";

    //Table
    public static final String BOOTCAMP_TABLE_NAME = "bootcamp";

    //EXCEPTIONS
    public static final String BOOTCAMP_ERROR = "Error on Bootcamp - [ERROR]";
    public static final String NO_ADDITIONAL_DETAILS = "No additional details available.";

    //Pagination
    // Query param keys
    public static final String QUERY_PARAM_PAGE = "page";
    public static final String QUERY_PARAM_SIZE = "size";
    public static final String QUERY_PARAM_SORT_DIRECTION = "sortDirection";
    public static final String QUERY_PARAM_SORT_FIELD = "sortField";
    // Default values
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_SIZE = 10;
    public static final String DEFAULT_SORT_DIRECTION = "ASC";
    public static final String DEFAULT_SORT_FIELD = "name";
}
