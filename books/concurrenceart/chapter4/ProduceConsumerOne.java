public class ProduceConsumerOne {
	static AtomicInteger plate=new AtomicInteger(0);//盘子
	static class Consumer implements Runnable{ 
		@Override
		public void run() { 
			while(true){
				try{
					//加锁
					synchronized(plate){
						//当条件不满足时，继续wait
						while(plate.intValue()==0){ 
								plate.wait(); 
						}
						//条件满足时，完成工作
						plate.set(0);
						System.out.println("从盘子里拿一个苹果！"); 
						plate.notifyAll();
					} 
				}catch (InterruptedException e) { 
					return;
				}
			}
		}
	}
	static class Produce implements Runnable{ 
		@Override
		public void run() { 
			while(true){
				try {
					//加锁
					synchronized(plate){ 
						while(plate.intValue()==1){
							plate.wait();
						}
						plate.set(1);
						System.out.println("向盘子里放入一个苹果！"); 
						plate.notifyAll();
					}
				} catch (InterruptedException e) { 
					return;
				}
			}
		} 
	}
	public static void main(String[] args) throws InterruptedException{
		Thread consumerThread=new Thread(new Consumer());
		consumerThread.start();
		Thread.sleep(10);
		Thread produceThread=new Thread(new Produce());
		produceThread.start();
		Thread.sleep(100);
		produceThread.interrupt();
		Thread.sleep(10);
		consumerThread.interrupt(); 
	}
}
