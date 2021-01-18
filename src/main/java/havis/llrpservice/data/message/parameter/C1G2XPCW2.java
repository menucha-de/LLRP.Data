package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class C1G2XPCW2 extends Parameter {

	private static final long serialVersionUID = -2436024654740882373L;

	private TVParameterHeader parameterHeader;
	private int xpcW2;

	public C1G2XPCW2() {
	}

	public C1G2XPCW2(TVParameterHeader parameterHeader, int xpcW2) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.C1G2_XPCW2);
		this.xpcW2 = xpcW2;
	}

	public TVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getXpcW2() {
		return xpcW2;
	}

	public void setXpcW2(int xpcW2) {
		this.xpcW2 = xpcW2;
	}

	@Override
	public String toString() {
		return "C1G2XPCW2 [parameterHeader=" + parameterHeader + ", xpcW2=" + xpcW2 + "]";
	}
}