package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class GPITriggerValue extends Parameter {

	private static final long serialVersionUID = 2668025750513036392L;

	private TLVParameterHeader parameterHeader;
	private int gpiPortNum;
	private boolean gpiEvent;
	private long timeOut;

	public GPITriggerValue() {
	}

	public GPITriggerValue(TLVParameterHeader parameterHeader, int gpiPortNum, boolean gpiEvent, long timeOut) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.GPI_TRIGGER_VALUE);
		this.gpiPortNum = gpiPortNum;
		this.gpiEvent = gpiEvent;
		this.timeOut = timeOut;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getGpiPortNum() {
		return gpiPortNum;
	}

	public void setGpiPortNum(int gpiPortNum) {
		this.gpiPortNum = gpiPortNum;
	}

	public boolean isGpiEvent() {
		return gpiEvent;
	}

	public void setGpiEvent(boolean gpiEvent) {
		this.gpiEvent = gpiEvent;
	}

	public long getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}

	@Override
	public String toString() {
		return "GPITriggerValue [parameterHeader=" + parameterHeader + ", gpiPortNum=" + gpiPortNum + ", gpiEvent=" + gpiEvent + ", timeOut=" + timeOut + "]";
	}
}