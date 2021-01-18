package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class GPIPortCurrentState extends Parameter {

	private static final long serialVersionUID = -8472874856351092140L;

	private TLVParameterHeader parameterHeader;
	private int gpiPortNum;
	private boolean gpiConfig;
	private GPIPortCurrentStateGPIState state;

	public GPIPortCurrentState() {
	}

	public GPIPortCurrentState(TLVParameterHeader header, int gpiPortNum, boolean gpiConfig, GPIPortCurrentStateGPIState state) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.GPI_PORT_CURRENT_STATE);
		this.gpiPortNum = gpiPortNum;
		this.gpiConfig = gpiConfig;
		this.state = state;
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

	public boolean getGpiConfig() {
		return gpiConfig;
	}

	public void setGpiConfig(boolean gpiConfig) {
		this.gpiConfig = gpiConfig;
	}

	public GPIPortCurrentStateGPIState getState() {
		return state;
	}

	public void setState(GPIPortCurrentStateGPIState state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "GPIPortCurrentState [parameterHeader=" + parameterHeader + ", gpiPortNum=" + gpiPortNum + ", gpiConfig=" + gpiConfig + ", state=" + state + "]";
	}
}