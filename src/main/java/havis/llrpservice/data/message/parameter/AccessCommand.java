package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.List;

public class AccessCommand extends Parameter {

	private static final long serialVersionUID = -8856735523953507780L;

	private TLVParameterHeader parameterHeader;
	private C1G2TagSpec c1g2TagSpec;
	private List<Parameter> opSpecList;
	private List<Custom> customList;

	public AccessCommand() {
	}

	public AccessCommand(TLVParameterHeader parameterHeader, C1G2TagSpec c1g2TagSpec, List<Parameter> opSpecList) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.ACCESS_COMMAND);
		this.c1g2TagSpec = c1g2TagSpec;
		this.opSpecList = opSpecList;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public C1G2TagSpec getC1g2TagSpec() {
		return c1g2TagSpec;
	}

	public void setC1g2TagSpec(C1G2TagSpec c1g2TagSpec) {
		this.c1g2TagSpec = c1g2TagSpec;
	}

	public List<Parameter> getOpSpecList() {
		return opSpecList;
	}

	public void setOpSpecList(List<Parameter> opSpecList) {
		this.opSpecList = opSpecList;
	}

	public List<Custom> getCustomList() {
		return customList;
	}

	public void setCustomList(List<Custom> customList) {
		this.customList = customList;
	}

	@Override
	public String toString() {
		return "AccessCommand [parameterHeader=" + parameterHeader + ", c1g2TagSpec=" + c1g2TagSpec + ", opSpecList=" + opSpecList + ", customList="
				+ customList + "]";
	}
}