package data.broker;

/*
 * This class defines the data which is exchanged between producer and consumer.
 * @author 
 */
public class Message {

	// private static final long serialVersionUID = 42L;

	/*
	 * Unique Identifier of the message.
	 */
	private final String m_id;

	/*
	 *  First random number
	 */
	private final int m_firstNumber;

	/*
	 * Second random number
	 */
	private final int m_secondNumber;

	public Message(final String id, final int firstNumber,
			final int secondNumber) {
		m_id = id;
		m_firstNumber = firstNumber;
		m_secondNumber = secondNumber;
	}

	public String get_id() {
		return m_id;
	}

	public int getFirstNumber() {
		return m_firstNumber;
	}

	public int getSecondNumber() {
		return m_secondNumber;
	}

	@Override
	public String toString() {
		return "{Id=" + m_id + ",FirstNumber=" + m_firstNumber
				+ ",secondNumber=" + m_secondNumber + '}';

	}
}
