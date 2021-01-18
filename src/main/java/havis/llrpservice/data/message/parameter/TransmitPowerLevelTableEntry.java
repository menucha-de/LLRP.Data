package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class TransmitPowerLevelTableEntry extends Parameter {

	private static final long serialVersionUID = -362973802637193526L;

	private TLVParameterHeader parameterHeader;
	private int index;
	private short transmitPowerValue;

	public TransmitPowerLevelTableEntry() {
	}

	public TransmitPowerLevelTableEntry(TLVParameterHeader header, int index, short transmitPowerValue) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.TRANSMIT_POWER_LEVEL_TABLE_ENTRY);
		this.index = index;
		this.transmitPowerValue = transmitPowerValue;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public short getTransmitPowerValue() {
		return transmitPowerValue;
	}

	public void setTransmitPowerValue(short transmitPowerValue) {
		this.transmitPowerValue = transmitPowerValue;
	}

	@Override
	public String toString() {
		return "TransmitPowerLevelTableEntry [parameterHeader=" + parameterHeader + ", index=" + index + ", transmitPowerValue=" + transmitPowerValue + "]";
	}
}