package com.pay.yc.common.result;

import org.springframework.data.domain.Page;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author dumx
 * 2017年8月1日 下午10:49:37
 */
public class PageData {
	@ApiModelProperty(value="总条数", position=1)
	  private long totalElements;
	  @ApiModelProperty(value="返回条数", position=2)
	  private int numberOfElements;
	  @ApiModelProperty(value="总页数", position=3)
	  private int totalPages;
	  @ApiModelProperty(value="当前页码（从0开始）", position=4)
	  private int number;
	  @ApiModelProperty(value="是否第一页", position=5)
	  private boolean first;
	  @ApiModelProperty(value="是否最后页", position=6)
	  private boolean last;
	  @ApiModelProperty(value="页SIZE", position=7)
	  private int size;
	  
	  public static PageData convert(Page<?> page)
	  {
	    PageData pageData = new PageData();
	    pageData.setFirst(page.isFirst());
	    pageData.setLast(page.isLast());
	    pageData.setNumber(page.getNumber());
	    pageData.setTotalPages(page.getTotalPages());
	    pageData.setNumberOfElements(page.getNumberOfElements());
	    pageData.setSize(page.getSize());
	    pageData.setTotalElements(page.getTotalElements());
	    return pageData;
	  }
	  
	  public long getTotalElements()
	  {
	    return this.totalElements;
	  }
	  
	  public void setTotalElements(long total)
	  {
	    this.totalElements = total;
	  }
	  
	  public int getNumberOfElements()
	  {
	    return this.numberOfElements;
	  }
	  
	  public void setNumberOfElements(int rows)
	  {
	    this.numberOfElements = rows;
	  }
	  
	  public int getTotalPages()
	  {
	    return this.totalPages;
	  }
	  
	  public void setTotalPages(int pages)
	  {
	    this.totalPages = pages;
	  }
	  
	  public boolean isFirst()
	  {
	    return this.first;
	  }
	  
	  public void setFirst(boolean first)
	  {
	    this.first = first;
	  }
	  
	  public boolean isLast()
	  {
	    return this.last;
	  }
	  
	  public void setLast(boolean last)
	  {
	    this.last = last;
	  }
	  
	  public int getSize()
	  {
	    return this.size;
	  }
	  
	  public void setSize(int size)
	  {
	    this.size = size;
	  }
	  
	  public int getNumber()
	  {
	    return this.number;
	  }
	  
	  public void setNumber(int page)
	  {
	    this.number = page;
	  }
	  
	  @ApiModelProperty(value="本页开始行（相对于总记录数，从1开始计数）", position=8)
	  public long getFromNumber()
	  {
	    if (this.numberOfElements == 0) {
	      return this.number * this.size;
	    }
	    return this.number * this.size + 1;
	  }
	  
	  @ApiModelProperty(value="本页结束行", position=9)
	  public long getToNumber()
	  {
	    return this.number * this.size + this.numberOfElements;
	  }
}
