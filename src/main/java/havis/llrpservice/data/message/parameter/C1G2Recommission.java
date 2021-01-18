package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class C1G2Recommission extends Parameter {

	private static final long serialVersionUID = -1182236767111738057L;

	private TLVParameterHeader parameterHeader;
	private int opSpecID;
	private long killPW;
	private boolean lsb;
	private boolean sb2;
	private boolean sb3;

	public C1G2Recommission() {
	}

	public C1G2Recommission(TLVParameterHeader parameterHeader, int opSpecID, long killPW, boolean lsb, boolean sb2, boolean sb3) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.C1G2_RECOMMISSION);
		this.opSpecID = opSpecID;
		this.killPW = killPW;
		this.lsb = lsb;
		this.sb2 = sb2;
		this.sb3 = sb3;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getOpSpecID() {
		return opSpecID;
	}

	public void setOpSpecID(int opSpecID) {
		this.opSpecID = opSpecID;
	}

	public long getKillPW() {
		return killPW;
	}

	public void setKillPW(long killPW) {
		this.killPW = killPW;
	}

	public boolean isLsb() {
		return lsb;
	}

	public void setLsb(boolean lsb) {
		this.lsb = lsb;
	}

	public boolean isSb2() {
		return sb2;
	}

	public void setSb2(boolean sb2) {
		this.sb2 = sb2;
	}

	public boolean isSb3() {
		return sb3;
	}

	public void setSb3(boolean sb3) {
		this.sb3 = sb3;
	}

	@Override
	public String toString() {
		return "C1G2Recommission [parameterHeader=" + parameterHeader + ", opSpecID=" + opSpecID + ", killPW=" + killPW + ", lsb=" + lsb + ", sb2=" + sb2
				+ ", sb3=" + sb3 + "]";
	}
}