package com.palette.handler;

import com.palette.constant.MessageConstant;
import com.palette.exception.BaseException;
import com.palette.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Global exception handler, handles business exceptions thrown in the project
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Catch business exceptions
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("Exception info: {}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * Handles SQL exception
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        // example exception msg if input tom again:
        // Duplicate entry 'tom' for key 'employee.idx_username
        if (ex.getMessage().contains("Duplicate entry")) {
            String[] split = ex.getMessage().split(" ");
            // tom should be after 2nd " "
            // split is a list of Strings, split[index = 2]
            String msg = split[2] + MessageConstant.ALREADY_EXISTS; // e.g. "tom account already exists"
            return Result.error(msg);
        } else {
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }

}
