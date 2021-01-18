package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class TagObservationTrigger extends Parameter {

	private static final long serialVersionUID = -8587313942232010028L;

	private TLVParameterHeader parameterHeader;
	private TagObservationTriggerType triggerType;
	private int numberOfTags;
	private int numberOfAttempts;
	private int t;
	private long timeOut;

	public TagObservationTrigger() {
	}

	public TagObservationTrigger(TLVParameterHeader parameterHeader, TagObservationTriggerType triggerType, int numberOfTags, int numberOfAttempts, int t,
			long timeOut) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.TAG_OBSERVATION_TRIGGER);
		this.triggerType = triggerType;
		this.numberOfTags = numberOfTags;
		this.numberOfAttempts = numberOfAttempts;
		this.t = t;
		this.timeOut = timeOut;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public TagObservationTriggerType getTriggerType() {
		return triggerType;
	}

	public void setTriggerType(TagObservationTriggerType triggerType) {
		this.triggerType = triggerType;
	}

	public int getNumberOfTags() {
		return numberOfTags;
	}

	public void setNumberOfTags(int numberOfTags) {
		this.numberOfTags = numberOfTags;
	}

	public int getNumberOfAttempts() {
		return numberOfAttempts;
	}

	public void setNumberOfAttempts(int numberOfAttempts) {
		this.numberOfAttempts = numberOfAttempts;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}

	public long getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}

	@Override
	public String toString() {
		return "TagObservationTrigger [parameterHeader=" + parameterHeader + ", triggerType=" + triggerType + ", numberOfTags=" + numberOfTags
				+ ", numberOfAttempts=" + numberOfAttempts + ", t=" + t + ", timeOut=" + timeOut + "]";
	}
}