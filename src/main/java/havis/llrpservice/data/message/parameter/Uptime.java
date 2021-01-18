package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.math.BigInteger;

public class Uptime extends Parameter {

	private static final long serialVersionUID = -7801398003620096427L;

	private TLVParameterHeader parameterHeader;
	private BigInteger microseconds;

	public Uptime() {
	}

	public Uptime(TLVParameterHeader header, BigInteger microseconds) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.UPTIME);
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
		return "Uptime [parameterHeader=" + parameterHeader + ", microseconds=" + microseconds + "]";
	}
}