package com.winter.config;

import com.winter.common.pojo.Response;
import com.winter.common.pojo.ResponseType;
import com.winter.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {
    /**
     * 全局异常捕捉处理
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Response errorHandler(Exception ex, HttpServletRequest request) {
        String uri = request.getRequestURI();
        log.error(uri, ex.getMessage(), ex);
        return Response.failed(ResponseType.SERVER_ERROR);
    }


    @ResponseBody
    @ExceptionHandler(value = ServiceException.class)
    public Response serviceErrorHandler(ServiceException e){
        log.error(e.getMessage());
        return Response.failed(ResponseType.SERVER_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public Response notSupported(){
        return Response.failed(ResponseType.NOT_SUPPORTED);
    }

    @ResponseBody
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public Response notReadable(HttpMessageNotReadableException ex){
        log.error(ex.getMessage());
        return Response.failed(ResponseType.NOT_READABLE);
    }
    /**
     * 404
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public Response handleNotFound(NoHandlerFoundException ex){
        return Response.failed(ResponseType.NOT_FOUND);
    }

    /**
     * 参数校验不通过
     *
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Response validateExceptionHandler(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult().getFieldError().getDefaultMessage();
//        log.error(errorMsg, ex);
        return Response.failed(errorMsg);
    }

    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public Response validateExceptionHandler(MissingServletRequestParameterException ex) {
        String message = ex.getMessage();
        return Response.failed(message);
    }


    /**
     * 参数校验不通过
     */
    @ResponseBody
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Response paramsExceptionHandler(IllegalArgumentException ex) {
        String errorMsg = ex.getMessage();
        log.error(errorMsg, ex);
        return Response.failed(ResponseType.PARAMS_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public Response maxUploadExceptionHandler(MaxUploadSizeExceededException ex) {
        String errorMsg = ex.getMessage();
        log.error(errorMsg, ex);
        return Response.failed("上传文件大小超过3M");
    }
}
