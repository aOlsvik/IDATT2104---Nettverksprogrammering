package org.ntnu.dockerservice.Controller;

import org.ntnu.dockerservice.Model.UserCode;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class DockerController {

    @GetMapping("/")
    public String test(){
        return "Hello World";
    }
    @PostMapping("/compile-and-run")
    @ResponseBody
    public UserCode compileAndRun(@RequestBody UserCode code){
        System.out.println(code.getCode());
        return code;
    }
}
