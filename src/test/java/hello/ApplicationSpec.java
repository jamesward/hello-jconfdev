package hello;

import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class ApplicationSpec {

    @Inject
    private Application application;

    @Test
    public void testHelloWorldResponse(){
        assertEquals("hello, world", application.index());
    }

}
