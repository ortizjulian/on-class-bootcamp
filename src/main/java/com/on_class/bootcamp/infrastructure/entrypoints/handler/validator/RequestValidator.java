package com.on_class.bootcamp.infrastructure.entrypoints.handler.validator;

import com.on_class.bootcamp.domain.enums.TechnicalMessage;
import com.on_class.bootcamp.domain.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;


@Component
@RequiredArgsConstructor
public class RequestValidator {

    private final Validator validator;

    public <T> void validate(T body) {
        Errors errors = new BeanPropertyBindingResult(body, body.getClass().getName());
        validator.validate(body, errors);
        if (errors.hasErrors()) {
                List<String> errorMessages = errors.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList();

            throw new BusinessException(TechnicalMessage.INVALID_REQUEST,errorMessages);
        }
    }
}
