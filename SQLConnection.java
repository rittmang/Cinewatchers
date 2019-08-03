import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
//to run this, must have .jar file in this directory. To set classpath , use 'export CLASSPATH=/path/to/mysql-connector-java-8.0.17.jar:$CLASSPATH'
class SQLConnection{ 
    
    public static void main(String args[]) throws Exception{  
        
        Statement stmt=null;
        ResultSet rs=null;
        try{  
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance(); 
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/cinewatchers","ritom","123123123");
            stmt=con.createStatement();
            rs=stmt.executeQuery("SELECT * FROM users");
            
        }
        catch(SQLException e)
        { 
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }  
        catch(Exception e)
        {
            System.out.println("Whoops");
        }
        finally
        {
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