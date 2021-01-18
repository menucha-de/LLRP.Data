package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class C1G2Kill extends Parameter {

	private static final long serialVersionUID = 1589401008959854173L;

	private TLVParameterHeader parameterHeader;
	private int opSpecId;
	private long killPw;

	public C1G2Kill() {
	}

	public C1G2Kill(TLVParameterHeader parameterHeader, int opSpecId, long killPw) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.C1G2_KILL);
		this.opSpecId = opSpecId;
		this.killPw = killPw;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getOpSpecId() {
		return opSpecId;
	}

	public void setOpSpecId(int opSpecId) {
		this.opSpecId = opSpecId;
	}

	public long getKillPw() {
		return killPw;
	}

	public void setKillPw(long killPw) {
		this.killPw = killPw;
	}

	@Override
	public String toString() {
		return "C1G2Kill [parameterHeader=" + parameterHeader + ", opSpecId=" + opSpecId + ", killPw=" + killPw + "]";
	}
}