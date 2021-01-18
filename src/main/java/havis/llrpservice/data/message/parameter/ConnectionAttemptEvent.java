package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class ConnectionAttemptEvent extends Parameter {

	private static final long serialVersionUID = 7423113081785511118L;

	private TLVParameterHeader parameterHeader;
	private ConnectionAttemptEventStatusType status;

	public ConnectionAttemptEvent() {
	}

	public ConnectionAttemptEvent(TLVParameterHeader header, ConnectionAttemptEventStatusType status) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.CONNECTION_ATTEMPT_EVENT);
		this.status = status;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public void setStatus(ConnectionAttemptEventStatusType status) {
		this.status = status;
	}

	public ConnectionAttemptEventStatusType getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return "ConnectionAttemptEvent [parameterHeader=" + parameterHeader + ", status=" + status + "]";
	}
}