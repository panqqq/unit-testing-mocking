package com.endava.internship.mocking.repository;

import com.endava.internship.mocking.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemPaymentRepositoryTest {
    private InMemPaymentRepository inMemPaymentRepository;
    private Payment p1;
    private Payment p2;
    private Payment p3;

    @BeforeEach
    void setUp() {
        inMemPaymentRepository = new InMemPaymentRepository();
        p1 = new Payment(1, 100d, "message1");
        p2 = new Payment(2, 200d, "message2");
        p3 = new Payment(3, 300d, "message3");
        inMemPaymentRepository.save(p1);
        inMemPaymentRepository.save(p2);
        inMemPaymentRepository.save(p3);
    }

    @Test
    void findById_WhenPaymentIdIsNull_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> inMemPaymentRepository.findById(null));
    }

    @Test
    void findById() {
        final Optional<Payment> expected = inMemPaymentRepository.findById(p1.getPaymentId());
        assertThat(Optional.of(p1)).isEqualTo(expected);
    }

    @Test
    void findAll() {
        final List<Payment> list = Arrays.asList(p1, p2, p3);
        assertThat(inMemPaymentRepository.findAll()).containsAll(list);
    }

    @Test
    void save_whenPaymentIsNull_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> inMemPaymentRepository.save(null));
    }

    @Test
    void save_whenPaymentOfIdIsNotNullAndPaymentIsPresentByPaymentId_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> inMemPaymentRepository.save(p1));
    }

    @Test
    void save() {
        final Payment p4 = new Payment(4, 400d, "message4");
        final Payment expected = inMemPaymentRepository.save(p4);
        assertThat(p4).isEqualTo(expected);
    }

    @Test
    void editMessage_whenUUIDIsNotInThePaymentsId_ShouldThrowNoSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> inMemPaymentRepository.editMessage(UUID.randomUUID(), "Message for Random UUID"));
    }

    @Test
    void editMessage() {
        final Payment expected = inMemPaymentRepository.editMessage(p1.getPaymentId(), "New Message for Payment ID 1");
        assertThat(p1).isEqualTo(expected);
    }
}