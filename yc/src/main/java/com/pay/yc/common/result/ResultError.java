package com.pay.yc.common.result;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author dumx
 * 2017年8月1日 下午9:53:00
 */
public class ResultError {
	 @ApiModelProperty(value="字段名（字段校验类异常）", position=0)
	  private String field;
	  @ApiModelProperty(value="异常消息", position=1)
	  private String errmsg;
	  @ApiModelProperty(value="异常编码", position=2)
	  private String errcode;
	  
	  public ResultError() {}
	  
	  public ResultError(String errmsg, String field)
	  {
	    this.field = field;
	    this.errmsg = errmsg;
	  }
	  
	  public ResultError(String errcode, String errmsg, String field)
	  {
	    this.field = field;
	    this.errcode = errcode;
	    this.errmsg = errmsg;
	  }
	  
	  public String getField()
	  {
	    return this.field;
	  }
	  
	  public String getErrmsg()
	  {
	    return this.errmsg;
	  }
	  
	  public String getErrcode()
	  {
	    return this.errcode;
	  }
	  
	  public void setErrcode(String errcode)
	  {
	    this.errcode = errcode;
	  }
	  
	  public void setField(String field)
	  {
	    this.field = field;
	  }
	  
	  public void setErrmsg(String errmsg)
	  {
	    this.errmsg = errmsg;
	  }
	  
	  public String toString()
	  {
	    return String.format("{errcode:%s, errmsg:%s, field:%s}", new Object[] { this.errcode, this.errmsg, this.field });
	  }
	  public static ResultErrorBuilder builder(){
	  	return new ResultErrorBuilder();
	  }

	public static class ResultErrorBuilder{
		private String field;
		private String errmsg;
		private String errcode;
		public ResultErrorBuilder withField(String field){
			this.field = field;
			return this;
		}
		public ResultErrorBuilder withErrmsg(String errmsg){
			this.errmsg = errmsg;
			return this;
		}
		public ResultErrorBuilder withErrcode(String errcode){
			this.errcode = errcode;
			return this;
		}
		public ResultError build(){
			ResultError re = new ResultError();
			re.setErrcode(this.errcode);
			re.setErrmsg(this.errmsg);
			re.setField(this.field);
			return re;
		}
	}
}
