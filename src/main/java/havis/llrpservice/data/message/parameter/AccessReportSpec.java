package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class AccessReportSpec extends Parameter {

	private static final long serialVersionUID = 552350242457123647L;

	private TLVParameterHeader parameterHeader;
	private AccessReportTrigger accessReportTrigger;

	public AccessReportSpec() {
	}

	public AccessReportSpec(TLVParameterHeader header, AccessReportTrigger accessReportTrigger) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.ACCESS_REPORT_SPEC);
		this.accessReportTrigger = accessReportTrigger;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public AccessReportTrigger getAccessReportTrigger() {
		return accessReportTrigger;
	}

	public void setAccessReportTrigger(AccessReportTrigger accessReportTrigger) {
		this.accessReportTrigger = accessReportTrigger;
	}

	@Override
	public String toString() {
		return "AccessReportSpec [parameterHeader=" + parameterHeader + ", accessReportTrigger=" + accessReportTrigger + "]";
	}
}