package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;
import havis.llrpservice.data.message.parameter.AccessSpec;
import havis.llrpservice.data.message.parameter.LLRPStatus;

import java.util.List;

public class GetAccessSpecsResponse implements Message {

	private static final long serialVersionUID = -8100282614188388579L;

	private MessageHeader messageHeader;
	private LLRPStatus status;
	private List<AccessSpec> accessSpecList;

	public GetAccessSpecsResponse() {
	}

	public GetAccessSpecsResponse(MessageHeader messageHeader, LLRPStatus status) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.GET_ACCESSSPECS_RESPONSE);
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

	public List<AccessSpec> getAccessSpecList() {
		return accessSpecList;
	}

	public void setAccessSpecList(List<AccessSpec> accessSpecList) {
		this.accessSpecList = accessSpecList;
	}

	@Override
	public String toString() {
		return "GetAccessSpecsResponse [messageHeader=" + messageHeader + ", status=" + status + ", accessSpecList=" + accessSpecList + "]";
	}
}