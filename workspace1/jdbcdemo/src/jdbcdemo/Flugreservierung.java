package jdbcdemo;

import javax.swing.*;
import java.sql.*;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Flugreservierung extends JFrame{
	
	
    JComboBox abflug = new JComboBox();
    JComboBox zielflug = new JComboBox();
    JComboBox flug = new JComboBox();
    
    JPanel panel = new JPanel();
    
    Connection con;
    
    Statement st;
    ResultSet rs;
    
    Statement s2;
    ResultSet rs2;
    
    public Flugreservierung() {
    	
    JLabel lblAbflug = new JLabel("Abflug--->");
    lblAbflug.setHorizontalAlignment(SwingConstants.CENTER);
    lblAbflug.setFont(new Font("Tahoma", Font.PLAIN, 20));
    panel.add(lblAbflug);
    
    this.getContentPane().add(panel);
    this.setVisible(true);
    this.setSize(1042, 666);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    
    
    panel.add(abflug);
    panel.add(zielflug);
    panel.add(flug);
    
    JLabel lblZielflug = new JLabel("<---Zielflug");
    lblZielflug.setFont(new Font("Tahoma", Font.PLAIN, 20));
    panel.add(lblZielflug);
    
    JButton btnSearch = new JButton("Search"); 
    btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 20));
    panel.add(btnSearch);
    
    try{
		
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/flightdata", "root", "Butz");
		st = con.createStatement();
      
		String s = "select * from airports";
		rs = st.executeQuery(s);
      
		while(rs.next())
        {
        	abflug.addItem(rs.getString(2));
        	zielflug.addItem(rs.getString(2));
        }
        
    }catch(Exception e){
    	
        JOptionPane.showMessageDialog(null, "ERROR");
        
    }finally{
    	
        try{

        }catch(Exception e){
        	
            JOptionPane.showMessageDialog(null, "ERROR CLOSE");
        }
        
    }

    btnSearch.addActionListener(new ActionListener() {	
    	
    public void actionPerformed(ActionEvent e) {
    		try {
    			
				s2 = con.createStatement();
				rs2 = s2.executeQuery("select * from flights,(select airportcode as 'depcode' from airports "
										+ "WHERE name='"+abflug.getSelectedItem()+"')dep,(select airportcode as 'arrcode' from airports WHERE name='"+zielflug.getSelectedItem()+"')arr "
										+ "WHERE depcode = departure_airport AND arrcode = destination_airport;");
				
				while(rs2.next()) {
					flug.addItem("Airline: " + rs2.getString(1) + " --- Flightnr: " + rs2.getInt(2) + " --- Depature time: " + rs2.getDate(3) + " " + rs2.getTime(3) + " --- Depature Airport: " + rs2.getString(4) + " --- Destination Time: " + rs2.getDate(5) + " " + rs2.getTime(5) + " --- Destination Ariport: " + rs2.getString(6) + " --- Planettype: " + rs2.getInt(7));
				}
				
				 	s2.close();
		            rs2.close();
		            con.close();
		            
    		} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	  		
    	}
    });
    
    }

    public static void main(String[] args){
            new Flugreservierung();
    }

}