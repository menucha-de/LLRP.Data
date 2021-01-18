package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class RFReceiver extends Parameter {

	private static final long serialVersionUID = 2256347185248199091L;

	private TLVParameterHeader parameterHeader;
	private int receiverSensitivity;

	public RFReceiver() {
	}

	public RFReceiver(TLVParameterHeader header, int receiverSensitivity) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.RF_RECEIVER);
		this.receiverSensitivity = receiverSensitivity;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getReceiverSensitivity() {
		return receiverSensitivity;
	}

	public void setReceiverSensitivity(int receiverSensitivity) {
		this.receiverSensitivity = receiverSensitivity;
	}

	@Override
	public String toString() {
		return "RFReceiver [parameterHeader=" + parameterHeader + ", receiverSensitivity=" + receiverSensitivity + "]";
	}
}