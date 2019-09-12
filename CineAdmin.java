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
}
class AdminHome{
    
    String user_id="",user_name="";
    JPanel p;JLabel rev;String all_movies="";
    JFrame new_frame;int already_shown=0;
    public void go(JFrame frame,String uid,String username)//uses same frame from Cinewatchers class
    {
        user_id=uid;
        user_name=username;
        
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
        JButton btnAddGenre = new JButton("Add genre");
        btnShowMovies.addActionListener(new ShowMoviesListener());
        btnAddMovies.addActionListener(new AddMoviesListener());
        btnAddGenre.addActionListener(new AddGenreListener());
        //box.add(l);
        box.add(btnShowReviews);
        box.add(Box.createRigidArea(new Dimension (50,10)));
        box.add(btnShowMovies);
        box.add(Box.createRigidArea(new Dimension (50,10)));
        box.add(btnAddMovies);
        box.add(Box.createRigidArea(new Dimension (50,10)));
        box.add(btnAddGenre);
        p.add(box);
        p.add(Box.createRigidArea(new Dimension(0, 80)));
    
        new_frame.add(p);
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
            rs=stmt.executeQuery("SELECT * FROM reviews");
            rev=new JLabel();
            while(rs.next())
            {
                all_movies+=rs.getString(5)+"\n\n";    
            }
            rev.setText("<html>"+all_movies.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n","<br />")+"</html>");
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
           // JOptionPane.showMessageDialog(null,msg,"Alert",JOptionPane.INFORMATION_MESSAGE);
                              
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
    public class AddMoviesListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            //btnAddMovies.requestFocus();
            AddMovies r=new AddMovies();
            r.go();
        }
    }
    public class AddGenreListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            //btnAddMovies.requestFocus();
            AddGenre r=new AddGenre();
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
        img = new JLabel();img.setBounds(10,120,140,250);//changed 670 to 250
        box = Box.createVerticalBox();
        box.setBorder(new EmptyBorder(0, 0, 0, 0));
        Box box1 = Box.createHorizontalBox();
        box1.setBorder(new EmptyBorder(0,0,0,0));
        txtName = new JTextField(30);
        txtYear = new JTextField(4);
        JButton btnChooseImage = new JButton("Choose Image");
        btnChooseImage.addActionListener(new ChooseImageListener());
        JButton btnAddToDB = new JButton("Add to Cinewatchers");
        btnAddToDB.addActionListener(new AddToDBListener());
        box.add(lblName);
        box.add(txtName);
        box.add(Box.createRigidArea(new Dimension (10,50)));
        box.add(lblYear);
        box.add(txtYear);//need to add image area as well
        box.add(Box.createRigidArea(new Dimension (10,80)));
        box.add(Box.createRigidArea(new Dimension (10,160)));
        box1.add(Box.createRigidArea(new Dimension (10,160)));
        box1.add(btnChooseImage);
        box1.add(Box.createRigidArea(new Dimension (50,10)));
        box1.add(btnAddToDB);
        box.add(box1);
        panel.add(box);    
        frame.getContentPane().add(BorderLayout.CENTER,panel);
        frame.getRootPane().setDefaultButton(btnChooseImage);
        ImageIcon ficon=new ImageIcon("/home/ritom/Desktop/Java/DBMS/icon_cw.png");
        frame.setIconImage(ficon.getImage());
        frame.setSize(200,800);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    public class ChooseImageListener implements ActionListener{
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
                PreparedStatement ps=con.prepareStatement("INSERT INTO movie VALUES(?,?,?,?)");
                InputStream is = new FileInputStream(new File(s));
                ps.setNull(1, java.sql.Types.INTEGER);
                if(!txtName.getText().equals(""))
                    ps.setString(2, txtName.getText());
                else
                    {is.close();throw new NullPointerException("Empty Movie Name");}
                ps.setInt(3,Integer.parseInt(txtYear.getText()));
                ps.setBlob(4, is);
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

class AddGenre
{
    String msg = "",s="";
    //JTextField txtName = null;
    //JTextField txtYear=null;
    //JLabel img;
    JComboBox<String> movie_name=null;
    JComboBox<String> genre=null;
    JFrame frame;
    Box box;
    //int already_shown;
    public void go()
    {
        //already_shown = as;
        frame = new JFrame("CW - Add Genre");
        frame.setTitle("CW - Add Genre");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        JLabel lblName = new JLabel("Choose Movie:");   
        JLabel lblYear = new JLabel("Choose Genre: ");
        //img = new JLabel();img.setBounds(10,120,140,250);//changed 670 to 250
        box = Box.createVerticalBox();
        box.setBorder(new EmptyBorder(0, 0, 0, 0));
        Box box1 = Box.createHorizontalBox();
        box1.setBorder(new EmptyBorder(0,0,0,0));
        //txtName = new JTextField(30);
        //txtYear = new JTextField(4);
        //JButton btnChooseImage = new JButton("Choose Image");
        //btnChooseImage.addActionListener(new ChooseImageListener());
        movie_name = new JComboBox<>();//JComboBox is now Generic, apparently
        genre = new JComboBox<>();
        try{
            //add code to fill genre and movie_name stuff
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/cinewatchers","ritom","123123123");
            Statement stmt=con.createStatement();Statement stmt2=con.createStatement();
            ResultSet rs=stmt.executeQuery("SELECT * FROM genre");
            ResultSet rs2=stmt2.executeQuery("SELECT name,year FROM movie");
            while(rs2.next())
            {
                movie_name.addItem(rs2.getString(1)+" ("+rs2.getInt(2)+")");    
            }
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
        JButton btnAddToDB = new JButton("Update Movie Info");
        btnAddToDB.addActionListener(new AddToDBListener());
        box.add(lblName);
        box.add(movie_name);
        box.add(Box.createRigidArea(new Dimension (10,50)));
        box.add(lblYear);
        box.add(genre);//need to add image area as well
        box.add(Box.createRigidArea(new Dimension (10,80)));
        //box1.add(Box.createRigidArea(new Dimension (10,160)));
        //box1.add(btnChooseImage);
        //box1.add(Box.createRigidArea(new Dimension (50,10)));
        box1.add(btnAddToDB);
        box.add(box1);
        panel.add(box);    
        frame.getContentPane().add(BorderLayout.CENTER,panel);
        frame.getRootPane().setDefaultButton(btnAddToDB);
        ImageIcon ficon=new ImageIcon("/home/ritom/Desktop/Java/DBMS/icon_cw.png");
        frame.setIconImage(ficon.getImage());
        frame.setSize(200,800);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    public class AddToDBListener implements ActionListener{
        public void actionPerformed(ActionEvent event){

            try{
                //get input from JComboBox
                String naam=(String)movie_name.getEditor().getItem();
                String gaanre = (String)genre.getEditor().getItem();
                int mid=0;
                Class.forName("com.mysql.cj.jdbc.Driver").newInstance(); 
                Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/cinewatchers","ritom","123123123");
                Statement stmt=con.createStatement();
                ResultSet rs=stmt.executeQuery("SELECT year FROM movie WHERE name="+naam);
                while(rs.next())
                {
                    int year=rs.getInt(1);
                    if(naam.contains(year+""))//considering that 1 name movie in 1 year
                    {
                        Statement stmt2 = con.createStatement();
                        ResultSet rs2 = stmt2.executeQuery("SELECT mid FROM movie WHERE name="+naam+" AND year="+year);
                        rs2.next();
                        mid=rs2.getInt(1);
                    }
                }
                if(mid == 0){throw new Exception("Umm, what happened again?");}                
                
                //add COMES_IN LOGIC HERE::::::::::::::::::::::::::::::::::::::::::::::::::::::>>>>>>>>>>>>>
                Statement stmt2=con.createStatement();
                ResultSet rs2=stmt.executeQuery("SELECT mid FROM genre WHERE name="+naam);          
                
                //NOW ADD INTO COMES_IN
                
                
                
                
                
                PreparedStatement ps=con.prepareStatement("INSERT INTO movie VALUES(?,?,?,?)");//for executing update, comes later
                
                
                // InputStream is = new FileInputStream(new File(s));
                // ps.setNull(1, java.sql.Types.INTEGER);
                // ps.setString(2, txtName.getText());
                // ps.setInt(3,Integer.parseInt(txtYear.getText()));
                // ps.setBlob(4, is);
                // ps.executeUpdate();
                // JOptionPane.showMessageDialog(null, "Data inserted successfully", "Alert", JOptionPane.INFORMATION_MESSAGE);
                // //already_shown = 0;
                // con.close();
                // frame.dispose();

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