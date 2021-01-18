package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class ROSpecEvent extends Parameter {

	private static final long serialVersionUID = -9065195332850159768L;

	private TLVParameterHeader parameterHeader;
	private ROSpecEventType eventType;
	private long roSpecID;
	private long preemptingROSpecID;

	public ROSpecEvent() {
	}

	public ROSpecEvent(TLVParameterHeader parameterHeader, ROSpecEventType eventType, long roSpecID, long preemptingROSpecID) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.RO_SPEC_EVENT);
		this.eventType = eventType;
		this.roSpecID = roSpecID;
		this.preemptingROSpecID = preemptingROSpecID;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public ROSpecEventType getEventType() {
		return eventType;
	}

	public void setEventType(ROSpecEventType eventType) {
		this.eventType = eventType;
	}

	public long getRoSpecID() {
		return roSpecID;
	}

	public void setRoSpecID(long roSpecID) {
		this.roSpecID = roSpecID;
	}

	public long getPreemptingROSpecID() {
		return preemptingROSpecID;
	}

	public void setPreemptingROSpecID(long preemptingROSpecID) {
		this.preemptingROSpecID = preemptingROSpecID;
	}

	@Override
	public String toString() {
		return "ROSpecEvent [parameterHeader=" + parameterHeader + ", eventType=" + eventType + ", roSpecID=" + roSpecID + ", preemptingROSpecID="
				+ preemptingROSpecID + "]";
	}
}