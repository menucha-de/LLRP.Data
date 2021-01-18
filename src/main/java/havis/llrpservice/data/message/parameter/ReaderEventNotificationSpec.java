package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.List;

public class ReaderEventNotificationSpec extends Parameter {

	private static final long serialVersionUID = -7785265490163542617L;

	private TLVParameterHeader parameterHeader;
	private List<EventNotificationState> eventNotificationStateList;

	public ReaderEventNotificationSpec() {
	}

	public ReaderEventNotificationSpec(TLVParameterHeader header, List<EventNotificationState> eventNotificationStateList) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.READER_EVENT_NOTIFICATION_SPEC);
		this.eventNotificationStateList = eventNotificationStateList;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public List<EventNotificationState> getEventNotificationStateList() {
		return eventNotificationStateList;
	}

	public void setEventNotificationStateList(List<EventNotificationState> eventNotificationStateList) {
		this.eventNotificationStateList = eventNotificationStateList;
	}

	@Override
	public String toString() {
		return "ReaderEventNotificationSpec [parameterHeader=" + parameterHeader + ", eventNotificationStateList=" + eventNotificationStateList + "]";
	}
}