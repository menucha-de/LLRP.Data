package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class ConnectionCloseEvent extends Parameter {

	private static final long serialVersionUID = 5874703368282772107L;

	private TLVParameterHeader parameterHeader;

	public ConnectionCloseEvent() {
	}

	public ConnectionCloseEvent(TLVParameterHeader header) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.CONNECTION_CLOSE_EVENT);
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	@Override
	public String toString() {
		return "ConnectionCloseEvent [parameterHeader=" + parameterHeader + "]";
	}
}