package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class C1G2RFControl extends Parameter {

	private static final long serialVersionUID = 3912226371509184710L;

	private TLVParameterHeader parameterHeader;
	private int modelIndex;
	private int tari;

	public C1G2RFControl() {
	}

	public C1G2RFControl(TLVParameterHeader header, int modelIndex, int tari) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.C1G2_RF_CONTROL);
		this.modelIndex = modelIndex;
		this.tari = tari;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getModelIndex() {
		return modelIndex;
	}

	public void setModelIndex(int modelIndex) {
		this.modelIndex = modelIndex;
	}

	public int getTari() {
		return tari;
	}

	public void setTari(int tari) {
		this.tari = tari;
	}

	@Override
	public String toString() {
		return "C1G2RFControl [parameterHeader=" + parameterHeader + ", modelIndex=" + modelIndex + ", tari=" + tari + "]";
	}
}