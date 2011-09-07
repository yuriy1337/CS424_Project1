import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;


final public class Utils {

    private Utils() {
        throw new AssertionError();
    }
    
    public static Float getArrayListMax(ArrayList<Float> a){
        if (a.size() == 0) return null;
        float max = a.get(0);
        for (Float f1 : a) {
            if(f1 > max){
                max = f1;
            }
        }
        return max;
    }
    
    public static Float getArrayListMin(ArrayList<Float> a){
        if (a.size() == 0) return null;
        float min = a.get(0);
        for (Float f1 : a) {
            if(f1 < min){
                min = f1;
            }
        }
        return min;
    }
    
    public static int[] floatArrToIntArr(ArrayList<Float> a){
        int[] intArr = new int[a.size()];
        for (int i = 0; i < a.size(); i++) {
            intArr[i] = Math.round(a.get(i));
        }
        return intArr;
    }
    
    //this is bad, but I am testing stuff...
    public static Integrator[] AppendItemToArray(Integrator[] array, Integrator newValue)
    {
        List<Integrator> list = new ArrayList<Integrator>();
        Collections.addAll(list, array);
        list.add(newValue);
        Integrator[] i = new Integrator[list.size()];
        list.toArray(i);

        return i;
    }

    
    public static <T extends Comparable>  void sort(T[] theArray) {  
        
        int smallest;  
          
        for (int i = 0; i < theArray.length; i++) {  
            smallest = i;  
              
            for (int index = i + 1; index < theArray.length; index++) {  
                  
                if (theArray[index].compareTo(theArray[smallest]) < 0) {  
                    smallest = index;  
                }  
                      
                T temp = theArray[i];  
                theArray[i] = theArray[smallest];  
                theArray[smallest] = temp;  
            } // end inner for  
        } // end outer for  
    } // end method sort  

}
