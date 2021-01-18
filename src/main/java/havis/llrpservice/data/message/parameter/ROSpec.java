package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.List;

public class ROSpec extends Parameter {

	private static final long serialVersionUID = -6470235531642595846L;

	private TLVParameterHeader parameterHeader;
	private long roSpecID;
	private short priority;
	private ROSpecCurrentState currentState;
	private ROBoundarySpec roBoundarySpec;
	private List<Parameter> specList;
	private ROReportSpec roReportSpec;

	public ROSpec() {
	}

	public ROSpec(TLVParameterHeader parameterHeader, long roSpecID, short priority, ROSpecCurrentState currentState, ROBoundarySpec roBoundarySpec,
			List<Parameter> specList) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.RO_SPEC);
		this.roSpecID = roSpecID;
		this.priority = priority;
		this.currentState = currentState;
		this.roBoundarySpec = roBoundarySpec;
		this.specList = specList;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public long getRoSpecID() {
		return roSpecID;
	}

	public void setRoSpecID(long roSpecID) {
		this.roSpecID = roSpecID;
	}

	public short getPriority() {
		return priority;
	}

	public void setPriority(short priority) {
		this.priority = priority;
	}

	public ROSpecCurrentState getCurrentState() {
		return currentState;
	}

	public void setCurrentState(ROSpecCurrentState currentState) {
		this.currentState = currentState;
	}

	public ROBoundarySpec getRoBoundarySpec() {
		return roBoundarySpec;
	}

	public void setRoBoundarySpec(ROBoundarySpec roBoundarySpec) {
		this.roBoundarySpec = roBoundarySpec;
	}

	public List<Parameter> getSpecList() {
		return specList;
	}

	public void setSpecList(List<Parameter> specList) {
		this.specList = specList;
	}

	public ROReportSpec getRoReportSpec() {
		return roReportSpec;
	}

	public void setRoReportSpec(ROReportSpec roReportSpec) {
		this.roReportSpec = roReportSpec;
	}

	@Override
	public String toString() {
		return "ROSpec [parameterHeader=" + parameterHeader + ", roSpecID=" + roSpecID + ", priority=" + priority + ", currentState=" + currentState
				+ ", roBoundarySpec=" + roBoundarySpec + ", specList=" + specList + ", roReportSpec=" + roReportSpec + "]";
	}
}