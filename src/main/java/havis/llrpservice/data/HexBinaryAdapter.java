package havis.llrpservice.data;

import java.util.BitSet;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class HexBinaryAdapter extends XmlAdapter<String, BitSet> {

	@Override
	public BitSet unmarshal(String v) {
		if (v == null)
			return null;
		BitSet t = new BitSet(v.length());

		int i = 0;
		for (byte c : v.getBytes()) {
			if (c == '1')
				t.set(i);
			i++;
		}
		return t;
	}

	@Override
	public String marshal(BitSet v) {
		if (v == null)
			return null;
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < v.length(); i++)
			builder.append(v.get(i) ? '1' : '0');

		return builder.toString();
	}
}