package client.ui;

import java.awt.event.*;
import java.io.IOException;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import client.DataBuffer;
import client.util.ClientUtil;
import common.model.entity.*;

/* ע�ᴰ�� */
public class RegisterFrame extends JFrame {
	private static final long serialVersionUID = -768631070458723803L;
	private JPasswordField pwdFld;
	private JPasswordField pwd2Fld;
	private JTextField nickname;
	private JTextField PhoneNum;
	private JTextField age;
	private JTextField username;
	private JTextField usertype;
	private JComboBox head;
	private JRadioButton sex0;
	private JRadioButton sex1;
	private JButton ok;
	private JButton reset;
	private JButton cancel;
	
	public RegisterFrame(){
		this.init();
		setVisible(true);
	}
	
	public void init(){
		this.setTitle("ע��JQ���˺�");//���ñ���
		setBounds((DataBuffer.screenSize.width - 430)/2, 
				(DataBuffer.screenSize.height - 500)/2, 
				430, 350);
		getContentPane().setLayout(null);
		setResizable(false);

		JLabel label = new JLabel("�ǳ�:"); //label��ʾ
		label.setBounds(24, 212, 59, 17);
		getContentPane().add(label);
		
		nickname = new JTextField(); //�ǳ�
		nickname.setBounds(90, 212, 110, 22);
		getContentPane().add(nickname);
		
		JLabel label5 = new JLabel("����:*");
		label5.setBounds(24, 72, 50, 17);
		getContentPane().add(label5);
		
		JLabel label3 = new JLabel("ȷ������:*");
		label3.setBounds(24, 107, 65, 17);
		getContentPane().add(label3);
		
		pwdFld = new JPasswordField();//�����
		pwdFld.setBounds(90, 70, 110, 22);
		getContentPane().add(pwdFld);

		pwd2Fld = new JPasswordField();
		pwd2Fld.setBounds(90, 105, 110, 22);
		getContentPane().add(pwd2Fld);
		
		JLabel label7 = new JLabel("�ֻ���*");
		label7.setBounds(24, 142, 65, 17);
		getContentPane().add(label7);
		
		JLabel label8 = new JLabel("����*");
		label8.setBounds(24, 177, 65, 17);
		getContentPane().add(label8);
		
		PhoneNum = new JTextField(); //�ֻ���
		PhoneNum.setBounds(90, 142, 110, 22);
		getContentPane().add(PhoneNum);
		
		age = new JTextField(); //����
		age.setBounds(90, 177, 110, 22);
		getContentPane().add(age);
		
		JLabel labe9 = new JLabel("�˺�*:"); //label��ʾ24, 36
		labe9.setBounds(24, 36, 59, 17);
		getContentPane().add(labe9);
		
		username = new JTextField(); //�˺�90, 34, 110, 22
		username.setBounds(90, 34, 110, 22);
		getContentPane().add(username);

		JLabel label4 = new JLabel("�Ա�:");
		label4.setBounds(230, 36, 50, 17);
		getContentPane().add(label4);
		
		sex1 = new JRadioButton("��",true);
		sex1.setBounds (290, 36, 60, 25);
		getContentPane().add(sex1);
		sex0 = new JRadioButton("Ů");
		sex0.setBounds(360, 36, 60, 25);
		getContentPane().add(sex0);
		ButtonGroup buttonGroup = new ButtonGroup();//��ѡ��ť��
		buttonGroup.add(sex0);
		buttonGroup.add(sex1);
		
		JLabel label6 = new JLabel("ͷ��:");
		label6.setBounds(230, 72, 45, 17);
		getContentPane().add(label6);
		
		head = new JComboBox();//�����б�ͼ��
		head.setBounds(290, 70, 65, 45);
		head.setMaximumRowCount(5);
		for (int i = 0; i < 11; i++) {
			head.addItem(new ImageIcon("images/" + i + ".png"));
			//ͨ��ѭ�����ͼƬ��ע��ͼƬ����Ҫȡ��1,2,3,4,5,��	
		}
		head.setSelectedIndex(0);
		getContentPane().add(head);
		
		JLabel labe10 = new JLabel("[�û�����]*:"); //label��ʾ
		labe10.setBounds(230, 150, 80, 45);
		getContentPane().add(labe10);
		
		usertype = new JTextField(); //�ǳ�
		usertype.setBounds(230, 185, 110, 22);
		getContentPane().add(usertype);
		usertype.setText("user");
		
		//��ť
		ok = new JButton("ȷ��");
		ok.setBounds(27, 260, 60, 28);
		getContentPane().add(ok);

		reset = new JButton("����");
		reset.setBounds(123, 260, 60, 28);
		getContentPane().add(reset);

		cancel = new JButton("ȡ��");
		cancel.setBounds(268, 260, 60, 28);
		getContentPane().add(cancel);
		
		//////////////////////ע���¼�������////////////////////////
		//ȡ����ť�����¼�����
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent event) {
				RegisterFrame.this.dispose();
			}
		});
		//�رմ���
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				RegisterFrame.this.dispose();
			}
		});
		
		// ���ð�ť�����¼�����
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				nickname.setText("");
				pwdFld.setText("");
				pwd2Fld.setText("");
				username.setText("");
				age.setText("");
				PhoneNum.setText("");
				usertype.setText("user");
				nickname.requestFocusInWindow();//�û�����ý���
			}
		});
		
		//ȷ�ϰ�ť�����¼�����
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
				String regex_num_A_Z = "^[0-9a-zA-Z]{1,}$";
				String phoneNum = PhoneNum.getText();
				String password = new String(pwdFld.getPassword());
				if (pwdFld.getPassword().length==0 || pwd2Fld.getPassword().length==0 
						|| PhoneNum.getText().equals("") || age.getText().equals("") || username.getText().equals("")) {
					JOptionPane.showMessageDialog(RegisterFrame.this, "�� �� * �� Ϊ��������!");
					return;
				//�ж��û����������Ƿ�Ϊ��
				}else if(phoneNum.length() != 11){
	                System.out.println("�ֻ���ӦΪ11λ��");
	                JOptionPane.showMessageDialog(RegisterFrame.this, "�ֻ���ӦΪ11λ��");
	                return;
	            } else{
		            Pattern p = Pattern.compile(regex);
		            Matcher m = p.matcher(phoneNum);
		            boolean isMatch = m.matches();
		            System.out.println(phoneNum.length());
		            if(isMatch){
		               System.out.println("�����ֻ���" + phoneNum + "����ȷ��ʽ@����@");
		            } else {
		               JOptionPane.showMessageDialog(RegisterFrame.this, "�ֻ��Ÿ�ʽ����ȷ");
		               return;
		            }
	            }
				//��֤����
				Pattern p = Pattern.compile(regex_num_A_Z);
	            Matcher m = p.matcher(password);
	            boolean isMatch = m.matches();
	            if(isMatch){
	               System.out.println("��������" + password + "����ȷ��ʽ@����@");
	            } else {
	               JOptionPane.showMessageDialog(RegisterFrame.this, "���������ʽ����ȷ���������ֺ���ĸ���");
	               return;
	            }
	            if (password.length()>16) {
	            	JOptionPane.showMessageDialog(RegisterFrame.this, "���������ʽ����ȷ��16λ����");
	            	return;
				}
	            if (Integer.valueOf(age.getText()) > 150) {
	            	JOptionPane.showMessageDialog(RegisterFrame.this, "����̫��");
	            	return;
				}
	            if (username.getText().length()>16) {
	            	JOptionPane.showMessageDialog(RegisterFrame.this, "�˺�16λ����");
	            	return;
				}
	            if (!new String(pwdFld.getPassword()).equals(new String(pwd2Fld.getPassword()))) {
					JOptionPane.showMessageDialog(RegisterFrame.this, "�����������벻һ��!");
					pwdFld.setText("");
					pwd2Fld.setText("");
					pwdFld.requestFocusInWindow();
					//�ж����������Ƿ�һ��
					return;
				}
	            System.out.println(usertype.getText().equals("user") || usertype.getText().equals("admin"));
	            System.out.println(usertype.getText().equals("user"));
	            System.out.println(usertype.getText().equals("admin"));
	            if (!(usertype.getText().equals("user") || usertype.getText().equals("admin"))) {
					System.out.println(usertype.getText());
					JOptionPane.showMessageDialog(RegisterFrame.this, "usertype������admin����user");
					return;
				}else{
					User user = new User();
					String type = usertype.getText();
					user.setUserpwd(pwdFld.getText());
					if (nickname.equals("") || nickname == null) {
						user.setNikename("����");
					} else {
				 		user.setNikename(nickname.getText());
					}
					user.setUsername(username.getText());
					char c = sex0.isSelected()?'Ů':'��';
					user.setSex(String.valueOf(c));
					user.setHead(head.getSelectedIndex());
					user.setType(type);
					user.setAge(Integer.valueOf(age.getText()));
					System.out.println(phoneNum);
					Long b = new Long(phoneNum);
					user.setPhoneNumber(b);
					try {
						RegisterFrame.this.registe(user);
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (ClassNotFoundException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
	}
	//ע�᷽��
	private void registe(User user) throws IOException, ClassNotFoundException{
		Request request = new Request();
		request.setAction("userRegiste");
		request.setAttribute("user", user);
		//��ȡ��Ӧ
		Response response = ClientUtil.sendTextRequest(request);
		
		ResponseStatus status = response.getStatus();
		switch(status){
		case OK:
			User user2 = (User)response.getData("user");
			JOptionPane.showMessageDialog(RegisterFrame.this, 
					"��ϲ��������Q��:"+ user2.getUserid() + ",���μ�!!!",
					"ע��ɹ�",JOptionPane.INFORMATION_MESSAGE);
			this.setVisible(false);
			break;
		default:
			JOptionPane.showMessageDialog(RegisterFrame.this, 
					"ע��ʧ�ܣ����Ժ����ԣ�����","�������ڲ�����",JOptionPane.ERROR_MESSAGE);
		}
	}
}