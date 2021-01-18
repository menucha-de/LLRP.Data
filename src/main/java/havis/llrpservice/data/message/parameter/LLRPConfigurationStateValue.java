package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class LLRPConfigurationStateValue extends Parameter {

	private static final long serialVersionUID = 8338397686559605035L;

	private TLVParameterHeader parameterHeader;
	private long llrpConfigStateValue;

	public LLRPConfigurationStateValue() {
	}

	public LLRPConfigurationStateValue(TLVParameterHeader header, long llrpConfigStateValue) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.LLRP_CONFIGURATION_STATE_VALUE);
		this.llrpConfigStateValue = llrpConfigStateValue;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public long getLlrpConfigStateValue() {
		return llrpConfigStateValue;
	}

	public void setLlrpConfigStateValue(long llrpConfigStateValue) {
		this.llrpConfigStateValue = llrpConfigStateValue;
	}

	@Override
	public String toString() {
		return "LLRPConfigurationStateValue [parameterHeader=" + parameterHeader + ", llrpConfigStateValue=" + llrpConfigStateValue + "]";
	}
}