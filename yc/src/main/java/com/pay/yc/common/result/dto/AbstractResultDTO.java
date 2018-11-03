package com.pay.yc.common.result.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pay.yc.common.result.ResultError;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 
 * @author dumx
 * 2017年8月1日 下午9:52:47
 */
public abstract class AbstractResultDTO
{
  @ApiModelProperty(value="结果状态（成功 或 失败）", position=0)
  protected Status status;
  @ApiModelProperty(value="异常信息", position=10)
  protected ResultError[] errors;
  @ApiModelProperty(value="系统处理时间戳（增量拉数据时使用）", position=11)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  protected Date timestamp;
  
  public static enum Status
  {
    success,  failure;
    
    private Status() {}
  }
  
  @JsonProperty(value="status", index=0)
  public Status getStatus()
  {
    return this.status;
  }
  
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonProperty(value="errors", index=1)
  public ResultError[] getErrors()
  {
    return this.errors;
  }
  
  protected void setErrors(ResultError... errors)
  {
    this.errors = errors;
  }
  
  @JsonIgnore
  public boolean isFailure()
  {
    return Status.failure == this.status;
  }
  
  @JsonIgnore
  public boolean isSuccess()
  {
    return Status.success == this.status;
  }
  
  public Date getTimestamp()
  {
    return this.timestamp;
  }
  
  public void setTimestamp(Date timestamp)
  {
    this.timestamp = timestamp;
  }
  
  @JsonIgnore
  public String errorsToString()
  {
    if ((this.errors != null) && (this.errors.length > 0))
    {
      StringBuilder builder = new StringBuilder();
      builder.append("Errors : [");
      for (ResultError error : this.errors) {
        builder.append(error.toString());
      }
      builder.append("]");
      return builder.toString();
    }
    return "errors : []";
  }
}
