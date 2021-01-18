package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class C1G2TagInventoryStateAwareFilterAction extends Parameter {

	private static final long serialVersionUID = -5971775471391985072L;

	private TLVParameterHeader parameterHeader;
	private C1G2TagInventoryStateAwareFilterActionTarget target;
	private short action;

	public C1G2TagInventoryStateAwareFilterAction() {
	}

	public C1G2TagInventoryStateAwareFilterAction(TLVParameterHeader header, C1G2TagInventoryStateAwareFilterActionTarget target, short action) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.C1G2_TAG_INVENTORY_STATE_AWARE_FILTER_ACTION);
		this.target = target;
		this.action = action;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public C1G2TagInventoryStateAwareFilterActionTarget getTarget() {
		return target;
	}

	public void setTarget(C1G2TagInventoryStateAwareFilterActionTarget target) {
		this.target = target;
	}

	public short getAction() {
		return action;
	}

	public void setAction(short action) {
		this.action = action;
	}

	@Override
	public String toString() {
		return "C1G2TagInventoryStateAwareFilterAction [parameterHeader=" + parameterHeader + ", target=" + target + ", action=" + action + "]";
	}
}