package havis.llrpservice.data.message;

import havis.llrpservice.data.message.MessageTypes.MessageType;
import havis.llrpservice.data.message.parameter.LLRPStatus;
import havis.llrpservice.data.message.parameter.ROSpec;

import java.util.List;

public class GetROSpecsResponse implements Message {

	private static final long serialVersionUID = -5778885919075883858L;

	private MessageHeader messageHeader;
	private LLRPStatus status;
	private List<ROSpec> roSpecList;

	public GetROSpecsResponse() {
	}

	public GetROSpecsResponse(MessageHeader messageHeader, LLRPStatus status) {
		this.messageHeader = messageHeader;
		this.messageHeader.setMessageType(MessageType.GET_ROSPECS_RESPONSE);
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

	public List<ROSpec> getRoSpecList() {
		return roSpecList;
	}

	public void setRoSpecList(List<ROSpec> roSpecList) {
		this.roSpecList = roSpecList;
	}

	@Override
	public String toString() {
		return "GetROSpecsResponse [messageHeader=" + messageHeader + ", status=" + status + ", roSpecList=" + roSpecList + "]";
	}
}