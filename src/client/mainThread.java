package client;

public class mainThread 
{
	static C c=new C();
	//flag������־���߳�ִ�н���
	static boolean flag=false;
	
	public static void main(String []arg)
	{	
		Thread mythread = new MyThread(new ClientMain());
		mythread.start();
		
		//�ȴ����߳�ִ�н���
		while(!flag);
		System.out.println("���߳�ִ��֮��value��ֵ�ǣ�"+c.getvalue());	
	}	
 
	public static void callback()
		{
			System.out.println("���߳�ִ�н���");	
			flag=true;
		}
}
 
 
class C
{
	private int value=0;
	public int getvalue()
	{
		return value;
	}
	public void setvalue(int v)
	{
		this.value=v;
	}
}
 
 
 
class MyThread extends Thread
{
	public MyThread(ClientMain clientMain)
	{
		this.clientMain=clientMain;
	}
	private ClientMain clientMain;
	@Override
	public void run() 
	{
		String[] arg = null;
		ClientMain.main(arg);			
		mainThread.callback();//����C#��ί�к��¼�
	}
	}
