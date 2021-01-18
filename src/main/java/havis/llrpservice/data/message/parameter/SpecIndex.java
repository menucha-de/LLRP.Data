package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class SpecIndex extends Parameter {

	private static final long serialVersionUID = -7846392010562154357L;

	private TVParameterHeader parameterHeader;
	private int specIndex;

	public SpecIndex() {
	}

	public SpecIndex(TVParameterHeader parameterHeader, int specIndex) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.SPEC_INDEX);
		this.specIndex = specIndex;
	}

	public TVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getSpecIndex() {
		return specIndex;
	}

	public void setSpecIndex(int specIndex) {
		this.specIndex = specIndex;
	}

	@Override
	public String toString() {
		return "SpecIndex [parameterHeader=" + parameterHeader + ", specIndex=" + specIndex + "]";
	}
}