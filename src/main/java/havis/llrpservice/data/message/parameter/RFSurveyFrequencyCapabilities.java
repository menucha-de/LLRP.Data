package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class RFSurveyFrequencyCapabilities extends Parameter {

	private static final long serialVersionUID = -5893853959583532499L;

	private TLVParameterHeader parameterHeader;
	private long minimumFrequency;
	private long maximumFrequency;

	public RFSurveyFrequencyCapabilities() {
	}

	public RFSurveyFrequencyCapabilities(TLVParameterHeader header, long minimumFrequency, long maximumFrequency) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.RF_SURVEY_FREQUENCY_CAPABILITIES);
		this.minimumFrequency = minimumFrequency;
		this.maximumFrequency = maximumFrequency;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public long getMinimumFrequency() {
		return minimumFrequency;
	}

	public void setMinimumFrequency(long minimumFrequency) {
		this.minimumFrequency = minimumFrequency;
	}

	public long getMaximumFrequency() {
		return maximumFrequency;
	}

	public void setMaximumFrequency(long maximumFrequency) {
		this.maximumFrequency = maximumFrequency;
	}

	@Override
	public String toString() {
		return "RFSurveyFrequencyCapabilities [parameterHeader=" + parameterHeader + ", minimumFrequency=" + minimumFrequency + ", maximumFrequency="
				+ maximumFrequency + "]";
	}
}