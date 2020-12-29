package dev.alexengrig.remittance.service;

import dev.alexengrig.remittance.MockitoAnnotationTest;
import dev.alexengrig.remittance.repository.AccountRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class SimpleMoneyTransferServiceBaseTest implements MockitoAnnotationTest {

    AutoCloseable closeable;

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    SimpleMoneyTransferService moneyTransferService;

    @Override
    public AutoCloseable getCloseable() {
        return closeable;
    }

    @Override
    public void setCloseable(AutoCloseable closeable) {
        this.closeable = closeable;
    }
}
