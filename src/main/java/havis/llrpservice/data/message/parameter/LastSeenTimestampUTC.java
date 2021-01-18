package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.math.BigInteger;

public class LastSeenTimestampUTC extends Parameter {

	private static final long serialVersionUID = -2281011598965556351L;

	private TVParameterHeader parameterHeader;
	private BigInteger microseconds;

	public LastSeenTimestampUTC() {
	}

	public LastSeenTimestampUTC(TVParameterHeader parameterHeader, BigInteger microseconds) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.LAST_SEEN_TIMESTAMP_UTC);
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
		return "LastSeenTimestampUTC [parameterHeader=" + parameterHeader + ", microseconds=" + microseconds + "]";
	}
}
