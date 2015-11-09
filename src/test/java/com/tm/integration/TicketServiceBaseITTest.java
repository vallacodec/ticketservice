package com.tm.integration;

import com.tm.config.DataBaseConfig;
import com.tm.config.RepositoryConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by svallaban1 on 11/8/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {RepositoryConfiguration.class,
        DataBaseConfig.class})
public class TicketServiceBaseITTest {

}
