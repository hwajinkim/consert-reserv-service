package kr.hhplus.be.server.integration_test.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@Disabled
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BaseIntegrationTest {
    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected ObjectMapper objectMapper;
}
