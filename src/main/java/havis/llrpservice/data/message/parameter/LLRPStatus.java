package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class LLRPStatus extends Parameter {

	private static final long serialVersionUID = 1808627712610235833L;

	private TLVParameterHeader parameterHeader;
	private LLRPStatusCode statusCode;
	private int errorDescriptionByteCount;
	private String errorDescription;
	private FieldError fieldError;
	private ParameterError parameterError;

	public LLRPStatus() {
	}

	public LLRPStatus(TLVParameterHeader header, LLRPStatusCode statusCode, String errorDescription) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.LLRP_STATUS);
		this.statusCode = statusCode;
		this.errorDescription = errorDescription;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public LLRPStatusCode getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(LLRPStatusCode statusCode) {
		this.statusCode = statusCode;
	}

	public int getErrorDescriptionByteCount() {
		return errorDescriptionByteCount;
	}

	public void setErrorDescriptionByteCount(int errorDescriptionByteCount) {
		this.errorDescriptionByteCount = errorDescriptionByteCount;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public FieldError getFieldError() {
		return fieldError;
	}

	public void setFieldError(FieldError fieldError) {
		this.fieldError = fieldError;
	}

	public ParameterError getParameterError() {
		return parameterError;
	}

	public void setParameterError(ParameterError parameterError) {
		this.parameterError = parameterError;
	}

	@Override
	public String toString() {
		return "LLRPStatus [parameterHeader=" + parameterHeader + ", statusCode=" + statusCode + ", errorDescriptionByteCount=" + errorDescriptionByteCount
				+ ", errorDescription=" + errorDescription + ", fieldError=" + fieldError + ", parameterError=" + parameterError + "]";
	}
}