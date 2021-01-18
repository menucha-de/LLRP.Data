package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class C1G2TagInventoryStateUnawareFilterAction extends Parameter {

	private static final long serialVersionUID = 9113181995884457425L;

	private TLVParameterHeader parameterHeader;
	private C1G2TagInventoryStateUnawareFilterActionValues action;

	public C1G2TagInventoryStateUnawareFilterAction() {
	}

	public C1G2TagInventoryStateUnawareFilterAction(TLVParameterHeader header, C1G2TagInventoryStateUnawareFilterActionValues action) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.C1G2_TAG_INVENTORY_STATE_UNAWARE_FILTER_ACTION);
		this.action = action;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public C1G2TagInventoryStateUnawareFilterActionValues getAction() {
		return action;
	}

	public void setAction(C1G2TagInventoryStateUnawareFilterActionValues action) {
		this.action = action;
	}

	@Override
	public String toString() {
		return "C1G2TagInventoryStateUnawareFilterAction [parameterHeader=" + parameterHeader + ", action=" + action + "]";
	}
}