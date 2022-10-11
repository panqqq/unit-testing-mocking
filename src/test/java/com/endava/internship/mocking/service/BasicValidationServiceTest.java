package com.endava.internship.mocking.service;

import com.endava.internship.mocking.model.Status;
import com.endava.internship.mocking.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class BasicValidationServiceTest {

    BasicValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new BasicValidationService();
    }

    @Test
    void validateAmount_whenParameterIsNull_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validationService.validateAmount(null));
    }
    @Test
    void validateAmount_whenParameterIsLessOrEqualToZero_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validationService.validateAmount(0d));
    }

    @Test
    void validatePaymentId_whenParameterIsNull_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validationService.validatePaymentId(null));
    }

    @Test
    void validateUserId_whenParameterIsNull_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validationService.validateUserId(null));
    }

    @Test
    void validateUser_whenUserStatusIsNotActive_ShouldThrowIllegalArgumentException() {
        User user = Mockito.mock(User.class);
        when(user.getStatus()).thenReturn(Status.INACTIVE);
        assertThrows(IllegalArgumentException.class, () -> validationService.validateUser(user));
    }

    @Test
    void validateMessage_whenParameterIsNull_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> validationService.validateMessage(null));
    }
}