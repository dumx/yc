package com.pay.yc.common.result.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public abstract class AbstractDTO {

	@ApiModelProperty(value="主键ID", position=0)
	private Long id;

	@JsonIgnore
	public boolean isNew()
	{
	  return this.id == null;
	}

	private Long createdBy;

	private Date createTime;

	private Long lastModifiedBy;

	private Date lastModifiedDate;

}
