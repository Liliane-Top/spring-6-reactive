package nl.top.spring6reactive.repositories;

import nl.top.spring6reactive.config.DatabaseConfig;
import org.junit.jupiter.api.Order;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;

@DataR2dbcTest
@Import(DatabaseConfig.class)
@Order(3)
public class CustomerRepositoryTest {
}
