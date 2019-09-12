import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.Box;
import javax.imageio.ImageIO;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
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
        JButton btnAdmin = new JButton("I'm an Admin");
        btnAdmin.addActionListener(new AdminListener());     
 
        panel.add(lblUsername);
        panel.add(txtUsername);
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(btnLogin);
        panel.add(btnCancel);
        panel.add(btnRegister);
        panel.add(btnAdmin);
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
            //Connection con=DriverManager.getConnection("jdbc:mysql://rightonrittman.mysql.database.azure.com:3306/cinewatchers?useSSL=true&requireSSL=false","jumperwire@rightonrittman","123123123a!");
            //Connection con=DriverManager.getConnection("jdbc:mysql://cinewatchers2.mysql.database.azure.com:3306/cinewatchers?useSSL=true&requireSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","thisissmall@cinewatchers2","123123123a!");
            
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
                         System.out.println("Logged In");
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
                e.printStackTrace();
                System.out.println("Something went wrong");
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
    public class AdminListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            //txtUsername.setText("");
            //txtPassword.setText("");
            //txtUsername.requestFocus();
            CineAdmin r=new CineAdmin();
            frame.dispose();
            r.go();
        }
    }
}

class Home
{
    
    String user_id="",user_name="",name="";
    JPanel p;JLabel rev;String all_reviews="",all_movies="";
    JFrame new_frame;int already_shown=0;
    public void go(JFrame frame,String uid,String username,String n)//uses same frame from Cinewatchers class
    {
        user_id=uid;
        user_name=username;
        name=n;
        new_frame=frame;
        Box box = Box.createHorizontalBox();
        box.setBorder(new EmptyBorder(0, 0, 0, 0));
        Box box1 = Box.createHorizontalBox();
        box1.setBorder(new EmptyBorder(0, 0, 0, 0));
        JLabel l=new JLabel();
        l.setText("Welcome to Cinewatchers, "+name);
        //l.setHorizontalAlignment(JLabel.CENTER);
        //l.setAlignmentX(p.CENTER_ALIGNMENT);
        p=new JPanel();
        
        //p.setBorder(new EmptyBorder(new Insets(100, 150, 100, 150)));
        //p.setBorder(BorderFactory.createTitledBorder("Home"));
        TitledBorder border = new TitledBorder("Home");
        border.setTitleJustification(TitledBorder.CENTER);
        border.setTitlePosition(TitledBorder.TOP);
        p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        p.setBorder(border);
        p.add(Box.createRigidArea(new Dimension(0, 40)));
        //p.add(l);
        box1.add(l);
        p.add(box1);
        p.add(Box.createRigidArea(new Dimension(0, 30)));
        
        JButton btnShowReviews = new JButton("Show my reviews");
        btnShowReviews.addActionListener(new ShowReviewsListener());
        
        JButton btnShowMovies = new JButton("Show movie database");
        btnShowMovies.addActionListener(new ShowMoviesListener());
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(new RefreshListener());
        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(new LogoutListener());
        
        //box.add(l);
        box.add(btnShowReviews);
        box.add(Box.createRigidArea(new Dimension (50,10)));
        box.add(btnShowMovies);
        box.add(Box.createRigidArea(new Dimension (50,10)));
        box.add(btnRefresh);
        box.add(Box.createRigidArea(new Dimension (50,10)));
        box.add(btnLogout);
        p.add(box);
        p.add(Box.createRigidArea(new Dimension(0, 80)));
        
        JScrollPane pane = new JScrollPane(p,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        pane.getVerticalScrollBar().setUnitIncrement(16);
        new_frame.add(pane);
        new_frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
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
            rs=stmt.executeQuery("SELECT * FROM reviews WHERE userid="+user_id);
            
            rev=new JLabel();
            

            while(rs.next())
            {
                all_reviews+=rs.getString(5)+"\n\n";    
            }
            
            rev.setText("<html>"+all_reviews.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n","<br />")+"</html>");
            rev.setHorizontalAlignment(JLabel.CENTER);
            rev.setVerticalAlignment(JLabel.CENTER);
            Border border = BorderFactory.createLineBorder(Color.BLACK);
            rev.setBorder(border);
            Box box = Box.createHorizontalBox();box.add(rev);
            p.add(box);
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
           
                              
        }
    }
    public class ShowMoviesListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            Statement stmt=null,stmt2=null;
            ResultSet rs=null,rs2=null;
            if(already_shown==1)
            {
                already_shown=0;
                //p.removeAll();
                p = new JPanel();
                new_frame.revalidate();new_frame.repaint();
                new_frame.removeAll();
                new_frame.dispose();
                Home h = new Home();
                
                JFrame q = new JFrame("Cinewatchers Administrator Window");
                ImageIcon ficon=new ImageIcon("/home/ritom/Desktop/Java/DBMS/icon_cw.png");
                q.setIconImage(ficon.getImage());
                q.setTitle("Cinewatchers Administrator");
                q.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                h.go(q,user_id,user_name,name);
                //JOptionPane.showMessageDialog(null,"All movies done","Alert",JOptionPane.INFORMATION_MESSAGE);
            }
            else{
            try{
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance(); 
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/cinewatchers","ritom","123123123");
            stmt=con.createStatement();stmt2=con.createStatement();
            rs=stmt.executeQuery("SELECT * FROM movie");
            rs2=stmt2.executeQuery("SELECT COUNT(1) FROM movie");rs2.next();int number = rs2.getInt(1);
            Box full = Box.createVerticalBox();
            Box row = Box.createHorizontalBox();
            int counter=0;
            while(rs.next())
            {
                rev=new JLabel();
                rev.setMinimumSize(new Dimension(500,100));
                rev.setMaximumSize(new Dimension(500,100));
                rev.setPreferredSize(new Dimension(500,100));
                all_movies=rs.getString(2)+"\t\t\t"+rs.getInt(3)+"\n\n";
                rev.setText("<html>"+all_movies.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n","<br />").replaceAll("\t","&nbsp;&nbsp;&nbsp;&nbsp;")+"</html>");
                rev.setHorizontalAlignment(JLabel.CENTER);
                rev.setVerticalAlignment(JLabel.CENTER);
                //Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
                Border border = BorderFactory.createLineBorder(new Color(220,220,220));
                rev.setBorder(border);
                
                BufferedImage im = ImageIO.read(rs.getBinaryStream("Image"));
                Image dimg = im.getScaledInstance(36, 64, Image.SCALE_SMOOTH);
                rev.setIcon(new ImageIcon(dimg));
                System.out.println(counter);
                if(counter%2==0)//issue is, if counter is even above 2, and that's the last movie, won't add to p. Ensure even number of movies for now
                {
                    row.add(rev);
                    
                }
                else{ 
                    row.add(rev);
                    full.add(row);
                    p.add(full);
                    row = Box.createHorizontalBox();
                    full = Box.createVerticalBox();
                }
                counter++;   
            }
            if(counter%2 != 0 && number%2!=0)
            {
                full.add(row);
                p.add(full);
                row = Box.createHorizontalBox();
                full = Box.createVerticalBox();
                
            }
            //full.add(box);
            //p.add(full);
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
                System.out.println(e);
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
    public class RefreshListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            //btnAddMovies.requestFocus();
            new_frame.removeAll();
            new_frame.dispose();
            Home h = new Home();
                
            JFrame q = new JFrame("Cinewatchers");
            ImageIcon ficon=new ImageIcon("/home/ritom/Desktop/Java/DBMS/icon_cw.png");
            q.setIconImage(ficon.getImage());
            q.setTitle("Cinewatchers");
            q.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            h.go(q,user_id,user_name,name);
        }
    }
    public class LogoutListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            //btnAddMovies.requestFocus();
            Cinewatchers r=new Cinewatchers();
            new_frame.dispose();
            r.go();
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
 
        frame.setSize(400,400);
        frame.setResizable(false);
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
            
            String password=new String(txtPassword.getPassword()).trim();//get from window
            if(password.isEmpty() || txtUsername.getText().trim().isEmpty() || txtName.getText().trim().isEmpty())
            {
                JOptionPane.showMessageDialog(null,"Please fill all fields!","Alert",JOptionPane.ERROR_MESSAGE);
            }
            else
               { stmt.executeUpdate("INSERT INTO users (uid,username,password,name) VALUES(null,'"+txtUsername.getText().trim()+"','"+password+"','"+txtName.getText().trim()+"');");
                con.close();
                frame.dispose();}
            }
            catch(SQLException e)
            {
                System.out.println("SQLException: " + e.getMessage());
                
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("VendorError: " + e.getErrorCode());
                
                if(e.getErrorCode()==1062)
                 JOptionPane.showMessageDialog(null,"Username already exists","Alert",JOptionPane.ERROR_MESSAGE);
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