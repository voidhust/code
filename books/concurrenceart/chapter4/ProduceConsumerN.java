public class ProduceConsumerN {
	static final int MaxSize=10;
	static AtomicInteger basket=new AtomicInteger(0);//篮子初始为空
	static class Consumer implements Runnable{ 
		@Override
		public void run() { 
			try{
				while(true){
					//加锁
					synchronized(basket){
						//当条件不满足时，继续wait
						while(basket.intValue()==0){ 
							basket.wait(); 
						}
						//条件满足时，完成工作
						int i=basket.intValue();
						basket.set(basket.intValue()-1);
						System.out.println("从篮子里拿苹果 "+i+" ！"); 
						basket.notifyAll();
					} 
				} 
			}catch (InterruptedException e) { 
				return;
			}

		}
	}
	static class Produce implements Runnable{ 
		@Override
		public void run() { 
			try {
				while(true){
					//加锁
					synchronized(basket){ 
						//当条件不满足时，继续wait
						while(basket.intValue()==MaxSize){
							basket.wait();
						}
						//当条件满足时，完成工作
						int i=basket.intValue();
						basket.set(basket.intValue()+1);
						System.out.println("向盘子里放入苹果"+(i+1)+"！"); 
						basket.notifyAll();
					}
				}
			} catch (InterruptedException e) { 
				return;
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
		consumerThread.interrupt();
		produceThread.interrupt();
	}
}
