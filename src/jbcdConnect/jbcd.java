package jbcdConnect;
import java.sql.*;
import java.util.Scanner;
 class jbcdConnect{
	 Connection connection = null;
	jbcdConnect(){
		try {
        	connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/HangMan", "gopika", "1234");
//            PreparedStatement statement = connection.prepareStatement("select * from Persons");
//            ResultSet resultSet = statement.executeQuery("select * from `Hobbylist`");
//            while (resultSet.next()) {
//                System.out.println(resultSet.getInt(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3));
//                ;
//
//            }
//            System.out.println("Connected to the database!");
//            
//            
//            connection.close();
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        
	}
	}
	
	void getConnection() {
		
	}
}

	




