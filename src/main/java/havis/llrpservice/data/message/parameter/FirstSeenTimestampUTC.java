package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.math.BigInteger;

public class FirstSeenTimestampUTC extends Parameter {

	private static final long serialVersionUID = 6365342222336576482L;

	private TVParameterHeader parameterHeader;
	private BigInteger microseconds;

	public FirstSeenTimestampUTC() {
	}

	public FirstSeenTimestampUTC(TVParameterHeader parameterHeader, BigInteger microseconds) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.FIRST_SEEN_TIMESTAMP_UTC);
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
		return "FirstSeenTimestampUTC [parameterHeader=" + parameterHeader + ", microseconds=" + microseconds + "]";
	}
}