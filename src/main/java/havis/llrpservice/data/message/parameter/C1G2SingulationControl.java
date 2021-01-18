package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

public class C1G2SingulationControl extends Parameter {

	private static final long serialVersionUID = -7184841451800849267L;

	private TLVParameterHeader parameterHeader;
	private byte session;
	private int tagPopulation;
	private long tagTransitTime;
	private C1G2TagInventoryStateAwareSingulationAction c1g2TagInventoryStateAwareSingulationAction;

	public C1G2SingulationControl() {
	}

	public C1G2SingulationControl(TLVParameterHeader header, byte session, int tagPopulation, long tagTransitTime) {
		parameterHeader = header;
		parameterHeader.setParameterType(ParameterType.C1G2_SINGULATION_CONTROL);
		this.session = session;
		this.tagPopulation = tagPopulation;
		this.tagTransitTime = tagTransitTime;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public byte getSession() {
		return session;
	}

	public void setSession(byte session) {
		this.session = session;
	}

	public int getTagPopulation() {
		return tagPopulation;
	}

	public void setTagPopulation(int tagPopulation) {
		this.tagPopulation = tagPopulation;
	}

	public long getTagTransitTime() {
		return tagTransitTime;
	}

	public void setTagTransitTime(long tagTransitTime) {
		this.tagTransitTime = tagTransitTime;
	}

	public C1G2TagInventoryStateAwareSingulationAction getC1g2TagInventoryStateAwareSingulationAction() {
		return c1g2TagInventoryStateAwareSingulationAction;
	}

	public void setC1g2TagInventoryStateAwareSingulationAction(C1G2TagInventoryStateAwareSingulationAction c1g2TagInventoryStateAwareSingulationAction) {
		this.c1g2TagInventoryStateAwareSingulationAction = c1g2TagInventoryStateAwareSingulationAction;
	}

	@Override
	public String toString() {
		return "C1G2SingulationControl [parameterHeader=" + parameterHeader + ", session=" + session + ", tagPopulation=" + tagPopulation + ", tagTransitTime="
				+ tagTransitTime + ", c1g2TagInventoryStateAwareSingulationAction=" + c1g2TagInventoryStateAwareSingulationAction + "]";
	}
}