package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class ReportBufferLevelWarningEvent extends Parameter {

	private static final long serialVersionUID = -5067375041773675810L;

	private TLVParameterHeader parameterHeader;
	private short reportBufferPercentageFull;

	public ReportBufferLevelWarningEvent() {
	}

	public ReportBufferLevelWarningEvent(TLVParameterHeader parameterHeader, short reportBufferPercentageFull) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.REPORT_BUFFER_LEVEL_WARNING_EVENT);
		this.reportBufferPercentageFull = reportBufferPercentageFull;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public short getReportBufferPercentageFull() {
		return reportBufferPercentageFull;
	}

	public void setReportBufferPercentageFull(short reportBufferPercentageFull) {
		this.reportBufferPercentageFull = reportBufferPercentageFull;
	}

	@Override
	public String toString() {
		return "ReportBufferLevelWarningEvent [parameterHeader=" + parameterHeader + ", reportBufferPercentageFull=" + reportBufferPercentageFull + "]";
	}
}