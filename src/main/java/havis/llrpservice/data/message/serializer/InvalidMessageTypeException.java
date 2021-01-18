package havis.llrpservice.data.message.serializer;

public class InvalidMessageTypeException extends Exception {

	private static final long serialVersionUID = -2110372842116224389L;

	public InvalidMessageTypeException(String message) {
		super(message);
	}
}