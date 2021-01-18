package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class GPIEvent extends Parameter {

	private static final long serialVersionUID = 7178082084970354156L;

	private TLVParameterHeader parameterHeader;
	private int gpiPortNumber;
	private boolean state;

	public GPIEvent() {
	}

	public GPIEvent(TLVParameterHeader parameterHeader, int gpiPortNumber, boolean state) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.GPI_EVENT);
		this.gpiPortNumber = gpiPortNumber;
		this.state = state;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getGpiPortNumber() {
		return gpiPortNumber;
	}

	public void setGpiPortNumber(int gpiPortNumber) {
		this.gpiPortNumber = gpiPortNumber;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "GPIEvent [parameterHeader=" + parameterHeader + ", gpiPortNumber=" + gpiPortNumber + ", state=" + state + "]";
	}
}