package dev.alexengrig.remittance;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

public interface MockitoAnnotationTest {

    @BeforeEach
    default void openMocks() {
        setCloseable(MockitoAnnotations.openMocks(this));
    }

    @AfterEach
    default void releaseMocks() throws Exception {
        getCloseable().close();
    }

    AutoCloseable getCloseable();

    void setCloseable(AutoCloseable closeable);
}
