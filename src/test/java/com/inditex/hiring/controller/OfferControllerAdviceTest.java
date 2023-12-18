package com.inditex.hiring.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OfferControllerAdviceTest {

    @Test
    public void testHandleValidationExceptions() {
        OfferControllerAdvice advice = new OfferControllerAdvice();
        BindingResult bindingResult = mock(BindingResult.class);
        MethodParameter parameter = mock(MethodParameter.class);

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, bindingResult);

        String fieldName = "BRAND_ID";
        String errorMessage = "The offer brand must be defined.";
        List<ObjectError> errors = getExampleErrors("Offer", fieldName, errorMessage);
        when(bindingResult.getAllErrors()).thenReturn(errors);

        Map<String, String> resultMap = advice.handleValidationExceptions(ex);
        Assertions.assertEquals(1, resultMap.size());
        Assertions.assertTrue(resultMap.containsKey(fieldName));
    }

    private List<ObjectError> getExampleErrors(String objectName, String fieldName, String defaultErrorMsg) {
        List<ObjectError> errors = new ArrayList<>();
        FieldError objectError = new FieldError(objectName, fieldName, defaultErrorMsg);
        errors.add(objectError);
        return errors;
    }
}
