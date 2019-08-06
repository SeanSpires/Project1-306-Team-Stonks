package sample.Model;

public class Processor {
    private int processorNumber;

    public Processor(int number){
        this.processorNumber = number;
    }

    public int getProcessorNumber(){
        return this.processorNumber;
    }

    public void setProcessorNumber(int number){
        this.processorNumber = number;
    }
}
