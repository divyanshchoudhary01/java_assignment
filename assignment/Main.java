import java.io.*;
import java.util.*;
import java.sql.*;
class  satResults implements Comparable<satResults>
{
private String Name ;
private String Address;
private String City;
private String Country;
private int pincode;
private  int satScore;
private boolean passed;
public satResults(String name,String address,String city,String country,int pincode,int satScore,boolean passed)
{
this.Name =name;
this.Address=address;
this.City=city;
this.Country=country;
this.pincode=pincode;
this.satScore=satScore;
this.passed=passed;
}
public String getName()
{
return this.Name;
}
public String getAddress()
{
return this.Address;
}
public String getCity()
{
return this.City;
}
public String getCountry()
{
return this.Country;
}
public int compareTo(satResults s)
{
return this.Name.compareToIgnoreCase(s.getName());
}
public int getPinCode()
{
return this.pincode;
}
public int getSatScore()
{
return this.satScore;
}
public boolean getPassed()
{
return this.passed;
}
}
class Main
{
public static void main(String args[])
{
//Menu take the input from the user
try
{
InputStreamReader r=new InputStreamReader(System.in);    
BufferedReader br=new BufferedReader(r);            
while(true)
{
System.out.println("---Menu---");
System.out.println("1. Insert data");
System.out.println("2. View all data");
System.out.println("3. Get rank");
System.out.println("4. Update score");
System.out.println("5. Delete one record");
System.out.println("6. Exit");
System.out.println("Select from the above options");
int choice=Integer.parseInt(br.readLine()); 
if(choice==1)
{


try
{

System.out.println("Enter Name ");    
String name=br.readLine();    
if(name.length()==0)
{
throw new DAOException("Empty String for Name");
}
Connection c=DAOConnection.getConnection();
PreparedStatement p=c.prepareStatement("select * from satresults where name=? ");;
p.setString(1,name);
ResultSet r3=p.executeQuery();
if(r3.next())
{
throw new DAOException("The given name already exists in the database");
}

System.out.println("Enter Address ");    
String Address=br.readLine();    
if(Address.length()==0)
{
throw new DAOException("Empty String for Address");
}

System.out.println("Enter City ");
String City=br.readLine();        
if(City.length()==0)
{
throw new DAOException("Empty String for City");
}

System.out.println("Enter Country ");
String Country=br.readLine();
if(Country.length()==0)
{
throw new DAOException("Empty String for Country");
}
System.out.println("Enter PinCode ");
int pincode=Integer.parseInt(br.readLine());
if(pincode<=0)
{
throw new DAOException("Invalid Pincode");
}
System.out.println("Enter SAT score out of 100 ");
int satScore=Integer.parseInt(br.readLine());
//30% of 100 will be 30
boolean pass=false;
if(satScore>100)
{
throw new DAOException("Invalid Test Score");
}
if(satScore>30)
{
pass=true;
}
c=DAOConnection.getConnection();
p=c.prepareStatement("insert into satresults (name,address,city,country,pincode,satScore,passed) values(?,?,?,?,?,?,?)");
p.setString(1,name);
p.setString(2,Address);
p.setString(3,City);
p.setString(4,Country);
p.setInt(5,pincode);
p.setInt(6,satScore);
p.setBoolean(7,pass);
p.executeUpdate();
p.close();
c.close();
System.out.println("Data inserted successfulyy ");
}catch(Exception e)
{
System.out.println(e.getMessage());
}

}
else if(choice==2)
{
//view all the data
//run (select * query here)


try
{
Connection c=DAOConnection.getConnection();
PreparedStatement p;
p=c.prepareStatement("select * from satresults");
ResultSet resultSet=p.executeQuery();
Set<satResults> data=new TreeSet<satResults>();
while(resultSet.next())
{
String name=resultSet.getString("name");           
String address=resultSet.getString("address");
String city=resultSet.getString("city");
String country=resultSet.getString("country");
int pincode=resultSet.getInt("pincode");
int satScore=resultSet.getInt("satScore");
boolean pass=resultSet.getBoolean("passed");
satResults s=new satResults(name,address,city,country,pincode,satScore,pass);
data.add(s);
}
for(satResults s:data)
{
System.out.println("Name: "+s.getName());
System.out.println("Address: "+s.getAddress());
System.out.println("City: "+s.getCity());
System.out.println("Country: "+s.getCountry());
System.out.println("PinCode: "+s.getPinCode());
System.out.println("SAT Score: "+s.getSatScore());
if(s.getPassed()) System.out.println("Status: Pass");
else System.out.println("Status: Fail");
System.out.println("--------------------------------");
}
if(data.size()==0)
{
c.close();
p.close();
resultSet.close();
throw new DAOException("No data present  in the database");
}
resultSet.close();
p.close();
c.close();
}catch(Exception e)
{
System.out.println(e.getMessage());
}





}
else if(choice==3)
{
//get rank from the data

try
{
System.out.println("Enter name of the student ");
String name=br.readLine();
if(name.length()==0)
{
throw new DAOException("Empty String for Name ");
}
Connection c=DAOConnection.getConnection();
PreparedStatement p;
p=c.prepareStatement("select name from satresults where name=?");
p.setString(1,name);
ResultSet resultSet=p.executeQuery();
if(resultSet.next()==false)
{
c.close();
p.close();
resultSet.close();
throw new DAOException("The given Name doesn't exist in the database");
}

p=c.prepareStatement("select * from satresults order by satScore desc");
resultSet=p.executeQuery();
int count=0;
while(resultSet.next())
{
String name2=resultSet.getString("name");
count++;
if(name2.equalsIgnoreCase(name))
{
break;
}
}

resultSet.close();
p.close();
c.close();
System.out.println("Data with Name  "+name+" got rank "+count);
}catch(Exception e)
{
System.out.println(e.getMessage());
}









}
else if(choice==4)
{
//update score
//
try
{
System.out.println("UPDATE Operation ");
System.out.println("Enter name of the student ");
String name=br.readLine();
System.out.println("Enter updated SAT Score out of 100 ");
int satScore=Integer.parseInt(br.readLine());
boolean passed=false;
if(satScore>100)
{
throw new DAOException("Invalid SAT score (score is above 100)");
}
if(satScore>30) passed=true;

if(name.length()==0)
{
throw new DAOException("Empty String ");
}
Connection c=DAOConnection.getConnection();
PreparedStatement p;
p=c.prepareStatement("select name from satresults where name=?");
p.setString(1,name);
ResultSet resultSet=p.executeQuery();
if(resultSet.next()==false)
{
c.close();
p.close();
resultSet.close();
throw new DAOException("The given Name doesn't exist in the database");
}

p=c.prepareStatement("update satresults set satScore=?,passed=? where name=?");
p.setInt(1,satScore);
p.setBoolean(2,passed);
p.setString(3,name);
p.executeUpdate();
resultSet.close();
p.close();
c.close();
System.out.println("Data with Name  "+name+" got updated with satScore "+satScore);
}catch(Exception e)
{
System.out.println(e.getMessage());
}




}
else if(choice==5)
{
//delete one record

try
{
System.out.println("Enter name of the student ");
String name=br.readLine();
if(name.length()==0)
{
throw new DAOException("Empty String ");
}
Connection c=DAOConnection.getConnection();
PreparedStatement p;
p=c.prepareStatement("select name from satresults where name=?");
p.setString(1,name);
ResultSet resultSet=p.executeQuery();
if(resultSet.next()==false)
{
c.close();
p.close();
resultSet.close();
throw new DAOException("Invalid Name ");
}
p=c.prepareStatement("delete from satresults where name=?");
p.setString(1,name);
p.executeUpdate();
resultSet.close();
p.close();
c.close();
System.out.println("Data with Name: "+name+" got deleted ");
}catch(Exception e)
{
System.out.println(e.getMessage());
}
}
else if(choice==6)
{
System.out.println("Ending the applications");
break;
}
else
{
System.out.println("Invalid Choice");
}

}


}catch(Exception e)
{
System.out.println("Invalid input");
}



}
}