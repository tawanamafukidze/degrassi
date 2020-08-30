import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReadInArray {
	
	
	Array Arr[];
	int size;
	Connection con = new DatabaseConnection().getConnection();
	PreparedStatement pst;
	
	
	
	public void inputArray() {
		// TODO Auto-generated method stub
		try {
			
			pst = con.prepareStatement("Select * from games");
			ResultSet rs = pst.executeQuery();
			
			while(rs.next()) {
	            Array title =   rs.getArray("Title");
	             Arr [size] = title;
	            size++;
				}
			
		}catch(SQLException e) {
			System.out.println("Data not found");
			
		}
		
	}
	
public ArrayList getStars() {
		
		ArrayList stars = new ArrayList();
		for(int i = 0; i < size; i++) {
			stars.add(Arr[i]);
		}
		System.out.println(stars);
		return stars;
	}


public void DisplayArrayList() {
	System.out.println(getStars());
}
}

	
	





