import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
 
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
     
 
        panel.add(lblUsername);
        panel.add(txtUsername);
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(btnLogin);
        panel.add(btnCancel);         
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
                         msg="Login Granted";
                         frame.getContentPane().removeAll();//removes all previous components, get a blank screen
                         frame.getContentPane().repaint();
                         Home h=new Home();
                         h.go(frame);
                         signed_in=1;
                     }
                 } 
            }
            if(signed_in==0)
                msg="Incorrect sign-in.";
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
            JOptionPane.showMessageDialog(null,msg,"Alert",JOptionPane.INFORMATION_MESSAGE);
                              
        }
    }
    public class CancelListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            txtUsername.setText("");
            txtPassword.setText("");
            txtUsername.requestFocus();
        }
    }
}

class Home
{
    
    public void go(JFrame frame)//uses same frame from Cinewatchers class
    {
        JLabel l=new JLabel();
        l.setText("Welcome to Cinewatchers");
        JPanel p=new JPanel();
        p.add(l);
        frame.add(p);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
}
