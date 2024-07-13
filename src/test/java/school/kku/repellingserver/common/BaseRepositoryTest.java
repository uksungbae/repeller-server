package school.kku.repellingserver.common;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import school.kku.repellingserver.config.TestConfig;

@DataJpaTest
@Import(TestConfig.class)
public class BaseRepositoryTest extends BaseDisplayNameConfig {
}
