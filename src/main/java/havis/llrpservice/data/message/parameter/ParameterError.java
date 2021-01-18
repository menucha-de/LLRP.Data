package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class ParameterError extends Parameter {

	private static final long serialVersionUID = -8611879620628461352L;

	private TLVParameterHeader parameterHeader;
	private LLRPStatusCode errorCode;
	private int parameterType;
	private FieldError fieldError;
	private ParameterError parameterError;

	public ParameterError() {
	}

	public ParameterError(TLVParameterHeader header, int parameterType, LLRPStatusCode errorCode) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.PARAMETER_ERROR);
		this.parameterType = parameterType;
		this.errorCode = errorCode;
	}

	public FieldError getFieldError() {
		return fieldError;
	}

	public void setFieldError(FieldError filedError) {
		this.fieldError = filedError;
	}

	public ParameterError getParameterError() {
		return parameterError;
	}

	public void setParameterError(ParameterError parameterError) {
		this.parameterError = parameterError;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public LLRPStatusCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(LLRPStatusCode errorCode) {
		this.errorCode = errorCode;
	}

	public int getParameterType() {
		return parameterType;
	}

	public void setParameterType(int parameterType) {
		this.parameterType = parameterType;
	}

	@Override
	public String toString() {
		return "ParameterError [parameterHeader=" + parameterHeader + ", errorCode=" + errorCode + ", parameterType=" + parameterType + ", filedError="
				+ fieldError + ", parameterError=" + parameterError + "]";
	}
}