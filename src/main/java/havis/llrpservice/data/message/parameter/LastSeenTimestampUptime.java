package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.math.BigInteger;

public class LastSeenTimestampUptime extends Parameter {

	private static final long serialVersionUID = 3449825067325536634L;

	private TVParameterHeader parameterHeader;
	private BigInteger microseconds;

	public LastSeenTimestampUptime() {
	}

	public LastSeenTimestampUptime(TVParameterHeader parameterHeader, BigInteger microseconds) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.LAST_SEEN_TIMESTAMP_UPTIME);
		this.microseconds = microseconds;
	}

	public TVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public BigInteger getMicroseconds() {
		return microseconds;
	}

	public void setMicroseconds(BigInteger microseconds) {
		this.microseconds = microseconds;
	}

	@Override
	public String toString() {
		return "LastSeenTimestampUptime [parameterHeader=" + parameterHeader + ", microseconds=" + microseconds + "]";
	}
}