package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.List;

public class InventoryParameterSpec extends Parameter {

	private static final long serialVersionUID = -8144591401470286800L;

	private TLVParameterHeader parameterHeader;
	private int specID;
	private ProtocolId protocolID;
	private List<AntennaConfiguration> antennaConfigList;
	private List<Custom> cusList;

	public InventoryParameterSpec() {
	}

	public InventoryParameterSpec(TLVParameterHeader parameterHeader, int specID, ProtocolId protocolID) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.INVENTORY_PARAMETER_SPEC);
		this.specID = specID;
		this.protocolID = protocolID;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getSpecID() {
		return specID;
	}

	public void setSpecID(int specID) {
		this.specID = specID;
	}

	public ProtocolId getProtocolID() {
		return protocolID;
	}

	public void setProtocolID(ProtocolId protocolID) {
		this.protocolID = protocolID;
	}

	public List<AntennaConfiguration> getAntennaConfigList() {
		return antennaConfigList;
	}

	public void setAntennaConfigList(List<AntennaConfiguration> antennaConfigList) {
		this.antennaConfigList = antennaConfigList;
	}

	public List<Custom> getCusList() {
		return cusList;
	}

	public void setCusList(List<Custom> cusList) {
		this.cusList = cusList;
	}

	@Override
	public String toString() {
		return "InventoryParameterSpec [parameterHeader=" + parameterHeader + ", specID=" + specID + ", protocolID=" + protocolID + ", antennaConfigList="
				+ antennaConfigList + ", cusList=" + cusList + "]";
	}
}