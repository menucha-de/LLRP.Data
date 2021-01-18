package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class GPOWriteData extends Parameter {

	private static final long serialVersionUID = -9066159980571450722L;

	private TLVParameterHeader parameterHeader;
	private int gpoPortNum;
	private boolean gpoState;

	public GPOWriteData() {
	}

	public GPOWriteData(TLVParameterHeader header, int gpoPortNum, boolean gpoState) {
		this.parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.GPO_WRITE_DATA);
		this.gpoPortNum = gpoPortNum;
		this.gpoState = gpoState;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getGpoPortNum() {
		return gpoPortNum;
	}

	public void setGpoPortNum(int gpoPortNum) {
		this.gpoPortNum = gpoPortNum;
	}

	public boolean getGpoState() {
		return gpoState;
	}

	public void setGpoState(boolean gpoState) {
		this.gpoState = gpoState;
	}

	@Override
	public String toString() {
		return "GPOWriteData [parameterHeader=" + parameterHeader + ", gpoPortNum=" + gpoPortNum + ", gpoState=" + gpoState + "]";
	}
}