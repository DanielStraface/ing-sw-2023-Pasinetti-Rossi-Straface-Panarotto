package it.polimi.ingsw;

public class Prova {
    public static void main(String[] args){
        /*new Thread(){
            @Override
            public void run(){
                String[] args = new String[1];
                args[0] = "2";
                try {
                    AppClientSocket.main(args);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();*/
        new Thread(()-> {
            AppClientImpl.startClient();
        }).start();
    }
}