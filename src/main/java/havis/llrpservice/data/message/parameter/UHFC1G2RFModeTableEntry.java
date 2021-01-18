package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class UHFC1G2RFModeTableEntry extends Parameter {

	private static final long serialVersionUID = 6369967994285677559L;

	private TLVParameterHeader parameterHeader;
	private long modeIdentifier;
	private UHFC1G2RFModeTableEntryDivideRatio drValue;
	private boolean epcHAGConformance;
	private UHFC1G2RFModeTableEntryModulation mValue;
	private UHFC1G2RFModeTableEntryForwardLinkModulation forwardLinkModulation;
	private UHFC1G2RFModeTableEntrySpectralMaskIndicator spectralMaskIndicator;
	private int bdrValue;
	private int pieValue;
	private int minTariValue;
	private int maxTariValue;
	private int stepTariValue;

	public UHFC1G2RFModeTableEntry() {
	}

	public UHFC1G2RFModeTableEntry(TLVParameterHeader header, long modeIdentifier, UHFC1G2RFModeTableEntryDivideRatio drValue, boolean epcHAGConformance,
			UHFC1G2RFModeTableEntryModulation mValue, UHFC1G2RFModeTableEntryForwardLinkModulation forwardLinkModulation,
			UHFC1G2RFModeTableEntrySpectralMaskIndicator spectralMaskIndicator, int bdrValue, int pieValue, int minTariValue, int maxTariValue,
			int stepTariValue) {

		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.UHF_C1G2_RF_MODE_TABLE_ENTRY);
		this.modeIdentifier = modeIdentifier;
		this.drValue = drValue;
		this.epcHAGConformance = epcHAGConformance;
		this.mValue = mValue;
		this.forwardLinkModulation = forwardLinkModulation;
		this.spectralMaskIndicator = spectralMaskIndicator;
		this.bdrValue = bdrValue;
		this.pieValue = pieValue;
		this.minTariValue = minTariValue;
		this.maxTariValue = maxTariValue;
		this.stepTariValue = stepTariValue;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public long getModeIdentifier() {
		return modeIdentifier;
	}

	public void setModeIdentifier(long modeIdentifier) {
		this.modeIdentifier = modeIdentifier;
	}

	public UHFC1G2RFModeTableEntryDivideRatio getDrValue() {
		return drValue;
	}

	public void setDrValue(UHFC1G2RFModeTableEntryDivideRatio drValue) {
		this.drValue = drValue;
	}

	public boolean isEpcHAGConformance() {
		return epcHAGConformance;
	}

	public void setEpcHAGConformance(boolean epcHAGConformance) {
		this.epcHAGConformance = epcHAGConformance;
	}

	public UHFC1G2RFModeTableEntryModulation getmValue() {
		return mValue;
	}

	public void setmValue(UHFC1G2RFModeTableEntryModulation mValue) {
		this.mValue = mValue;
	}

	public UHFC1G2RFModeTableEntryForwardLinkModulation getForwardLinkModulation() {
		return forwardLinkModulation;
	}

	public void setForwardLinkModulation(UHFC1G2RFModeTableEntryForwardLinkModulation forwardLinkModulation) {
		this.forwardLinkModulation = forwardLinkModulation;
	}

	public UHFC1G2RFModeTableEntrySpectralMaskIndicator getSpectralMaskIndicator() {
		return spectralMaskIndicator;
	}

	public void setSpectralMaskIndicator(UHFC1G2RFModeTableEntrySpectralMaskIndicator spectralMaskIndicator) {
		this.spectralMaskIndicator = spectralMaskIndicator;
	}

	public int getBdrValue() {
		return bdrValue;
	}

	public void setBdrValue(int bdrValue) {
		this.bdrValue = bdrValue;
	}

	public int getPieValue() {
		return pieValue;
	}

	public void setPieValue(int pieValue) {
		this.pieValue = pieValue;
	}

	public int getMinTariValue() {
		return minTariValue;
	}

	public void setMinTariValue(int minTariValue) {
		this.minTariValue = minTariValue;
	}

	public int getMaxTariValue() {
		return maxTariValue;
	}

	public void setMaxTariValue(int maxTariValue) {
		this.maxTariValue = maxTariValue;
	}

	public int getStepTariValue() {
		return stepTariValue;
	}

	public void setStepTariValue(int stepTariValue) {
		this.stepTariValue = stepTariValue;
	}

	@Override
	public String toString() {
		return "UHFC1G2RFModeTableEntry [parameterHeader=" + parameterHeader + ", modeIdentifier=" + modeIdentifier + ", drValue=" + drValue
				+ ", epcHAGConformance=" + epcHAGConformance + ", mValue=" + mValue + ", forwardLinkModukation=" + forwardLinkModulation
				+ ", spectralMaskIndicator=" + spectralMaskIndicator + ", bdrValue=" + bdrValue + ", pieValue=" + pieValue + ", minTariValue=" + minTariValue
				+ ", maxTariValue=" + maxTariValue + ", stepTariValue=" + stepTariValue + "]";
	}
}