package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class ROSpecStopTrigger extends Parameter {

	private static final long serialVersionUID = 3824731294812043849L;

	private TLVParameterHeader parameterHeader;
	private ROSpecStopTriggerType roSpecStopTriggerType;
	private long durationTriggerValue;
	private GPITriggerValue gpiTriggerValue;

	public ROSpecStopTrigger() {
	}

	public ROSpecStopTrigger(TLVParameterHeader parameterHeader, ROSpecStopTriggerType roSpecStopTriggerType, long durationTriggerValue) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.RO_SPEC_STOP_TRIGGER);
		this.roSpecStopTriggerType = roSpecStopTriggerType;
		this.durationTriggerValue = durationTriggerValue;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public ROSpecStopTriggerType getRoSpecStopTriggerType() {
		return roSpecStopTriggerType;
	}

	public void setRoSpecStopTriggerType(ROSpecStopTriggerType roSpecStopTriggerType) {
		this.roSpecStopTriggerType = roSpecStopTriggerType;
	}

	public long getDurationTriggerValue() {
		return durationTriggerValue;
	}

	public void setDurationTriggerValue(long durationTriggerValue) {
		this.durationTriggerValue = durationTriggerValue;
	}

	public GPITriggerValue getGpiTriggerValue() {
		return gpiTriggerValue;
	}

	public void setGpiTriggerValue(GPITriggerValue gpiTriggerValue) {
		this.gpiTriggerValue = gpiTriggerValue;
	}

	@Override
	public String toString() {
		return "ROSpecStopTrigger [parameterHeader=" + parameterHeader + ", roSpecStopTriggerType=" + roSpecStopTriggerType + ", durationTriggerValue="
				+ durationTriggerValue + ", gpiTriggerValue=" + gpiTriggerValue + "]";
	}
}