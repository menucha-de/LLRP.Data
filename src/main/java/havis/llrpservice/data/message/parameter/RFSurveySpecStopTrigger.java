package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class RFSurveySpecStopTrigger extends Parameter {

	private static final long serialVersionUID = 8939715799685944522L;

	private TLVParameterHeader parameterHeader;
	private RFSurveySpecStopTriggerType triggerType;
	private long duration;
	private long n;

	public RFSurveySpecStopTrigger() {
	}

	public RFSurveySpecStopTrigger(TLVParameterHeader parameterHeader, RFSurveySpecStopTriggerType triggerType, long duration, long n) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.RF_SURVEY_SPEC_STOP_TRIGGER);
		this.triggerType = triggerType;
		this.duration = duration;
		this.n = n;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public RFSurveySpecStopTriggerType getTriggerType() {
		return triggerType;
	}

	public void setTriggerType(RFSurveySpecStopTriggerType triggerType) {
		this.triggerType = triggerType;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getN() {
		return n;
	}

	public void setN(long n) {
		this.n = n;
	}

	@Override
	public String toString() {
		return "RFSurveySpecStopTrigger [parameterHeader=" + parameterHeader + ", stopTriggerType=" + triggerType + ", duration=" + duration + ", n=" + n + "]";
	}
}