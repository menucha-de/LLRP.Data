package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class AntennaEvent extends Parameter {

	private static final long serialVersionUID = 5642724502847814856L;

	private TLVParameterHeader parameterHeader;
	private AntennaEventType eventType;
	private int antennaID;

	public AntennaEvent() {
	}

	public AntennaEvent(TLVParameterHeader parameterHeader, AntennaEventType eventType, int antennaID) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.ANTENNA_EVENT);
		this.eventType = eventType;
		this.antennaID = antennaID;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public AntennaEventType getEventType() {
		return eventType;
	}

	public void setEventType(AntennaEventType eventType) {
		this.eventType = eventType;
	}

	public int getAntennaID() {
		return antennaID;
	}

	public void setAntennaID(int antennaID) {
		this.antennaID = antennaID;
	}

	@Override
	public String toString() {
		return "AntennaEvent [parameterHeader=" + parameterHeader + ", eventType=" + eventType + ", antennaID=" + antennaID + "]";
	}
}