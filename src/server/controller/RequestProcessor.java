package server.controller;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.text.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import common.model.entity.*;
import common.util.DataSourceUtils;
import server.*;
import server.model.service.UserService;

/** ���������������� */
public class RequestProcessor implements Runnable{
	private Socket currentClientSocket;  //��ǰ��������������Ŀͻ���Socket
	
	public RequestProcessor(Socket currentClientSocket){
		this.currentClientSocket = currentClientSocket;
	}
	private static RequestProcessor instance;
    private RequestProcessor(){

    }
    public static synchronized RequestProcessor getInstance(){
        if(instance==null){
            instance=new RequestProcessor();
        }
        return instance;
    }
	public void run() {
		boolean flag = true; //�Ƿ񲻼�ϼ���
		try{
			OnlineClientIOCache currentClientIOCache = new OnlineClientIOCache(
					new ObjectInputStream(currentClientSocket.getInputStream()), 
					new ObjectOutputStream(currentClientSocket.getOutputStream()));
			while(flag){ //��ͣ�ض�ȡ�ͻ��˷��������������
				//�������������ж�ȡ���ͻ����ύ���������
				Request request = (Request)currentClientIOCache.getOis().readObject();
				System.out.println("Server��ȡ�˿ͻ��˵�����:" + request.getAction());

				String actionName = request.getAction();   //��ȡ�����еĶ���
				if(actionName.equals("userRegiste")){      //�û�ע��
					registe(currentClientIOCache, request);
				}else if(actionName.equals("userLogin")){  //�û���¼
					login(currentClientIOCache, request);
				}else if("exit".equals(actionName)){       //����Ͽ�����
					flag = logout(currentClientIOCache, request);
				}else if("chat".equals(actionName)){       //����
					chat(request);
				}else if("shake".equals(actionName)){      //��
					shake(request);
				}else if("toSendFile".equals(actionName)){ //׼�������ļ�
					toSendFile(request);
				}else if("agreeReceiveFile".equals(actionName)){ //ͬ������ļ�
					agreeReceiveFile(request);
				}else if("refuseReceiveFile".equals(actionName)){ //�ܾ������ļ�
					refuseReceiveFile(request);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/** �ܾ������ļ� */
	private void refuseReceiveFile(Request request) throws IOException {
		FileInfo sendFile = (FileInfo)request.getAttribute("sendFile");
		Response response = new Response();  //����һ����Ӧ����
		response.setType(ResponseType.REFUSERECEIVEFILE);
		response.setData("sendFile", sendFile);
		response.setStatus(ResponseStatus.OK);
		//�����󷽵�����������Ӧ
		OnlineClientIOCache ocic = DataBuffer.onlineUserIOCacheMap.get(sendFile.getFromUser().getUserid());
		this.sendResponse(ocic, response);
	}

	/** ͬ������ļ� */
	private void agreeReceiveFile(Request request) throws IOException {
		FileInfo sendFile = (FileInfo)request.getAttribute("sendFile");
		//������(���ͷ�)������������Ӧ
		Response response = new Response();  //����һ����Ӧ����
		response.setType(ResponseType.AGREERECEIVEFILE);
		response.setData("sendFile", sendFile);
		response.setStatus(ResponseStatus.OK);
		OnlineClientIOCache sendIO = DataBuffer.onlineUserIOCacheMap.get(sendFile.getFromUser().getUserid());
		this.sendResponse(sendIO, response);
		
		//����շ����������ļ�����Ӧ
		Response response2 = new Response();  //����һ����Ӧ����
		response2.setType(ResponseType.RECEIVEFILE);
		response2.setData("sendFile", sendFile);
		response2.setStatus(ResponseStatus.OK);
		OnlineClientIOCache receiveIO = DataBuffer.onlineUserIOCacheMap.get(sendFile.getToUser().getUserid());
		this.sendResponse(receiveIO, response2);
	}
	
	/** �ͻ����˳� */
	public boolean logout(OnlineClientIOCache oio, Request request) throws IOException{
		System.out.println(currentClientSocket.getInetAddress().getHostAddress()
				+ ":" + currentClientSocket.getPort() + "����");

		User user = (User)request.getAttribute("user");
		//�ѵ�ǰ���߿ͻ��˵�IO��Map��ɾ��
		DataBuffer.onlineUserIOCacheMap.remove(user.getUserid());
		//�������û�����Map��ɾ����ǰ�û�
		DataBuffer.onlineUsersMap.remove(user.getUserid());
			
		Response response = new Response();  //����һ����Ӧ����
		response.setType(ResponseType.LOGOUT);
		response.setData("logoutUser", user);
		oio.getOos().writeObject(response);  //����Ӧ�������ͻ���д
		oio.getOos().flush();
		currentClientSocket.close();  //�ر�����ͻ���Socket
		
		DataBuffer.onlineUserTableModel.remove(user.getUserid()); //�ѵ�ǰ�����û��������û���Model��ɾ��
		iteratorResponse(response);//֪ͨ�����������߿ͻ���
		
		return false;  //�Ͽ�����
	}
	
	public boolean logout_user(User user) throws IOException{
		OnlineClientIOCache io = DataBuffer.onlineUserIOCacheMap.get(user.getUserid());
		Response response = new Response();
		response.setStatus(ResponseStatus.OK);
		response.setType(ResponseType.LOGYOU);
		response.setData("txtMsg", "admin_logout");
		io.getOos().writeObject(response);  //����Ӧ�������ͻ���д
		io.getOos().flush();
		//�ѵ�ǰ���߿ͻ��˵�IO��Map��ɾ��
		DataBuffer.onlineUserIOCacheMap.remove(user.getUserid());
		//�������û�����Map��ɾ����ǰ�û�
		DataBuffer.onlineUsersMap.remove(user.getUserid());
		DataBuffer.onlineUserTableModel.remove(user.getUserid()); //�ѵ�ǰ�����û��������û���Model��ɾ��
		response.setType(ResponseType.LOGOUT);
		response.setData("logoutUser", user);
		iteratorResponse(response);//֪ͨ�����������߿ͻ���
		return false;  //�Ͽ�����
	}
	/** ע�� */
	public void registe(OnlineClientIOCache oio, Request request) throws IOException {
		User user = (User)request.getAttribute("user");
		UserService userService = new UserService();
		Boolean res = userService.addUser(user);
		if (res) {
			List<User> list = null;
			QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		    String sql = "select * from imusers";
		    try {
				list = (List<User>) qr.query(sql, new BeanListHandler<>(User.class));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    int count =0;
		    
		    for (User user2 : list) {
		    	count++;
				if (count==list.size()) {
					user = user2;
				}
			}
		    System.out.println(user.getUserid());
			Response response = new Response();  //����һ����Ӧ����
			response.setStatus(ResponseStatus.OK);
			response.setData("user", user);
			
			oio.getOos().writeObject(response);  //����Ӧ�������ͻ���д
			oio.getOos().flush();
		}else {
			Response response = new Response();  //����һ����Ӧ����
			response.setStatus(ResponseStatus.SERVER_ERROR);
			response.setData("user", user);
			
			oio.getOos().writeObject(response);  //����Ӧ�������ͻ���д
			oio.getOos().flush();
		}
		
		
		//����ע���û���ӵ�RegistedUserTableModel��
		DataBuffer.registedUserTableModel.add(new String[]{
			String.valueOf(user.getUserid()),
			user.getUserpwd(),
			user.getUsername(),
			String.valueOf(user.getSex())
		});
	}
	
	/** ��¼ */
	public void login(OnlineClientIOCache currentClientIO, Request request) throws IOException {
		String username = (String)request.getAttribute("username");
		String password = (String) request.getAttribute("password");
		UserService userService = new UserService();
		User user = userService.login(username, password);
		
		Response response = new Response();  //����һ����Ӧ����
		if(null != user){
			if(DataBuffer.onlineUsersMap.containsKey(user.getUserid())){ //�û��Ѿ���¼��
				response.setStatus(ResponseStatus.OK);
				response.setData("msg", "�� �û��Ѿ��ڱ������ˣ�");
				currentClientIO.getOos().writeObject(response);  //����Ӧ�������ͻ���д
				currentClientIO.getOos().flush();
			}else { //��ȷ��¼
				DataBuffer.onlineUsersMap.put((long) user.getUserid(), user); //��ӵ������û�
				//���������û�
				response.setData("onlineUsers", 
						new CopyOnWriteArrayList<User>(DataBuffer.onlineUsersMap.values()));
				
				response.setStatus(ResponseStatus.OK);
				response.setData("user", user);
				currentClientIO.getOos().writeObject(response);  //����Ӧ�������ͻ���д
				currentClientIO.getOos().flush();
				
				//֪ͨ�����û�����������
				Response response2 = new Response();
				response2.setType(ResponseType.LOGIN);
				response2.setData("loginUser", user);
				iteratorResponse(response2);
				
				//�ѵ�ǰ���ߵ��û�IO��ӵ�����Map��
				DataBuffer.onlineUserIOCacheMap.put((long) user.getUserid(),currentClientIO);
				
				//�ѵ�ǰ�����û���ӵ�OnlineUserTableModel��
				DataBuffer.onlineUserTableModel.add(
						new String[]{String.valueOf(user.getUserid()), 
										user.getUsername(), 
										String.valueOf(user.getSex())});
			}
		}else{ //��¼ʧ��
			response.setStatus(ResponseStatus.OK);
			response.setData("msg", "�˺Ż����벻��ȷ��");
			currentClientIO.getOos().writeObject(response);
			currentClientIO.getOos().flush();
		}
	}
	
	/** ���� */
	public void chat(Request request) throws IOException {
		Message msg = (Message)request.getAttribute("msg");
		Response response = new Response();
		response.setStatus(ResponseStatus.OK);
		response.setType(ResponseType.CHAT);
		response.setData("txtMsg", msg);
		
		//��Ϣ��¼�ŵ����ݿ�
		/*QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "insert into user values(?,?,?,?,?,?);";	*/
		
		
		
		if(msg.getToUser() != null){ //˽��:ֻ��˽�ĵĶ��󷵻���Ӧ
			OnlineClientIOCache io = DataBuffer.onlineUserIOCacheMap.get(msg.getToUser().getUserid());
			sendResponse(io, response);
		}else{  //Ⱥ��:�����˷���Ϣ�����пͻ��˶�������Ӧ
			for(long id : DataBuffer.onlineUserIOCacheMap.keySet()){
				if(msg.getFromUser().getUserid() == id ){	continue; }
				sendResponse(DataBuffer.onlineUserIOCacheMap.get(id), response);
			}
		}
	}
	
	/** ������ */
	public void shake(Request request)throws IOException {
		Message msg = (Message)request.getAttribute("msg");
		
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		StringBuffer sb = new StringBuffer();
		sb.append(" ").append(msg.getFromUser().getUsername())
			.append("(").append(msg.getFromUser().getUserid()).append(") ")
			.append(df.format(msg.getSendTime())).append("\n  ����������һ�����ڶ���\n");
		msg.setMessage(sb.toString());
		
		Response response = new Response();
		response.setStatus(ResponseStatus.OK);
		response.setType(ResponseType.SHAKE);
		response.setData("ShakeMsg", msg);
		
		OnlineClientIOCache io = DataBuffer.onlineUserIOCacheMap.get(msg.getToUser().getUserid());
		sendResponse(io, response);
	}
	
	/** ׼�������ļ� */
	public void toSendFile(Request request)throws IOException{
		Response response = new Response();
		response.setStatus(ResponseStatus.OK);
		response.setType(ResponseType.TOSENDFILE);
		FileInfo sendFile = (FileInfo)request.getAttribute("file");
		response.setData("sendFile", sendFile);
		//���ļ����շ�ת���ļ����ͷ�������
		OnlineClientIOCache ioCache = DataBuffer.onlineUserIOCacheMap.get(sendFile.getToUser().getUserid());
		sendResponse(ioCache, response);
	}
	
	/** ���������߿ͻ���������Ӧ */
	private void iteratorResponse(Response response) throws IOException {
		for(OnlineClientIOCache onlineUserIO : DataBuffer.onlineUserIOCacheMap.values()){
			ObjectOutputStream oos = onlineUserIO.getOos();
			oos.writeObject(response);
			oos.flush();
		}
	}
	
	/** ��ָ���ͻ���IO������������ָ����Ӧ */
	private void sendResponse(OnlineClientIOCache onlineUserIO, Response response)throws IOException {
		ObjectOutputStream oos = onlineUserIO.getOos();
		oos.writeObject(response);
		oos.flush();
	}
	public Boolean logout_all() {
		
		Response response = new Response();
		response.setStatus(ResponseStatus.OK);
		response.setType(ResponseType.LOGYOU);
		response.setData("txtMsg", "admin_logout");
		
		try {
			iteratorResponse(response);
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}//֪ͨ�����������߿ͻ���
		return false;
	}
}