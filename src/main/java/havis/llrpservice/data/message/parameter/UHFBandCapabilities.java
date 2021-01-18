package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.List;

public class UHFBandCapabilities extends Parameter {

	private static final long serialVersionUID = -1145855145886302427L;

	private TLVParameterHeader parameterHeader;
	private List<TransmitPowerLevelTableEntry> transmitPowerTable;
	private FrequencyInformation frequencyInformation;
	private List<UHFC1G2RFModeTable> uhfC1G2RFModeTable;
	private RFSurveyFrequencyCapabilities rfSurveyFrequencyCapabilities;

	public UHFBandCapabilities() {
	}

	public UHFBandCapabilities(TLVParameterHeader header, List<TransmitPowerLevelTableEntry> transmitPowerTable, FrequencyInformation frequencyInformation,
			List<UHFC1G2RFModeTable> uhfC1G2RFModeTable) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.UHF_BAND_CAPABILITIES);
		this.transmitPowerTable = transmitPowerTable;
		this.frequencyInformation = frequencyInformation;
		this.uhfC1G2RFModeTable = uhfC1G2RFModeTable;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public List<TransmitPowerLevelTableEntry> getTransmitPowerTable() {
		return transmitPowerTable;
	}

	public void setTransmitPowerTable(List<TransmitPowerLevelTableEntry> transmitPowerTable) {
		this.transmitPowerTable = transmitPowerTable;
	}

	public FrequencyInformation getFrequencyInformation() {
		return frequencyInformation;
	}

	public void setFrequencyInformation(FrequencyInformation frequencyInformation) {
		this.frequencyInformation = frequencyInformation;
	}

	public List<UHFC1G2RFModeTable> getUhfC1G2RFModeTable() {
		return uhfC1G2RFModeTable;
	}

	public void setUhfC1G2RFModeTable(List<UHFC1G2RFModeTable> uhfC1G2RFModeTable) {
		this.uhfC1G2RFModeTable = uhfC1G2RFModeTable;
	}

	public RFSurveyFrequencyCapabilities getRfSurveyFrequencyCapabilities() {
		return rfSurveyFrequencyCapabilities;
	}

	public void setRfSurveyFrequencyCapabilities(RFSurveyFrequencyCapabilities rfSurveyFrequencyCapabilities) {
		this.rfSurveyFrequencyCapabilities = rfSurveyFrequencyCapabilities;
	}

	@Override
	public String toString() {
		return "UHFBandCapabilities [parameterHeader=" + parameterHeader + ", transmitPowerTable=" + transmitPowerTable + ", frequencyInformation="
				+ frequencyInformation + ", uhfC1G2RFModeTable=" + uhfC1G2RFModeTable + ", rfSurveyFrequencyCapabilities=" + rfSurveyFrequencyCapabilities
				+ "]";
	}
}