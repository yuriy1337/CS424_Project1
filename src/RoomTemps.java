import processing.core.*;


public class RoomTemps extends PApplet {
    
    public void setup() {
        size(200, 200);
        background(0);
    }

    public void draw() {
        stroke(255);
        if (mousePressed) {
            line(mouseX, mouseY, pmouseX, pmouseY);
        }
    }
    
    public static void main(String args[]) {
        RoomTempParser rm = new RoomTempParser();
        
        rm.parseData("C:\\Users\\Yuriy\\Downloads\\history.txt\\history_small.txt");
        //PApplet.main(new String[] { "--present", "RoomTemp" });
      }

}
