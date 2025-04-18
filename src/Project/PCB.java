package Project;

//PCB class creates process from random values 
public class PCB {
    private int id;
    private int size;
    private int lifeTime;

    //Process constructor creates process based on id, size and life time
    public PCB(int id, int size, int lifeTime){
        this.id = id;
        this.size = size;
        this.lifeTime = lifeTime;
    }
    
    //Gets the id
    public int getId()
    {
        return this.id;
    }

    //Sets the id
    public void setId(int id){
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
        String str = "| P" + id + " [" + lifeTime + "s] (" + size + "KB) "; 
        return str;
    }
}
