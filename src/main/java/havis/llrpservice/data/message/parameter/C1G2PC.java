package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class C1G2PC extends Parameter {

	private static final long serialVersionUID = 3913443701455475172L;

	private TVParameterHeader parameterHeader;
	private int pcBits;

	public C1G2PC() {
	}

	public C1G2PC(TVParameterHeader parameterHeader, int pcBits) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.C1G2_PC);
		this.pcBits = pcBits;
	}

	public TVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getPcBits() {
		return pcBits;
	}

	public void setPcBits(int pcBits) {
		this.pcBits = pcBits;
	}

	@Override
	public String toString() {
		return "C1G2PC [parameterHeader=" + parameterHeader + ", pcBits=" + pcBits + "]";
	}
}