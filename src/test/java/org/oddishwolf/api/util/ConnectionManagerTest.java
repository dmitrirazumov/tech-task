package org.oddishwolf.api.util;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Make sure that your properties exist.
 * After you can comment @Disabled.
 */

@Disabled
@Tag("fast")
@Tag("util_connectionManager")
public class ConnectionManagerTest {

    @Test
    @SneakyThrows
    void isOpenedAndClosed() {
        Connection connection = ConnectionManager.open();
        assertThat(connection).isNotNull();
        assertThat(connection.isClosed()).isFalse();

        connection.close();
        assertThat(connection.isClosed()).isTrue();
    }
}
