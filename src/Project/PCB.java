package Project;

//PCB class creates process from random values 
public class PCB {
    private String id;
    private int size;
    private int lifeTime;
    private int index;

    //Process constructor creates process based on id, size and life time
    public PCB(int index, String id, int size, int lifeTime){
        this.id = id;
        this.size = size;
        this.lifeTime = lifeTime;
        this.index = index;
    }

    //Overloading to create a "process" which will represent the free holes
    public PCB(int index, int size){
        this.id = "Free";
        this.size = size;
        this.index = index;
    }
    
    //Gets the id
    public String getId()
    {
        return this.id;
    }

    //Sets the id
    public void setId(String id){
        this.id = id;
    }
    
    //Gets the size
    public int getSize(){
        return this.size;
    }
    
    //Sets the size
    public void setSize(int size){
        this.size = size;
    }

    //Gets the life time
    public int getLifeTime(){
        return this.lifeTime;
    }
    
    //Sets the life time
    public void setLifeTime(int lifeTime){
        this.lifeTime = lifeTime;
    }

    //Get the index of a hole
    public int getIndex(){
        return this.index;
    }

    //Set the index of a hole
    public void setIndex(int i){
        this.index = i;
    }

    @Override
    public String toString(){
        String str;
        if(id.equals("Free")){
            str = "| Free " + "(" + size + "KB)"; 
        }
        else{
            str = "| " + id + " [" + lifeTime + "s] (" + size + "KB) "; 
        }
        return str;
    }
}
