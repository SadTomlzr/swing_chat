package client.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.text.*;
import java.util.Date;
import java.util.List;

import javax.swing.*;
import client.*;
import client.model.entity.MyCellRenderer;
import client.model.entity.MyFriendsTableModel;
import client.model.entity.OnlineUserListModel;
import client.util.*;
import common.model.entity.*;
import common.util.DataSourceUtils;
import server.model.service.UserService;
import sun.security.util.Length;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.mysql.fabric.xmlrpc.Client;
import com.sun.javafx.geom.AreaOp.IntOp;

/** ���촰�� */
public class ChatFrame extends JFrame{
	private static final long serialVersionUID = -2310785591507878535L;
	/**����Է�����ϢLabel*/
	private JLabel otherInfoLbl;
	/** ��ǰ�û���ϢLbl */
	private JLabel currentUserLbl;
	/**������Ϣ�б�����*/
	public static JTextArea msgListArea;
	/**Ҫ���͵���Ϣ����*/
	public static JTextArea sendArea; 
	/** �����û��б� */
	public static JList onlineList;
	/** �����û���ͳ��Lbl */
	public static JLabel onlineCountLbl;
	/** ׼�����͵��ļ� */
	public static FileInfo sendFile;
	/** ˽�ĸ�ѡ�� */
	public JCheckBox rybqBtn;
	MyFriendsListUI mfl;
	AddUsersUI adui;

	
	public ChatFrame(){
		this.init();
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void init(){
		this.setTitle("JQ������");
		this.setSize(550, 500);
		this.setResizable(false);
		long id = DataBuffer.currentUser.getUserid();
		
		//����Ĭ�ϴ�������Ļ����
		int x = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int y = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		this.setLocation((x - this.getWidth()) / 2, (y-this.getHeight())/ 2);
		
		//��������
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		//�ұ��û����
		JPanel userPanel = new JPanel();
		userPanel.setLayout(new BorderLayout());
		
		// ����һ���ָ�����
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				mainPanel, userPanel);
		splitPane.setDividerLocation(380);
		splitPane.setDividerSize(10);
		splitPane.setOneTouchExpandable(true);
		getContentPane().add(splitPane, BorderLayout.CENTER);
		
		//���ϱ���Ϣ��ʾ���
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BorderLayout());
		//������������Ϣ���
		JPanel sendPanel = new JPanel();
		sendPanel.setLayout(new BorderLayout());
		
		// ����һ���ָ�����
		JSplitPane splitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				infoPanel, sendPanel);
		splitPane2.setDividerLocation(300);
		splitPane2.setDividerSize(1);
		mainPanel.add(splitPane2, BorderLayout.CENTER);
		
		otherInfoLbl = new JLabel("��ǰ״̬��Ⱥ����...");
		infoPanel.add(otherInfoLbl, BorderLayout.NORTH);
		
		msgListArea = new JTextArea();
		msgListArea.setLineWrap(true);
		infoPanel.add(new JScrollPane(msgListArea, 
						JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
						JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		
		JPanel tempPanel = new JPanel();
		tempPanel.setLayout(new BorderLayout());
		sendPanel.add(tempPanel, BorderLayout.NORTH);
		
		// ���찴ť���
		JPanel btnPanel = new JPanel();
		tempPanel.add(btnPanel, BorderLayout.CENTER);
		
		/*//���尴ť
		JButton fontBtn = new JButton(new ImageIcon("images/font.png"));
		fontBtn.setMargin(new Insets(0,0,0,0));
		fontBtn.setToolTipText("��������͸�ʽ");
		
		//���鰴ť
		JButton faceBtn = new JButton(new ImageIcon("images/sendFace.png"));
		faceBtn.setMargin(new Insets(0,0,0,0));
		faceBtn.setToolTipText("ѡ�����");*/
		
		//�����ļ���ť
		JButton shakeBtn = new JButton(new ImageIcon("images/shake.png"));
		shakeBtn.setMargin(new Insets(0,0,0,0));
		shakeBtn.setToolTipText("��Է����ʹ�����");
		
		//�����ļ���ť
		JButton sendFileBtn = new JButton(new ImageIcon("images/sendPic.png"));
		sendFileBtn.setMargin(new Insets(0,0,0,0));
		sendFileBtn.setToolTipText("��Է������ļ�");
		
		JLabel label = new JLabel("");
		
		MyFriend_Label = new JLabel("\u6211\u7684\u597D\u53CB");
		MyFriend_Label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MyFriend_LabelmouseClicked(e);
			}
		});
		MyFriend_Label.setFont(new Font("����", Font.BOLD, 14));
		
		AddFriend_Label = new JLabel("\u6DFB\u52A0\u597D\u53CB");
		AddFriend_Label.setFont(new Font("����", Font.BOLD, 14));
		AddFriend_Label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				AddFriend_LabelmouseClicked(e);
			}
		});
		GroupLayout gl_btnPanel = new GroupLayout(btnPanel);
		gl_btnPanel.setHorizontalGroup(
			gl_btnPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_btnPanel.createSequentialGroup()
					/*.addGap(5)
					.addComponent(fontBtn)
					.addGap(5)
					.addComponent(faceBtn)*/
					.addGap(5)
					.addComponent(shakeBtn)
					.addGap(5)
					.addComponent(sendFileBtn)
					.addGroup(gl_btnPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_btnPanel.createSequentialGroup()
							.addGap(113)
							.addComponent(label)
							.addContainerGap(175, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, gl_btnPanel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(MyFriend_Label)
							.addGap(18)
							.addComponent(AddFriend_Label)
							.addGap(43))))
		);
		gl_btnPanel.setVerticalGroup(
			gl_btnPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_btnPanel.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_btnPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_btnPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(MyFriend_Label)
							.addComponent(AddFriend_Label))
						.addComponent(label)/*
						.addComponent(fontBtn)
						.addComponent(faceBtn)*/
						.addComponent(shakeBtn)
						.addComponent(sendFileBtn))
					.addContainerGap())
		);
		btnPanel.setLayout(gl_btnPanel);
		
		//˽�İ�ť
		rybqBtn = new JCheckBox("˽��");
		tempPanel.add(rybqBtn, BorderLayout.EAST);
		
		//Ҫ���͵���Ϣ������
		sendArea = new JTextArea();
		sendArea.setLineWrap(true);
		sendPanel.add(new JScrollPane(sendArea, 
						JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
						JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		
		// ���찴ť���
		JPanel btn2Panel = new JPanel();
		btn2Panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(btn2Panel, BorderLayout.SOUTH);
		JButton closeBtn = new JButton("�ر�");
		closeBtn.setToolTipText("�˳���������");
		btn2Panel.add(closeBtn);
		JButton submitBtn = new JButton("����");
		submitBtn.setToolTipText("��Enter��������Ϣ");
		btn2Panel.add(submitBtn);
		//sendPanel.add(btn2Panel, BorderLayout.SOUTH);
		
		//�����û��б����
		JPanel onlineListPane = new JPanel();
		onlineListPane.setLayout(new BorderLayout());
		onlineCountLbl = new JLabel("�����û��б�(1)"); 
		onlineListPane.add(onlineCountLbl, BorderLayout.NORTH);
		
		//��ǰ�û����
		JPanel currentUserPane = new JPanel();
		currentUserPane.setLayout(new BorderLayout());
		currentUserPane.add(new JLabel("��ǰ�û�"), BorderLayout.NORTH);
		
		// �ұ��û��б���һ���ָ�����
		JSplitPane splitPane3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				onlineListPane, currentUserPane);
		splitPane3.setDividerLocation(340);
		splitPane3.setDividerSize(1);
		userPanel.add(splitPane3, BorderLayout.CENTER);
		
		
		
		//��ȡ�����û�������
		DataBuffer.onlineUserListModel = new OnlineUserListModel(DataBuffer.onlineUsers);
		//�����û��б� 
		onlineList = new JList(DataBuffer.onlineUserListModel);
		onlineList.setCellRenderer(new MyCellRenderer());
		//����Ϊ��ѡģʽ
		onlineList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		onlineListPane.add(new JScrollPane(onlineList,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		
		//��ȡ�����б�
		List<User> users = new UserService().FindFriends(id);
		//�Ѻ��ѷŵ��ұ��б�����
		/*for (User user : users) {
			DataBuffer.myFriendsTableModel.add(
					new String[]{String.valueOf(user.getUserid()), 
									user.getUsername(), 
									String.valueOf(user.getUsersex())});
			DataBuffer.myFriendsTableModel = (List<User>) new MyFriendsTableModel(user);
		}*/
		
		
		
		//��ǰ�û���ϢLabel
		currentUserLbl = new JLabel();
		currentUserPane.add(currentUserLbl);
		
		
		///////////////////////ע���¼�������/////////////////////////
		//�رմ���
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				logout();
			}
		});
		
		//�رհ�ť���¼�
		closeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				logout();
			}
		});
		
		//ѡ��ĳ���û�˽��
		rybqBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(rybqBtn.isSelected()){
					User selectedUser = (User)onlineList.getSelectedValue();
					if(null == selectedUser){
						otherInfoLbl.setText("��ǰ״̬��˽��(ѡ�������û��б���ĳ���û�����˽��)...");
					}else if(DataBuffer.currentUser.getUserid() == selectedUser.getUserid()){
						otherInfoLbl.setText("��ǰ״̬������������?...ϵͳ������");
					}else{
						otherInfoLbl.setText("��ǰ״̬���� "+ selectedUser.getUsername()
								+"(" + selectedUser.getUserid() + ") ˽����...");
					}
				}else{
					otherInfoLbl.setText("��ǰ״̬��Ⱥ��...");
				}
			}
		});
		//ѡ��ĳ���û�
		onlineList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				User selectedUser = (User)onlineList.getSelectedValue();
				if(rybqBtn.isSelected()){
					if(DataBuffer.currentUser.getUserid() == selectedUser.getUserid()){
						otherInfoLbl.setText("��ǰ״̬������������?...ϵͳ������");
					}else{
						otherInfoLbl.setText("��ǰ״̬���� "+ selectedUser.getUsername()
								+"(" + selectedUser.getUserid() + ") ˽����...");
					}
				}
			}
		});
		
		//�����ı���Ϣ
		sendArea.addKeyListener(new KeyAdapter(){   
			public void keyPressed(KeyEvent e){   
				if(e.getKeyCode() == Event.ENTER){   
					sendTxtMsg();
				}
			}   
		});
		submitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				sendTxtMsg();
			}
		});
		
		//������
		shakeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				sendShakeMsg();
			}
		});
		
		//�����ļ�
		sendFileBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				sendFile();
			}
		});
		
		this.loadData();  //���س�ʼ����
		
		mfl = new MyFriendsListUI(id);
		mfl.setVisible(false);
		adui = new AddUsersUI(id);
		adui.setVisible(false);
	}
	
						/**  �����¼� */
	
	
			//	��Ӻ��ѱ�ǩ��ǩ���¼�
	private void MyFriend_LabelmouseClicked(MouseEvent e) 
	{
		if (e.getButton()==MouseEvent.BUTTON1)
		{
			mfl.setVisible(true);
		}
	}
	
	private void AddFriend_LabelmouseClicked(MouseEvent e) 
	{
		if (e.getButton()==MouseEvent.BUTTON1)
		{
			adui.setVisible(true);
		}
	}

	/**  �������� */
	public void loadData(){
		//���ص�ǰ�û�����
		if(null != DataBuffer.currentUser){ 
			currentUserLbl.setIcon(
					new ImageIcon("images/" + DataBuffer.currentUser.getHead() + ".png"));
			currentUserLbl.setText(DataBuffer.currentUser.getUsername()
					+ "(�ǳƣ�" + DataBuffer.currentUser.getNikename() + ")");
		}
		//���������û��б�
		onlineCountLbl.setText("�����û��б�("+ DataBuffer.onlineUserListModel.getSize() +")");
		
		
		//��ȡ��������
		List<User> users = new UserService().FindFriends(DataBuffer.currentUser.getUserid());
		
		
		//����������������Ϣ���߳�
		new ClientThread(this).start();
	}
	
	/** ������ */
	public void sendShakeMsg(){
		User selectedUser = (User)onlineList.getSelectedValue();
		if(null != selectedUser){
			if(DataBuffer.currentUser.getUserid() == selectedUser.getUserid()){
				JOptionPane.showMessageDialog(ChatFrame.this, "���ܸ��Լ�������!",
					"���ܷ���", JOptionPane.ERROR_MESSAGE);
			}else{
				Message msg = new Message();
				msg.setFromUser(DataBuffer.currentUser);
				msg.setToUser(selectedUser);
				msg.setSendTime(new Date());
				
				DateFormat df = new SimpleDateFormat("HH:mm:ss");
				StringBuffer sb = new StringBuffer();
				sb.append(" ").append(msg.getFromUser().getUsername())
					.append("(").append(msg.getFromUser().getUserid()).append(") ")
					.append(df.format(msg.getSendTime()))
					.append("\n  ��").append(msg.getToUser().getUsername())
					.append("(").append(msg.getToUser().getUserid()).append(") ")
					.append("������һ�����ڶ���\n");
				msg.setMessage(sb.toString());
				
				Request request = new Request();
				request.setAction("shake");
				request.setAttribute("msg", msg);
				try {
					ClientUtil.sendTextRequest2(request);
				} catch (IOException e) {
					e.printStackTrace();
				}
				ClientUtil.appendTxt2MsgListArea(msg.getMessage());
				new JFrameShaker(ChatFrame.this).startShake();
			}
		}else{
			JOptionPane.showMessageDialog(ChatFrame.this, "����Ⱥ������!",
					"���ܷ���", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/** �����ı���Ϣ */
	public void sendTxtMsg(){
		String content = sendArea.getText();
		
		if ("".equals(content)) { //������
			JOptionPane.showMessageDialog(ChatFrame.this, "���ܷ��Ϳ���Ϣ!",
					"���ܷ���", JOptionPane.ERROR_MESSAGE);
		} else { //����
			User selectedUser = (User)onlineList.getSelectedValue();
			if(null != selectedUser && 
					DataBuffer.currentUser.getUserid() == selectedUser.getUserid()){
				JOptionPane.showMessageDialog(ChatFrame.this, "���ܸ��Լ�������Ϣ!",
						"���ܷ���", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			//���������ToUser��ʾ˽�ģ�����Ⱥ��
			Message msg = new Message();
			if(rybqBtn.isSelected()){  //˽��
				if(null == selectedUser){
					JOptionPane.showMessageDialog(ChatFrame.this, "û��ѡ��˽�Ķ���!",
							"���ܷ���", JOptionPane.ERROR_MESSAGE);
					return;
				}else{
					msg.setToUser(selectedUser);
				}
			}
			msg.setFromUser(DataBuffer.currentUser);
			msg.setSendTime(new Date());
				
			DateFormat df = new SimpleDateFormat("HH:mm:ss");
			StringBuffer sb = new StringBuffer();
			sb.append(" ").append(df.format(msg.getSendTime())).append(" ")
				.append(msg.getFromUser().getUsername())
				.append("(").append(msg.getFromUser().getUserid()).append(") ");
			if(!this.rybqBtn.isSelected()){//Ⱥ��
				if(null == selectedUser){
					sb.append("�Դ��˵");
				}else{
					sb.append("��").append(selectedUser.getUsername())
						.append("(").append(selectedUser.getUserid()).append(")")
						.append("˵");
				}
			}
			sb.append("\n  ").append(content).append("\n");
			msg.setMessage(sb.toString());
			
			Request request = new Request();
			request.setAction("chat");
			request.setAttribute("msg", msg);
			try {
				ClientUtil.sendTextRequest2(request);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//��Ϣ��¼�ŵ����ݿ�
			
			QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
			String sql = "select * from imchatlog";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
			String ssql = "insert into imchatlog values(?,?,?,?,?,?);";
			List<ImChatLog> ImChatLogs = null;
			try {
				ImChatLogs = (List<ImChatLog>) qr.query(sql, new BeanListHandler(ImChatLog.class));
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			int count=1;
			for(ImChatLog imChatLog:ImChatLogs) {
				count++;
			}
			if(null == selectedUser){
				try {
					qr.update(ssql,count,DataBuffer.currentUser.getUserid(),
							0,sdf.format(new Date()).toString(),sb.toString(),0);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}else {
				Long touser = ((User) onlineList.getSelectedValue()).getUserid();
				try {
					qr.update(ssql,count,DataBuffer.currentUser.getUserid(),
							touser,sdf.format(new Date()).toString(),sb.toString(),1);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			
			
			
			//JTextArea�а���Enter��ʱ��������ݲ��ص�����
			InputMap inputMap = sendArea.getInputMap();
			ActionMap actionMap = sendArea.getActionMap();
			Object transferTextActionKey = "TRANSFER_TEXT";
			inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),transferTextActionKey);
			actionMap.put(transferTextActionKey,new AbstractAction() {
				private static final long serialVersionUID = 7041841945830590229L;
				public void actionPerformed(ActionEvent e) {
					sendArea.setText("");
					sendArea.requestFocus();
				}
			});
			sendArea.setText("");
			ClientUtil.appendTxt2MsgListArea(msg.getMessage());
		}
	}
	
	/** �����ļ� */
	private void sendFile() {
		User selectedUser = (User)onlineList.getSelectedValue();
		if(null != selectedUser){
			if(DataBuffer.currentUser.getUserid() == selectedUser.getUserid()){
				JOptionPane.showMessageDialog(ChatFrame.this, "���ܸ��Լ������ļ�!",
					"���ܷ���", JOptionPane.ERROR_MESSAGE);
			}else{
				JFileChooser jfc = new JFileChooser();
				if (jfc.showOpenDialog(ChatFrame.this) == JFileChooser.APPROVE_OPTION) {
					File file = jfc.getSelectedFile();
					sendFile = new FileInfo();
					sendFile.setFromUser(DataBuffer.currentUser);
					sendFile.setToUser(selectedUser);
					try {
						sendFile.setSrcName(file.getCanonicalPath());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					sendFile.setSendTime(new Date());
					
					Request request = new Request();
					request.setAction("toSendFile");
					request.setAttribute("file", sendFile);
					try {
						ClientUtil.sendTextRequest2(request);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					ClientUtil.appendTxt2MsgListArea("���ļ���Ϣ���� "
								+ selectedUser.getUsername() + "("
								+ selectedUser.getUserid() + ") �����ļ� ["
								+ file.getName() + "]���ȴ��Է�����...\n");
				}
			}
		}else{
			JOptionPane.showMessageDialog(ChatFrame.this, "���ܸ����������û������ļ�!",
					"���ܷ���", JOptionPane.ERROR_MESSAGE);
		}
	}

	/** �رտͻ��� */
	private void logout() {
		int select = JOptionPane.showConfirmDialog(ChatFrame.this,
				"ȷ���˳���\n\n�˳������ж��������������!", "�˳�������",
				JOptionPane.YES_NO_OPTION);
		if (select == JOptionPane.YES_OPTION) {
			Request req = new Request();
			req.setAction("exit");
			req.setAttribute("user", DataBuffer.currentUser);
			try {
				ClientUtil.sendTextRequest(req);
			} catch (IOException ex) {
				ex.printStackTrace();
			}finally{
				System.exit(0);
			}
		}else{
			this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		}
	}
	public void LogOutMe(){  
		JOptionPane.showMessageDialog(null,"��������Ա�˳�Ⱥ��!");
		
		ChatFrame.this.dispose();
		/*Request req = new Request();
		req.setAction("exit");
		req.setAttribute("user", DataBuffer.currentUser);
		try {
			ClientUtil.sendTextRequest(req);
		} catch (IOException ex) {
			ex.printStackTrace();
		}finally{
			//System.exit(0);
			ChatFrame.this.dispose();
		}*/
    }  
	private JLabel MyFriend_Label;
	private JLabel AddFriend_Label;
}