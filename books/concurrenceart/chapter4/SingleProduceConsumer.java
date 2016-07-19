public class SingleProduceConsumer { 
	static AtomicInteger plate=new AtomicInteger(0);//盘子
	static class Consumer implements Runnable{ 
		@Override
		public void run() { 
			//加锁
			synchronized(plate){
				//当条件不满足时，继续wait
				while(plate.intValue()==0){
					try {
						plate.wait();
					} catch (InterruptedException e) { 
						e.printStackTrace();
					}
				}
				//条件满足时，完成工作
				System.out.println("从盘子里拿一个苹果！"); 
			} 
		}
	}
	static class Produce implements Runnable{ 
		@Override
		public void run() { 
			//加锁
			synchronized(plate){ 
				//改变条件
				plate.set(1);
				System.out.println("向盘子里放入一个苹果！"); 
				plate.notifyAll();
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
