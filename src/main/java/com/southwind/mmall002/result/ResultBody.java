package com.southwind.mmall002.result;

import com.alibaba.fastjson.JSONObject;
import com.southwind.mmall002.enums.CommonEnum;
import com.southwind.mmall002.exception.BaseErrorInfoInterface;

/**
 *
 * @Title: ResultBody
 * @Description: 返回格式
 * @Version:1.0.0
 * @author pancm
 * @date 2018年3月7日
 */
public class ResultBody {
	/**
	 * 响应代码
	 */
	private String code;

	/**
	 * 响应消息
	 */
	private String message;

	/**
	 * 响应结果
	 */
	private Object result;

	public ResultBody() {
	}

	public ResultBody(BaseErrorInfoInterface errorInfo) {
		this.code = errorInfo.getResultCode();
		this.message = errorInfo.getResultMsg();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	/**
	 * 成功
	 * 
	 * @return
	 */
	public static com.southwind.mmall002.result.ResultBody success() {
		return success(null);
	}

	/**
	 * 成功
	 * @param data
	 * @return
	 */
	public static com.southwind.mmall002.result.ResultBody success(Object data) {
		com.southwind.mmall002.result.ResultBody rb = new com.southwind.mmall002.result.ResultBody();
		rb.setCode(CommonEnum.SUCCESS.getResultCode());
		rb.setMessage(CommonEnum.SUCCESS.getResultMsg());
		rb.setResult(data);
		return rb;
	}

	/**
	 * 失败
	 */
	public static com.southwind.mmall002.result.ResultBody error(BaseErrorInfoInterface errorInfo) {
		com.southwind.mmall002.result.ResultBody rb = new com.southwind.mmall002.result.ResultBody();
		rb.setCode(errorInfo.getResultCode());
		rb.setMessage(errorInfo.getResultMsg());
		rb.setResult(null);
		return rb;
	}

	/**
	 * 失败
	 */
	public static com.southwind.mmall002.result.ResultBody error(String code, String message) {
		com.southwind.mmall002.result.ResultBody rb = new com.southwind.mmall002.result.ResultBody();
		rb.setCode(code);
		rb.setMessage(message);
		rb.setResult(null);
		return rb;
	}

	/**
	 * 失败
	 */
	public static com.southwind.mmall002.result.ResultBody error(String message) {
		com.southwind.mmall002.result.ResultBody rb = new com.southwind.mmall002.result.ResultBody();
		rb.setCode("-1");
		rb.setMessage(message);
		rb.setResult(null);
		return rb;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
