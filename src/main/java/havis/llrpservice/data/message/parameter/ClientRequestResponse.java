package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.List;

public class ClientRequestResponse extends Parameter {

	private static final long serialVersionUID = 5401128264255895833L;

	private TLVParameterHeader parameterHeader;
	private long accessSpecID;
	private EPCData epcData;
	private List<Parameter> opSpecList;

	public ClientRequestResponse() {
	}

	public ClientRequestResponse(TLVParameterHeader parameterHeader, long accessSpecID, EPCData epcData) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.CLIENT_REQUEST_RESPONSE);
		this.accessSpecID = accessSpecID;
		this.epcData = epcData;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public long getAccessSpecID() {
		return accessSpecID;
	}

	public void setAccessSpecID(long accessSpecID) {
		this.accessSpecID = accessSpecID;
	}

	public EPCData getEpcData() {
		return epcData;
	}

	public void setEpcData(EPCData epcData) {
		this.epcData = epcData;
	}

	public List<Parameter> getOpSpecList() {
		return opSpecList;
	}

	public void setOpSpecList(List<Parameter> opSpecList) {
		this.opSpecList = opSpecList;
	}

	@Override
	public String toString() {
		return "ClientRequestResponse [parameterHeader=" + parameterHeader + ", accessSpecID=" + accessSpecID + ", epcData=" + epcData + ", opSpecList="
				+ opSpecList + "]";
	}
}