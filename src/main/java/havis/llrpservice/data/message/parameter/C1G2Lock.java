package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.List;

public class C1G2Lock extends Parameter {

	private static final long serialVersionUID = 2351504791941632660L;

	private TLVParameterHeader parameterHeader;
	private int opSpecId;
	private long accessPw;
	private List<C1G2LockPayload> c1g2LockPayloadList;

	public C1G2Lock() {
	}

	public C1G2Lock(TLVParameterHeader parameterHeader, int opSpecId, long accessPW, List<C1G2LockPayload> c1g2LockPayloadList) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.C1G2_LOCK);
		this.opSpecId = opSpecId;
		this.accessPw = accessPW;
		this.c1g2LockPayloadList = c1g2LockPayloadList;
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

	public long getAccessPw() {
		return accessPw;
	}

	public void setAccessPw(long accessPw) {
		this.accessPw = accessPw;
	}

	public List<C1G2LockPayload> getC1g2LockPayloadList() {
		return c1g2LockPayloadList;
	}

	public void setC1g2LockPayloadList(List<C1G2LockPayload> c1g2LockPayloadList) {
		this.c1g2LockPayloadList = c1g2LockPayloadList;
	}

	@Override
	public String toString() {
		return "C1G2Lock [parameterHeader=" + parameterHeader + ", opSpecId=" + opSpecId + ", accessPw=" + accessPw + ", c1g2LockPayloadList="
				+ c1g2LockPayloadList + "]";
	}
}