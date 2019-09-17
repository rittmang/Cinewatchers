import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.Box;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CineAdmin{
    String msg = "";
    JTextField txtUsername = null;
    JPasswordField txtPassword = null;
    JFrame frame;
    Statement stmt=null;//SQL
    ResultSet rs=null;//SQL
     
    public static void main(String[] args){
        CineAdmin gui = new CineAdmin();
        gui.go();
    }
    public void go(){
        frame = new JFrame("Cinewatchers Administrator Window");
        frame.setTitle("Cinewatchers Administrator");
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
        JButton btnUser = new JButton("I'm a User");
        btnUser.addActionListener(new UserListener());
 
        panel.add(lblUsername);
        panel.add(txtUsername);
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(btnLogin);
        panel.add(btnCancel);
        panel.add(btnUser);
        
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
            rs=stmt.executeQuery("SELECT * FROM admin");
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
                         
                         msg="Admin Login Granted";
                         frame.getContentPane().removeAll();//removes all previous components, get a blank screen
                         frame.getContentPane().repaint();
                         AdminHome h=new AdminHome();
                         h.go(frame,uid,username);
                         signed_in=1;
                         System.out.println("Admin Logged In");
                     }
                 } 
            }
            if(signed_in==0)
                JOptionPane.showMessageDialog(null,"Admin credentials are incorrect","Alert",JOptionPane.INFORMATION_MESSAGE);
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
    public class UserListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            Cinewatchers r=new Cinewatchers();
            frame.dispose();
            r.go();
        }
    }
}
class AdminHome{
    
    String user_id="",user_name="";
    JPanel p;JLabel rev;String all_movies="",all_reviews="";
    JFrame new_frame;int already_shown=0;
    public void go(JFrame frame,String uid,String username)//uses same frame from Cinewatchers class
    {
        user_id=uid;
        user_name=username;
        System.out.println(user_name+" is on Home page");
        new_frame=frame;
        Box box = Box.createHorizontalBox();
        box.setBorder(new EmptyBorder(0, 0, 0, 0));
        Box box1 = Box.createHorizontalBox();
        box1.setBorder(new EmptyBorder(0, 0, 0, 0));
        JLabel l=new JLabel();
        l.setText("Logged in as "+user_name);
        //l.setHorizontalAlignment(JLabel.CENTER);
        //l.setAlignmentX(p.CENTER_ALIGNMENT);
        p=new JPanel();
        
        TitledBorder border = new TitledBorder("Admin Dashboard");
        border.setTitleJustification(TitledBorder.CENTER);
        border.setTitlePosition(TitledBorder.TOP);
        p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        p.setBorder(border);
        p.add(Box.createRigidArea(new Dimension(0, 40)));
        //p.add(l);
        box1.add(l);
        p.add(box1);
        p.add(Box.createRigidArea(new Dimension(0, 30)));
        
        JButton btnShowReviews = new JButton("Show all reviews");
        btnShowReviews.addActionListener(new ShowReviewsListener());
        
        JButton btnShowMovies = new JButton("Show movie database");
        JButton btnAddMovies = new JButton("Add movie");
        JButton btnEditMovies = new JButton("Edit/Delete movie");
        JButton btnRefresh = new JButton("Refresh");
        JButton btnLogout = new JButton("Log out");
        btnShowMovies.addActionListener(new ShowMoviesListener());
        btnAddMovies.addActionListener(new AddMoviesListener());
        btnEditMovies.addActionListener(new EditMoviesListener());
        btnRefresh.addActionListener(new RefreshListener());
        btnLogout.addActionListener(new LogoutListener());
        //box.add(l);
        box.add(btnShowReviews);
        box.add(Box.createRigidArea(new Dimension (50,10)));
        box.add(btnShowMovies);
        box.add(Box.createRigidArea(new Dimension (50,10)));
        box.add(btnAddMovies);
        box.add(Box.createRigidArea(new Dimension (50,10)));
        box.add(btnEditMovies);
        box.add(Box.createRigidArea(new Dimension (50,10)));
        box.add(btnRefresh);
        box.add(Box.createRigidArea(new Dimension (50,10)));
        box.add(btnLogout);
        p.add(box);
        p.add(Box.createRigidArea(new Dimension(0, 80)));
        JScrollPane pane = new JScrollPane(p,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        pane.getVerticalScrollBar().setUnitIncrement(16);
        new_frame.add(pane);//changed from p
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
                AdminHome h = new AdminHome();
                
                JFrame q = new JFrame("Cinewatchers Administrator Window");
                ImageIcon ficon=new ImageIcon("/home/ritom/Desktop/Java/DBMS/icon_cw.png");
                q.setIconImage(ficon.getImage());
                q.setTitle("Cinewatchers Administrator");
                q.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                q.getContentPane().add(BorderLayout.CENTER,p);
                q.setExtendedState(JFrame.MAXIMIZED_BOTH);
                h.go(q,user_id,user_name);
            }
            else{
            try{
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance(); 
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/cinewatchers","ritom","123123123");
            stmt=con.createStatement();
            rs=stmt.executeQuery("select r.movieid,m.name, r.userid,r.text, r.stars, m.year from reviews r inner join movie m on r.movieid=m.mid order by m.year desc,r.movieid desc;");
            
            
            Box box2 = Box.createVerticalBox();
            JTextArea new_rev;
            JScrollPane pp=null;
            while(rs.next())
            {
                new_rev=new JTextArea();
                new_rev.setMinimumSize(new Dimension(5,5));
                
                new_rev.setColumns(5);
                
                //int mid=rs.getInt(1);
                //Statement stmt2 = con.createStatement();
                //ResultSet rs2 = stmt2.executeQuery("SELECT name,year FROM movie WHERE mid="+mid);
                //rs2.next();
                String movie_name=rs.getString(2)+" ("+rs.getInt(6)+")";

                int uid=rs.getInt(3);
                Statement stmt3=con.createStatement();
                ResultSet rs3=stmt3.executeQuery("SELECT username FROM users WHERE uid="+uid);
                rs3.next();
                String usser=rs3.getString(1);
                all_reviews=usser +"\t"+movie_name+"\t"+rs.getInt(5)+"★"+"\n\n"+rs.getString(4).trim();
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
                e.printStackTrace();
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
                AdminHome h = new AdminHome();
                
                JFrame q = new JFrame("Cinewatchers Administrator Window");
                ImageIcon ficon=new ImageIcon("/home/ritom/Desktop/Java/DBMS/icon_cw.png");
                q.setIconImage(ficon.getImage());
                q.setTitle("Cinewatchers Administrator");
                q.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                h.go(q,user_id,user_name);
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
                //Statement stmt3=con.createStatement();
                //rs3=stmt3.executeQuery("SELECT genre FROM movie WHERE mid="+mid);
                // while(rs3.next())
                // {
                    
                //     all_movies+=rs3.getString(1);
                //     if(!rs3.isLast())
                //         all_movies+=", ";
                // }
                all_movies+=rs.getString(5);
                //all_movies+="\b";
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
    public class AddMoviesListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            //btnAddMovies.requestFocus();
            AddMovies r=new AddMovies();
            r.go();
        }
    }
    public class EditMoviesListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            //btnAddMovies.requestFocus();
            EditMovies r=new EditMovies();
            r.go();
        }
    }
    public class RefreshListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            //btnAddMovies.requestFocus();
            new_frame.removeAll();
            new_frame.dispose();
            AdminHome h = new AdminHome();
                
            JFrame q = new JFrame("Cinewatchers Administrator Window");
            ImageIcon ficon=new ImageIcon("/home/ritom/Desktop/Java/DBMS/icon_cw.png");
            q.setIconImage(ficon.getImage());
            q.setTitle("Cinewatchers Administrator");
            q.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            h.go(q,user_id,user_name);
        }
    }
    public class LogoutListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            //btnAddMovies.requestFocus();
            CineAdmin r=new CineAdmin();
            new_frame.dispose();
            r.go();
        }
    }

}

class AddMovies
{
    String msg = "",s="";
    JTextField txtName = null;
    JTextField txtYear=null;
    JLabel img;
    JFrame frame;
    Box box;
    JTextArea jta=null;
    //int already_shown;
    public void go()
    {
        //already_shown = as;
        frame = new JFrame("CW - Add Movie");
        frame.setTitle("CW - Add Movie");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        JLabel lblName = new JLabel("Enter Movie Name: [max 30]");   
        JLabel lblYear = new JLabel("Enter Year: ");
        JLabel lblGenre = new JLabel("Select genre/s:");
        img = new JLabel();img.setBounds(10,120,140,250);//changed 670 to 250
        box = Box.createVerticalBox();
        box.setBorder(new EmptyBorder(0, 0, 0, 0));
        Box box1 = Box.createHorizontalBox();
        box1.setBorder(new EmptyBorder(0,0,0,0));
        txtName = new JTextField(30);
        txtYear = new JTextField(4);
        JButton btnDeleteMovie = new JButton("Choose Image");
        btnDeleteMovie.addActionListener(new DeleteMovieListener());
        JComboBox<String> genre=new JComboBox<>();
        jta = new JTextArea(5,5);jta.setLineWrap(true);jta.setWrapStyleWord(true);
        try{
            //add code to fill genre and movie_name stuff
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/cinewatchers","ritom","123123123");
            Statement stmt=con.createStatement();Statement stmt2=con.createStatement();
            ResultSet rs=stmt.executeQuery("SELECT * FROM genre");
            ResultSet rs2=stmt2.executeQuery("SELECT name,year FROM movie ORDER BY year DESC");
            while(rs.next())
            {
                genre.addItem(rs.getString(1));    
            }
            con.close();
        }
        catch(SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        ActionListener act = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String s=jta.getText();
                s+=","+genre.getSelectedItem();
                jta.setText(s);
            }
        };
        genre.addActionListener(act);
        JButton btnAddToDB = new JButton("Add to Cinewatchers");
        btnAddToDB.addActionListener(new AddToDBListener());
        box.add(lblName);
        box.add(txtName);
        box.add(Box.createRigidArea(new Dimension (10,50)));
        box.add(lblYear);
        box.add(txtYear);//need to add image area as well
        box.add(Box.createRigidArea(new Dimension (10,80)));
        box.add(Box.createRigidArea(new Dimension(10,200)));box.add(lblGenre);box.add(genre);box.add(jta);
        box.add(Box.createRigidArea(new Dimension (10,100)));
        //box1.add(Box.createRigidArea(new Dimension (10,160)));
        box1.add(btnDeleteMovie);
        box1.add(Box.createRigidArea(new Dimension (50,10)));
        box1.add(btnAddToDB);
        box.add(box1);
        panel.add(box);    
        frame.getContentPane().add(BorderLayout.CENTER,panel);
        frame.getRootPane().setDefaultButton(btnDeleteMovie);
        ImageIcon ficon=new ImageIcon("/home/ritom/Desktop/Java/DBMS/icon_cw.png");
        frame.setIconImage(ficon.getImage());
        frame.setSize(200,900);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    public class DeleteMovieListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            try{
            
                JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory(new File(System.getProperty("user.home")));
                FileNameExtensionFilter filter = new FileNameExtensionFilter("*.IMAGE", "jpg","gif","png");
                fc.addChoosableFileFilter(filter);
                fc.setMultiSelectionEnabled(false);
                fc.setAcceptAllFileFilterUsed(false);
                int result = fc.showSaveDialog(null);
                if(result == JFileChooser.APPROVE_OPTION)
                {
                    File selectedFile = fc.getSelectedFile();
                    String path = selectedFile.getAbsolutePath();
                    //img.setIcon(ResizeImage(path));
                    ImageIcon Icon = new ImageIcon(path);
                    Image image = Icon.getImage().getScaledInstance(img.getWidth(), img.getHeight(), Image.SCALE_AREA_AVERAGING);
                    Icon = new ImageIcon(image);
                    img.setIcon(Icon);
                    //box.add(Box.createRigidArea(new Dimension (80,80)));
                    box.add(img);
                    frame.getContentPane().repaint();
                    s=path;
                    
                    

                }
                else if(result == JFileChooser.CANCEL_OPTION)
                {
                    System.out.println("No Image Data");
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }                 
         }
    }
    public class AddToDBListener implements ActionListener{
        public void actionPerformed(ActionEvent event){

            try{
                Class.forName("com.mysql.cj.jdbc.Driver").newInstance(); 
                Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/cinewatchers","ritom","123123123");
                PreparedStatement ps=con.prepareStatement("INSERT INTO movie VALUES(?,?,?,?,?)");
                InputStream is = new FileInputStream(new File(s));
                ps.setNull(1, java.sql.Types.INTEGER);
                if(!txtName.getText().equals(""))
                    ps.setString(2, txtName.getText());
                else
                    {is.close();throw new NullPointerException("Empty Movie Name");}
                ps.setInt(3,Integer.parseInt(txtYear.getText()));
                ps.setBlob(4, is);
                ps.setString(5,jta.getText().substring(1));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data inserted successfully", "Alert", JOptionPane.INFORMATION_MESSAGE);
                //already_shown = 0;
                con.close();
                frame.dispose();

            }
            catch(SQLException e)
            {
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("VendorError: " + e.getErrorCode());
                if(e.getErrorCode()==1406)
                {
                    JOptionPane.showMessageDialog(null, "Image is too big. Please keep within 65 kB", "Alert", JOptionPane.ERROR_MESSAGE);
                }
                if(e.getErrorCode()==1062)
                {
                    JOptionPane.showMessageDialog(null, "Duplicate entry found. Cinewatchers cannot support duplicate (name,year) pairs in this version.", "Alert", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(NumberFormatException e)
            {
                JOptionPane.showMessageDialog(null, "Year not entered", "Alert", JOptionPane.ERROR_MESSAGE);
            }
            catch(FileNotFoundException e)
            {
                JOptionPane.showMessageDialog(null, "Please choose a poster image", "Alert", JOptionPane.ERROR_MESSAGE);
            }
            catch(NullPointerException e)
            {
                System.out.println("Movie name wasn't entered");
                JOptionPane.showMessageDialog(null, "Movie name not entered", "Alert", JOptionPane.ERROR_MESSAGE);
            }
            catch(Exception e)
            {
                System.out.println(e.getClass());
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Something went wrong", "Alert", JOptionPane.ERROR_MESSAGE);
                System.out.println("Something went wrong");
            }
        } 
            
    }
    
    
}

class EditMovies
{
    String msg = "",s="";
    JTextField txtName = null;
    JTextField txtYear=null;
    JLabel img;
    JFrame frame;
    Box box;
    JTextArea jta=null;
    JComboBox<String> movie_chooser;
    //int already_shown;
    public void go()
    {
        //already_shown = as;
        frame = new JFrame("CW - Edit Movie");
        frame.setTitle("CW - Edit Movie");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        JLabel lblName = new JLabel("Change Movie Name: [max 30]");   
        JLabel lblYear = new JLabel("Change Year: ");
        JLabel lblGenre = new JLabel("Edit genre/s:");
        img = new JLabel();img.setBounds(10,120,140,250);//changed 670 to 250
        box = Box.createVerticalBox();
        box.setBorder(new EmptyBorder(0, 0, 0, 0));
        Box box1 = Box.createHorizontalBox();
        box1.setBorder(new EmptyBorder(0,0,0,0));
        txtName = new JTextField(30);
        txtYear = new JTextField(4);
        JButton btnDeleteMovie = new JButton("Delete Movie");
        btnDeleteMovie.setBackground(Color.red);
        btnDeleteMovie.addActionListener(new DeleteMovieListener());
        movie_chooser=new JComboBox<>();
        JComboBox<String> genre=new JComboBox<>();
        
        jta = new JTextArea(5,5);jta.setLineWrap(true);jta.setWrapStyleWord(true);
        try{
            //add code to fill genre and movie_name stuff
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/cinewatchers","ritom","123123123");
            Statement stmt=con.createStatement();Statement stmt2=con.createStatement();
            ResultSet rs=stmt.executeQuery("SELECT * FROM genre");
            ResultSet rs2=stmt2.executeQuery("SELECT name,year FROM movie ORDER BY year DESC");
            
            movie_chooser.addItem("");
            while(rs.next())
            {
                genre.addItem(rs.getString(1));    
            }
            while(rs2.next())
            {
                movie_chooser.addItem(rs2.getString(1)+" ("+rs2.getInt(2)+")");
                
            }
            con.close();
        }
        catch(SQLException e)
        {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        ActionListener act = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String s=jta.getText();
                if(s.charAt(0)==',')
                    jta.setText(s);
                else
                    jta.setText(","+s);
                s+=","+genre.getSelectedItem();
                jta.setText(s);
                //if(jta.getText().charAt(0)==','&&jta.getText().charAt(1)==',')
                  //  jta.setText(s.substring(1));
                
            }
        };
        ActionListener act2 = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            
                String s= (String) movie_chooser.getSelectedItem();
                if(s.equals(""))return;
                txtName.setText(s.substring(0,s.lastIndexOf(' ')));
                txtYear.setText(s.substring(s.indexOf('(')+1,s.indexOf(')')));
                try{
                Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/cinewatchers","ritom","123123123");
                Statement s3=con.createStatement();
                ResultSet rs=s3.executeQuery("SELECT genre FROM movie WHERE name='"+txtName.getText()+ "' AND year="+Integer.parseInt(txtYear.getText()));
                rs.next();
                jta.setText(","+rs.getString(1));
                }
                catch(SQLException e1)
                {
                    System.out.println("SQLException: " + e1.getMessage());
                    System.out.println("SQLState: " + e1.getSQLState());
                    System.out.println("VendorError: " + e1.getErrorCode());
                }
                
            }
        };
        genre.addActionListener(act);
        movie_chooser.addActionListener(act2);
        JButton btnAddToDB = new JButton("Make changes in Cinewatchers");
        btnAddToDB.addActionListener(new AddToDBListener());
        box.add(movie_chooser);
        box.add(Box.createRigidArea(new Dimension(10,50)));
        box.add(lblName);
        box.add(txtName);
        box.add(Box.createRigidArea(new Dimension (10,50)));
        box.add(lblYear);
        box.add(txtYear);//need to add image area as well
        box.add(Box.createRigidArea(new Dimension (10,50)));
        //box.add(Box.createRigidArea(new Dimension(10,200)));
        box.add(lblGenre);box.add(genre);box.add(jta);
        box.add(Box.createRigidArea(new Dimension (10,100)));
        //box1.add(Box.createRigidArea(new Dimension (10,160)));
        box1.add(btnDeleteMovie);
        box1.add(Box.createRigidArea(new Dimension (50,10)));
        box1.add(btnAddToDB);
        box.add(box1);
        panel.add(box);    
        frame.getContentPane().add(BorderLayout.CENTER,panel);
        frame.getRootPane().setDefaultButton(btnDeleteMovie);
        ImageIcon ficon=new ImageIcon("/home/ritom/Desktop/Java/DBMS/icon_cw.png");
        frame.setIconImage(ficon.getImage());
        frame.setSize(200,900);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    public class DeleteMovieListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            try{
            
                int output=JOptionPane.showConfirmDialog(frame,"Delete Movie?","Cinewatchers",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
                if(output==JOptionPane.YES_OPTION)
                {
                    Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/cinewatchers","ritom","123123123");
                    //Statement stmt=con.createStatement();
                    //ResultSet rs=stmt.executeQuery("DELETE FROM movie WHERE name='"+txtName.getText()+ "' AND year="+Integer.parseInt(txtYear.getText()));
                    PreparedStatement ps=con.prepareStatement("DELETE FROM movie WHERE name=? AND year=?");
                    ps.setString(1, txtName.getText());
                    ps.setInt(2,Integer.parseInt(txtYear.getText()));
                    ps.execute();
                    JOptionPane.showMessageDialog(null, "Movie deleted from Cinewatchers", "Success", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                }
                
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }        
            catch(NumberFormatException e)
            {
                JOptionPane.showMessageDialog(null, "Something went wrong", "Error", JOptionPane.ERROR_MESSAGE);
            }         
         }
    }
    public class AddToDBListener implements ActionListener{
        public void actionPerformed(ActionEvent event){

            try{
                Class.forName("com.mysql.cj.jdbc.Driver").newInstance(); 
                Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/cinewatchers","ritom","123123123");
                String s= (String) movie_chooser.getSelectedItem();
                String previous_movie=s.substring(0,s.lastIndexOf(' '));
                String previous_year=s.substring(s.indexOf('(')+1,s.indexOf(')'));

                PreparedStatement ps=con.prepareStatement("UPDATE movie SET name=?,year=?,genre=? WHERE name='"+previous_movie+"' AND year="+ Integer.parseInt(previous_year));
                if(txtName.getText().equals(""))
                {
                    throw new NullPointerException("Empty Movie Name");
                }
                if(jta.getText().lastIndexOf(',')==0 && jta.getText().length()>1)
                {
                    throw new ArithmeticException("Empty Genre");
                }
                ps.setString(1, txtName.getText());
                ps.setInt(2, Integer.parseInt(txtYear.getText()));
                if(jta.getText().charAt(0)==',' && jta.getText().charAt(1)==',')
                    ps.setString(3,jta.getText().substring(2));
                if(jta.getText().charAt(0)==',' && jta.getText().charAt(1)!=',')
                    ps.setString(3,jta.getText().substring(1));
                if(jta.getText().charAt(0)!=',')
                    ps.setString(3,","+jta.getText().substring(2));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data updated successfully", "Alert", JOptionPane.INFORMATION_MESSAGE);
                //already_shown = 0;
                con.close();
                frame.dispose();

            }
            catch(SQLException e)
            {
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("VendorError: " + e.getErrorCode());
                
                if(e.getErrorCode()==1062)
                {
                    JOptionPane.showMessageDialog(null, "Duplicate entry found. Cinewatchers cannot support duplicate (name,year) pairs in this version.", "Alert", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(NumberFormatException e)
            {
                JOptionPane.showMessageDialog(null, "Year not entered", "Alert", JOptionPane.ERROR_MESSAGE);
            }
            
            catch(NullPointerException e)
            {
                System.out.println("Movie name wasn't entered");
                JOptionPane.showMessageDialog(null, "Movie name not entered", "Alert", JOptionPane.ERROR_MESSAGE);
            }
            catch(StringIndexOutOfBoundsException e)
            {
                System.out.println("Genre seems to be empty");
                JOptionPane.showMessageDialog(null, "Genre seems to be empty", "Alert", JOptionPane.ERROR_MESSAGE);
            }
            catch(ArithmeticException e)
            {
                System.out.println("Genre wasn't entered");
                JOptionPane.showMessageDialog(null, "Genre not entered", "Alert", JOptionPane.ERROR_MESSAGE);
            }
            catch(Exception e)
            {
                System.out.println(e.getClass());
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Something went wrong", "Alert", JOptionPane.ERROR_MESSAGE);
                System.out.println("Something went wrong");
            }
        } 
            
    }
    
}