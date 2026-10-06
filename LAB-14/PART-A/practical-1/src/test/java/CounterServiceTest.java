import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CounterServiceTest {
    @Test
    void addingIncreasesTheCount() {
        CounterService service = new CounterService();

        service.add(3);

        assertEquals(3, service.getCount());
    }

    @Test
    void invalidAmountThrows() {
        CounterService service = new CounterService();

        assertThrows(IllegalArgumentException.class, () -> service.add(0));
    }
}
