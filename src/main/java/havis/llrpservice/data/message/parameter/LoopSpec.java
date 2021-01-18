package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class LoopSpec extends Parameter {

	private static final long serialVersionUID = 2649739442228506463L;

	private TLVParameterHeader parameterHeader;
	private long loopCount;

	public LoopSpec() {
	}

	public LoopSpec(TLVParameterHeader parameterHeader, long loopCount) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.LOOP_SPEC);
		this.loopCount = loopCount;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public long getLoopCount() {
		return loopCount;
	}

	public void setLoopCount(long loopCount) {
		this.loopCount = loopCount;
	}

	@Override
	public String toString() {
		return "LoopSpec [parameterHeader=" + parameterHeader + ", loopCount=" + loopCount + "]";
	}
}