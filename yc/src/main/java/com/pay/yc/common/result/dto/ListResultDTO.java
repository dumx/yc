package com.pay.yc.common.result.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pay.yc.common.result.ResultError;
import com.pay.yc.common.util.CustomRuntimeException;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author dumx
 * 2017年8月1日 下午9:52:53
 * @param <T>
 */
public class ListResultDTO<T> extends AbstractResultDTO {
	@ApiModelProperty(value="业务数据（List）", position=1)
	  protected List<T> data;
	  
	  @JsonInclude(JsonInclude.Include.NON_NULL)
	  @JsonProperty(value="data", index=3)
	  public List<T> getData()
	  {
	    return this.data;
	  }
	  
	  public void setData(List<T> data)
	  {
	    this.data = data;
	  }
	  
	  public ListResultDTO() {}
	  
	  ListResultDTO(AbstractResultDTO.Status status)
	  {
	    this.status = status;
	  }
	  
	  public static <T> ListResultDTO<T> success(List<T> listData)
	  {
	    if (listData == null) {
	      throw new CustomRuntimeException("NullPointerException", "The formal parameter 'listData' cannot be null");
	    }
	    ListResultDTO<T> result = new ListResultDTO(AbstractResultDTO.Status.success);
	    result.setData(listData);
	    return result;
	  }
	  
	  public static <T> ListResultDTO<T> failure(List<T> listData, ResultError... errors)
	  {
	    ListResultDTO<T> result = new ListResultDTO(AbstractResultDTO.Status.failure);
	    result.setData(listData);
	    result.setErrors(errors);
	    return result;
	  }
}
