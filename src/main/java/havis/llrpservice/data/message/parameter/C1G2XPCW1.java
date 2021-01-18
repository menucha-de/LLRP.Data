package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class C1G2XPCW1 extends Parameter {

	private static final long serialVersionUID = -1680769514354831845L;

	private TVParameterHeader parameterHeader;
	private int xpcW1;

	public C1G2XPCW1() {
	}

	public C1G2XPCW1(TVParameterHeader parameterHeader, int xpcW1) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.C1G2_XPCW1);
		this.xpcW1 = xpcW1;
	}

	public TVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getXpcW1() {
		return xpcW1;
	}

	public void setXpcW1(int xpcW1) {
		this.xpcW1 = xpcW1;
	}

	@Override
	public String toString() {
		return "C1G2XPCW1 [parameterHeader=" + parameterHeader + ", xpcW1=" + xpcW1 + "]";
	}
}