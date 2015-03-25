package data.broker;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/*
 * This class serves as the channel/broker between consumer and producer holding
 * the data which is exchanged between producer and consumer.
 * @author 
 */
public class Broker {
	private BlockingQueue<Message> m_queue;
    private volatile boolean m_poisonPill = false;
    
	public Broker(final int capacity) {
		m_queue = new LinkedBlockingQueue<Message>(capacity);
	}

	/*
	 * BlockingQueue implementation has put which blocks the producer when queue
	 * is full
	 * 
	 * @Input message(@Message which represents the data exchanged between
	 * producer and consumer)
	 * 
	 * @throws (@InterruptedException.Class exception)
	 */
	public void put(Message data) throws InterruptedException {
		this.m_queue.put(data);
	}

	/*
	 * BlockingQueue implementation has poll with timeout which returns the head
	 * of the queue if it exists or waits until one is available if not
	 * available, returns null.
	 * 
	 * @returns message(@Message which represents the data exchanged between
	 * producer and consumer)
	 * 
	 * @throws (@InterruptedException.Class when the thread is interrrupted)
	 */
	public Message get() throws InterruptedException {
		return this.m_queue.poll(10, TimeUnit.MILLISECONDS);
	}
	
	/*
	 * For instance which has a termination condition of fixedMessages, m_continueEnqueing
	 * is set by producer to indicate the termination of message Enqueue. 
	 * 
	 */	
	public void setPoisonPill(){
		m_poisonPill = true;
	}
	
	/*
	 * Consumer uses m_continueEnqueing to determine if producer is still emitting any messages.
	 */	
	public boolean getPoisonPill(){
		
		return m_poisonPill;
	}

}
