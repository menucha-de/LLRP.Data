package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.List;

public class UHFC1G2RFModeTable extends Parameter {

	private static final long serialVersionUID = -6907399361561294549L;

	private TLVParameterHeader parameterHeader;
	private List<UHFC1G2RFModeTableEntry> uhfC1G2RFModeSet;

	public UHFC1G2RFModeTable() {
	}

	public UHFC1G2RFModeTable(TLVParameterHeader header, List<UHFC1G2RFModeTableEntry> uhfC1G2RFModeSet) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.UHF_C1G2_RF_MODE_TABLE);
		this.uhfC1G2RFModeSet = uhfC1G2RFModeSet;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public List<UHFC1G2RFModeTableEntry> getUhfC1G2RFModeSet() {
		return uhfC1G2RFModeSet;
	}

	public void setUhfC1G2RFModeSet(List<UHFC1G2RFModeTableEntry> uhfC1G2RFModeSet) {
		this.uhfC1G2RFModeSet = uhfC1G2RFModeSet;
	}

	@Override
	public String toString() {
		return "UHFC1G2RFModeTable [parameterHeader=" + parameterHeader + ", uhfC1G2RFModeSet=" + uhfC1G2RFModeSet + "]";
	}
}