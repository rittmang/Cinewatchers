import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.Box;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
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
    JScrollPane pane;
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
        
        JButton btnAddReview = new JButton("Add Review");
        btnAddReview.addActionListener(new AddReviewListener());
        JButton btnShowReviews = new JButton("Show my reviews");
        btnShowReviews.addActionListener(new ShowReviewsListener());
        
        JButton btnShowMovies = new JButton("Show movie database");
        btnShowMovies.addActionListener(new ShowMoviesListener());
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(new RefreshListener());
        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(new LogoutListener());
        
        //box.add(l);
        box.add(btnAddReview);
        box.add(Box.createRigidArea(new Dimension (50,10)));
        box.add(btnShowReviews);
        box.add(Box.createRigidArea(new Dimension (50,10)));
        box.add(btnShowMovies);
        box.add(Box.createRigidArea(new Dimension (50,10)));
        box.add(btnRefresh);
        box.add(Box.createRigidArea(new Dimension (50,10)));
        box.add(btnLogout);
        p.add(box);
        p.add(Box.createRigidArea(new Dimension(0, 80)));
        
        pane = new JScrollPane(p,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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
                already_shown=0;
                //p.removeAll();
                p = new JPanel();
                new_frame.revalidate();new_frame.repaint();
                new_frame.removeAll();
                new_frame.dispose();
                Home h = new Home();
                
                JFrame q = new JFrame("Cinewatchers");
                ImageIcon ficon=new ImageIcon("/home/ritom/Desktop/Java/DBMS/icon_cw.png");
                q.setIconImage(ficon.getImage());
                q.setTitle("Cinewatchers");
                q.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                q.getContentPane().add(BorderLayout.CENTER,p);
                q.setExtendedState(JFrame.MAXIMIZED_BOTH);
                h.go(q,user_id,user_name,name);
            }
            else{
            try{
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance(); 
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/cinewatchers","ritom","123123123");
            stmt=con.createStatement();
            rs=stmt.executeQuery("SELECT * FROM reviews WHERE userid="+user_id);
            
            
            Box box2 = Box.createVerticalBox();
            JTextArea new_rev;
            JScrollPane pp=null;
            while(rs.next())
            {
                new_rev=new JTextArea();
                new_rev.setMinimumSize(new Dimension(5,5));
                
                new_rev.setColumns(5);
                
                int mid=rs.getInt(2);
                Statement stmt2 = con.createStatement();
                ResultSet rs2 = stmt2.executeQuery("SELECT name,year FROM movie WHERE mid="+mid);
                rs2.next();String movie_name=rs2.getString(1)+" ("+rs2.getInt(2)+")";
                all_reviews=movie_name+"\t"+rs.getInt(4)+"★"+"\n\n"+rs.getString(5).trim();
                new_rev.setText(all_reviews);
                
                new_rev.setWrapStyleWord(true);new_rev.setLineWrap(true);new_rev.setEditable(false);new_rev.setFocusable(false);new_rev.setOpaque(false);
                
                Border border = BorderFactory.createLineBorder(new Color(220,220,220));
                new_rev.setBorder(border);
                pp = new JScrollPane(new_rev,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                //p.add(pp);
                box2.add(pp);
                box2.add(Box.createRigidArea(new Dimension (50,50)));
            }
            
            p.add(box2);
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
            ResultSet rs=null,rs2=null,rs3=null;
            if(already_shown==1)
            {
                already_shown=0;
                //p.removeAll();
                p = new JPanel();
                new_frame.revalidate();new_frame.repaint();
                new_frame.removeAll();
                new_frame.dispose();
                Home h = new Home();
                
                JFrame q = new JFrame("Cinewatchers");
                ImageIcon ficon=new ImageIcon("/home/ritom/Desktop/Java/DBMS/icon_cw.png");
                q.setIconImage(ficon.getImage());
                q.setTitle("Cinewatchers");
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
                int mid=rs.getInt(1);
                // Statement stmt3=con.createStatement();
                // rs3=stmt3.executeQuery("SELECT mgenre FROM comes_in WHERE movieid="+mid);
                // while(rs3.next())
                // {
                    
                //     all_movies+=rs3.getString(1);
                //     if(!rs3.isLast())
                //         all_movies+=", ";
                // }
                //all_movies+="\b";
                all_movies+=rs.getString(5);
                rev.setText("<html>"+all_movies.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n","<br />").replaceAll("\t","&nbsp;&nbsp;&nbsp;&nbsp;")+"</html>");
                rev.setHorizontalAlignment(JLabel.LEFT);
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
    public class AddReviewListener implements ActionListener{
        public void actionPerformed(ActionEvent event){

            AddReview r = new AddReview();
            r.go(user_id);
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
class AddReview
{
    String msg = "",s="",user_id="";
    JComboBox<String> movie_name=null;
    JComboBox<Integer> rating_stars=null;
    JTextArea review = null;
    JFrame frame;
    Box box;
    //int already_shown;
    public void go(String user_id)
    {
        //already_shown = as;
        this.user_id=user_id;
        frame = new JFrame("CW - Add Review");
        frame.setTitle("CW - Add Review");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        JLabel lblName = new JLabel("Choose Movie:");   
        JLabel lblRating = new JLabel("Choose Rating(1-5): ");
        JLabel lblReview = new JLabel("Enter review:");

        box = Box.createVerticalBox();
        box.setBorder(new EmptyBorder(0, 0, 0, 0));
        Box box1 = Box.createHorizontalBox();
        box1.setBorder(new EmptyBorder(0,0,0,0));
        
        movie_name = new JComboBox<>();//JComboBox is now Generic, apparently
        rating_stars = new JComboBox<>();
        review = new JTextArea(10, 10);
        review.setLineWrap(true);
        review.setWrapStyleWord(true);
        try{
            //add code to fill genre and movie_name stuff
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/cinewatchers","ritom","123123123");
            Statement stmt=con.createStatement();Statement stmt2=con.createStatement();
            ResultSet rs=stmt.executeQuery("SELECT * FROM stars_allowed GROUP BY val ORDER BY val DESC;");
            ResultSet rs2=stmt2.executeQuery("SELECT name,year FROM movie ORDER BY year DESC;");
            while(rs2.next())
            {
                movie_name.addItem(rs2.getString(1)+" ("+rs2.getInt(2)+")");    
            }
            while(rs.next())
            {
                rating_stars.addItem(rs.getInt(1));    
            }
            con.close();
        }
        catch(SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        JButton btnAddToDB = new JButton("Add Review");
        btnAddToDB.addActionListener(new AddToDBListener());
        btnAddToDB.setBackground(new Color(0,255,0));
        btnAddToDB.setBorderPainted(false);
        box.add(lblName);
        box.add(movie_name);
        box.add(Box.createRigidArea(new Dimension (10,50)));
        box.add(lblRating);
        box.add(rating_stars);
        box.add(Box.createRigidArea(new Dimension (10,50)));
        box.add(lblReview);
        //box.add(review);
        JScrollPane pane1 = new JScrollPane(review,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        pane1.getVerticalScrollBar().setUnitIncrement(16);
        box.add(BorderLayout.CENTER,pane1);
        box.add(Box.createRigidArea(new Dimension (10,80)));
       
        box1.add(btnAddToDB);
        box.add(box1);
        panel.add(box);
        JScrollPane pane = new JScrollPane(panel,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        pane.getVerticalScrollBar().setUnitIncrement(16);
        frame.add(BorderLayout.CENTER,pane);
        //frame.getContentPane().add(BorderLayout.CENTER,panel);
        frame.getRootPane().setDefaultButton(btnAddToDB);
        ImageIcon ficon=new ImageIcon("/home/ritom/Desktop/Java/DBMS/icon_cw.png");
        frame.setIconImage(ficon.getImage());
        frame.setMinimumSize(new Dimension(500,500));
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    public class AddToDBListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            String naam="",naam2="",text="";
            int rating=0;
            try{
                //get input from JComboBox
                naam=(String)movie_name.getSelectedItem();
                
                naam2=naam.substring(0,naam.indexOf("("));//System.out.println("Name="+naam);
                rating = (Integer)rating_stars.getSelectedItem();
                text=review.getText().trim().replaceAll("'", "\'");
                if(text.equals(""))throw new NullPointerException("Empty Review");
                System.out.println(text);
                int mid=0;
                Class.forName("com.mysql.cj.jdbc.Driver").newInstance(); 
                Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/cinewatchers","ritom","123123123");
                Statement stmt=con.createStatement();
                ResultSet rs=stmt.executeQuery("SELECT year FROM movie WHERE name="+"'"+naam2+"'");
                while(rs.next())
                {
                    int year=rs.getInt(1);
                    //System.out.println(year);
                    if(naam.contains(year+""))//considering that 1 name movie in 1 year
                    {
                        Statement stmt2 = con.createStatement();
                        ResultSet rs2 = stmt2.executeQuery("SELECT mid FROM movie WHERE name='"+naam2+"' AND year="+year);
                        rs2.next();
                        mid=rs2.getInt(1);
                    }
                }
                
                Connection con2=DriverManager.getConnection("jdbc:mysql://localhost:3306/cinewatchers","ritom","123123123");
                Statement stmt2=con2.createStatement();
                //System.out.println("INSERT INTO reviews VALUES(null,"+mid+","+user_id+","+rating+",\'"+text+"\');");            
                stmt2.executeUpdate("INSERT INTO reviews VALUES(null,"+mid+","+user_id+","+rating+",\""+text+"\");");
                con2.close();
                frame.dispose();
                

            }
            catch(SQLException e)
            {
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("VendorError: " + e.getErrorCode());
                //e.getCause().getStackTrace();
                e.printStackTrace();
                if(e.getErrorCode()==1406)
                {
                    JOptionPane.showMessageDialog(null, "Your review exceeds the 600 character limit!", "Alert", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(NullPointerException e)
            {
                JOptionPane.showMessageDialog(null, "Review cannot be empty!", "Alert", JOptionPane.ERROR_MESSAGE);
            }
            catch(Exception e)
            {
                System.out.println(e);
                e.printStackTrace();
                System.out.println("Something went wrong");
            }
        } 
            
    }
    
}