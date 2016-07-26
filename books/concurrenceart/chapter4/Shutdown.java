public class Shutdown {
	static class Runner implements Runnable{ 
		private long i;
		private volatile boolean on=true;
		@Override
		public void run() { 
			while(on&&!Thread.currentThread().isInterrupted()){
				i++;
			}
			System.out.println("Count i="+i);
			
		} 
		public void cancel(){
			on=false;
		}
	}
	public static void main(String[] args) throws InterruptedException{ 
		Thread countThread=new Thread(new Runner(),"CountThread");
		countThread.start();
		//睡眠1秒，main线程对CountThread进行中断，使CountThread能够感知中断而结束
		Thread.sleep(1000*1);
		countThread.interrupt();//使用中断来终止线程
		//重新开启一个计数线程
		Runner second=new Runner();
		countThread=new Thread(second,"CountThread");
		countThread.start();
		//睡眠1秒，main线程对second进行取消，使CountThread能够感知on为false而结束
		Thread.sleep(1000*1);
		second.cancel();
		
	}
}
