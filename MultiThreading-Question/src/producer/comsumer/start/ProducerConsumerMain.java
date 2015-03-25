package producer.comsumer.start;

import generate.producer.Producer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import data.broker.Broker;

/*
 * This class contains (@Main.class) where the execution begins. This class creates the broker, producer and consumers
 * @author 
 */
public class ProducerConsumerMain {
	
	private static int m_size = 500;
	private static int m_numOfConsumers = 5;
	private static final int m_numOfProducers = 1;
	private static int m_noOfMessages=0;

	/*
	 * This is the start point of the application. It creates the broker, consumer and producer and starts
	 * the application.
	 */
	public static void main(final String[] args) {
		
		// These parameters are set only when running the Junit test.
		if(args.length == 4){
			configureParameters(args[0], args[1], args[2], args[3]);
		}

		cleanupOldFiles();
		
		final Broker sharedQueue = DependencyFactory.getSharedBroker(m_size);
		
		// creates the executor service which handles thread failure and re creation and handles shutdown.
		final ExecutorService threadPool = Executors
				.newFixedThreadPool(m_numOfConsumers + m_numOfProducers);

		DependencyFactory.log("Starting Consumer threads");

		for (int i = 1; i <= m_numOfConsumers; i++) {
			// Creates the buffereWriter and creates consumers using it
			BufferedWriter writer = null;
			try {
				writer = DependencyFactory.getFileWriter(i);
			} catch (IOException e) {
				DependencyFactory.error("creating file handler encountered exception");
				e.printStackTrace();
				return;
			}

			threadPool.execute(DependencyFactory.getConsumer(writer,
					sharedQueue));
		}

		DependencyFactory.log("Starting Producer thread");

		// Creating Producer thread
		Future<?> producerStatus = threadPool.submit(new Producer(sharedQueue, m_noOfMessages));

		try {
			// waits for the producer thread to complete.
			producerStatus.get();
		} catch (InterruptedException | ExecutionException e) {
			DependencyFactory.error("Producer thread is interrupted");
			e.printStackTrace();
		}
		finally{
			threadPool.shutdown(); // Disable new tasks from being submitted
		}
	}		
	
	/*
	 * This method is used for cleaning up old files.
	 */
	private static void cleanupOldFiles() {
		/*
		 * This deletes any files which exists before.
		 */
		for (int i = 1; i <= m_numOfConsumers; i++) {

			final String fileName = String.format("file%s.txt", i);

			File file = new File("files/", fileName);

			// if file exists, then delete it
			if (file.exists()) {
				file.delete();
			}
		}
	}

	/*
	 * This method is used for testing only
	 */
	private static void configureParameters(final String size,
			final String noOfConsumers, final String noOfMessages, final String debugLevelLoggingEnabled) {

		m_size = Integer.parseInt(size);
		m_numOfConsumers = Integer.parseInt(noOfConsumers);
		m_noOfMessages = Integer.parseInt(noOfMessages);
		DependencyFactory.setConsoleLevelLoggingDisabled(Boolean.parseBoolean(debugLevelLoggingEnabled));
	}

}
