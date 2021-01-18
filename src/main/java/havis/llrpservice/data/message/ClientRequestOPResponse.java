package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;
import havis.llrpservice.data.message.parameter.ClientRequestResponse;

public class ClientRequestOPResponse implements Message {

	private static final long serialVersionUID = 4894695470034524529L;

	private MessageHeader messageHeader;
	private ClientRequestResponse clientRequestResponse;

	public ClientRequestOPResponse() {
	}

	public ClientRequestOPResponse(MessageHeader messageHeader, ClientRequestResponse clientRequestResponse) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.CLIENT_REQUEST_OP_RESPONSE);
		this.clientRequestResponse = clientRequestResponse;
	}

	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	public ClientRequestResponse getClientRequestResponse() {
		return clientRequestResponse;
	}

	public void setClientRequestResponse(ClientRequestResponse clientRequestResponse) {
		this.clientRequestResponse = clientRequestResponse;
	}

	@Override
	public String toString() {
		return "ClientRequestOPResponse [messageHeader=" + messageHeader + ", clientRequestResponse=" + clientRequestResponse + "]";
	}
}