package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class ROSpecStartTrigger extends Parameter {

	private static final long serialVersionUID = 3754475578601946773L;

	private TLVParameterHeader parameterHeader;
	private ROSpecStartTriggerType roSpecStartTriggerType;
	private PeriodicTriggerValue periodicTV;
	private GPITriggerValue gpiTV;

	public ROSpecStartTrigger() {
	}

	public ROSpecStartTrigger(TLVParameterHeader parameterHeader, ROSpecStartTriggerType roSpecStartTriggerType) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.RO_SPEC_START_TRIGGER);
		this.roSpecStartTriggerType = roSpecStartTriggerType;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public ROSpecStartTriggerType getRoSpecStartTriggerType() {
		return roSpecStartTriggerType;
	}

	public void setRoSpecStartTriggerType(ROSpecStartTriggerType roSpecStartTriggerType) {
		this.roSpecStartTriggerType = roSpecStartTriggerType;
	}

	public PeriodicTriggerValue getPeriodicTV() {
		return periodicTV;
	}

	public void setPeriodicTV(PeriodicTriggerValue periodicTV) {
		this.periodicTV = periodicTV;
	}

	public GPITriggerValue getGpiTV() {
		return gpiTV;
	}

	public void setGpiTV(GPITriggerValue gpiTV) {
		this.gpiTV = gpiTV;
	}

	@Override
	public String toString() {
		return "ROSpecStartTrigger [parameterHeader=" + parameterHeader + ", roSpecStartTriggerType=" + roSpecStartTriggerType + ", periodicTV=" + periodicTV
				+ ", gpiTV=" + gpiTV + "]";
	}
}