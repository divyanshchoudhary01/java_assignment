import java.sql.*;
public class DAOConnection
{
private DAOConnection(){}
public static Connection getConnection() throws DAOException
{
Connection connection=null;
try
{
Class.forName("com.mysql.cj.jdbc.Driver");    
connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/hrdb","root","2525");
}catch(Exception e)
{
System.out.println(e.getMessage());
throw new DAOException(e.getMessage());
}
return connection;
}
}