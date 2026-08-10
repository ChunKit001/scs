package com.scs.adapter.web;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import com.scs.adapter.idempotent.Idempotent;
import com.scs.adapter.idempotent.IdempotencyConstants;
import com.scs.adapter.trace.TraceIdConstants;
import com.scs.app.ProjectException;
import com.scs.client.api.DemoServiceI;
import com.scs.client.dto.CustomerAddCmd;
import com.scs.client.dto.PageQuery;
import com.scs.client.dto.PageResponses;
import com.scs.client.dto.ValidDTO;
import com.scs.client.dto.data.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * cola框架的一些常用返回体、分页、幂等示例
 */
@Tag(name = "Demo", description = "COLA 常用返回体、分页与幂等示例")
@RestController
@RequestMapping("demo")
@Validated
@Slf4j
@AllArgsConstructor
public class DemoController {

    private final DemoServiceI demoServiceI;

    @Operation(summary = "成功响应示例")
    @GetMapping("base-s")
    public Response baseS() {
        return Response.buildSuccess();
    }

    @Operation(summary = "业务失败示例（i18n）")
    @GetMapping("base-f")
    public Response baseF() {
        // 通过调整 Accept-Language 为 en-US 可以看到返回的错误信息是英文
        throw new ProjectException(ErrorCode.B_DEMO_SAMPLE, "a", "b");
    }

    @Operation(summary = "表单 POST")
    @PostMapping(value = "/post")
    public String listCustomerByName(@RequestParam(required = false, name = "name") String name) {
        return "success";
    }

    @Operation(summary = "JSON POST")
    @PostMapping(value = "/post-json")
    public String addCustomer(@RequestBody CustomerAddCmd customerAddCmd) {
        return "success";
    }

    @Operation(summary = "文件上传")
    @PostMapping(value = "/post-file")
    public String postFile(@RequestParam MultipartFile file) {
        return file.getName();
    }

    @Operation(summary = "SingleResponse 示例")
    @GetMapping("single")
    public SingleResponse<String> base() {
        return SingleResponse.of("single");
    }

    @Operation(summary = "分页约定示例（pageIndex 从 1 起）")
    @GetMapping("page")
    public PageResponse<String> page(@Valid @ModelAttribute PageQuery pageQuery) {
        List<String> all = List.of("p", "a", "g", "e", "1", "2", "3", "4", "5");
        int from = Math.min(pageQuery.offset(), all.size());
        int to = Math.min(from + pageQuery.safePageSize(), all.size());
        return PageResponses.of(all.subList(from, to), all.size(), pageQuery);
    }

    @Operation(summary = "MultiResponse 示例")
    @GetMapping("list")
    public MultiResponse<String> list() {
        return MultiResponse.of(List.of("p", "a", "g", "e"));
    }

    @Operation(summary = "参数校验")
    @PostMapping("valid")
    public String valid(@RequestBody @Valid ValidDTO validDTO) {
        return demoServiceI.valid1(validDTO);
    }

    @Operation(summary = "幂等示例（请求头 " + IdempotencyConstants.HEADER + "）")
    @Idempotent(ttlSeconds = 300)
    @PostMapping("idempotent")
    public SingleResponse<String> idempotent() {
        return SingleResponse.of("created-" + UUID.randomUUID());
    }

    @Operation(summary = "返回当前 MDC traceId（也可看响应头 X-Trace-Id）")
    @GetMapping("trace")
    public SingleResponse<String> trace() {
        return SingleResponse.of(MDC.get(TraceIdConstants.MDC_KEY));
    }

    @Operation(summary = "虚拟线程演示（查看是否在虚拟线程上处理请求）")
    @GetMapping("virtual-thread")
    public SingleResponse<Map<String, Object>> virtualThread() {
        Thread t = Thread.currentThread();
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("name", t.getName());
        info.put("virtual", t.isVirtual());
        return SingleResponse.of(info);
    }
}
