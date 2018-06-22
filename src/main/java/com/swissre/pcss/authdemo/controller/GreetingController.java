package com.swissre.pcss.authdemo.controller;

import com.swissre.pcss.authdemo.domain.Greeting;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping(value = "/api/hello", produces = {MediaType.APPLICATION_JSON_VALUE})
@Api(tags = {"hello"}, description = "Hello World API")
@Slf4j
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping
    @ApiOperation(value = "Get a greeting", notes = "Return a nice greeting", response = Greeting.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Greetings are retrieved"),
            @ApiResponse(code = 400, message = "Invalid input field"),
            @ApiResponse(code = 404, message = "Greeting not found"),
            @ApiResponse(code = 500, message = "Internal server error") })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "Who to greet", required = false, dataType = "string",
                    paramType = "query")
    })
    public Greeting helloWorld(@RequestParam(value="name", defaultValue="World") String name) {
        log.info("Hello World");
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

}
