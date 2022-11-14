public class MainClass {
    public static void main(String[] args) {
        Get_bilibili function=new Get_bilibili();
        Thread myThread=new Thread(function);
        myThread.start();
    }
}
