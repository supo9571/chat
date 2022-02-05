package com.pangugle.im.limit;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pangugle.framework.bean.ApiJsonTemplate;
import com.pangugle.im.TokenErrorResult;

/**
 * @author XXX
 * @create 2018-11-22 13:55
 */
//@ControllerAdvice
public class MyTokenExceptionHandle {
//    private static Log LOG = LogFactory.getLog(MyTokenExceptionHandle.class);
    
//    @ExceptionHandler(UnauthorizedException.class)
//    public String unauthorizedExceptionHandle(Exception e){
//        return "redirect:/admin/unauthorized";
//    }
//    @ExceptionHandler(UnauthenticatedException.class)
//    public String unauthenticatedExceptionHandle(Exception e){
//        return "redirect:/admin/toLogin";
//    }

    @ExceptionHandler(MyTokenLimitException.class)
    @ResponseBody
    public String exceptionHandleMyLoginLimit(Throwable e){
        ApiJsonTemplate apiJsonTemplate = new ApiJsonTemplate();
        apiJsonTemplate.setJsonResult(TokenErrorResult.ERR_ACCESSTOKEN_INVALID);
        return apiJsonTemplate.toJSONString();
    }
    
    
//  @ExceptionHandler(Throwable.class)
//  @ResponseBody
//  public String handleException(Throwable e){
//	  LOG.error("un handle error:", e);
//      ApiJsonTemplate apiJsonTemplate = new ApiJsonTemplate();
//      apiJsonTemplate.setJsonResult(SystemErrorResult.ERR_SYS_BUSY);
//      return apiJsonTemplate.toJSONString();
//  }
  

}
