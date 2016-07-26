public class ConnectionPoolTest {
	static ConnectionPool pool=new ConnectionPool(10);
	//保证所有的ConnectionRunner同时开始
	static CountDownLatch start=new CountDownLatch(1);
	//保证main线程等到所有ConnectionRunner结束后才能继续执行
	static CountDownLatch end;
	//一个不断尝试从连接池获取连接的任务
	static class ConnectionRunner implements Runnable{ 
		int count;//每条线程尝试获取次数
		AtomicInteger got; //所有线程获取到的总数
		AtomicInteger notgot;//所有线程没获取 到的总数
		
		public ConnectionRunner(int count,AtomicInteger got,AtomicInteger notgot){
			this.count=count;
			this.got=got;
			this.notgot=notgot;
		}
		@Override
		public void run() {
			 try {
				start.await();//等待start.countDown()的执行
			} catch (InterruptedException e) { 
				e.printStackTrace();
			}
			while(count-->0){
				try{
					//从线程中获取连接，如果1000ms内无法获取到，将会返回null
					//分别统计所有线程的：连接获取数got和连接失败数notgot
					Connection connection=pool.fetchConnection(1000);
					if(connection==null){
						notgot.incrementAndGet();
					}else{
						try{
							connection.createStatement();
							connection.commit();
						} finally{
							pool.releaseConnection(connection);
							got.incrementAndGet();
						}
					}
				} catch (Exception e) {
					e.printStackTrace(); 
				}
			}
			end.countDown();
		} 
	}
	
	public static void main(String[] arg) throws InterruptedException{
		//线程数量
		int threadCount=30;
		end=new CountDownLatch(threadCount);
		
		int count=20;
		AtomicInteger got=new AtomicInteger();
		AtomicInteger notgot=new AtomicInteger();
		for(int i=0;i<threadCount;i++){
			Thread thread=new Thread(new ConnectionRunner(count,got,notgot),
					"ConnectionRunnerThread"+i);
			thread.start();
		}
		start.countDown();//发送启动命令
		end.await();//等待所有ConnectionRunner线程结束
		System.out.println("total invoke："+(threadCount*count));
		System.out.println("got connection："+got.get());
		System.out.println("not got connection："+notgot.get());
	}
}
