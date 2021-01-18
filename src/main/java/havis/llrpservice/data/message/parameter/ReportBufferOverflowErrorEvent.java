package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class ReportBufferOverflowErrorEvent extends Parameter {

	private static final long serialVersionUID = 7117600161743076997L;

	private TLVParameterHeader parameterHeader;

	public ReportBufferOverflowErrorEvent() {
	}

	public ReportBufferOverflowErrorEvent(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.REPORT_BUFFER_OVERFLOW_ERROR_EVENT);
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	@Override
	public String toString() {
		return "ReportBufferOverflowErrorEvent [parameterHeader=" + parameterHeader + "]";
	}
}