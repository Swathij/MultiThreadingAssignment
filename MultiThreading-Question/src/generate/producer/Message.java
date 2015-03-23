package generate.producer;


/*
 * This class defines the message which is exchanged between producer and consumer.
 */
public class Message  {

	//private static final long serialVersionUID = 42L;

	/**
	 * Unique Identifier of the message.
	 * 
	 * @serial
	 */
	private final String m_id;
	
	/**
	 * First random number
	 * 
	 * @serial
	 */
	private final float m_firstNumber;
	
	/**
	 * Second random number
	 * 
	 * @serial
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
	public String toString(){
	      return "{Id="  + m_id + ",FirstNumber=" + m_firstNumber + ",secondNumber=" + m_secondNumber + '}';

	}
}