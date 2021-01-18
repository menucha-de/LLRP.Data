package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.io.Serializable;

public abstract class ParameterHeader implements Serializable {

	private static final long serialVersionUID = 8121475884064858223L;

	private ParameterType parameterType;
	private int parameterLength;

	/**
	 * Returns the parameter type. It is set when the root DTO of the parameter
	 * is instantiated.
	 * 
	 * @return The parameter type
	 */
	public ParameterType getParameterType() {
		return parameterType;
	}

	public void setParameterType(ParameterType type) {
		this.parameterType = type;
	}

	/**
	 * Returns the length of the parameter in bytes. It set during the
	 * de-/serialization.
	 * 
	 * @return The parameter length
	 */
	public int getParameterLength() {
		return parameterLength;
	}

	public void setParameterLength(int length) {
		this.parameterLength = length;
	}

	/**
	 * Whether the parameter header is of type <code>Type Length Value</code> or
	 * <code>Type Value</code>
	 * 
	 * @return True, if is <code>Type Length Value</code>, false otherwise
	 */
	public abstract boolean isTLV();

	public String toString() {
		return "ParameterHeader [parameterType=" + parameterType + ", parameterLength=" + parameterLength + "]";
	}
}