package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class GPIOCapabilities extends Parameter {

	private static final long serialVersionUID = 1443214823414464781L;

	private TLVParameterHeader parameterHeader;
	private int numGPIs;
	private int numGPOs;

	public GPIOCapabilities() {
	}

	public GPIOCapabilities(TLVParameterHeader header, int numGPIs, int numGPOs) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.GPIO_CAPABILITIES);
		this.numGPIs = numGPIs;
		this.numGPOs = numGPOs;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getNumGPIs() {
		return numGPIs;
	}

	public void setNumGPIs(int numGPIs) {
		this.numGPIs = numGPIs;
	}

	public int getNumGPOs() {
		return numGPOs;
	}

	public void setNumGPOs(int numGPOs) {
		this.numGPOs = numGPOs;
	}

	@Override
	public String toString() {
		return "GPIOCapabilities [parameterHeader=" + parameterHeader + ", numGPIs=" + numGPIs + ", numGPOs=" + numGPOs + "]";
	}
}