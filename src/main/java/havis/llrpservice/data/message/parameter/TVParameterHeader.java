package havis.llrpservice.data.message.parameter;

/**
 * A parameter header of type <code>Type Value</code>.
 */
public class TVParameterHeader extends ParameterHeader {

	private static final long serialVersionUID = 129610182803744175L;

	public TVParameterHeader() {
	}

	@Override
	public String toString() {
		return "TVParameterHeader [toString()=" + super.toString() + "]";
	}

	@Override
	public boolean isTLV() {
		return false;
	}
}