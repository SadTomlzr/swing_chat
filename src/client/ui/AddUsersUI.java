package client.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;



import common.model.entity.User;
import server.model.service.UserService;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class AddUsersUI extends JFrame
{
	
	private JTable tab;
	private JScrollPane scp;
	private JPanel contentPane;
	private JTextField findFriendText;
	private JButton btnNewButton;
	private JPanel resultPanel;
	private User user;
	Pattern pattern;
	Matcher isNum;
	private JPanel panel;
	private JButton button;
	int flag = 0;
	/**
	 * Launch the application.
	 */
	public AddUsersUI(long id)
	{
		init(id);
	}

	/**
	 * Create the frame.
	 * @param id 
	 * @return 
	 */
	public void init(long id)
	{
		setTitle("���/��������");
		setLocationRelativeTo(null);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		this.scp = new JScrollPane();
		this.scp.setBounds(0, 0, 391, 411);
		
		JLabel label = new JLabel("�������ѣ�");
		
		label.setFont(new Font("����", Font.BOLD, 14));
		
		findFriendText = new JTextField();
		findFriendText.setColumns(10);
		
		JLabel label_1 = new JLabel("(������Ҫ�������˺�)");
		
		btnNewButton = new JButton("����");
		btnNewButton.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent arg0) {
				btnNewButtonActionPerformed();
			}
		});
		
		resultPanel = new JPanel();
		
		panel = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(label)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(label_1)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(findFriendText, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(btnNewButton))))
						.addComponent(resultPanel, GroupLayout.PREFERRED_SIZE, 423, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 425, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(label)
						.addComponent(findFriendText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(label_1)
					.addPreferredGap(ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
					.addComponent(resultPanel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
					.addGap(3)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
		);
		panel.setLayout(null);
		
		button = new JButton("��Ӻ���");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				buttonActionPerformed(id);
			}
		});
		button.setFont(new Font("����", Font.BOLD, 14));
		button.setBounds(98, 0, 93, 25);
		button.setVisible(false);
		panel.add(button);
		GroupLayout gl_panel = new GroupLayout(resultPanel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 423, Short.MAX_VALUE)
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 150, Short.MAX_VALUE)
		);
		resultPanel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
		
		/*Object[][] friendsInfo = new Object[1][2];
			friendsInfo[0][0] = user.getUserid();
			friendsInfo[0][1] = user.getUsername();
			
		String[] title = {"id","����"};
		this.tab = new JTable(friendsInfo,title);
		this.tab.getTableHeader();
		this.scp.setViewportView(tab);*/
	}
	
	//�¼�����
	public void btnNewButtonActionPerformed() {
		pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(findFriendText.getText());

		if (btnNewButton.getActionCommand()=="����") 
		{
			if (findFriendText.getText().isEmpty())					//����û��Ƿ������˶���
			{
				JOptionPane.showMessageDialog(null,"�������˺ź��ٲ��ң�", "δ�����˺�",JOptionPane.WARNING_MESSAGE);
			}
			else if (!isNum.matches()) 								//�������ĸ�ʽ
			{
				JOptionPane.showMessageDialog(null,"��������ȷ��ʽ���˺ţ�", "�˺Ÿ�ʽ����",JOptionPane.WARNING_MESSAGE);
				button.setVisible(false);
			}
			else
			{
				user = new UserService().loadUser(Long.parseLong(findFriendText.getText()));//��ȷ����Ͳ����û�
				
				if (user == null)
				{
					JOptionPane.showMessageDialog(null,"δ�ҵ��ú��ѣ��볢������������", "����ʧ��",JOptionPane.WARNING_MESSAGE);
					button.setVisible(false);
				}else
				{
					Object[][] friendsInfo = new Object[1][2];
					friendsInfo[0][0] = user.getUserid();
					friendsInfo[0][1] = user.getUsername();
				
					String[] title = {"id","����"};
					this.tab = new JTable(friendsInfo,title);
					this.tab.getTableHeader();
					this.scp.setViewportView(tab);
					resultPanel.add(scp);
					button.setVisible(true);
				}
			}
			
		}
		
		
	}
	
	
	public void buttonActionPerformed(long id) {
		
		UserService userService = new UserService();
		long toid = user.getUserid();
		flag = userService.addFriends(id, toid);
		if (flag==1) 
		{
			JOptionPane.showMessageDialog(null, "��Ӻ��ѳɹ�!","��ӽ��",JOptionPane.WARNING_MESSAGE);
			button.setVisible(false);
		}else if (flag==2) {
			JOptionPane.showMessageDialog(null, "���û��Ѿ������ĺ���!","��ӽ��",JOptionPane.WARNING_MESSAGE);
		}else if (flag==0) {
			JOptionPane.showMessageDialog(null, "��Ӻ���ʧ��!","��ӽ��",JOptionPane.WARNING_MESSAGE);
		}else if (flag == 3) {
			JOptionPane.showMessageDialog(null, "�Ҹ����Լ�������....������","��ӽ��",JOptionPane.WARNING_MESSAGE);
		}
	}
}