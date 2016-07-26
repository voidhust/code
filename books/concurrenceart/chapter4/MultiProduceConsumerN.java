public class MultiProduceConsumerN {
	static final int MaxSize=10;
	static Object mutexProduce=new Object();
	static Object mutexConsumer=new Object();
	static AtomicInteger basket=new AtomicInteger(0);//篮子初始为空
	static class Consumer implements Runnable{ 
		@Override
		public void run() { 
			try{
				while(true){
					synchronized(mutexConsumer){
						//加锁
						synchronized(basket){
							//当条件不满足时，继续wait
							while(basket.intValue()==0){ 
								basket.wait(); 
							}
							//条件满足时，完成工作
							int i=basket.intValue();
							basket.set(basket.intValue()-1);
							System.out.println(Thread.currentThread().getName()+"从篮子里拿苹果 "+i+" ！"); 
							basket.notifyAll();
						} 
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
					synchronized(mutexProduce){
						//加锁
						synchronized(basket){ 
							//当条件不满足时，继续wait
							while(basket.intValue()==MaxSize){
								basket.wait();
							}
							//当条件满足时，完成工作
							int i=basket.intValue();
							basket.set(basket.intValue()+1);
							System.out.println(Thread.currentThread().getName()+"向盘子里放入苹果"+(i+1)+"！"); 
							basket.notifyAll();
						}
					} 
				}
			} catch (InterruptedException e) { 
				return;
			}

		} 
	}
	public static void main(String[] args) throws InterruptedException{
		Thread consumer1=new Thread(new Consumer(),"Consumer1");
		consumer1.start();
		Thread consumer2=new Thread(new Consumer(),"Consumer2");
		consumer2.start(); 
		Thread.sleep(10);
		Thread produce1=new Thread(new Produce(),"Produce1");
		produce1.start();
		Thread produce2=new Thread(new Produce(),"Produce2");
		produce2.start();
		Thread.sleep(100);
		produce1.interrupt();
		produce2.interrupt(); 
		Thread.sleep(10);//消费完剩余产品
		consumer1.interrupt();
		consumer2.interrupt();

	}
}
