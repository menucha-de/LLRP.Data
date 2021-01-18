package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.List;

public class C1G2InventoryCommand extends Parameter {

	private static final long serialVersionUID = -434419207941486348L;

	private TLVParameterHeader parameterHeader;
	boolean tagInventStateAware;
	private List<C1G2Filter> c1g2FilterList;
	private C1G2RFControl c1g2RFControl;
	private C1G2SingulationControl c1g2SingulationControl;
	private List<Custom> cusList;

	public C1G2InventoryCommand() {
	}

	public C1G2InventoryCommand(TLVParameterHeader header, boolean tagInventStateAware) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.C1G2_INVENTORY_COMMAND);
		this.tagInventStateAware = tagInventStateAware;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public boolean isTagInventStateAware() {
		return tagInventStateAware;
	}

	public void setTagInventStateAware(boolean tagInventStateAware) {
		this.tagInventStateAware = tagInventStateAware;
	}

	public List<C1G2Filter> getC1g2FilterList() {
		return c1g2FilterList;
	}

	public void setC1g2FilterList(List<C1G2Filter> c1g2FilterList) {
		this.c1g2FilterList = c1g2FilterList;
	}

	public C1G2RFControl getC1g2RFControl() {
		return c1g2RFControl;
	}

	public void setC1g2RFControl(C1G2RFControl c1g2rfControl) {
		c1g2RFControl = c1g2rfControl;
	}

	public C1G2SingulationControl getC1g2SingulationControl() {
		return c1g2SingulationControl;
	}

	public void setC1g2SingulationControl(C1G2SingulationControl c1g2SingulationControl) {
		this.c1g2SingulationControl = c1g2SingulationControl;
	}

	public List<Custom> getCusList() {
		return cusList;
	}

	public void setCusList(List<Custom> cusList) {
		this.cusList = cusList;
	}

	@Override
	public String toString() {
		return "C1G2InventoryCommand [parameterHeader=" + parameterHeader + ", tagInventStateAware=" + tagInventStateAware + ", c1g2FilterList="
				+ c1g2FilterList + ", c1g2RFControl=" + c1g2RFControl + ", c1g2SingulationControl=" + c1g2SingulationControl + ", cusList=" + cusList + "]";
	}
}