package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;
import havis.llrpservice.data.message.parameter.TagReportData;

public class ClientRequestOP implements Message {

	private static final long serialVersionUID = -6486354880792746156L;

	private MessageHeader messageHeader;
	private TagReportData tagReportData;

	public ClientRequestOP() {
	}

	public ClientRequestOP(MessageHeader messageHeader, TagReportData tagReportData) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.CLIENT_REQUEST_OP);
		this.tagReportData = tagReportData;
	}

	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	public TagReportData getTagReportData() {
		return tagReportData;
	}

	public void setTagReportData(TagReportData tagReportData) {
		this.tagReportData = tagReportData;
	}

	@Override
	public String toString() {
		return "ClientRequestOP [messageHeader=" + messageHeader + ", tagReportData=" + tagReportData + "]";
	}
}