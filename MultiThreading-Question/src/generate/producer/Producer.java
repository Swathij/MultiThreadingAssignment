package generate.producer;

import java.util.Random;
import java.util.UUID;

import producer.comsumer.start.DependencyFactory;

import data.broker.Broker;
import data.broker.Message;

/*
 * The class defines the methods which produce message (@Message.class) and enqueues to broker (@Broker.class)
 * by generating two random numbers and a UUID.
 * @author 
 */
public class Producer implements Runnable {

	private final Broker m_sharedQueue;
	private final int m_noOfMessages;

	public Producer(final Broker sharedQueue, final int noOfMessages) {
		m_sharedQueue = sharedQueue;
		m_noOfMessages = noOfMessages;
	}

	/*
	 * (non-Javadoc) This method has logic for producing the (@Message.class)
	 * using generateMessage and enqueue it to broker (Broker.class)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		DependencyFactory.log("Started producer "
				+ Thread.currentThread().getName());

		/*
		 * The below parameters determine if producer emits fixedNumber of
		 * messages or not.
		 */
		int fixedMessageCount = 0;
		boolean termination = false;
		// Start time of the production.
		final long startTime = System.nanoTime();

		/*
		 * if the noOfMessages is not 0, then it producer emits m_noOfMessages
		 * number of messages which is useful in testing and optimizing.
		 */
		if (m_noOfMessages != 0) {
			fixedMessageCount = (m_noOfMessages - 1);
			termination = true;
		}
		try {
			do {
				final Message message = generateMessage();

				try {
					// writes into broker and waits if broker is full.
					m_sharedQueue.put(message);
					DependencyFactory.log(String.format(
							" message  %s  added by thread %s", message, Thread
									.currentThread().getName()));
				} catch (InterruptedException e) {
					DependencyFactory.error(Thread.currentThread().getName()
							+ " is interrupted");
					e.printStackTrace();
				}

				if (termination) {
					fixedMessageCount--;
				}
			} while (fixedMessageCount >= 0);
		} finally {

			if (termination) {
				m_sharedQueue.setPoisonPill();
				/*
				 * This time includes all the time it took for producer to emit
				 * fixedNumber of messages.
				 */
				final long endTime = System.nanoTime();
				final double difference = (endTime - startTime) / 1e6;
				final double rate = m_noOfMessages / difference;
				DependencyFactory.log(String.format(
						" Total messages %d generation time %f rate %f",
						m_noOfMessages, difference, rate));
			}
		}
	}

	/*
	 * This method generates two random numbers and UUID and constructs the
	 * message (@Message.class).
	 * 
	 * @returns message (@Message.class)
	 */
	private Message generateMessage() {
		final Random randomGenerator = new Random();
		final int firstRandom = randomGenerator.nextInt(1000);
		final int secondRandom = randomGenerator.nextInt(2000);

		final UUID idOne = UUID.randomUUID();

		return new Message(String.valueOf(idOne), firstRandom, secondRandom);
	}

}
