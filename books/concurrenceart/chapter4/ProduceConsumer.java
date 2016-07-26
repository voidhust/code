public class ProduceConsumer {
	static boolean empty=true; 
	static Object plate=new Object();//盘子
	static class Consumer implements Runnable{ 
		@Override
		public void run() { 
			//加锁
			synchronized(plate){
				//当条件不满足时，继续wait
				while(empty){
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
				System.out.println("向盘子里放入一个苹果！");
				empty=false; 
				plate.notifyAll();
			} 
		} 
	}
	public static void main(String[] args) throws InterruptedException{
		Thread consumerThread=new Thread(new Consumer());
		consumerThread.start();
		Thread produceThread=new Thread(new Produce());
		produceThread.start();
	}
}
