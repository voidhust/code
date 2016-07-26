public class ThreadPoolTest {
	static class MyJob implements Runnable{ 
		private String name;
		public MyJob(String name){
			this.name=name;
		}
		@Override
		public void run() {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) { 
				e.printStackTrace();
			} finally{
				System.out.println(this.toString()+"任务执行完成！");
			}
		}
		@Override
		public String toString(){
			return name; 
		}
	}
	public static void main(String[] arg) throws InterruptedException{
		long start=System.currentTimeMillis();
		DefaultThreadPool<MyJob> pool=new DefaultThreadPool<MyJob>();
		for(int i=0;i<2000;i++){
			MyJob job=new MyJob("MyJob"+i);
			pool.execute(job);
		}
		Thread.sleep(1000);
		pool.shutDown();
		System.err.println("main线程执行时间："+(System.currentTimeMillis()-start));
	}
}
