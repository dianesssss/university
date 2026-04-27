package ru.msu.cmc.webprac;

import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mockStatic;

public class WebpracApplicationTests {

    @Test
    public void testMainDelegatesToSpringApplication() {
        try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {
            String[] args = {"arg"};

            WebpracApplication.main(args);

            springApplication.verify(() -> SpringApplication.run(WebpracApplication.class, args));
        }
    }
}
