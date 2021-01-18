package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class EventNotificationState extends Parameter {

	private static final long serialVersionUID = 2396953220106950377L;

	private TLVParameterHeader parameterHeader;
	private EventNotificationStateEventType eventType;
	private boolean notificationState;

	public EventNotificationState() {
	}

	public EventNotificationState(TLVParameterHeader header, EventNotificationStateEventType eventType, boolean notificationState) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.EVENT_NOTIFICATION_STATE);
		this.eventType = eventType;
		this.notificationState = notificationState;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public EventNotificationStateEventType getEventType() {
		return eventType;
	}

	public void setEventType(EventNotificationStateEventType eventType) {
		this.eventType = eventType;
	}

	public boolean isNotificationState() {
		return notificationState;
	}

	public void setNotificationState(boolean notificationState) {
		this.notificationState = notificationState;
	}

	@Override
	public String toString() {
		return "EventNotificationState [parameterHeader=" + parameterHeader + ", eventType=" + eventType + ", notificationState=" + notificationState + "]";
	}
}