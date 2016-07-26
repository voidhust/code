public class Join {
	static class Domino implements Runnable{
		private Thread preThread;
		public Domino(Thread preThread){
			this.preThread=preThread;
		}
		@Override
		public void run() {
			 try {
				preThread.join();
			} catch (InterruptedException e) { 
				e.printStackTrace();
			}
			 System.out.println(Thread.currentThread().getName()+" terminate.");
		} 
	}
	
	public static void main(String[] args) throws InterruptedException{
		Thread preThread=Thread.currentThread();
		for(int i=0;i<10;i++){
			//每个线程都拥有前一个线程的引用，需要等待前一个线程终止，才能从等待中返回
			Thread thread=new Thread(new Domino(preThread),"Thread"+String.valueOf(i));
			thread.start();
			preThread=thread;
		}
		Thread.sleep(1000);
		System.out.println(Thread.currentThread().getName()+" terminate.");
	}
}
