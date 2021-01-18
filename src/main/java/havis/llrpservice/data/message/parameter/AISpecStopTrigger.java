package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class AISpecStopTrigger extends Parameter {

	private static final long serialVersionUID = -1933955025629424439L;

	private TLVParameterHeader parameterHeader;
	private AISpecStopTriggerType aiSpecStopTriggerType;
	private long durationTrigger;
	private GPITriggerValue gpiTV;
	private TagObservationTrigger tagOT;

	public AISpecStopTrigger() {
	}

	public AISpecStopTrigger(TLVParameterHeader parameterHeader, AISpecStopTriggerType aiSpecStopTriggerType, long durationTrigger) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.AI_SPEC_STOP_TRIGGER);
		this.aiSpecStopTriggerType = aiSpecStopTriggerType;
		this.durationTrigger = durationTrigger;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public AISpecStopTriggerType getAiSpecStopTriggerType() {
		return aiSpecStopTriggerType;
	}

	public void setAiSpecStopTriggerType(AISpecStopTriggerType aiSpecStopTriggerType) {
		this.aiSpecStopTriggerType = aiSpecStopTriggerType;
	}

	public long getDurationTrigger() {
		return durationTrigger;
	}

	public void setDurationTrigger(long durationTrigger) {
		this.durationTrigger = durationTrigger;
	}

	public GPITriggerValue getGpiTV() {
		return gpiTV;
	}

	public void setGpiTV(GPITriggerValue gpiTV) {
		this.gpiTV = gpiTV;
	}

	public TagObservationTrigger getTagOT() {
		return tagOT;
	}

	public void setTagOT(TagObservationTrigger tagOT) {
		this.tagOT = tagOT;
	}

	@Override
	public String toString() {
		return "AISpecStopTrigger [parameterHeader=" + parameterHeader + ", aiSpecStopTriggerType=" + aiSpecStopTriggerType + ", durationTrigger="
				+ durationTrigger + ", gpiTV=" + gpiTV + ", tagOT=" + tagOT + "]";
	}
}