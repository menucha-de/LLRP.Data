package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.List;

public class AISpec extends Parameter {

	private static final long serialVersionUID = -439585987180326892L;

	private TLVParameterHeader parameterHeader;
	private int antennaCount;
	private List<Integer> antennaIdList;
	private AISpecStopTrigger aiSpecStopTrigger;
	private List<InventoryParameterSpec> inventoryParameterList;
	private List<Custom> customList;

	public AISpec() {
	}

	public AISpec(TLVParameterHeader parameterHeader, List<Integer> antennaIdList, AISpecStopTrigger aiSpecStopTrigger,
			List<InventoryParameterSpec> inventoryParameterList) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.AI_SPEC);
		this.antennaIdList = antennaIdList;
		this.aiSpecStopTrigger = aiSpecStopTrigger;
		this.inventoryParameterList = inventoryParameterList;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getAntennaCount() {
		return antennaCount;
	}

	public void setAntennaCount(int antennaCount) {
		this.antennaCount = antennaCount;
	}

	public List<Integer> getAntennaIdList() {
		return antennaIdList;
	}

	public void setAntennaIdList(List<Integer> antennaIdList) {
		this.antennaIdList = antennaIdList;
	}

	public AISpecStopTrigger getAiSpecStopTrigger() {
		return aiSpecStopTrigger;
	}

	public void setAiSpecStopTrigger(AISpecStopTrigger aiSpecStopTrigger) {
		this.aiSpecStopTrigger = aiSpecStopTrigger;
	}

	public List<InventoryParameterSpec> getInventoryParameterList() {
		return inventoryParameterList;
	}

	public void setInventoryParameterList(List<InventoryParameterSpec> inventParameterList) {
		this.inventoryParameterList = inventParameterList;
	}

	public List<Custom> getCustomList() {
		return customList;
	}

	public void setCustomList(List<Custom> customList) {
		this.customList = customList;
	}

	@Override
	public String toString() {
		return "AISpec [parameterHeader=" + parameterHeader + ", antennaCount=" + antennaCount + ", antennaIDList=" + antennaIdList + ", aiSpecStopTrigger="
				+ aiSpecStopTrigger + ", inventoryParameterList=" + inventoryParameterList + ", customList=" + customList + "]";
	}
}