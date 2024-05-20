package pcd.lab12.rmi;

import java.io.Serializable;
//Serializable permette di ricavare una sequenza di byte che rappresenta gli oggetti della classe. E' ricorsivo.
public class Message implements Serializable   {
	
	private String content;
	
	public Message(String s){
		content = s;
	}
	
	public String getContent(){
		return content;
	}
}
