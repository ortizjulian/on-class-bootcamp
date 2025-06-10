package com.on_class.bootcamp.domain.constants;

public class DomainConstants {
    private DomainConstants() {
        throw new UnsupportedOperationException(UTILITY_CLASS_SHOULD_NOT_BE_INSTANTIATED);
    }

    public static final String UTILITY_CLASS_SHOULD_NOT_BE_INSTANTIATED = "Utility class should not be instantiated";

    // Status Codes
    public static final String STATUS_CODE_BAD_REQUEST = "400";
    public static final String STATUS_CODE_CONFLICT = "409";
    public static final String STATUS_CODE_NOT_FOUND = "404";
    public static final String STATUS_CODE_INTERNAL_ERROR = "500";

    // Messages
    public static final String MESSAGE_BAD_REQUEST = "Bad Request, please verify data";
    public static final String MESSAGE_EMPTY_BODY = "Empty body, please verify data";
    public static final String MESSAGE_ALREADY_EXISTS = "Already exists, please verify data";
    public static final String MESSAGE_INTERNAL_ERROR = "Something went wrong, please try again";
    public static final String MESSAGE_INTERNAL_ERROR_IN_ADAPTERS = "Something went wrong in adapters, please try again.";
    public static final String MESSAGE_ADAPTER_RESPONSE_NOT_FOUND = "Something went wrong in adapters, please try again.";
    public static final String MESSAGE_PAGINATION_BAD_REQUEST = "Bad Request, please verify pagination data";
    //Pagination
    public static final Integer PAGINATION_MIN_SIZE = 0;
    public static final Integer PAGINATION_MIN_PAGE =0 ;
    //Pagination Exceptions
    public static final String EXCEPTION_PAGINATION_PAGE = "The page must be 0 or greater.";
    public static final String EXCEPTION_PAGINATION_SIZE = "The size must be greater than 0.";
    public static final String EXCEPTION_PAGINATION_SORT = "The sort must be ASC or DESC.";
    public static final String EXCEPTION_PAGINATION_SORT_FIELD = "The sort by must be name or capabilityQuantity.";
    //Capabilities sort options
    public static final String BOOTCAMP_SORT_BY_NAME = "name";
    public static final String BOOTCAMP_SORT_BY_CAPABILITY_QUANTITY = "capabilityquantity";

    public static final String SORT_BY_ASC = "ASC";
    public static final String SORT_BY_DESC = "DESC";

    public static final int MIN_CAPABILITIES= 1;
    public static final int MAX_CAPABILITIES = 4;

    public static final String EXCEPTION_CAPABILITIES_INVALID_QUANTITY =
            String.format("The number of capabilities must be between %d and %d.", MIN_CAPABILITIES, MAX_CAPABILITIES);
    public static final String EXCEPTION_CAPABILITIES_DUPLICATED_ID = "There are capabilities with duplicate IDs.";
}
