package co.empathy.academy.search;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping("/greet/{name}")
    public String greet(@PathVariable String name){
        return "Hello " +name;
    }

}
