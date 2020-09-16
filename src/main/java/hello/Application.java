package hello;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.runtime.Micronaut;

@Controller
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }

    @Get
    String index() {
        return "hello, world";
    }
}
