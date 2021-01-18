package havis.llrpservice.data.message.parameter;

/**
 * A parameter header of type <code>Type Length Value</code>.
 */
public class TLVParameterHeader extends ParameterHeader {

	private static final long serialVersionUID = -7040856162437526180L;

	private byte reserved;

	public TLVParameterHeader() {
	}

	public TLVParameterHeader(byte reserved) {
		this.reserved = reserved;
	}

	public byte getReserved() {
		return reserved;
	}

	public void setReserved(byte reserved) {
		this.reserved = reserved;
	}

	@Override
	public boolean isTLV() {
		return true;
	}

	@Override
	public String toString() {
		return "TLVParameterHeader [reserved=" + reserved + ", toString()=" + super.toString() + "]";
	}
}