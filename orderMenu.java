import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class orderMenu {
	
	public ArrayList<menuItem> menu;

	public orderMenu() throws FileNotFoundException{
		
		this.menu = new ArrayList<>();
		
		for(int i = 1; i < 6; i++){
			Scanner scan = new Scanner(new File("menu0" + i + ".csv"));
			String header = scan.nextLine();
			
			while(scan.hasNextLine()){
				String[] line = scan.nextLine().split(",");
				menu.add(new menuItem(line[0], Integer.parseInt(line[1]), line[2], line[3]));
			}
			
			
		}
	}
	
	

}