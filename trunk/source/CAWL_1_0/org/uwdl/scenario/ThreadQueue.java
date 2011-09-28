package org.uwdl.scenario;

import java.util.LinkedList;
import java.util.Queue;

public class ThreadQueue extends Thread {
	private final Queue<UNodeThread> queue = new LinkedList<UNodeThread>();
	
	public synchronized UNodeThread getUNodeThread(UNodeThread nodeThread) {
		while( queue.peek()==null ) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return queue.remove();
	}
	
	public synchronized void putUNodeThread(UNodeThread nodeThread) {
		queue.offer(nodeThread);
		notifyAll();
	}
}
