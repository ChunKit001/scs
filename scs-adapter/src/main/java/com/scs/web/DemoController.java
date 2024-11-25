package com.scs.web;

import com.scs.dto.CustomerAddCmd;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 常用的spring-web接口定义
 */
@RestController
@RequestMapping("demo")
@Slf4j
public class DemoController {

    @GetMapping(value = "/get")
    public String helloWorld() {
        String str = "Hello, welcome to COLA world!";
        log.info(str);
        return str;
    }

    @PostMapping(value = "/post")
    public String listCustomerByName(@RequestParam(required = false, name = "name") String name) {
        return "success";
    }

    @PostMapping(value = "/post-json")
    public String addCustomer(@RequestBody CustomerAddCmd customerAddCmd) {
        return "success";
    }

    @PostMapping(value = "/post-file")
    public String postFile(@RequestParam MultipartFile file) {
        return file.getName();
    }
}
