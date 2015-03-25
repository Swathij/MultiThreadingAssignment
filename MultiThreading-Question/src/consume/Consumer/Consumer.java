package consume.Consumer;

import java.io.BufferedWriter;
import java.io.IOException;

import producer.comsumer.start.DependencyFactory;

import data.broker.Broker;
import data.broker.Message;

/*
 * This class has definitions for consumer which retrives messages (@Message.class) from broker (@Broker)
 * and writes the messages to file.
 * @author 
 */
public class Consumer implements Runnable {

	private final BufferedWriter m_writer;
	private final Broker m_sharedQueue;

	public Consumer(final BufferedWriter writer, final Broker sharedQueue) {
		m_writer = writer;
		m_sharedQueue = sharedQueue;
	}

	/*
	 * (non-Javadoc) This method has logic for retrieving the (@Message) from
	 * broker and calls processMessage
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		DependencyFactory.log("Started consumer "
				+ Thread.currentThread().getName());

		final long start_time = System.nanoTime();
		int noOfMessages = 0;

		try {

			while (true) {

				try {

					final Message message = m_sharedQueue.get();
					/*
					 * If sharedQueue has no messages(empty), .get() method
					 * would return null.
					 */
					if (message == null) {
						if (m_sharedQueue.getPoisonPill()){
							break;
						}
						else {
							continue;
						}
					}
					/*
					 * processMessage the maximum number between the two numbers
					 * and writes into file.
					 */
					processMessage(message);
					noOfMessages++;

				} catch (InterruptedException e) {
					DependencyFactory.error(Thread.currentThread().getName()
							+ " is interrupted");
					e.printStackTrace();
				}
			}

		} finally {
			try {
				m_writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			final long end_time = System.nanoTime();

			final double difference = (end_time - start_time) / 1e6;
			double rate = 0;
			if (noOfMessages > 0) {
				rate = noOfMessages/difference;
			}
			DependencyFactory.log(String.format(
					" Total messages %d consumed time %f rate %f",
					noOfMessages, difference, rate));
		}

	}

	/*
	 * This method has logic for processing the message which finding maximum of
	 * two numbers sent in the message and printing the numbers in the
	 * respective file.
	 * 
	 * @Input message (@Message.Class data which is exchanged between consumer
	 * and producer)
	 */
	private void processMessage(Message message) {

		// final int operation = Math.max(message.getFirstNumber(),
		// message.getSecondNumber());

		final int operation = GCD(message.getFirstNumber(),
				message.getSecondNumber());

		final String content = String.format("%s : GCD (%d : %d) : %d ",
				message.get_id(), message.getFirstNumber(),
				message.getSecondNumber(), operation);

		try {
			m_writer.write(content);
			m_writer.newLine();
			m_writer.flush();
			DependencyFactory.debug(String.format(
					" message  %s  written to file by thread %s", message,
					Thread.currentThread().getName()));
		} catch (IOException e) {
			DependencyFactory.error(Thread.currentThread().getName()
					+ " encountered IOException");
			e.printStackTrace();
		}

	}

	/*
	 * This method finds GCD of two numbers.
	 * 
	 * @Input a
	 * 
	 * @Input b
	 * 
	 * @returns gcd
	 */
	private int GCD(int a, int b) {
		return b == 0 ? a : GCD(b, a % b);
	}

}
