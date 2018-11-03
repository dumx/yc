package com.pay.yc.common.result.dto;

import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.UnknownHttpStatusCodeException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.pay.yc.common.result.ResultError;
import com.pay.yc.common.util.CustomRuntimeException;
/**
 * 异常处理
 * @author dumingxin
 *
 */
@Conditional({RestResponseExceptionHandler.HandlerCondition.class})
@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(RestResponseExceptionHandler.class);
	@Autowired
	protected MessageSource messageSource;

	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		String method = ((ServletWebRequest) request).getHttpMethod().toString();
		String url = ((ServletWebRequest) request).getRequest().getRequestURL().toString();
		logger.error(method + " " + url, ex);
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ArrayList errors = new ArrayList();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String defaultMessage = error.getDefaultMessage();
			String message = this.getLocalMessage(defaultMessage, defaultMessage, new Object[0]);
			errors.add(new ResultError(defaultMessage, message, (String) null));
		});
		ex.getBindingResult().getFieldErrors().forEach((error) -> {
			String objectName = error.getObjectName();
			String defaultMessage = error.getDefaultMessage();
			String field = error.getField();
			errors.add(new ResultError(objectName, defaultMessage, field));
		});
		ResultDTO resultDTO = ResultDTO.failure((ResultError[]) errors.toArray(new ResultError[0]));
		return new ResponseEntity(resultDTO, headers, status);
	}

	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String parameterName = ex.getParameterName();
		String parameterType = ex.getParameterType();
		String message = ex.getMessage();
		ResultDTO resultDTO = ResultDTO
				.failure(new ResultError[]{new ResultError(parameterType, message, parameterName)});
		return new ResponseEntity(resultDTO, headers, status);
	}

	@ExceptionHandler({CustomRuntimeException.class})
	public ResponseEntity<Object> exceptionHandler(CustomRuntimeException ex, HttpServletRequest request) {
		String method = request.getMethod();
		String url = request.getRequestURL().toString();
		logger.info(method + " " + url, ex);
		String code = ex.getCode();
		String message = this.getLocalMessage(code, ex.getMessage(), ex.getMessage());
		logger.info("[" + code + "] - " + message);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		ResultDTO resultDTO = ResultDTO.failure(new ResultError[]{new ResultError(code, message, (String) null)});
		return new ResponseEntity(resultDTO, headers, HttpStatus.OK);
	}

	@ExceptionHandler({RestClientException.class})
	public ResponseEntity<Object> exceptionHandler(RestClientException ex, HttpServletRequest request) {
		String method = request.getMethod();
		String url = request.getRequestURL().toString();
		logger.error(method + " " + url, ex);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String statusCode;
		String statusText;
		String responseBody;
		if (ex instanceof HttpStatusCodeException) {
			statusCode = String.valueOf(((HttpStatusCodeException) ex).getStatusCode());
			statusText = ((HttpStatusCodeException) ex).getStatusText();
			responseBody = ((HttpStatusCodeException) ex).getResponseBodyAsString();
			logger.error("[" + statusCode + "] - " + statusText);
			return new ResponseEntity(responseBody, headers, HttpStatus.OK);
		} else if (ex instanceof UnknownHttpStatusCodeException) {
			statusCode = String.valueOf(((UnknownHttpStatusCodeException) ex).getRawStatusCode());
			statusText = ((UnknownHttpStatusCodeException) ex).getStatusText();
			responseBody = ((UnknownHttpStatusCodeException) ex).getResponseBodyAsString();
			logger.error("[" + statusCode + "] - " + statusText);
			return new ResponseEntity(responseBody, headers, HttpStatus.OK);
		} else {
			return new ResponseEntity(ResultDTO.failure(new ResultError[0]), headers, HttpStatus.OK);
		}
	}

	@ExceptionHandler({AccessDenyException.class})
	public ResponseEntity<Object> exceptionHandler(AccessDenyException ex, HttpServletRequest request) {
		String method = request.getMethod();
		String url = request.getRequestURL().toString();
		logger.info(method + " " + url, ex);
		String message = this.getLocalMessage(ex.getCode(), ex.getMessage(), new Object[0]);
		logger.info(message);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		ResultDTO resultDTO = ResultDTO.failure(new ResultError[]{new ResultError("403", message, (String) null)});
		return new ResponseEntity(resultDTO, headers, HttpStatus.OK);
	}

	@ExceptionHandler({Exception.class})
	public ResponseEntity<Object> exceptionHandler(Exception ex, HttpServletRequest request) {
		String method = request.getMethod();
		String url = request.getRequestURL().toString();
		logger.error(method + " " + url + " 请求发生异常.", ex);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return !ex.getClass().getName().contains("Authentication")
				&& !ex.getClass().getName().contains("AccessDeniedException")
						? new ResponseEntity(
								ResultDTO.failure(new ResultError[]{new ResultError("406", "服务异常", (String) null)}),
								headers, HttpStatus.OK)
						: new ResponseEntity(
								ResultDTO.failure(new ResultError[]{new ResultError("403", "无访问权限", (String) null)}),
								headers, HttpStatus.OK);
	}

	protected String getLocalMessage(String code, String defaultMsg, Object... params) {
		Locale local = LocaleContextHolder.getLocale();
		return this.messageSource.getMessage(code, params, defaultMsg, local);
	}

	public static class HandlerCondition implements Condition {
		public boolean matches(ConditionContext paramConditionContext,
				AnnotatedTypeMetadata paramAnnotatedTypeMetadata) {
			String[] existingBeans = paramConditionContext.getBeanFactory()
					.getBeanNamesForType(RestResponseExceptionHandler.class);
			return existingBeans == null || existingBeans.length == 0;
		}
	}
}