package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.math.BigInteger;

public class FirstSeenTimestampUptime extends Parameter {

	private static final long serialVersionUID = 3303273995831201190L;

	private TVParameterHeader parameterHeader;
	private BigInteger microseconds;

	public FirstSeenTimestampUptime() {
	}

	public FirstSeenTimestampUptime(TVParameterHeader parameterHeader, BigInteger microseconds) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.FIRST_SEEN_TIMESTAMP_UPTIME);
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
		return "FirstSeenTimestampUptime [parameterHeader=" + parameterHeader + ", microseconds=" + microseconds + "]";
	}
}