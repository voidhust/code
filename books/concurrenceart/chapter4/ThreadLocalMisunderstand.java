public class ThreadLocalMisunderstand {

	static class Index {
		private int num; 
		public void increase() {
			num++;
		}
		public int getValue() {
			return num;
		}
	}
	private static ThreadLocal<Index> local = new ThreadLocal<Index>() {
		@Override
		protected Index initialValue() {
			return new Index(); //注意这里
		}
	};
	//计数
	static class Counter implements Runnable{
		@Override
		public void run() {
            //获取当前线程的本地变量，然后累加5次
			Index num = local.get();
			for (int i = 0; i < 10000; i++) {
				num.increase();
			}
            //重新设置累加后的本地变量
			local.set(num);
			System.out.println(Thread.currentThread().getName() + " : "+ local.get().getValue());
		}
	}
	public static void main(String[] args) throws InterruptedException {
		Thread[] threads = new Thread[5];
		for (int i = 0; i < 5; i++) {		
            threads[i] = new Thread(new Counter() ,"CounterThread-[" + i+"]");
		} 
		for (int i = 0; i < 5; i++) {	
			threads[i].start();
		}
	}
}
