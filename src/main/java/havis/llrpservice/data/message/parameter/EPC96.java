package havis.llrpservice.data.message.parameter;

import havis.llrpservice.data.message.parameter.ParameterTypes.ParameterType;

import java.util.Arrays;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class EPC96 extends Parameter {

	private static final long serialVersionUID = -2497041334905107350L;

	private TVParameterHeader parameterHeader;
	private byte[] epc;

	public EPC96() {
	}

	public EPC96(TVParameterHeader parameterHeader, byte[] epc) {
		this.parameterHeader = parameterHeader;
		this.parameterHeader.setParameterType(ParameterType.EPC_96);
		this.epc = epc;
	}

	public TVParameterHeader getParameterHeader() {
		return parameterHeader;
	}

	public void setParameterHeader(TVParameterHeader parameterHeader) {
		this.parameterHeader = parameterHeader;
	}

	@XmlJavaTypeAdapter(HexBinaryAdapter.class)
	public byte[] getEpc() {
		return epc;
	}

	public void setEpc(byte[] epc) {
		this.epc = epc;
	}

	@Override
	public String toString() {
		return "EPC96 [parameterHeader=" + parameterHeader + ", epc=" + Arrays.toString(epc) + "]";
	}
}