package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.List;

public class ReaderExceptionEvent extends Parameter {

	private static final long serialVersionUID = 2021550122502244224L;

	private TLVParameterHeader parameterHeader;

	private int stringByteCount;
	private String stringMessage;
	private ROSpecID roSpecID;
	private SpecIndex specIndex;
	private InventoryParameterSpecID ipSpecID;
	private AntennaId antennaId;
	private AccessSpecId accessSpecId;
	private OpSpecID opSpecID;
	private List<Custom> customList;

	public ReaderExceptionEvent() {
	}

	public ReaderExceptionEvent(TLVParameterHeader header, String stringMessage) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.READER_EXCEPTION_EVENT);
		this.stringMessage = stringMessage;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getStringByteCount() {
		return stringByteCount;
	}

	public void setStringByteCount(int stringByteCount) {
		this.stringByteCount = stringByteCount;
	}

	public String getStringMessage() {
		return stringMessage;
	}

	public void setStringMessage(String stringMessage) {
		this.stringMessage = stringMessage;
	}

	public ROSpecID getRoSpecID() {
		return roSpecID;
	}

	public void setRoSpecID(ROSpecID roSpecID) {
		this.roSpecID = roSpecID;
	}

	public SpecIndex getSpecIndex() {
		return specIndex;
	}

	public void setSpecIndex(SpecIndex specIndex) {
		this.specIndex = specIndex;
	}

	public InventoryParameterSpecID getIpSpecID() {
		return ipSpecID;
	}

	public void setIpSpecID(InventoryParameterSpecID ipSpecID) {
		this.ipSpecID = ipSpecID;
	}

	public AntennaId getAntennaId() {
		return antennaId;
	}

	public void setAntennaId(AntennaId antennaId) {
		this.antennaId = antennaId;
	}

	public AccessSpecId getAccessSpecId() {
		return accessSpecId;
	}

	public void setAccessSpecId(AccessSpecId accessSpecId) {
		this.accessSpecId = accessSpecId;
	}

	public OpSpecID getOpSpecID() {
		return opSpecID;
	}

	public void setOpSpecID(OpSpecID opSpecID) {
		this.opSpecID = opSpecID;
	}

	public List<Custom> getCustomList() {
		return customList;
	}

	public void setCustomList(List<Custom> customList) {
		this.customList = customList;
	}

	@Override
	public String toString() {
		return "ReaderExceptionEvent [parameterHeader=" + parameterHeader + ", stringByteCount=" + stringByteCount + ", stringMessage=" + stringMessage
				+ ", roSpecID=" + roSpecID + ", specIndex=" + specIndex + ", ipSpecID=" + ipSpecID + ", antennaId=" + antennaId + ", accessSpecId="
				+ accessSpecId + ", opSpecID=" + opSpecID + ", customList=" + customList + "]";
	}
}