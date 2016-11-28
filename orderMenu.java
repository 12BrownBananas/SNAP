import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class orderMenu {
	
	public ArrayList<menuItem> menu;
	int screenX = orderViewer.width/2;
	int screenY = orderViewer.height/5;
	int screenXoffset = 0; //280
	int screenYoffset = 0; //100
	int count = 1;
	public int page = 1;
	public int startIndex= 0;
	public int endIndex = 10;
	
	ArrayList<menuItem> currentList =new ArrayList<>();

	public orderMenu() throws NumberFormatException, IOException{
		
		this.menu = new ArrayList<>();
		

		for(int i = 1; i < 6; i++){
			Scanner scan = new Scanner(new File("menu0" + i + ".csv"));
			String header = scan.nextLine();
			
			while(scan.hasNextLine()){
				String[] line = scan.nextLine().split(",");
				menu.add(new menuItem(line[0], Integer.parseInt(line[1]), line[2], line[3]));
			}
			scan.close();
		}
		
		this.currentList = generateCurrent();
	}
	
	public ArrayList<menuItem> generateCurrent(){
		for(int index = startIndex; index<endIndex; index++){
			currentList.add(menu.get(index));
			menu.get(index).setX(screenX + screenXoffset);
			menu.get(index).setY(screenY + screenYoffset);
			if(count%2!=0){
				screenXoffset+=280;}
				else{screenXoffset=0; screenYoffset+=100;}
			count++;
		}
		for(menuItem item: currentList){
			item.setRect(new Rectangle(item.x, item.y, item.button.getWidth(), item.button.getHeight()));
			}
		
		return currentList;
	}
	
	public void increment(){
		this.startIndex +=10;
		this.endIndex +=10;
		this.page += 1;
		currentList.clear();
		resetPosition();
		generateCurrent();
		for(menuItem item: currentList){
			System.out.println(item.name);
		}
	}
	
	public void decrement(){
		this.startIndex -= 10;
		this.endIndex -= 10;
		this.page+=1;
		currentList.clear();
		resetPosition();
		generateCurrent();
		
	}
	
	public void resetPosition(){
		this.screenXoffset = 0; //280
		this.screenYoffset = 0; //100
	}
	
	

}