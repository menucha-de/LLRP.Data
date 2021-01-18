package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class PeriodicTriggerValue extends Parameter {

	private static final long serialVersionUID = 6187564030046555919L;

	private TLVParameterHeader parameterHeader;
	private long offSet;
	private long period;
	private UTCTimestamp utc;

	public PeriodicTriggerValue() {
	}

	public PeriodicTriggerValue(TLVParameterHeader parameterHeader, long offSet, long period) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.PERIODIC_TRIGGER_VALUE);
		this.offSet = offSet;
		this.period = period;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public long getOffSet() {
		return offSet;
	}

	public void setOffSet(long offSet) {
		this.offSet = offSet;
	}

	public long getPeriod() {
		return period;
	}

	public void setPeriod(long period) {
		this.period = period;
	}

	public UTCTimestamp getUtc() {
		return utc;
	}

	public void setUtc(UTCTimestamp utc) {
		this.utc = utc;
	}

	@Override
	public String toString() {
		return "PeriodicTriggerValue [parameterHeader=" + parameterHeader + ", offSet=" + offSet + ", period=" + period + ", utc=" + utc + "]";
	}
}