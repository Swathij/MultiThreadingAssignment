package producer.comsumer.start;

import generate.producer.Producer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import consume.Consumer.Consumer;
import data.broker.Broker;

/*
 *  This class provides methods for creating objects needed for the application acts as
 *  a dependency injection class.
 * 
 */
public class DependencyFactory {

	/*
	 * This method creates a file if it doesn't exists and returns a
	 * bufferedWriter object.
	 * 
	 * @returns bufferedWriter(@BufferedWriter.class)
	 */
	public static BufferedWriter getFileWriter(int fileIndex)
			throws IOException {

		final String fileName = String.format("file%s.txt", fileIndex);

		File file = new File("files/", fileName);

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

		return bufferedWriter;

	}

	public static Thread getConsumerThread(final BufferedWriter writer,
			final Broker sharedQueue) {
		return new Thread(new Consumer(writer, sharedQueue));

	}

	public static Thread getProducerThread(final Broker sharedQueue, final int noOfMessages) {
		return new Thread(new Producer(sharedQueue, noOfMessages));

	}

	/*
	 * This method creates consumer (@Consumer.class) Object
	 * 
	 * @returns (@Consumer.class)
	 */
	public static Consumer getConsumer(final BufferedWriter writer,
			final Broker sharedQueue) {
		return new Consumer(writer, sharedQueue);

	}

	/*
	 * This method creates producer (@Producer.class) Object
	 * 
	 * @returns (@Producer.class)
	 */
	public static Producer getProducer(final Broker sharedQueue, final int noOfMessages) {
		return new Producer(sharedQueue, noOfMessages);

	}

	/*
	 * This creates a sharedBorker.
	 * 
	 * @returns broker ( @Broker.class)
	 */
	public static Broker getSharedBroker(final int capacity) {
		return new Broker(capacity);

	}

	/*
	 * This method logs the info on console.
	 */
	public static void log(Object aObject) {
		System.out.println(String.valueOf(aObject));
	}
	
	/*
	 * This method logs the errors on console.
	 */
	public static void error(Object aObject) {
		 System.err.println(String.valueOf(aObject));
	}

	
}
