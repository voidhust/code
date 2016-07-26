public class DefaultThreadPool<Job extends Runnable> implements ThreadPool<Job> {
	//最大线程数
	private static final int MaxWorkerNumber=10;
	//默认线程数
	private static final int DefaultWorkerNumber=5;
	//最小线程数
	private static final int MinWorkerNumber=1;
	//这是一个任务队列，将会向里面插入任务
	private final LinkedList<Job> jobs=new LinkedList<Job>();
	//工作者列表
	private final List<Worker> workers=
			Collections.synchronizedList(new ArrayList<Worker>());
	//工作者线程的数量
	private int workerNum=DefaultWorkerNumber;
	//线程编号生成
	private AtomicLong threadNum=new AtomicLong();
	public DefaultThreadPool(){
		initializeWorker(DefaultWorkerNumber);
	}
	public DefaultThreadPool(int num){
		workerNum=num>MaxWorkerNumber?MaxWorkerNumber:
			num<MinWorkerNumber?MinWorkerNumber:num;
		initializeWorker(workerNum);
	}
	//初始化工作者
	private void initializeWorker(int num){
		for(int i=0;i<num;i++){
			Worker worker=new Worker();
			workers.add(worker);
			Thread thread=new Thread(worker,"ThreadPool-Worker-"+threadNum.incrementAndGet());
			System.out.println("添加工作者线程：ThreadPool-Worker-"+threadNum.get());
			thread.start();
		}
	}
	@Override
	public void execute(Job job) {
		if(job!=null){
			//添加一个任务，然后进行通知
			synchronized(jobs){
				jobs.addLast(job);
				jobs.notifyAll();
				System.out.println("提交任务："+job.toString());
			}
		} 
	}

	@Override
	public void shutDown() {
		for(Worker worker:workers){
			worker.shutdown();
		}  
		System.err.println(" 关闭线程池！ ");
		//注意这里需要通知等待队列中的工作者线程
		synchronized(jobs){
			jobs.notifyAll();
		} 
	}

	@Override
	public void addWorkers(int num) {
		//限定新增的Worder数量不能超过最大值
		if(num+this.workerNum>MaxWorkerNumber){
			num=MaxWorkerNumber-this.workerNum;
		}
		synchronized(workers){
			initializeWorker(num);
			this.workerNum+=num;
		} 
	}

	@Override
	public void removeWorker(int num) {
		if(num>=this.workerNum){
			throw new IllegalArgumentException("beyond workNum");
		}
		synchronized(workers){
			//按照给定的数量停止Worker
			int count=num;
			while(count>0){
				Worker worker=workers.get(0);
				if(workers.remove(worker)){
					worker.shutdown();
					count--;
				}
			}
			this.workerNum-=num;
		}
	}

	@Override
	public int getJobSize() { 
		return jobs.size();
	} 
	
	//工作者线程消费任务
	public class Worker implements Runnable{
		//是否工作
		private volatile boolean running=true;
		@Override
		public void run() {
			while(true){
				Job job=null;
				synchronized(jobs){
					//如果任务队列是空的，那么就wait
					while(jobs.isEmpty()){
						if(running==false){
							System.out.println(Thread.currentThread().getName()+"结束");
							return;
						}
						try {
							jobs.wait();
						} catch (InterruptedException e) { 
							//感知外部对WorkerThread的中断操作，返回
							Thread.currentThread().interrupt();
							return; 
						}
					} 
					if(running==false){
						System.out.println(Thread.currentThread().getName()+"结束");
						return;
					}
					//取出一个Job
					job=jobs.removeFirst(); 
				}
				if(job!=null){
					try{
						System.out.println(Thread.currentThread().getName()+" 执行任务 "+job.toString());
						job.run();
					}catch(Exception e){
						//忽略Job执行中的Exception
					} 
				}
			} 
			
		} 
		//关闭当前工作者
		public void shutdown(){
			running=false;
			System.err.println("关闭一个工作者线程");
		}
	}
}
