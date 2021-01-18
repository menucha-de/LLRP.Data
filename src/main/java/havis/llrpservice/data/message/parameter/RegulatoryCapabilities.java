package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.List;

public class RegulatoryCapabilities extends Parameter {

	private static final long serialVersionUID = 1869613466487304844L;

	private TLVParameterHeader parameterHeader;
	private CountryCodes.CountryCode countryCode;
	private CommunicationsStandard communicationsStandard;
	private UHFBandCapabilities uhfBandCapabilities;
	private List<Custom> customExtensionPoint;

	public RegulatoryCapabilities() {
	}

	public RegulatoryCapabilities(TLVParameterHeader header, CountryCodes.CountryCode countryCode, CommunicationsStandard communicationsStandard) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.REGULATORY_CAPABILITIES);
		this.countryCode = countryCode;
		this.communicationsStandard = communicationsStandard;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public CountryCodes.CountryCode getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(CountryCodes.CountryCode countryCode) {
		this.countryCode = countryCode;
	}

	public CommunicationsStandard getCommunicationsStandard() {
		return communicationsStandard;
	}

	public void setCommunicationsStandard(CommunicationsStandard communicationsStandard) {
		this.communicationsStandard = communicationsStandard;
	}

	public UHFBandCapabilities getUhfBandCapabilities() {
		return uhfBandCapabilities;
	}

	public void setUhfBandCapabilities(UHFBandCapabilities uhfBandCapabilities) {
		this.uhfBandCapabilities = uhfBandCapabilities;
	}

	public List<Custom> getCustomExtensionPoint() {
		return customExtensionPoint;
	}

	public void setCustomExtensionPoint(List<Custom> customExtensionPoint) {
		this.customExtensionPoint = customExtensionPoint;
	}

	@Override
	public String toString() {
		return "RegulatoryCapabilities [parameterHeader=" + parameterHeader + ", countryCode=" + countryCode + ", communicationsStandard="
				+ communicationsStandard + ", uhfBandCapabilities=" + uhfBandCapabilities + ", customExtensionPoint=" + customExtensionPoint + "]";
	}
}