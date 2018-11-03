package com.pay.yc.common.result.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pay.yc.common.result.PageData;
import com.pay.yc.common.util.CustomRuntimeException;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author dumx
 * 2017年8月1日 下午10:49:42
 * @param <T>
 */
public class PageResultDTO<T> extends ListResultDTO<T> {
	@ApiModelProperty(value="分页信息", position=2)
	  private PageData pageable;
	  
	  @JsonInclude(JsonInclude.Include.NON_NULL)
	  @JsonProperty(value="data", index=2)
	  public List<T> getData()
	  {
	    return super.getData();
	  }
	  
	  @JsonInclude(JsonInclude.Include.NON_NULL)
	  @JsonProperty(value="pageable", index=3)
	  public PageData getPageable()
	  {
	    return this.pageable;
	  }
	  	
	  public void setPageable(PageData pageable)
	  {
	    this.pageable = pageable;
	  }
	  
	  public PageResultDTO() {}
	  
	  PageResultDTO(AbstractResultDTO.Status status)
	  {
	    super(status);
	  }
	  
	  public static <T> PageResultDTO<T> success(Page<T> pageData)
	  {
	    if (pageData == null) {
	      throw new CustomRuntimeException("NullPointerException", "The formal parameter 'pageData' cannot be null");
	    }
	    PageResultDTO<T> result = new PageResultDTO(AbstractResultDTO.Status.success);
	    result.setData(pageData.getContent());
	    result.setPageable(PageData.convert(pageData));
	    return result;
	  }
	  
	  public static <T> PageResultDTO<T> success(List<T> listData, Page<?> pageData)
	  {
	    if (pageData == null) {
	      throw new CustomRuntimeException("NullPointerException", "The formal parameter 'pageData' cannot be null");
	    }
	    PageResultDTO<T> result = new PageResultDTO(AbstractResultDTO.Status.success);
	    result.setData(listData);
	    result.setPageable(PageData.convert(pageData));
	    return result;
	  }
}
