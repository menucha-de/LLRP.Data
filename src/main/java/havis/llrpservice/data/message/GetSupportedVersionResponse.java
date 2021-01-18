package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;
import havis.llrpservice.data.message.parameter.LLRPStatus;

public class GetSupportedVersionResponse implements Message {

	private static final long serialVersionUID = 2089843475602476893L;

	private MessageHeader messageHeader;
	private LLRPStatus status;
	private ProtocolVersion currentVersion;
	private ProtocolVersion supportedVersion;

	public GetSupportedVersionResponse() {
	}

	public GetSupportedVersionResponse(MessageHeader header, ProtocolVersion currentVersion, ProtocolVersion supportedVersion, LLRPStatus status) {
		messageHeader = header;
		messageHeader.setMessageType(MessageType.GET_SUPPORTED_VERSION_RESPONSE);
		this.currentVersion = currentVersion;
		this.supportedVersion = supportedVersion;
		this.status = status;
	}

	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	public LLRPStatus getStatus() {
		return status;
	}

	public void setStatus(LLRPStatus status) {
		this.status = status;
	}

	public ProtocolVersion getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(ProtocolVersion currentVersion) {
		this.currentVersion = currentVersion;
	}

	public ProtocolVersion getSupportedVersion() {
		return supportedVersion;
	}

	public void setSupportedVersion(ProtocolVersion supportedVersion) {
		this.supportedVersion = supportedVersion;
	}

	@Override
	public String toString() {
		return "GetSupportedVersionResponse [messageHeader=" + messageHeader + ", status=" + status + ", currentVersion=" + currentVersion
				+ ", supportedVersion=" + supportedVersion + "]";
	}
}