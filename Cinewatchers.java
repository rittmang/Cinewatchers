import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
 //add new user (Register) in SQL table: insert into users (username,password,name) values ('ritom','ritom','Ritom');
public class Cinewatchers{
    String msg = "";
    JTextField txtUsername = null;
    JPasswordField txtPassword = null;
    JFrame frame;
    Statement stmt=null;//SQL
    ResultSet rs=null;//SQL
     
    public static void main(String[] args){
        Cinewatchers gui = new Cinewatchers();
        gui.go();
    }
    public void go(){
        frame = new JFrame("Cinewatchers");
        frame.setTitle("Cinewatchers");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        JLabel lblUsername = new JLabel("Username:");   
        JLabel lblPassword = new JLabel("Password:");
        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);
        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(new LoginListener());
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new CancelListener());
        JButton btnRegister = new JButton("Register");//basically add new user
        btnRegister.addActionListener(new RegisterListener());
     
 
        panel.add(lblUsername);
        panel.add(txtUsername);
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(btnLogin);
        panel.add(btnCancel);
        panel.add(btnRegister);         
        frame.getContentPane().add(BorderLayout.CENTER,panel);
        frame.getRootPane().setDefaultButton(btnLogin);
        ImageIcon ficon=new ImageIcon("/home/ritom/Desktop/Java/DBMS/icon_cw.png");
        frame.setIconImage(ficon.getImage());
 
        //frame.setSize(300,300);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        //frame.setUndecorated(true);
        frame.setVisible(true);
 
 
    }
 
    public class LoginListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            try{
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance(); 
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/cinewatchers","ritom","123123123");
            stmt=con.createStatement();
            rs=stmt.executeQuery("SELECT * FROM users");
            String password=new String(txtPassword.getPassword());//get from window
            int signed_in=0;

            while(rs.next())
            {
                 if((txtUsername.getText()).equals(rs.getString(2)))
                 {
                     if(password.equals(rs.getString(3)))
                     {
                         String uid=rs.getString(1);//get uid
                         String username=rs.getString(2);
                         String name=rs.getString(4);
                         msg="Login Granted";
                         frame.getContentPane().removeAll();//removes all previous components, get a blank screen
                         frame.getContentPane().repaint();
                         Home h=new Home();
                         h.go(frame,uid,username,name);
                         signed_in=1;
                     }
                 } 
            }
            if(signed_in==0)
                JOptionPane.showMessageDialog(null,"Incorrect credentials","Alert",JOptionPane.INFORMATION_MESSAGE);
            con.close();
            }
            catch(SQLException e)
            {
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("VendorError: " + e.getErrorCode());
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
            finally{
                if(rs!=null)
            {
                try{
                    rs.close();
                }
                catch(SQLException sqlEx)
                {

                }
                rs=null;
            }
            if(stmt!=null)
            {
                try{
                    stmt.close();
                }
                catch(SQLException sqlEx)
                {

                }
                stmt=null;
            }
            }
            
                              
        }
    }
    
    public class CancelListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            txtUsername.setText("");
            txtPassword.setText("");
            txtUsername.requestFocus();
        }
    }
    public class RegisterListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            txtUsername.setText("");
            txtPassword.setText("");
            txtUsername.requestFocus();
            Register r=new Register();
            r.go();
        }
    }
}

class Home
{
    
    String user_id="",user_name="",name="";
    JPanel p;JLabel rev;String all_reviews="";
    JFrame new_frame;int already_shown=0;
    public void go(JFrame frame,String uid,String username,String n)//uses same frame from Cinewatchers class
    {
        user_id=uid;
        user_name=username;
        name=n;
        new_frame=frame;
        JLabel l=new JLabel();
        l.setText("Welcome to Cinewatchers, "+name);
        l.setHorizontalAlignment(JLabel.CENTER);
        p=new JPanel();
        p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        p.add(l);
        new_frame.add(p);
        new_frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        JButton btnShowReviews = new JButton("Show my reviews");
        btnShowReviews.addActionListener(new ShowReviewsListener());
        p.add(btnShowReviews);
        //System.out.println(all_reviews);
        //rev.setText(all_reviews);
        //p.add(rev);
        new_frame.getContentPane().add(BorderLayout.CENTER,p);
        new_frame.setVisible(true);

    }
    public class ShowReviewsListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            Statement stmt=null;
            ResultSet rs=null;
            if(already_shown==1)
            {
                JOptionPane.showMessageDialog(null,"All reviews done","Alert",JOptionPane.INFORMATION_MESSAGE);
            }
            else{
            try{
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance(); 
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/cinewatchers","ritom","123123123");
            stmt=con.createStatement();
            rs=stmt.executeQuery("SELECT * FROM reviews WHERE uid="+user_id);
            
            rev=new JLabel();
            

            while(rs.next())
            {
                all_reviews+=rs.getString(3)+"\n\n";    
            }
            
            rev.setText("<html>"+all_reviews.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n","<br />")+"</html>");
            rev.setHorizontalAlignment(JLabel.CENTER);
            rev.setVerticalAlignment(JLabel.CENTER);
            Border border = BorderFactory.createLineBorder(Color.BLACK);
            rev.setBorder(border);
            p.add(rev);
            new_frame.validate();
            new_frame.repaint();
            already_shown=1;
            con.close();
            }
            catch(SQLException e)
            {
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("VendorError: " + e.getErrorCode());
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
            finally{
                if(rs!=null)
            {
                try{rs.close();}
                catch(SQLException sqlEx){}
                rs=null;
            }
            if(stmt!=null)
            {
                try{stmt.close();}
                catch(SQLException sqlEx){}
                stmt=null;
            }
            }
        }
           // JOptionPane.showMessageDialog(null,msg,"Alert",JOptionPane.INFORMATION_MESSAGE);
                              
        }
    }

}

class Register
{
    String msg = "";
    JTextField txtUsername = null;
    JPasswordField txtPassword = null;
    JTextField txtName=null;
    JFrame frame;
    Statement stmt=null;//SQL
   // ResultSet rs=null;//SQL
    public void go()
    {
        frame = new JFrame("Cinewatchers-Register");
        frame.setTitle("Cinewatchers-Register");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        JLabel lblUsername = new JLabel("Enter new username: [max 20]");   
        JLabel lblPassword = new JLabel("Enter new password: [max 20]");
        JLabel lblName=new JLabel("Enter name:");
        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);
        txtName=new JTextField(30);
        JButton btnRegister = new JButton("Register");
        btnRegister.addActionListener(new RegisterListener());
        JButton btnCancel = new JButton("Clear");
        btnCancel.addActionListener(new CancelListener());
        
     
 
        panel.add(lblUsername);
        panel.add(txtUsername);
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(lblName);
        panel.add(txtName);

        panel.add(btnRegister);
        panel.add(btnCancel);         
        frame.getContentPane().add(BorderLayout.CENTER,panel);
        frame.getRootPane().setDefaultButton(btnRegister);
        ImageIcon ficon=new ImageIcon("/home/ritom/Desktop/Java/DBMS/icon_cw.png");
        frame.setIconImage(ficon.getImage());
 
        frame.setSize(300,300);
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        //frame.setUndecorated(true);
        frame.setVisible(true);
    }

    public class RegisterListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            try{
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance(); 
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/cinewatchers","ritom","123123123");
            stmt=con.createStatement();
            
            String password=new String(txtPassword.getPassword());//get from window
            stmt.executeUpdate("INSERT INTO users (uid,username,password,name) VALUES(null,'"+txtUsername.getText()+"','"+password+"','"+txtName.getText()+"');");
            
            con.close();
            frame.dispose();
            }
            catch(SQLException e)
            {
                System.out.println("SQLException: " + e.getMessage());
                
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("VendorError: " + e.getErrorCode());
                
                if(e.getErrorCode()==1062)
                 JOptionPane.showMessageDialog(null,"Username exists","Alert",JOptionPane.ERROR_MESSAGE);
                else
                JOptionPane.showMessageDialog(null,"Error occurred. Please try again.","Alert",JOptionPane.ERROR_MESSAGE);
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
            finally{
                
            if(stmt!=null)
            {
                try{
                    stmt.close();
                }
                catch(SQLException sqlEx)
                {

                }
                stmt=null;
            }
            }
            
                              
        }
    }
    public class CancelListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            txtUsername.setText("");
            txtPassword.setText("");
            txtName.setText("");
            txtUsername.requestFocus();
        }
    }
    
}