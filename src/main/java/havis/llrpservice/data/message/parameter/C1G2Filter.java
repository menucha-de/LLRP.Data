package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class C1G2Filter extends Parameter {

	private static final long serialVersionUID = -2171043389552345073L;

	private TLVParameterHeader parameterHeader;
	private C1G2FilterTruncateAction t;
	private C1G2TagInventoryMask c1g2TagInventoryMask;
	private C1G2TagInventoryStateAwareFilterAction c1g2TagInventoryStateAwareFilterAction;
	private C1G2TagInventoryStateUnawareFilterAction c1g2TagInventoryStateUnawareFilterAction;

	public C1G2Filter() {
	}

	public C1G2Filter(TLVParameterHeader header, C1G2FilterTruncateAction t, C1G2TagInventoryMask c1g2TagInventoryMask) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.C1G2_FILTER);
		this.t = t;
		this.c1g2TagInventoryMask = c1g2TagInventoryMask;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public C1G2FilterTruncateAction getT() {
		return t;
	}

	public void setT(C1G2FilterTruncateAction t) {
		this.t = t;
	}

	public C1G2TagInventoryMask getC1g2TagInventoryMask() {
		return c1g2TagInventoryMask;
	}

	public void setC1g2TagInventoryMask(C1G2TagInventoryMask c1g2TagInventoryMask) {
		this.c1g2TagInventoryMask = c1g2TagInventoryMask;
	}

	public C1G2TagInventoryStateAwareFilterAction getC1g2TagInventoryStateAwareFilterAction() {
		return c1g2TagInventoryStateAwareFilterAction;
	}

	public void setC1g2TagInventoryStateAwareFilterAction(C1G2TagInventoryStateAwareFilterAction c1g2TagInventoryStateAwareFilterAction) {
		this.c1g2TagInventoryStateAwareFilterAction = c1g2TagInventoryStateAwareFilterAction;
	}

	public C1G2TagInventoryStateUnawareFilterAction getC1g2TagInventoryStateUnawareFilterAction() {
		return c1g2TagInventoryStateUnawareFilterAction;
	}

	public void setC1g2TagInventoryStateUnawareFilterAction(C1G2TagInventoryStateUnawareFilterAction c1g2TagInventoryStateUnawareFilterAction) {
		this.c1g2TagInventoryStateUnawareFilterAction = c1g2TagInventoryStateUnawareFilterAction;
	}

	@Override
	public String toString() {
		return "C1G2Filter [parameterHeader=" + parameterHeader + ", t=" + t + ", c1g2TagInventoryMask=" + c1g2TagInventoryMask
				+ ", c1g2TagInventoryStateAwareFilterAction=" + c1g2TagInventoryStateAwareFilterAction + ", c1g2TagInventoryStateUnawareFilterAction="
				+ c1g2TagInventoryStateUnawareFilterAction + "]";
	}
}