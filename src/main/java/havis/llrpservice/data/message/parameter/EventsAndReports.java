package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class EventsAndReports extends Parameter {

	private static final long serialVersionUID = 7037327204110103055L;

	private TLVParameterHeader parameterHeader;
	private boolean hold;

	public EventsAndReports() {
	}

	public EventsAndReports(TLVParameterHeader header, boolean hold) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.EVENTS_AND_REPORTS);
		this.hold = hold;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public boolean getHold() {
		return hold;
	}

	public void setHold(boolean hold) {
		this.hold = hold;
	}

	@Override
	public String toString() {
		return "EventsAndReports [parameterHeader=" + parameterHeader + ", hold=" + hold + "]";
	}
}