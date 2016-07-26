public class Interrupted {
	static class SleepRunner implements Runnable{ 
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
	static class BusyRunner implements Runnable{ 
		@Override
		public void run() { 
			while(true){
				
			}
		} 
	}
	public static void main(String[] args) throws InterruptedException{
		//sleepRunner不停的尝试睡眠
		Thread sleepRunner=new Thread(new SleepRunner(),"SleepThread");
		sleepRunner.setDaemon(true);
		
		//busyRunner不停的运转
		Thread busyRunner=new Thread(new BusyRunner(),"BusyThread");
		busyRunner.setDaemon(true);
		sleepRunner.start();
		busyRunner.start();
		//休眠5秒，让sleepRunner和busyRunner充分运行
		Thread.sleep(1000*5);
		sleepRunner.interrupt();
		busyRunner.interrupt();
		System.out.println("SleepThread interrupted is "+sleepRunner.isInterrupted());
		System.out.println("BusyThread interrupted is "+busyRunner.isInterrupted());
		//防止sleepRunner和busyRunner立刻退出
		Thread.sleep(1000*2);
	}
}
