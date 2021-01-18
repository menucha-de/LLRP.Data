package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.List;

public class AccessSpec extends Parameter {

	private static final long serialVersionUID = -4987100186335965130L;

	private TLVParameterHeader parameterHeader;
	private long accessSpecId;
	private int antennaId;
	private ProtocolId protocolId;
	private boolean currentState;
	private long roSpecId;
	private AccessSpecStopTrigger accessSpecStopTrigger;
	private AccessCommand accessCommand;
	private AccessReportSpec accessReportSpec;
	private List<Custom> customList;

	public AccessSpec() {
	}

	public AccessSpec(TLVParameterHeader parameterHeader, long accessSpecID, int antennaID, ProtocolId protocolID, boolean currentState, long roSpecID,
			AccessSpecStopTrigger accessSpecStopTrigger, AccessCommand accessCommand) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.ACCESS_SPEC);
		this.accessSpecId = accessSpecID;
		this.antennaId = antennaID;
		this.protocolId = protocolID;
		this.currentState = currentState;
		this.roSpecId = roSpecID;
		this.accessSpecStopTrigger = accessSpecStopTrigger;
		this.accessCommand = accessCommand;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public long getAccessSpecId() {
		return accessSpecId;
	}

	public void setAccessSpecId(long accessSpecId) {
		this.accessSpecId = accessSpecId;
	}

	public int getAntennaId() {
		return antennaId;
	}

	public void setAntennaId(int antennaId) {
		this.antennaId = antennaId;
	}

	public void setProtocolId(ProtocolId protocolId) {
		this.protocolId = protocolId;
	}

	public ProtocolId getProtocolId() {
		return protocolId;
	}

	public boolean isCurrentState() {
		return currentState;
	}

	public void setCurrentState(boolean currentState) {
		this.currentState = currentState;
	}

	public long getRoSpecId() {
		return roSpecId;
	}

	public void setRoSpecId(long roSpecId) {
		this.roSpecId = roSpecId;
	}

	public AccessSpecStopTrigger getAccessSpecStopTrigger() {
		return accessSpecStopTrigger;
	}

	public void setAccessSpecStopTrigger(AccessSpecStopTrigger accessSpecStopTrigger) {
		this.accessSpecStopTrigger = accessSpecStopTrigger;
	}

	public AccessCommand getAccessCommand() {
		return accessCommand;
	}

	public void setAccessCommand(AccessCommand accessCommand) {
		this.accessCommand = accessCommand;
	}

	public AccessReportSpec getAccessReportSpec() {
		return accessReportSpec;
	}

	public void setAccessReportSpec(AccessReportSpec accessReportSpec) {
		this.accessReportSpec = accessReportSpec;
	}

	public List<Custom> getCustomList() {
		return customList;
	}

	public void setCustomList(List<Custom> customList) {
		this.customList = customList;
	}

	@Override
	public String toString() {
		return "AccessSpec [parameterHeader=" + parameterHeader + ", accessSpecId=" + accessSpecId + ", antennaId=" + antennaId + ", protocolId=" + protocolId
				+ ", currentState=" + currentState + ", roSpecId=" + roSpecId + ", accessSpecStopTrigger=" + accessSpecStopTrigger + ", accessCommand="
				+ accessCommand + ", accessReportSpec=" + accessReportSpec + ", cusList=" + customList + "]";
	}
}