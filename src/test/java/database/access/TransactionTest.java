package database.access;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

@TestMethodOrder(OrderAnnotation.class)
class TransactionTest {

    @Test
    @Order(1)
    void start() {
        assertTrue(Transaction.start());
    }

    @Test
    void commit() {
        assertTrue(Transaction.commit());
    }

    @Test
    void rollback() {
        assertTrue(Transaction.start());
        assertTrue(Transaction.rollback());
    }
}