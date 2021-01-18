package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class MaximumReceiveSensitivity extends Parameter {

	private static final long serialVersionUID = 8356962929733443844L;

	private TLVParameterHeader parameterHeader;
	private int maximumSensitivityValue;

	public MaximumReceiveSensitivity() {
	}

	public MaximumReceiveSensitivity(TLVParameterHeader header, int maximumSensitivityValue) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.MAXIMUM_RECEIVE_SENSITIVITY);
		this.maximumSensitivityValue = maximumSensitivityValue;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getMaximumSensitivityValue() {
		return maximumSensitivityValue;
	}

	public void setMaximumSensitivityValue(int maximumSensitivityValue) {
		this.maximumSensitivityValue = maximumSensitivityValue;
	}

	@Override
	public String toString() {
		return "MaximumReceiveSensitivity [parameterHeader=" + parameterHeader + ", maximumSensitivityValue=" + maximumSensitivityValue + "]";
	}
}