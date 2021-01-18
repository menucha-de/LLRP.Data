package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class ROBoundarySpec extends Parameter {

	private static final long serialVersionUID = 7528879148181694314L;

	private TLVParameterHeader parameterHeader;
	private ROSpecStartTrigger roSStartTrigger;
	private ROSpecStopTrigger roSStopTrigger;

	public ROBoundarySpec() {
	}

	public ROBoundarySpec(TLVParameterHeader parameterHeader, ROSpecStartTrigger roSStartTrigger, ROSpecStopTrigger roSStopTrigger) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.RO_BOUNDARY_SPEC);
		this.roSStartTrigger = roSStartTrigger;
		this.roSStopTrigger = roSStopTrigger;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public ROSpecStartTrigger getRoSStartTrigger() {
		return roSStartTrigger;
	}

	public void setRoSStartTrigger(ROSpecStartTrigger roSStartTrigger) {
		this.roSStartTrigger = roSStartTrigger;
	}

	public ROSpecStopTrigger getRoSStopTrigger() {
		return roSStopTrigger;
	}

	public void setRoSStopTrigger(ROSpecStopTrigger roSStopTrigger) {
		this.roSStopTrigger = roSStopTrigger;
	}

	@Override
	public String toString() {
		return "ROBoundarySpec [parameterHeader=" + parameterHeader + ", roSStartTrigger=" + roSStartTrigger + ", roSStopTrigger=" + roSStopTrigger + "]";
	}
}