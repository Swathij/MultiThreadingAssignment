package producer.comsumer.start;

import generate.producer.Producer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import data.broker.Broker;

/*
 * This class contains (@Main.class) where the excution begins. This class creates the broker, producer and consumers
 * @author 
 */
public class ProducerConsumerMain {

	/*
	 * This is the start point of the application. It creates the broker, consumer and producer and starts
	 * the application.
	 * 
	 */
	public static void main(final String[] args) {

		final int size = 50;
		final int numOfConsumers = 5;
		final int numOfProducers = 1;

		final Broker sharedQueue = DependencyFactory.getSharedBroker(size);
		
		// creates the executor service which handles thread failure and re creation and handles shutdown.
		final ExecutorService threadPool = Executors
				.newFixedThreadPool(numOfConsumers + numOfProducers);

		DependencyFactory.log("Starting Consumer threads");

		for (int i = 1; i <= numOfConsumers; i++) {
			// Creates the buffereWriter and creates consumers using it
			BufferedWriter writer = null;
			try {
				writer = DependencyFactory.getFileWriter(i);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}

			threadPool.execute(DependencyFactory.getConsumer(writer,
					sharedQueue));

		}

		DependencyFactory.log("Starting Producer thread");

		// Creating Producer thread

		Future<?> producerStatus = threadPool.submit(new Producer(sharedQueue));

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


}
