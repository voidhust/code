public class Daemon {
	static class DaemonRunner implements Runnable{ 
		@Override
		public void run() { 
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) { 
				e.printStackTrace();
			}finally{
				System.out.println("DeamonThread finally run.");
			}
		} 
	}
	public static void main(String[] args){
		Thread thread=new Thread(new DaemonRunner(),"DaemonRunner");
		thread.setDaemon(true);
		thread.start();
	} 
}
