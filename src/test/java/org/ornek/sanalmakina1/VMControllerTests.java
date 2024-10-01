package org.ornek.sanalmakina1;

import org.junit.jupiter.api.Test;
import org.ornek.sanalmakina1.controller.VMController;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class VMControllerTests {

    @Test
    void contextLoads() throws Exception {
        VMController vmController = new VMController();
        assertThat(vmController).isNotNull();
    }
}

