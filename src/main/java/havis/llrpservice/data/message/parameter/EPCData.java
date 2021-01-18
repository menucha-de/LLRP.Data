package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.HexBinaryAdapter;
import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.BitSet;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class EPCData extends Parameter {

	private static final long serialVersionUID = 4448972663827774134L;

	private TLVParameterHeader parameterHeader;
	private int epcLengthBits;
	private BitSet epc;

	public EPCData() {
	}

	public EPCData(TLVParameterHeader parameterHeader, BitSet epc) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.EPC_DATA);
		this.epc = epc;
	}

	public TLVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TLVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	public int getEpcLengthBits() {
		return epcLengthBits;
	}

	public void setEpcLengthBits(int epcLengthBits) {
		this.epcLengthBits = epcLengthBits;
	}

	@XmlJavaTypeAdapter(HexBinaryAdapter.class)
	public BitSet getEpc() {
		return epc;
	}

	public void setEpc(BitSet epc) {
		this.epc = epc;
	}

	@Override
	public String toString() {
		return "EPCData [parameterHeader=" + parameterHeader + ", epcLengthBits=" + epcLengthBits + ", epc=" + epc + "]";
	}
}