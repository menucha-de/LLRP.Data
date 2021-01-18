package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class C1G2LLRPCapabilities extends Parameter {

	private static final long serialVersionUID = 6036941914300847971L;

	private TLVParameterHeader parameterHeader;
	private Boolean canSupportBlockErase;
	private Boolean canSupportBlockWrite;
	private Boolean canSupportBlockPermalock;
	private Boolean canSupportTagRecommissioning;
	private Boolean canSupportUMIMethod2;
	private Boolean canSupportXPC;
	private int maxNumSelectFiltersPerQuery;

	public C1G2LLRPCapabilities() {
	}

	public C1G2LLRPCapabilities(TLVParameterHeader header, Boolean canSupportBlockErase, Boolean canSupportBlockWrite, Boolean canSupportBlockPermalock,
			Boolean canSupportTagRecommissioning, Boolean canSupportUMIMethod2, Boolean canSupportXPC, int maxNumSelectFiltersPerQuery) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.C1G2_LLRP_CAPABILITIES);
		this.canSupportBlockWrite = canSupportBlockWrite;
		this.canSupportBlockPermalock = canSupportBlockPermalock;
		this.canSupportTagRecommissioning = canSupportTagRecommissioning;
		this.canSupportUMIMethod2 = canSupportUMIMethod2;
		this.canSupportXPC = canSupportXPC;
		this.maxNumSelectFiltersPerQuery = maxNumSelectFiltersPerQuery;
		this.canSupportBlockErase = canSupportBlockErase;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public Boolean getCanSupportBlockErase() {
		return canSupportBlockErase;
	}

	public void setCanSupportBlockErase(Boolean canSupportBlockErase) {
		this.canSupportBlockErase = canSupportBlockErase;
	}

	public Boolean getCanSupportBlockWrite() {
		return canSupportBlockWrite;
	}

	public void setCanSupportBlockWrite(Boolean canSupportBlockWrite) {
		this.canSupportBlockWrite = canSupportBlockWrite;
	}

	public Boolean getCanSupportBlockPermalock() {
		return canSupportBlockPermalock;
	}

	public void setCanSupportBlockPermalock(Boolean canSupportBlockPermalock) {
		this.canSupportBlockPermalock = canSupportBlockPermalock;
	}

	public Boolean getCanSupportTagRecommissioning() {
		return canSupportTagRecommissioning;
	}

	public void setCanSupportTagRecommissioning(Boolean canSupportTagRecommissioning) {
		this.canSupportTagRecommissioning = canSupportTagRecommissioning;
	}

	public Boolean getCanSupportUMIMethod2() {
		return canSupportUMIMethod2;
	}

	public void setCanSupportUMIMethod2(Boolean canSupportUMIMethod2) {
		this.canSupportUMIMethod2 = canSupportUMIMethod2;
	}

	public Boolean getCanSupportXPC() {
		return canSupportXPC;
	}

	public void setCanSupportXPC(Boolean canSupportXPC) {
		this.canSupportXPC = canSupportXPC;
	}

	public int getMaxNumSelectFiltersPerQuery() {
		return maxNumSelectFiltersPerQuery;
	}

	public void setMaxNumSelectFiltersPerQuery(int maxNumSelectFiltersPerQuery) {
		this.maxNumSelectFiltersPerQuery = maxNumSelectFiltersPerQuery;
	}

	@Override
	public String toString() {
		return "C1G2LLRPCapabilities [parameterHeader=" + parameterHeader + ", canSupportBlockErase=" + canSupportBlockErase + ", canSupportBlockWrite="
				+ canSupportBlockWrite + ", canSupportBlockPermalock=" + canSupportBlockPermalock + ", canSupportTagRecommissioning="
				+ canSupportTagRecommissioning + ", canSupportUMIMethod2=" + canSupportUMIMethod2 + ", canSupportXPC=" + canSupportXPC
				+ ", maxNumSelectFiltersPerQuery=" + maxNumSelectFiltersPerQuery + "]";
	}
}