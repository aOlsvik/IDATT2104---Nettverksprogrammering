package org.ntnu.dockerservice.Controller;

import org.ntnu.dockerservice.Model.UserCode;
import org.ntnu.dockerservice.Service.CompilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class DockerController {

    @Autowired
    CompilerService compiler;

    @GetMapping("/")
    public String test(){
        return "Hello World";
    }
    @PostMapping("/compile-and-run")
    @ResponseBody
    public UserCode compileAndRun(@RequestBody UserCode code) throws IOException {
        return this.compiler.compileAndRun(code);
    }
}
