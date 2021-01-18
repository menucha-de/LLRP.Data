package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class ROSpecID extends Parameter {

	private static final long serialVersionUID = 274241300014700929L;

	private TVParameterHeader parameterHeader;
	private long roSpecID;

	public ROSpecID() {
	}

	public ROSpecID(TVParameterHeader parameterHeader, long roSpecID) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.RO_SPEC_ID);
		this.roSpecID = roSpecID;
	}

	public TVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public long getRoSpecID() {
		return roSpecID;
	}

	public void setRoSpecID(long roSpecID) {
		this.roSpecID = roSpecID;
	}

	@Override
	public String toString() {
		return "ROSpecID [parameterHeader=" + parameterHeader + ", roSpecID=" + roSpecID + "]";
	}
}