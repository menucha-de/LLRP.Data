package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class AccessSpecStopTrigger extends Parameter {

	private static final long serialVersionUID = -4286875563589619432L;

	private TLVParameterHeader parameterHeader;
	private AccessSpecStopTriggerType accessSpecStopTriggerType;
	private int operationCountValue;

	public AccessSpecStopTrigger() {
	}

	public AccessSpecStopTrigger(TLVParameterHeader parameterHeader, AccessSpecStopTriggerType accessSpecStopTriggerType, int operationCountValue) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.ACCESS_SPEC_STOP_TRIGGER);
		this.accessSpecStopTriggerType = accessSpecStopTriggerType;
		this.operationCountValue = operationCountValue;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public AccessSpecStopTriggerType getAccessSpecStopTriggerType() {
		return accessSpecStopTriggerType;
	}

	public void setAccessSpecStopTriggerType(AccessSpecStopTriggerType accessSpecStopTriggerType) {
		this.accessSpecStopTriggerType = accessSpecStopTriggerType;
	}

	public void setAccessSpecStopTrigger(AccessSpecStopTriggerType accessSpecStopTriggerType) {
		this.accessSpecStopTriggerType = accessSpecStopTriggerType;
	}

	public int getOperationCountValue() {
		return operationCountValue;
	}

	public void setOperationCountValue(int operationCountValue) {
		this.operationCountValue = operationCountValue;
	}

	@Override
	public String toString() {
		return "AccessSpecStopTrigger [parameterHeader=" + parameterHeader + ", accessSpecStopTriggerType=" + accessSpecStopTriggerType
				+ ", operationCountValue=" + operationCountValue + "]";
	}
}