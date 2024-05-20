package pcd.lab12.rmi;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
        
public class Test01_Server  {
                
    public static void main(String args[]) {
        
        try {
            HelloService helloObj = new HelloServiceImpl();
            HelloService helloObjStub = (HelloService) UnicastRemoteObject.exportObject(helloObj, 0);

            Counter count = new CounterImpl(0); //Creo l'oggetto in locale
            Counter countStub = (Counter) UnicastRemoteObject.exportObject(count, 0); //Lo rendo disponibile al remoto in questo modo
            
            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry(); //Singleton che mi permette di interagire con il middleware per cercare un oggetto remoto
            
            registry.rebind("helloObj", helloObjStub); // Non riesce a registrare gli oggetti se non c'è il demone che gira
            registry.rebind("countObj", countStub); // è possibile "scoprire" il counter da remoto con questo nome
            
            System.out.println("Objects registered.");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}