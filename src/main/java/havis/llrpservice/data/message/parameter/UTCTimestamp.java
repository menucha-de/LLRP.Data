package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.math.BigInteger;

public class UTCTimestamp extends Parameter {

	private static final long serialVersionUID = -5353402914596215067L;

	private TLVParameterHeader parameterHeader;
	private BigInteger microseconds;

	public UTCTimestamp() {
	}

	public UTCTimestamp(TLVParameterHeader header, BigInteger microseconds) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.UTC_TIMESTAMP);
		this.microseconds = microseconds;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
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
		return "UTCTimestamp [parameterHeader=" + parameterHeader + ", microseconds=" + microseconds + "]";
	}
}