package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class FieldError extends Parameter {

	private static final long serialVersionUID = 891079294415278927L;

	private TLVParameterHeader parameterHeader;
	private int fieldNum;
	private LLRPStatusCode errorCode;

	public FieldError() {
	}

	public FieldError(TLVParameterHeader header, int fieldNum, LLRPStatusCode errorCode) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.FIELD_ERROR);
		this.fieldNum = fieldNum;
		this.errorCode = errorCode;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getFieldNum() {
		return fieldNum;
	}

	public void setFieldNum(int fieldNum) {
		this.fieldNum = fieldNum;
	}

	public LLRPStatusCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(LLRPStatusCode errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String toString() {
		return "FieldError [parameterHeader=" + parameterHeader + ", fieldNum=" + fieldNum + ", errorCode=" + errorCode + "]";
	}
}