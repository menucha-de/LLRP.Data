package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.List;

public class RFSurveyReportData extends Parameter {

	private static final long serialVersionUID = 8429002571558106226L;

	private TLVParameterHeader parameterHeader;
	private ROSpecID roSpecID;
	private SpecIndex specIndex;
	private List<FrequencyRSSILevelEntry> frequencyRSSILevelEntryList;
	private List<Custom> cusList;

	public RFSurveyReportData() {
	}

	public RFSurveyReportData(TLVParameterHeader parameterHeader, List<FrequencyRSSILevelEntry> frequencyRSSILevelEntryList) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.RF_SURVEY_REPORT_DATA);
		this.frequencyRSSILevelEntryList = frequencyRSSILevelEntryList;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public ROSpecID getRoSpecID() {
		return roSpecID;
	}

	public void setRoSpecID(ROSpecID roSpecID) {
		this.roSpecID = roSpecID;
	}

	public SpecIndex getSpecIndex() {
		return specIndex;
	}

	public void setSpecIndex(SpecIndex specIndex) {
		this.specIndex = specIndex;
	}

	public List<FrequencyRSSILevelEntry> getFrequencyRSSILevelEntryList() {
		return frequencyRSSILevelEntryList;
	}

	public void setFrequencyRSSILevelEntryList(List<FrequencyRSSILevelEntry> frequencyRSSILevelEntryList) {
		this.frequencyRSSILevelEntryList = frequencyRSSILevelEntryList;
	}

	public List<Custom> getCusList() {
		return cusList;
	}

	public void setCusList(List<Custom> cusList) {
		this.cusList = cusList;
	}

	@Override
	public String toString() {
		return "RFSurveyReportData [parameterHeader=" + parameterHeader + ", roSpecID=" + roSpecID + ", specIndex=" + specIndex
				+ ", frequencyRSSILevelEntryList=" + frequencyRSSILevelEntryList + ", cusList=" + cusList + "]";
	}
}