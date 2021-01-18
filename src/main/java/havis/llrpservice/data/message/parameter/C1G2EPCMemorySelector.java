package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class C1G2EPCMemorySelector extends Parameter {

	private static final long serialVersionUID = -6964821168088915030L;

	private TLVParameterHeader parameterHeader;
	private boolean enableCRC;
	private boolean enablePCBits;
	private boolean enableXPCBits;

	public C1G2EPCMemorySelector() {
	}

	public C1G2EPCMemorySelector(TLVParameterHeader header, boolean enableCRC, boolean enablePCBits, boolean enableXPCBits) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.C1G2_EPC_MEMORY_SELECTOR);
		this.enableCRC = enableCRC;
		this.enablePCBits = enablePCBits;
		this.enableXPCBits = enableXPCBits;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public boolean isEnableCRC() {
		return enableCRC;
	}

	public void setEnableCRC(boolean enableCRC) {
		this.enableCRC = enableCRC;
	}

	public boolean isEnablePCBits() {
		return enablePCBits;
	}

	public void setEnablePCBits(boolean enablePCBits) {
		this.enablePCBits = enablePCBits;
	}

	public boolean isEnableXPCBits() {
		return enableXPCBits;
	}

	public void setEnableXPCBits(boolean enableXPCBits) {
		this.enableXPCBits = enableXPCBits;
	}

	@Override
	public String toString() {
		return "C1G2EPCMemorySelector [parameterHeader=" + parameterHeader + ", enableCRC=" + enableCRC + ", enablePCBits=" + enablePCBits + ", enableXPCBits="
				+ enableXPCBits + "]";
	}
}