public class WaitNotify {
	static boolean flag=true;
	static Object lock=new Object();
	static class Wait implements Runnable{ 
		@Override
		public void run() { 
			//加锁拥有lock的Monitor
			synchronized(lock){
				//当条件不满足时， 继续wait，同时释放了lock的锁
				while(flag){ 
					try {
						System.out.println("flag is true.wait! "+System.currentTimeMillis());
						lock.wait();
					} catch (InterruptedException e) { 
						e.printStackTrace();
					}
				}
				//条件满足时，完成工作
				System.out.println("flag is false.running "+System.currentTimeMillis());
			}
		} 
	}
	
	static class Notify implements Runnable{ 
		@Override
		public void run() { 
			//加锁，拥有lock的Monitor
			synchronized(lock){
				//发送通知，通知时不会释放lock的锁
				//直到当前线程释放了lock后，WaitThread才能从wait方法中返回
				System.out.println("hold lock.notify "+System.currentTimeMillis());
				lock.notifyAll();
				flag=false;
				try {
					Thread.sleep(1000*5);
				} catch (InterruptedException e) { 
					e.printStackTrace();
				}
			}
			//再次加锁
			synchronized(lock){
				System.out.println("hold lock.again "+System.currentTimeMillis());
				try {
					Thread.sleep(1000*5);
				} catch (InterruptedException e) { 
					e.printStackTrace();
				}
			}
		} 
	}
	public static void main(String[] args) throws InterruptedException{
		Thread waitThread=new Thread(new Wait(),"WaitThread");
		waitThread.start();
		Thread.sleep(1000);
		Thread notifyThread=new Thread(new Notify(),"NotifyThread");
		notifyThread.start();
	}
}
