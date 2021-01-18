package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class ReceiveSensitivityTabelEntry extends Parameter {

	private static final long serialVersionUID = -1783088329269307004L;

	private TLVParameterHeader parameterHeader;
	private int index;
	private int receiveSensitivityValue;

	public ReceiveSensitivityTabelEntry() {
	}

	public ReceiveSensitivityTabelEntry(TLVParameterHeader header, int index, int receiveSensitivityValue) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.RECEIVE_SENSITIVITY_TABLE_ENTRY);
		this.index = index;
		this.receiveSensitivityValue = receiveSensitivityValue;
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

	public int getReceiveSensitivityValue() {
		return receiveSensitivityValue;
	}

	public void setReceiveSensitivityValue(int receiveSensitivityValue) {
		this.receiveSensitivityValue = receiveSensitivityValue;
	}

	@Override
	public String toString() {
		return "ReceiveSensitivityTabelEntry [parameterHeader=" + parameterHeader + ", index=" + index + ", receiveSensitivityValue=" + receiveSensitivityValue
				+ "]";
	}
}