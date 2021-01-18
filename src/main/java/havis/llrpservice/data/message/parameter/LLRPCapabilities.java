package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class LLRPCapabilities extends Parameter {

	private static final long serialVersionUID = -5701195886811380386L;

	private TLVParameterHeader parameterHeader;

	private boolean canDoRFSurvey;
	private boolean canReportBufferFillWarning;
	private boolean supportsClientRequestOpSpec;
	private boolean canDoTagInventoryStateAwareSingulation;
	private boolean supportsEventAndReportHolding;
	private byte maxPriorityLevelSupported;
	private int clientRequestOpSpecTimeout;
	private long maxNumROSpecs;
	private long maxNumSpecsPerROSpec;
	private long maxNumInventoryParameterSpecsPerAISpec;
	private long maxNumAccessSpecs;
	private long maxNumOpSpecsPerAccessSpec;

	public LLRPCapabilities() {
	}

	public LLRPCapabilities(TLVParameterHeader header, Boolean canDoRFSurvey, Boolean canReportBufferFillWarning, Boolean supportsClientRequestOpSpec,
			Boolean canDoTagInventoryStateAwareSingulation, Boolean supportsEventAndReportHolding, byte maxPriorityLevelSupported,
			int clientRequestOpSpecTimeout, long maxNumROSpecs, long maxNumSpecsPerROSpec, long maxNumInventoryParameterSpecsPerAISpec, long maxNumAccessSpecs,
			long maxNumOpSpecsPerAccessSpec) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.LLRP_CAPABILITIES);
		this.canDoRFSurvey = canDoRFSurvey;
		this.canReportBufferFillWarning = canReportBufferFillWarning;
		this.supportsClientRequestOpSpec = supportsClientRequestOpSpec;
		this.canDoTagInventoryStateAwareSingulation = canDoTagInventoryStateAwareSingulation;
		this.supportsEventAndReportHolding = supportsEventAndReportHolding;
		this.maxPriorityLevelSupported = maxPriorityLevelSupported;
		this.clientRequestOpSpecTimeout = clientRequestOpSpecTimeout;
		this.maxNumROSpecs = maxNumROSpecs;
		this.maxNumSpecsPerROSpec = maxNumSpecsPerROSpec;
		this.maxNumInventoryParameterSpecsPerAISpec = maxNumInventoryParameterSpecsPerAISpec;
		this.maxNumAccessSpecs = maxNumAccessSpecs;
		this.maxNumOpSpecsPerAccessSpec = maxNumOpSpecsPerAccessSpec;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public boolean isCanDoRFSurvey() {
		return canDoRFSurvey;
	}

	public void setCanDoRFSurvey(boolean canDoRFSurvey) {
		this.canDoRFSurvey = canDoRFSurvey;
	}

	public boolean isCanReportBufferFillWarning() {
		return canReportBufferFillWarning;
	}

	public void setCanReportBufferFillWarning(boolean canReportBufferFillWarning) {
		this.canReportBufferFillWarning = canReportBufferFillWarning;
	}

	public boolean isSupportsClientRequestOpSpec() {
		return supportsClientRequestOpSpec;
	}

	public void setSupportsClientRequestOpSpec(boolean supportsClientRequestOpSpec) {
		this.supportsClientRequestOpSpec = supportsClientRequestOpSpec;
	}

	public boolean isCanDoTagInventoryStateAwareSingulation() {
		return canDoTagInventoryStateAwareSingulation;
	}

	public void setCanDoTagInventoryStateAwareSingulation(boolean canDoTagInventoryStateAwareSingulation) {
		this.canDoTagInventoryStateAwareSingulation = canDoTagInventoryStateAwareSingulation;
	}

	public boolean isSupportsEventAndReportHolding() {
		return supportsEventAndReportHolding;
	}

	public void setSupportsEventAndReportHolding(boolean supportsEventAndReportHolding) {
		this.supportsEventAndReportHolding = supportsEventAndReportHolding;
	}

	public byte getMaxPriorityLevelSupported() {
		return maxPriorityLevelSupported;
	}

	public void setMaxPriorityLevelSupported(byte maxPriorityLevelSupported) {
		this.maxPriorityLevelSupported = maxPriorityLevelSupported;
	}

	public int getClientRequestOpSpecTimeout() {
		return clientRequestOpSpecTimeout;
	}

	public void setClientRequestOpSpecTimeout(int clientRequestOpSpecTimeout) {
		this.clientRequestOpSpecTimeout = clientRequestOpSpecTimeout;
	}

	public long getMaxNumROSpecs() {
		return maxNumROSpecs;
	}

	public void setMaxNumROSpecs(long maxNumROSpecs) {
		this.maxNumROSpecs = maxNumROSpecs;
	}

	public long getMaxNumSpecsPerROSpec() {
		return maxNumSpecsPerROSpec;
	}

	public void setMaxNumSpecsPerROSpec(long maxNumSpecsPerROSpec) {
		this.maxNumSpecsPerROSpec = maxNumSpecsPerROSpec;
	}

	public long getMaxNumInventoryParameterSpecsPerAISpec() {
		return maxNumInventoryParameterSpecsPerAISpec;
	}

	public void setMaxNumInventoryParameterSpecsPerAISpec(long maxNumInventoryParameterSpecsPerAISpec) {
		this.maxNumInventoryParameterSpecsPerAISpec = maxNumInventoryParameterSpecsPerAISpec;
	}

	public long getMaxNumAccessSpecs() {
		return maxNumAccessSpecs;
	}

	public void setMaxNumAccessSpecs(long maxNumAccessSpecs) {
		this.maxNumAccessSpecs = maxNumAccessSpecs;
	}

	public long getMaxNumOpSpecsPerAccessSpec() {
		return maxNumOpSpecsPerAccessSpec;
	}

	public void setMaxNumOpSpecsPerAccessSpec(long maxNumOpSpecsPerAccessSpec) {
		this.maxNumOpSpecsPerAccessSpec = maxNumOpSpecsPerAccessSpec;
	}

	@Override
	public String toString() {
		return "LLRPCapabilities [parameterHeader=" + parameterHeader + ", canDoRFSurvey=" + canDoRFSurvey + ", canReportBufferFillWarning="
				+ canReportBufferFillWarning + ", supportsClientRequestOpSpec=" + supportsClientRequestOpSpec + ", canDoTagInventoryStateAwareSingulation="
				+ canDoTagInventoryStateAwareSingulation + ", supportsEventAndReportHolding=" + supportsEventAndReportHolding + ", maxPriorityLevelSupported="
				+ maxPriorityLevelSupported + ", clientRequestOpSpecTimeout=" + clientRequestOpSpecTimeout + ", maxNumROSpecs=" + maxNumROSpecs
				+ ", maxNumSpecsPerROSpec=" + maxNumSpecsPerROSpec + ", maxNumInventoryParameterSpecsPerAISpec=" + maxNumInventoryParameterSpecsPerAISpec
				+ ", maxNumAccessSpecs=" + maxNumAccessSpecs + ", maxNumOpSpecsPerAccessSpec=" + maxNumOpSpecsPerAccessSpec + "]";
	}
}