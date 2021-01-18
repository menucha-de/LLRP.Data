package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class AISpecEvent extends Parameter {

	private static final long serialVersionUID = 7321333668693116775L;

	private TLVParameterHeader parameterHeader;

	private AISpecEventType eventType;
	private long roSpecID;
	private int specIndex;
	private C1G2SingulationDetails c1g2SingulationDetails;

	public AISpecEvent() {
	}

	public AISpecEvent(TLVParameterHeader parameterHeader, AISpecEventType eventType, long roSpecID, int specIndex) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.AI_SPEC_EVENT);
		this.eventType = eventType;
		this.roSpecID = roSpecID;
		this.specIndex = specIndex;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public AISpecEventType getEventType() {
		return eventType;
	}

	public void setEventType(AISpecEventType eventType) {
		this.eventType = eventType;
	}

	public long getRoSpecID() {
		return roSpecID;
	}

	public void setRoSpecID(long roSpecID) {
		this.roSpecID = roSpecID;
	}

	public int getSpecIndex() {
		return specIndex;
	}

	public void setSpecIndex(int specIndex) {
		this.specIndex = specIndex;
	}

	public C1G2SingulationDetails getC1g2SingulationDetails() {
		return c1g2SingulationDetails;
	}

	public void setC1g2SingulationDetails(C1G2SingulationDetails c1g2SingulationDetails) {
		this.c1g2SingulationDetails = c1g2SingulationDetails;
	}

	@Override
	public String toString() {
		return "AISpecEvent [parameterHeader=" + parameterHeader + ", eventType=" + eventType + ", roSpecID=" + roSpecID + ", specIndex=" + specIndex
				+ ", c1g2SingulationDetails=" + c1g2SingulationDetails + "]";
	}
}