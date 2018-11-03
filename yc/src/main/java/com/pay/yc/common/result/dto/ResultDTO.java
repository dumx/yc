package com.pay.yc.common.result.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pay.yc.common.result.ResultError;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author dumx
 * 2017年8月1日 下午10:49:47
 * @param <T>
 */
public class ResultDTO<T> extends AbstractResultDTO {
	@ApiModelProperty(value="业务数据", position=1)
	  private T data;
	  
	  @JsonInclude(JsonInclude.Include.NON_NULL)
	  @JsonProperty(value="data", index=2)
	  public T getData()
	  {
	    return this.data;
	  }
	  
	  private void setData(T data)
	  {
	    this.data = data;
	  }
	  
	  public ResultDTO() {}
	  
	  ResultDTO(AbstractResultDTO.Status status)
	  {
	    this.status = status;
	  }
	  
	  public static ResultDTO<Void> success()
	  {
	    ResultDTO<Void> result = new ResultDTO(AbstractResultDTO.Status.success);
	    return result;
	  }
	  
	  public static ResultDTO<Void> failure(ResultError... errors)
	  {
	    ResultDTO<Void> result = new ResultDTO(AbstractResultDTO.Status.failure);
	    result.setErrors(errors);
	    return result;
	  }
	  
	  public static <T> ResultDTO<T> success(T data)
	  {
	    ResultDTO<T> result = new ResultDTO(AbstractResultDTO.Status.success);
	    result.setData(data);
	    return result;
	  }
	  
	  public static <T> ResultDTO<T> failure(T data, ResultError... errors)
	  {
	    ResultDTO<T> result = new ResultDTO(AbstractResultDTO.Status.failure);
	    result.setData(data);
	    result.setErrors(errors);
	    return result;
	  }
}
