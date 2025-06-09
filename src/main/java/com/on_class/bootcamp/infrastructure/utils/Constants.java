package com.on_class.bootcamp.infrastructure.utils;

public class Constants {
    private Constants() {
        throw new UnsupportedOperationException(UTILITY_CLASS_SHOULD_NOT_BE_INSTANTIATED);
    }

    public static final String UTILITY_CLASS_SHOULD_NOT_BE_INSTANTIATED = "Utility class should not be instantiated";

    //Routes
    public static final String ROUTE_BOOTCAMP = "/bootcamp";

    //Capability-Micro
    public static final String PROPERTIES_PREFIX_TECHNOLOGY = "capability";

    public static final String BOOTCAMP_CAPABILITIES_PATH = "/bootcamps/{bootcampId}/capabilities";

    //Table
    public static final String BOOTCAMP_TABLE_NAME = "bootcamp";

    //EXCEPTIONS
    public static final String BOOTCAMP_ERROR = "Error on Bootcamp - [ERROR]";
    public static final String NO_ADDITIONAL_DETAILS = "No additional details available.";

}
