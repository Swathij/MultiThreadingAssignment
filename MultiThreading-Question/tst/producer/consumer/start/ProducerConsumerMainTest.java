package producer.consumer.start;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashSet;

import org.junit.Test;

import producer.comsumer.start.ProducerConsumerMain;

public class ProducerConsumerMainTest {

	/*
	 * This test verifies that all the messages produced are consumed by
	 * consumers by counting total number of lines in the file.
	 */
	@Test
	public void testMain_ForProducedMessageCountEqualsConsumedCount()
			throws IOException {

		int noOfConsumers = 5;
		int noOfMessages = 100;
		String[] args = new String[3];
		args[0] = "50"; // size
		args[1] = String.valueOf(noOfConsumers); // NoOfConsumers
		args[2] = String.valueOf(noOfMessages); // NoOfMessages

		/*
		 * This deletes any files which exists before.
		 */
		for (int i = 1; i <= noOfConsumers; i++) {

			final String fileName = String.format("file%s.txt", i);

			File file = new File("files/", fileName);

			// if file exists, then delete it
			if (file.exists()) {
				file.delete();
			}

		}

		ProducerConsumerMain.main(args);

		int totalMessages = 0;

		for (int i = 1; i <= noOfConsumers; i++) {
			final String fileName = String.format("files/file%s.txt", i);
			totalMessages = countLines(fileName, totalMessages);
		}
		assertEquals("The messageCount is not equal", noOfMessages,
				totalMessages);

	}

	/*
	 * This test verifies that all the messages read by all consumers are
	 * unique.
	 */
	@Test
	public void testMain_EveryMessageReadbyConsumerisUnique()
			throws IOException {

		int noOfConsumers = 5;
		int noOfMessages = 10;
		String[] args = new String[3];

		args[0] = "50"; // size
		args[1] = String.valueOf(noOfConsumers); // NoOfConsumers
		args[2] = String.valueOf(noOfMessages); // NoOfMessages

		/*
		 * This deletes any files which exists before.
		 */
		for (int i = 1; i <= noOfConsumers; i++) {

			final String fileName = String.format("file%s.txt", i);

			File file = new File("files/", fileName);

			// if file exists, then delete it
			if (file.exists()) {
				file.delete();
			}

		}

		ProducerConsumerMain.main(args);
		HashSet<String> fileLineHolder = new HashSet<String>();

		int totalMessages = 0;

		for (int i = 1; i <= noOfConsumers; i++) {
			final String fileName = String.format("files/file%s.txt", i);
			totalMessages = countLines(fileName, totalMessages);
			fileLineHolder = createUniqueLineHolder(fileName, fileLineHolder);
		}
		assertFalse("This is empty if there occured a duplicate",
				fileLineHolder.isEmpty());
		int totalUniqueLinesBasedonHashSetSize = fileLineHolder.size();

		assertEquals("The totalmessages and UniqueMessage Holder size is same",
				totalMessages, totalUniqueLinesBasedonHashSetSize);

	}

	/*
	 * Helper method to calculate the number of lines in a file which correspond
	 * to enqueued messages read by each consumer.
	 * 
	 * @Input fileName
	 * 
	 * @Input cnt (existingCount)
	 * 
	 * @return count
	 */
	@SuppressWarnings("resource")
	public HashSet<String> createUniqueLineHolder(final String filename,
			final HashSet<String> fileLineHolder) throws IOException {

		BufferedReader br = null;
		String line;

		try {
			br = new BufferedReader(new FileReader(filename));
		} catch (IOException e) {
			System.err.println("Cannot read '" + filename + "': "
					+ e.getMessage());
		}

		while ((line = br.readLine()) != null) {
			// HashSet add returns false if it is a existing message.
			if (fileLineHolder.add(line)) {
				System.out.println(filename + ": " + line);
			} else {
				System.out.println(filename + ": " + line + "Found duplicate");
				return new HashSet<String>();
			}
		}

		return fileLineHolder;

	}

	/*
	 * Helper method to calculate the number of lines in a file which correspond
	 * to enqueued messages read by each consumer.
	 * 
	 * @Input fileName
	 * 
	 * @Input cnt (existingCount)
	 * 
	 * @return count
	 */
	public int countLines(String filename, int cnt) throws IOException {
		LineNumberReader reader = new LineNumberReader(new FileReader(filename));
		String lineRead = "";
		while ((lineRead = reader.readLine()) != null) {
		}

		cnt = cnt + reader.getLineNumber();
		reader.close();
		return cnt;
	}
	

	/*
	 * This test is used for finding best queue size.
	 */
	@Test
	public void testMain_queuesizevariton()
			throws IOException {

		int noOfConsumers = 5;
		int noOfMessages = 10000;
		String[] args = new String[3];
		args[0] = "50"; // size
		args[1] = String.valueOf(noOfConsumers); // NoOfConsumers
		args[2] = String.valueOf(noOfMessages); // NoOfMessages

		/*
		 * This deletes any files which exists before.
		 */
		for (int i = 1; i <= noOfConsumers; i++) {

			final String fileName = String.format("file%s.txt", i);

			File file = new File("files/", fileName);

			// if file exists, then delete it
			if (file.exists()) {
				file.delete();
			}

		}

		ProducerConsumerMain.main(args);
		
		int totalMessages = 0;

		for (int i = 1; i <= noOfConsumers; i++) {
			final String fileName = String.format("files/file%s.txt", i);
			totalMessages = countLines(fileName, totalMessages);
		}
		assertEquals("The messageCount is not equal", noOfMessages,
				totalMessages);

	}
}
