package com.on_class.bootcamp.domain.enums;

import com.on_class.bootcamp.domain.constants.DomainConstants;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TechnicalMessage {

    INVALID_REQUEST(DomainConstants.STATUS_CODE_BAD_REQUEST, DomainConstants.MESSAGE_BAD_REQUEST),
    EMPTY_BODY(DomainConstants.STATUS_CODE_BAD_REQUEST, DomainConstants.MESSAGE_EMPTY_BODY),
    ALREADY_EXISTS(DomainConstants.STATUS_CODE_CONFLICT, DomainConstants.MESSAGE_ALREADY_EXISTS),
    INTERNAL_ERROR(DomainConstants.STATUS_CODE_INTERNAL_ERROR, DomainConstants.MESSAGE_INTERNAL_ERROR),
    INTERNAL_ERROR_IN_ADAPTERS(DomainConstants.STATUS_CODE_INTERNAL_ERROR, DomainConstants.MESSAGE_INTERNAL_ERROR_IN_ADAPTERS),
    ADAPTER_RESPONSE_NOT_FOUND(DomainConstants.STATUS_CODE_NOT_FOUND, DomainConstants.MESSAGE_ADAPTER_RESPONSE_NOT_FOUND);


    private final String code;
    private final String message;
}