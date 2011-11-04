package org.uwdl.scenario;

import java.util.LinkedList;
import java.util.Queue;

public class ThreadQueue {
	private final Queue<UNodeThread> queue = new LinkedList<UNodeThread>();
	
	public synchronized UNodeThread getUNodeThread(UNodeThread nodeThread) {
		while( queue.peek()==null || !queue.contains(nodeThread) ) {
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
