package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.List;

public class RFSurveySpec extends Parameter {

	private static final long serialVersionUID = 7613173912120274541L;

	private TLVParameterHeader parameterHeader;
	private int antennaID;
	private long startFreq;
	private long endFreq;
	private RFSurveySpecStopTrigger rfSSStopTrigger;
	private List<Custom> cusList;

	public RFSurveySpec() {
	}

	public RFSurveySpec(TLVParameterHeader parameterHeader, int antennaID, long startFreq, long endFreq, RFSurveySpecStopTrigger rfSSStopTrigger) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.RF_SURVEY_SPEC);
		this.antennaID = antennaID;
		this.startFreq = startFreq;
		this.endFreq = endFreq;
		this.rfSSStopTrigger = rfSSStopTrigger;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getAntennaID() {
		return antennaID;
	}

	public void setAntennaID(int antennaID) {
		this.antennaID = antennaID;
	}

	public long getStartFreq() {
		return startFreq;
	}

	public void setStartFreq(long startFreq) {
		this.startFreq = startFreq;
	}

	public long getEndFreq() {
		return endFreq;
	}

	public void setEndFreq(long endFreq) {
		this.endFreq = endFreq;
	}

	public RFSurveySpecStopTrigger getRfSSStopTrigger() {
		return rfSSStopTrigger;
	}

	public void setRfSSStopTrigger(RFSurveySpecStopTrigger rfSSStopTrigger) {
		this.rfSSStopTrigger = rfSSStopTrigger;
	}

	public List<Custom> getCusList() {
		return cusList;
	}

	public void setCusList(List<Custom> cusList) {
		this.cusList = cusList;
	}

	@Override
	public String toString() {
		return "RFSurveySpec [parameterHeader=" + parameterHeader + ", antennaID=" + antennaID + ", startFreq=" + startFreq + ", endFreq=" + endFreq
				+ ", rfSSStopTrigger=" + rfSSStopTrigger + ", cusList=" + cusList + "]";
	}
}