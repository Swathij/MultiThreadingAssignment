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

	public Producer(final Broker sharedQueue) {
		m_sharedQueue = sharedQueue;
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

		while (true) {
			final long startTime = System.nanoTime();
			final Message message = generateMessage();

			try {
				// writes into broker and waits if broker is full.
				m_sharedQueue.put(message);
				DependencyFactory.log(String.format(
						" message  %s  added by thread %s", message, Thread
								.currentThread().getName()));
			} catch (InterruptedException e) {
				DependencyFactory.error(Thread.currentThread().getName()+ " is interrupted");
				e.printStackTrace();
			}

			final long endTime = System.nanoTime();
			final double difference = (endTime - startTime) / 1e6;
			DependencyFactory.log(String.format(" message generation time %f",
					difference));
		}
	}

	/*
	 * This method generates two random numbers and UUID and constructors the
	 * message (@Message.class). 
	 * @returns message (@Message.class)
	 * 
	 */
	private Message generateMessage() {
		final Random randomGenerator = new Random();
		final float firstRandom = randomGenerator.nextInt(1000);
		final float secondRandom = randomGenerator.nextInt(2000);

		final UUID idOne = UUID.randomUUID();

		return new Message(String.valueOf(idOne), firstRandom, secondRandom);
	}

}
