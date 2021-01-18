package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.Arrays;

public class C1G2GetBlockPermalockStatusOpSpecResult extends Parameter {

	private static final long serialVersionUID = -391300943733119446L;

	private TLVParameterHeader parameterHeader;
	private C1G2GetBlockPermalockStatusOpSpecResultValues result;
	private int opSpecID;
	private int statusWordCount;
	private byte[] permalockStatus;

	public C1G2GetBlockPermalockStatusOpSpecResult() {
	}

	public C1G2GetBlockPermalockStatusOpSpecResult(TLVParameterHeader parameterHeader, C1G2GetBlockPermalockStatusOpSpecResultValues result, int opSpecID,
			byte[] permalockStatus) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.C1G2_GET_BLOCK_PERMALOCK_STATUS_OP_SPEC_RESULT);
		this.result = result;
		this.opSpecID = opSpecID;
		this.permalockStatus = permalockStatus;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public C1G2GetBlockPermalockStatusOpSpecResultValues getResult() {
		return result;
	}

	public void setResult(C1G2GetBlockPermalockStatusOpSpecResultValues result) {
		this.result = result;
	}

	public int getOpSpecID() {
		return opSpecID;
	}

	public void setOpSpecID(int opSpecID) {
		this.opSpecID = opSpecID;
	}

	public int getStatusWordCount() {
		return statusWordCount;
	}

	public void setStatusWordCount(int statusWordCount) {
		this.statusWordCount = statusWordCount;
	}

	public byte[] getPermalockStatus() {
		return permalockStatus;
	}

	public void setPermalockStatus(byte[] permalockStatus) {
		this.permalockStatus = permalockStatus;
	}

	@Override
	public String toString() {
		return "C1G2GetBlockPermalockStatusOpSpecResult [parameterHeader=" + parameterHeader + ", result=" + result + ", opSpecID=" + opSpecID
				+ ", statusWordCount=" + statusWordCount + ", permalockStatus=" + Arrays.toString(permalockStatus) + "]";
	}
}