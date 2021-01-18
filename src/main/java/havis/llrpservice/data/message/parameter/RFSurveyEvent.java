package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class RFSurveyEvent extends Parameter {

	private static final long serialVersionUID = -1438124325204819628L;

	private TLVParameterHeader parameterHeader;
	private RFSurveyEventType eventType;
	private long roSpecID;
	private int specIndex;

	public RFSurveyEvent() {
	}

	public RFSurveyEvent(TLVParameterHeader parameterHeader, RFSurveyEventType eventType, long roSpecID, int specIndex) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.RF_SURVEY_EVENT);
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

	public RFSurveyEventType getEventType() {
		return eventType;
	}

	public void setEventType(RFSurveyEventType eventType) {
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

	@Override
	public String toString() {
		return "RFSurveyEvent [parameterHeader=" + parameterHeader + ", eventType=" + eventType + ", roSpecID=" + roSpecID + ", specIndex=" + specIndex + "]";
	}
}