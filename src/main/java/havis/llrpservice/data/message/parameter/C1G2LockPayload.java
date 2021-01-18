package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class C1G2LockPayload extends Parameter {

	private static final long serialVersionUID = 3811400522810324491L;

	private TLVParameterHeader parameterHeader;
	private C1G2LockPayloadPrivilege privilege;
	private C1G2LockPayloadDataField dataField;

	public C1G2LockPayload() {
	}

	public C1G2LockPayload(TLVParameterHeader parameterHeader, C1G2LockPayloadPrivilege privilege, C1G2LockPayloadDataField dataField) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.C1G2_LOCK_PAYLOAD);
		this.privilege = privilege;
		this.dataField = dataField;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public C1G2LockPayloadPrivilege getPrivilege() {
		return privilege;
	}

	public void setPrivilege(C1G2LockPayloadPrivilege privilege) {
		this.privilege = privilege;
	}

	public C1G2LockPayloadDataField getDataField() {
		return dataField;
	}

	public void setDataField(C1G2LockPayloadDataField dataField) {
		this.dataField = dataField;
	}

	@Override
	public String toString() {
		return "C1G2LockPayload [parameterHeader=" + parameterHeader + ", privilege=" + privilege + ", dataField=" + dataField + "]";
	}
}