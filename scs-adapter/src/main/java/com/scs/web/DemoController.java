package com.scs.web;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import com.scs.ProjectException;
import com.scs.api.DemoServiceI;
import com.scs.dto.CustomerAddCmd;
import com.scs.dto.ValidDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * cola框架的一些常用反回体
 */
@RestController
@RequestMapping("demo")
@Slf4j
public class DemoController {

    @Autowired
    private DemoServiceI demoServiceI;

    @GetMapping("base-s")
    @ResponseBody
    public Response baseS() {
        return Response.buildSuccess();
    }

    @GetMapping("base-f")
    @ResponseBody
    public Response baseF() {
//        通过调整 Accept-Language 为 en-US 可以看到返回的错误信息是英文
        throw new ProjectException("000001", "a", "b");
    }

    @PostMapping(value = "/post")
    @ResponseBody
    public String listCustomerByName(@RequestParam(required = false, name = "name") String name) {
        return "success";
    }

    @PostMapping(value = "/post-json")
    @ResponseBody
    public String addCustomer(@RequestBody CustomerAddCmd customerAddCmd) {
        return "success";
    }

    @PostMapping(value = "/post-file")
    @ResponseBody
    public String postFile(@RequestParam MultipartFile file) {
        return file.getName();
    }

    @GetMapping("single")
    @ResponseBody
    public SingleResponse<String> base() {
        return SingleResponse.of("single");
    }

    @GetMapping("page")
    @ResponseBody
    public PageResponse<String> page() {
        PageResponse<String> response = PageResponse.of(10, 1);
        response.setData(List.of("p", "a", "g", "e"));
        return response;
    }

    @GetMapping("list")
    @ResponseBody
    public MultiResponse<String> list() {
        return MultiResponse.of(List.of("p", "a", "g", "e"));
    }

    @PostMapping("valid")
    @ResponseBody
    public String valid(@RequestBody @Validated ValidDTO validDTO) {
        return "valid";
    }


}
