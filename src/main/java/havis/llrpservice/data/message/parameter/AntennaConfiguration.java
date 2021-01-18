package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.List;

public class AntennaConfiguration extends Parameter {

	private static final long serialVersionUID = -319681646333766407L;

	private TLVParameterHeader parameterHeader;
	private int antennaID;
	private RFReceiver rfReceiver;
	private RFTransmitter rfTransmitter;
	private List<C1G2InventoryCommand> c1g2InventoryCommandList;
	private List<Custom> customList;

	public AntennaConfiguration() {
	}

	public AntennaConfiguration(TLVParameterHeader header, int antennaID) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.ANTENNA_CONFIGURATION);
		this.antennaID = antennaID;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getAntennaID() {
		return antennaID;
	}

	public void setAntennaID(int antennaID) {
		this.antennaID = antennaID;
	}

	public RFReceiver getRfReceiver() {
		return rfReceiver;
	}

	public void setRfReceiver(RFReceiver rfReceiver) {
		this.rfReceiver = rfReceiver;
	}

	public RFTransmitter getRfTransmitter() {
		return rfTransmitter;
	}

	public void setRfTransmitter(RFTransmitter rfTransmitter) {
		this.rfTransmitter = rfTransmitter;
	}

	public List<C1G2InventoryCommand> getC1g2InventoryCommandList() {
		return c1g2InventoryCommandList;
	}

	public void setC1g2InventoryCommandList(List<C1G2InventoryCommand> c1g2InventoryCommandList) {
		this.c1g2InventoryCommandList = c1g2InventoryCommandList;
	}

	public List<Custom> getCustomList() {
		return customList;
	}

	public void setCustomList(List<Custom> cusList) {
		this.customList = cusList;
	}

	@Override
	public String toString() {
		return "AntennaConfiguration [parameterHeader=" + parameterHeader + ", antennaID=" + antennaID + ", rfReceiver=" + rfReceiver + ", rfTransmitter="
				+ rfTransmitter + ", c1g2InventCommandList=" + c1g2InventoryCommandList + ", customList=" + customList + "]";
	}
}