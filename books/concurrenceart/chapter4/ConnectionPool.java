//连接池
public class ConnectionPool {
	
	//通过一个双向队列来维护连接
	private LinkedList<Connection> pool=new LinkedList<Connection>();
	//构造函数，设定初始化连接池大小，并进行填充
	public ConnectionPool(int initialSize){
		for(int i=0;i<initialSize;i++){
			pool.addLast(ConnectionDriver.createConnection());
		} 
	}
	//获取Connection，在mills内无法获取将返回null
	public Connection fetchConnection(long mills) throws InterruptedException{
		synchronized(pool){ 
			if(mills<=0){
				//无限等待模式
				while(pool.isEmpty()){
					pool.wait();
				}
				return pool.removeFirst();
			}else{
				//超时等待模式
				long future=System.currentTimeMillis()+mills;
				long remaining=mills;
				while(pool.isEmpty()&&remaining>0){
					pool.wait(remaining);
					remaining=future-System.currentTimeMillis();
				}
				//条件满足（要么是超时，要么线程池容量非空。），处理任务
				Connection result=null;
				if(!pool.isEmpty()){
					result=pool.removeFirst();
				}
				return result;
			}
		}
	}
	//释放Connection
	public void releaseConnection(Connection connection){
		synchronized(pool){
			pool.addLast(connection);
			pool.notifyAll();
		}
	} 
}
