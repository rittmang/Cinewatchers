import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
 
public class Login{
    String fusername = new String("james3302");
    String fpassword = new String("pass");
    String msg = "";
    JTextField txtUsername = null;
    JTextField txtPassword = null;
    JFrame frame;
     
    public static void main(String[] args){
        Login gui = new Login();
        gui.go();
    }
    public void go(){
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        JLabel lblUsername = new JLabel("Username:");   
        JLabel lblPassword = new JLabel("Password:");
        txtUsername = new JTextField(20);
        txtPassword = new JTextField(20);
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
 
 
        //frame.setSize(300,300);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        //frame.setUndecorated(true);
        frame.setVisible(true);
 
 
    }
 
    public class LoginListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            
            if(fusername.equals(txtUsername.getText())){
                if(fpassword.equals(txtPassword.getText())){
                   
                    msg = "Login Granted!";
                     frame.getContentPane().removeAll();//removes all previous components, get a blank screen
                     frame.getContentPane().repaint();
                     Home h=new Home();
                     h.beginning();
                }
                else{
                    msg = "Username/Password Error";
                }
            }else{
                msg = "Account does not exist";
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
    JFrame home_frame;
    public static void beginning()
    {
        Home h=new Home();
        h.go();
    }
    public void go()
    {
        home_frame=new JFrame();
    }
}
