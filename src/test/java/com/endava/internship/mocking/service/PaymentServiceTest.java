package com.endava.internship.mocking.service;

import com.endava.internship.mocking.model.Payment;
import com.endava.internship.mocking.model.Status;
import com.endava.internship.mocking.model.User;
import com.endava.internship.mocking.repository.InMemPaymentRepository;
import com.endava.internship.mocking.repository.InMemUserRepository;
import com.endava.internship.mocking.repository.PaymentRepository;
import com.endava.internship.mocking.repository.UserRepository;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @Mock
    ValidationService basicValidationService;
    @Mock
    UserRepository userRepository;
    @Mock
    PaymentRepository paymentRepository;
    PaymentService paymentService;

    @BeforeEach
    void setUp() {
        basicValidationService = Mockito.mock(BasicValidationService.class);
        userRepository = Mockito.mock(InMemUserRepository.class);
        paymentRepository = Mockito.mock(InMemPaymentRepository.class);
        paymentService = new PaymentService(userRepository,paymentRepository,basicValidationService);
    }
    @Test
    void createPayment_whenUserNotExists_ShouldThrowNoSuchElementException() {
        Integer userId = 7;
        Double amount = 500d;

        Assertions.assertThrows(NoSuchElementException.class, () -> paymentService.createPayment(userId,amount));

        Mockito.verify(basicValidationService).validateUserId(userId);
        Mockito.verify(basicValidationService).validateAmount(amount);
        Mockito.verify(basicValidationService).validateUserId(userId);
        Mockito.verify(basicValidationService, Mockito.never()).validateUser(new User(userId,"John", Status.ACTIVE));
        Mockito.verify(paymentRepository, Mockito.never()).save(new Payment(userId,amount,"message"));
    }
    @Test
    void createPayment() {
        ArgumentCaptor<Payment> capturedPayment = ArgumentCaptor.forClass(Payment.class);
        Integer userId = 1;
        String name = "John";
        Double amount = 500d;
        User user = new User(userId, name, Status.ACTIVE);
        String message = "Payment from user " + name;
        Payment payment = new Payment(userId, amount, message);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        paymentService.createPayment(userId, amount);

        Mockito.verify(paymentRepository).save(capturedPayment.capture());
        Payment capturedPaymentValue = capturedPayment.getValue();

        assertAll(
                () -> assertThat(capturedPaymentValue.getUserId()).isEqualTo(payment.getUserId()),
                () -> assertThat(capturedPaymentValue.getAmount()).isEqualTo(payment.getAmount()),
                () -> assertThat(capturedPaymentValue.getMessage()).isEqualTo(payment.getMessage())
        );

        Mockito.verify(basicValidationService).validateUserId(userId);
        Mockito.verify(basicValidationService).validateAmount(amount);
        Mockito.verify(userRepository).findById(userId);
        Mockito.verify(basicValidationService).validateUser(user);

    }

    @Test
    void editMessage() {
        UUID uid = UUID.randomUUID();
        String message = "New Message for uuid: " + uid;

        Payment p2 = new Payment(1,500d, message);

        Mockito.when(paymentRepository.editMessage(any(UUID.class),anyString())).thenReturn(p2);

        assertThat(p2).isEqualTo(paymentService.editPaymentMessage(uid,message));

        Mockito.verify(basicValidationService).validatePaymentId(uid);
        Mockito.verify(basicValidationService).validateMessage(message);

    }



    @Test
    void getAllByAmountExceeding() {
        Payment p1 = new Payment(1, 100d, "message 1");
        Payment p2 = new Payment(2, 200d, "message 2");
        Payment p3 = new Payment(3, 300d, "message 3");
        List<Payment> paymentsList = Arrays.asList(p1,p2,p3);

        Mockito.when(paymentRepository.findAll()).thenReturn(paymentsList);

        List<Payment> list = paymentService.getAllByAmountExceeding(150d);

        assertThat(list).containsAll(Arrays.asList(p2,p3));

        Mockito.verify(paymentRepository).findAll();
    }

}
