package com.scs.web;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import com.alibaba.cola.exception.BizException;
import com.scs.util.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * cola框架的一些常用反回体
 */
@RestController
@RequestMapping("cola")
@Slf4j
public class ColaController {
    @GetMapping("base-s")
    public Response baseS(HttpServletRequest request) {
        return Response.buildSuccess();
    }

    @GetMapping("base-f")
    public Response baseF() {
//        通过调整 Accept-Language 为 en-US 可以看到返回的错误信息是英文
        throw new BizException("000001","a,b");
    }

    @GetMapping("single")
    public SingleResponse<String> base() {
        return SingleResponse.of("single");
    }

    @GetMapping("page")
    public PageResponse<String> page() {
        PageResponse<String> response = PageResponse.of(10, 1);
        response.setData(List.of("p", "a", "g", "e"));
        return response;
    }

    @GetMapping("list")
    public MultiResponse<String> list() {
        return MultiResponse.of(List.of("p", "a", "g", "e"));
    }
}
