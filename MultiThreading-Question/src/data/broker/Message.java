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
	private final float m_firstNumber;

	/*
	 * Second random number
	 */
	private final float m_secondNumber;

	public Message(final String id, final float firstNumber,
			final float secondNumber) {
		m_id = id;
		m_firstNumber = firstNumber;
		m_secondNumber = secondNumber;
	}

	public String get_id() {
		return m_id;
	}

	public float getFirstNumber() {
		return m_firstNumber;
	}

	public float getSecondNumber() {
		return m_secondNumber;
	}

	@Override
	public String toString() {
		return "{Id=" + m_id + ",FirstNumber=" + m_firstNumber
				+ ",secondNumber=" + m_secondNumber + '}';

	}
}
