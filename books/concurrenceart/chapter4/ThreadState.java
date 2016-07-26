public class ThreadState {
	//该线程不断进行睡眠，进行有限时间等待
	static class TimeWaiting implements Runnable{ 
		@Override
		public void run() { 
			while(true){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) { 
					e.printStackTrace();
				}
			}
		} 
	}
	//该线程在Waiting.class实例上wait()进入等待状态，知道其他线程调用Waiting.class实例上的notify()和notifyAll()
	static class Waiting implements Runnable{ 
		@Override
		public void run() { 
			while(true){
				synchronized(Waiting.class){
					try {
						Waiting.class.wait();
					} catch (InterruptedException e) { 
						e.printStackTrace();
					}
				}
			}
		} 
	}
	//该线程在Blocked.class实例上加锁后，过一段时间再释放锁，因此下一个线程申请锁将会阻塞住
	static class Blocked implements Runnable{ 
		@Override
		public void run() { 
			synchronized(Blocked.class){
				while(true){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) { 
						e.printStackTrace();
					}
				}
			}
		} 
	}
	public static void main(String[] args){
		new Thread(new TimeWaiting(),"TimeWaitingThread").start();
		new Thread(new Waiting(),"WaitingThread").start();
		//使用两个Blocked线程，一个获取锁成功，另一个将会被阻塞
		new Thread(new Blocked(),"BlockedThread-1").start();
		new Thread(new Blocked(),"BlockedThread-1").start();
	}
}
