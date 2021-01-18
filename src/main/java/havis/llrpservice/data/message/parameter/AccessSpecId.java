package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class AccessSpecId extends Parameter {

	private static final long serialVersionUID = -1427995953625789080L;

	private TVParameterHeader parameterHeader;
	private long accessSpecId;

	public AccessSpecId() {
	}

	public AccessSpecId(TVParameterHeader parameterHeader, long accessSpecId) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.ACCESS_SPEC_ID);
		this.accessSpecId = accessSpecId;
	}

	public TVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public long getAccessSpecId() {
		return accessSpecId;
	}

	public void setAccessSpecId(long accessSpecId) {
		this.accessSpecId = accessSpecId;
	}

	public void setAccessSpecID(long accessSpecId) {
		this.accessSpecId = accessSpecId;
	}

	@Override
	public String toString() {
		return "AccessSpecId [parameterHeader=" + parameterHeader + ", accessSpecId=" + accessSpecId + "]";
	}
}