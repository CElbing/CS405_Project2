package Project;

//PCB class creates process from random values 
public class PCB {
    private String id;
    private int size;
    private int lifeTime;

    //Process constructor creates process based on id, size and life time
    public PCB(String id, int size, int lifeTime){
        this.id = id;
        this.size = size;
        this.lifeTime = lifeTime;
    }

    //Overloading to create a "process" which will represent the free holes
    public PCB(int size){
        this.id = "Free";
        this.size = size;
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
    public void setLifeTme(int lifeTime){
        this.lifeTime = lifeTime;
    }

    @Override
    public String toString(){
        String str;
        if(id.equals("Free")){
            str = "| Free " + "(" + size + "KB) |"; 
        }
        else{
            str = "| " + id + " [" + lifeTime + "s] (" + size + "KB) |"; 
        }
        return str;
    }
}
