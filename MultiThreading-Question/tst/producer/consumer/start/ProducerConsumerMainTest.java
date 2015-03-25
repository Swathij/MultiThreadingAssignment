package producer.consumer.start;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

import producer.comsumer.start.ProducerConsumerMain;

public class ProducerConsumerMainTest {

	@Test
	public void testMain() throws IOException {
		//ProducerConsumerMain.main(null);	
		
		File file = new File("files/", "file1.txt");

		FileReader fileWriter = new FileReader(file.getAbsoluteFile());
		BufferedReader bufferedreader = new BufferedReader(fileWriter);

		String line = bufferedreader.readLine();
		
		System.out.println(line.split(":"));
	}
}
