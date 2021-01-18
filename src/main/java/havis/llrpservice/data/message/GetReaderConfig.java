package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;
import havis.llrpservice.data.message.parameter.Custom;

import java.util.List;

public class GetReaderConfig implements Message {

	private static final long serialVersionUID = -5232012981424952038L;

	private MessageHeader messageHeader;
	private int antennaID;
	private GetReaderConfigRequestedData requestedData;
	private int gpiPortNum;
	private int gpoPortNum;
	private List<Custom> customList;

	public GetReaderConfig() {
	}

	public GetReaderConfig(MessageHeader header, int antennaID, GetReaderConfigRequestedData requestedData, int gpiPortNum, int gpoPortNum) {
		messageHeader = header;
		messageHeader.setMessageType(MessageType.GET_READER_CONFIG);
		this.antennaID = antennaID;
		this.requestedData = requestedData;
		this.gpiPortNum = gpiPortNum;
		this.gpoPortNum = gpoPortNum;
	}

	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	public int getAntennaID() {
		return antennaID;
	}

	public void setAntennaID(int antennaID) {
		this.antennaID = antennaID;
	}

	public GetReaderConfigRequestedData getRequestedData() {
		return requestedData;
	}

	public void setRequestedData(GetReaderConfigRequestedData requestedData) {
		this.requestedData = requestedData;
	}

	public int getGpiPortNum() {
		return gpiPortNum;
	}

	public void setGpiPortNum(int gpiPortNum) {
		this.gpiPortNum = gpiPortNum;
	}

	public int getGpoPortNum() {
		return gpoPortNum;
	}

	public void setGpoPortNum(int gpoPortNum) {
		this.gpoPortNum = gpoPortNum;
	}

	public List<Custom> getCustomList() {
		return customList;
	}

	public void setCustomList(List<Custom> customList) {
		this.customList = customList;
	}

	@Override
	public String toString() {
		return "GetReaderConfig [messageHeader=" + messageHeader + ", antennaID=" + antennaID + ", requestedData=" + requestedData + ", gpiPortNum="
				+ gpiPortNum + ", gpoPortNum=" + gpoPortNum + ", customList=" + customList + "]";
	}
}