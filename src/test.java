import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import common.model.entity.User;
import common.util.DataSourceUtils;

public class test {

	public static void main(String[] args) throws SQLException {
		/*CREATE TABLE `imuserlog` (
				  `logid` int(11) NOT NULL AUTO_INCREMENT,
				  `logtime` datetime DEFAULT NULL,
				  `loguserid` int(11) DEFAULT NULL,
				  `loguser` varchar(20) DEFAULT NULL,
				  `logip` varchar(15) DEFAULT NULL,
				  `logcontent` varchar(100) DEFAULT NULL,
				  PRIMARY KEY (`logid`)
				) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "insert into user values(?,?,?,?,?,?);";
		try {
			qr.update(sql, user.getUsername(), user.getUserpwd(),
			        user.getUsertruename(), user.getUsersex(), user.getUserisused());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ע��ʧ��");
		}
*/
		
		/**
		 * �������
		 */
		/*QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "insert into imuserlog values(?,?,?,?,?,?);";
		try {
			qr.update(sql,1,2018-12-5,4,123,123,123);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ע��ʧ��");
		}*/
		/*QueryRunner qrr = new QueryRunner(DataSourceUtils.getDataSource());
	    String sqll = "select * from imusers";
	    System.out.println(qrr.query(sqll, new BeanListHandler<>(User.class)));*/
		
		/**
		 * ���ϲ�ѯ
		 */
		/*List<User> users =null;
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select userid,username,userpwd,usertruename,usersex,userisused,userloginflag,userloginip,head from imusers a inner join imfriends b on a.userid = b.touser where b.fromuser = ?;";
		users = qr.query(sql, new BeanListHandler<>(User.class),4);
		int count = 0;
		
		for(User user : users){
			System.out.println(count);
			System.out.println(user);
			count++;
		}*/
		/*QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "insert into imusers values(?,?)";
		qr.update(sql,4,6);
		*//**
		 * ��ȡ��ǰʱ��
		 *//*
		 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		 System.out.println(df.format(new Date()).toString());// new Date()Ϊ��ȡ��ǰϵͳʱ��
		 
*/	
		
		        try{
		            InetAddress addr;
		            Socket sock=new Socket("47.94.144.7",9000); //���ӵ��ٶȷ�������80�˿�
		            addr=sock.getInetAddress();
		            System.out.println("���ӵ�"+addr);
		            sock.close();
		        }catch(IOException e){
		            System.out.println("�޷�����");
		            System.out.println(e);
		        }
		    }
		

}
