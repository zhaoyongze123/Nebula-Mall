package com.nebulamall.product.exception;

import com.common.utils.R;
import com.common.exception.BizCodeEnum;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 * 统一处理数据校验异常和其他业务异常
 *
 * @author zyz
 * @email zhaoyongze2023@gmail.com
 * @date 2026-02-09
 */
@RestControllerAdvice("com.nebulamall.product.controller")
public class GlobalExceptionHandler {

    /**
     * 处理数据校验异常
     * MethodArgumentNotValidException: @Valid 验证失败时抛出
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errorMap = new HashMap<>();
        
        // 提取所有字段验证错误
        e.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errorMap.put(fieldName, errorMessage);
        });
        
        // 返回标准格式，使用通用的参数格式校验错误码
        BizCodeEnum bizCode = BizCodeEnum.VALID_EXCEPTION;
        return R.error(bizCode.getCode(), bizCode.getMsg()).put("data", errorMap);
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        e.printStackTrace();
        BizCodeEnum bizCode = BizCodeEnum.UNKNOWN_EXCEPTION;
        return R.error(bizCode.getCode(), bizCode.getMsg());
    }
}
