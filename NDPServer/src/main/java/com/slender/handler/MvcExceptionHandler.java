package com.slender.handler;

import com.slender.exception.authentication.captcha.CaptchaMisMatchException;
import com.slender.exception.authentication.captcha.CaptchaNotFoundException;
import com.slender.exception.authentication.captcha.CaptchaPersistenceException;
import com.slender.exception.authentication.login.LocalCacheNotFoundException;
import com.slender.exception.authentication.login.LoginExpiredException;
import com.slender.exception.authentication.login.LoginStatusException;
import com.slender.exception.category.*;
import com.slender.exception.file.ExcelExportException;
import com.slender.exception.order.OrderNotFoundException;
import com.slender.exception.order.OrderNotPaidSuccessException;
import com.slender.exception.product.ProductNotFoundException;
import com.slender.exception.file.FileNameErrorException;
import com.slender.exception.file.FileNameNotFoundException;
import com.slender.exception.product.ReviewNotFoundException;
import com.slender.exception.request.FrequentRequestCaptchaException;
import com.slender.exception.file.OSSProcessingException;
import com.slender.exception.user.*;
import com.slender.message.ExceptionMessage;
import com.slender.result.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class MvcExceptionHandler {
    @ExceptionHandler
    public Response<Void> handleException(Exception e){
        log.error("内部异常",e);
        return Response.fail(e.getMessage());
    }

    @ExceptionHandler
    public Response<Void> validation(MethodArgumentNotValidException e){
        return Response.fail(e.getBindingResult().getAllErrors().getFirst().getDefaultMessage());
    }

    @ExceptionHandler
    public Response<Void> request(RequestException ex){
        return switch (ex){
            case FrequentRequestCaptchaException e -> Response.fail(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            default -> handleException(ex);
        };
    }

    @ExceptionHandler
    public Response<Void> file(FileException ex){
        return switch (ex){
            case FileNameErrorException _ -> Response.fail(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.FILE_NAME_ERROR);
            case FileNameNotFoundException _ -> Response.fail(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.FILE_NAME_NOT_FOUND);
            case OSSProcessingException _ -> Response.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), ExceptionMessage.OSS_ERROR);
            case ExcelExportException _ -> Response.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), ExceptionMessage.EXCEL_EXPORT_ERROR);
            default -> handleException(ex);
        };
    }

    @ExceptionHandler
    public Response<Void> captcha(CaptchaException ex){
        return switch (ex){
            case CaptchaNotFoundException _ -> Response.fail(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.CAPTCHA_NOT_FOUND);
            case CaptchaMisMatchException _ -> Response.fail(HttpStatus.UNAUTHORIZED.value(), ExceptionMessage.CAPTCHA_ERROR);
            case CaptchaPersistenceException _ -> Response.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), ExceptionMessage.CAPTCHA_PERSISTENCE_ERROR);
            default -> handleException(ex);
        };
    }

    @ExceptionHandler
    public Response<Void> login(LoginException ex){
        return switch (ex){
            case LocalCacheNotFoundException _ -> Response.fail(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.LOCAL_CACHE_ERROR);
            case LoginExpiredException _ -> Response.fail(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.LOGIN_EXPIRED_ERROR);
            case LoginStatusException _ -> Response.fail(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.LOGIN_STATUS_ERROR);
            default -> handleException(ex);
        };
    }

    @ExceptionHandler
    public Response<Void> user(UserException ex){
        return switch (ex){
            case UserNotFoundException _ -> Response.fail(HttpStatus.NOT_FOUND.value(), ExceptionMessage.USER_NOT_FOUND);
            case AddressNotFoundException _ -> Response.fail(HttpStatus.NOT_FOUND.value(), ExceptionMessage.ADDRESS_NOT_FOUND);
            case MerchantNotFoundException _ -> Response.fail(HttpStatus.NOT_FOUND.value(), ExceptionMessage.MERCHANT_NOT_FOUND);
            case IllegalOperationException _ -> Response.fail(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.ILLEGAL_OPERATION);
            case DuplicateFavouriteException _ -> Response.fail(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.DUPLICATE_FAVOURITE);
            case FavouriteNotFoundException _ -> Response.fail(HttpStatus.NOT_FOUND.value(), ExceptionMessage.FAVOURITE_NOT_FOUND);
            default -> handleException(ex);
        };
    }

    @ExceptionHandler
    public Response<Void> product(ProductException ex){
        return switch (ex){
            case ReviewNotFoundException _ -> Response.fail(HttpStatus.NOT_FOUND.value(), ExceptionMessage.REVIEW_NOT_FOUND);
            case ProductNotFoundException _ -> Response.fail(HttpStatus.NOT_FOUND.value(), ExceptionMessage.PRODUCT_NOT_FOUND);
            default -> handleException(ex);
        };
    }

    @ExceptionHandler
    public Response<Void> order(OrderException ex){
        return switch (ex){
            case OrderNotFoundException _ -> Response.fail(HttpStatus.NOT_FOUND.value(), ExceptionMessage.ORDER_NOT_FOUND);
            case OrderNotPaidSuccessException _ -> Response.fail(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.ORDER_NOT_PAID_SUCCESS);
            default -> handleException(ex);
        };
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<Void> duplicateKey(DuplicateKeyException e){
        String[] split = e.getCause().getMessage().split(" ");
        String[] key = split[split.length - 1].replace("'", "").split("\\.");
        String value = split[2].replace("'", "");
        String keyFirst=key[0];
        String keySecond=key[1];
        return switch (keyFirst){
            case "user" ->
                switch (keySecond) {
                    case "user_user_name" -> Response.fail(HttpStatus.BAD_REQUEST.value(), "用户名"+value+"已存在");
                    case "user_phone_number" -> Response.fail(HttpStatus.BAD_REQUEST.value(),"手机号"+value+"已存在");
                    case "user_email" -> Response.fail(HttpStatus.BAD_REQUEST.value(),"邮箱"+value+"已存在");
                    default -> handleException(e);
                };
            case "review" ->
                switch (keySecond){
                    case "review_oid" -> Response.fail(HttpStatus.BAD_REQUEST.value(),"该订单已评价");
                    default -> handleException(e);
                };
            default -> handleException(e);
        };
    }

}
