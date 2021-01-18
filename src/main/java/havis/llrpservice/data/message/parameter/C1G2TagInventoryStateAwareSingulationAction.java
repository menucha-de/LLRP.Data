package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class C1G2TagInventoryStateAwareSingulationAction extends Parameter {

	private static final long serialVersionUID = -7304985083627134653L;

	private TLVParameterHeader parameterHeader;
	private C1G2TagInventoryStateAwareSingulationActionI i;
	private C1G2TagInventoryStateAwareSingulationActionS s;
	private C1G2TagInventoryStateAwareSingulationActionSAll a;

	public C1G2TagInventoryStateAwareSingulationAction() {
	}

	public C1G2TagInventoryStateAwareSingulationAction(TLVParameterHeader header, C1G2TagInventoryStateAwareSingulationActionI i,
			C1G2TagInventoryStateAwareSingulationActionS s, C1G2TagInventoryStateAwareSingulationActionSAll a) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.C1G2_TAG_INVENTORY_STATE_AWARE_SINGULATION_ACTION);
		this.i = i;
		this.s = s;
		this.a = a;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public C1G2TagInventoryStateAwareSingulationActionI getI() {
		return i;
	}

	public void setI(C1G2TagInventoryStateAwareSingulationActionI i) {
		this.i = i;
	}

	public C1G2TagInventoryStateAwareSingulationActionS getS() {
		return s;
	}

	public void setS(C1G2TagInventoryStateAwareSingulationActionS s) {
		this.s = s;
	}

	public C1G2TagInventoryStateAwareSingulationActionSAll getA() {
		return a;
	}

	public void setA(C1G2TagInventoryStateAwareSingulationActionSAll a) {
		this.a = a;
	}

	@Override
	public String toString() {
		return "C1G2TagInventoryStateAwareSingulationAction [parameterHeader=" + parameterHeader + ", i=" + i + ", s=" + s + ", a=" + a + "]";
	}
}