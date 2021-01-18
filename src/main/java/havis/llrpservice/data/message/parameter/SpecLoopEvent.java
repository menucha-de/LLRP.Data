package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class SpecLoopEvent extends Parameter {

	private static final long serialVersionUID = 6819373067409048912L;

	private TLVParameterHeader parameterHeader;
	private long roSpecID;
	private long loopCount;

	public SpecLoopEvent() {
	}

	public SpecLoopEvent(TLVParameterHeader parameterHeader, long roSpecID, long loopCount) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.SPEC_LOOP_EVENT);
		this.roSpecID = roSpecID;
		this.loopCount = loopCount;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public long getRoSpecID() {
		return roSpecID;
	}

	public void setRoSpecID(long roSpecID) {
		this.roSpecID = roSpecID;
	}

	public long getLoopCount() {
		return loopCount;
	}

	public void setLoopCount(long loopCount) {
		this.loopCount = loopCount;
	}

	@Override
	public String toString() {
		return "SpecLoopEvent [parameterHeader=" + parameterHeader + ", roSpecID=" + roSpecID + ", loopCount=" + loopCount + "]";
	}
}