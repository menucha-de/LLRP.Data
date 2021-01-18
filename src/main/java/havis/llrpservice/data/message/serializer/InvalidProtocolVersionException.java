package havis.llrpservice.data.message.serializer;

public class InvalidProtocolVersionException extends Exception {

	private static final long serialVersionUID = -2110372842116224389L;

	public InvalidProtocolVersionException(String message) {
		super(message);
	}
}