package fraj.lab.recruit.test2.atm;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ATMTest {
    @Mock
    AmountSelector amountSelector;
    @Mock
    CashManager cashManager;
    @Mock
    PaymentProcessor paymentProcessor;


    @Test
    void should_throws_ATMTechnicalException() {
        when(amountSelector.selectAmount()).thenReturn(-1);
        ATM atm = new ATM(amountSelector, cashManager, paymentProcessor);

        Assertions.assertThrows(ATMTechnicalException.class, atm::runCashWithdrawal);
    }

    @Test
    void should_return_CASH_NOT_AVAILABLE() throws ATMTechnicalException {
        when(amountSelector.selectAmount()).thenReturn(100);
        when(cashManager.canDeliver(100)).thenReturn(false);
        ATM atm = new ATM(amountSelector, cashManager, paymentProcessor);

        assertEquals(atm.runCashWithdrawal(), ATMStatus.CASH_NOT_AVAILABLE);
    }

    @Test
    void should_return_PAYMENT_REJECTED() throws ATMTechnicalException {
        when(amountSelector.selectAmount()).thenReturn(100);
        when(cashManager.canDeliver(100)).thenReturn(true);
        when(paymentProcessor.pay(100)).thenReturn(PaymentStatus.FAILURE);
        ATM atm = new ATM(amountSelector, cashManager, paymentProcessor);

        assertEquals(atm.runCashWithdrawal(), ATMStatus.PAYMENT_REJECTED);
    }

    @Test
    void should_return_DONE() throws ATMTechnicalException {
        when(amountSelector.selectAmount()).thenReturn(100);
        when(cashManager.canDeliver(100)).thenReturn(true);
        when(paymentProcessor.pay(100)).thenReturn(PaymentStatus.SUCCESS);
        ATM atm = new ATM(amountSelector, cashManager, paymentProcessor);

        assertEquals(atm.runCashWithdrawal(), ATMStatus.DONE);
    }
}
