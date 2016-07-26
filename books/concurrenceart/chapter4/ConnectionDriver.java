//java.sql.Connection是一个接口，最终的实现是由数据库驱动提供方来实现。
//我们通过动态代理构造一个Connection，仅仅用于示范。
public class ConnectionDriver { 
	//动态代理的处理器类
	static class ConnectionHandler implements InvocationHandler{ 
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Thread.sleep(100);
			return null;
		}
		
	}
	//创建一个Connection的代理，在commit时休眠100毫秒
	public static final Connection createConnection(){
		return (Connection)Proxy.newProxyInstance(ConnectionDriver.class.getClassLoader(), 
				new Class<?>[]{Connection.class}, new ConnectionHandler());
	}
}
