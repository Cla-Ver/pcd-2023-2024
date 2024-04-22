package pcd.lab08.rx;

import io.reactivex.rxjava3.core.*;

public class Test02b_creation_async {

	public static void main(String[] args) throws Exception {

		log("Creating an observable (cold) using its own thread.");
		//Viene creato un altro thread che funge da generatore.
		//Sono chiamati "cold" perchè la creazione degli elementi inizia solamente quando c'è almeno un subscriber, che avviene in un altro thread
		Observable<Integer> source = Observable.create(emitter -> {		     
			new Thread(() -> {
				int i = 0;
				while (i < 20){
					try {
						log("source: "+i); 
						emitter.onNext(i);
						Thread.sleep(200);
						i++;
					} catch (Exception ex){}
				}
				emitter.onComplete();
			}).start();
		 });

		Thread.sleep(1000);
		
		log("Subscribing A.");
		//Subscribe, in questo caso, ritorna subito: questo perchè a creare gli elementi è un altro thread e non il main.
		//Quindi rende il programma asincrono
		source.subscribe((s) -> {
			log("Subscriber A: " + s); 
		});	

		Thread.sleep(1000);

		log("Subscribing B.");

		source.subscribe((s) -> {
			log("Subscriber B: " + s); 
		});	

		log("Done.");
	}
	
	static private void log(String msg) {
		System.out.println("[ " + Thread.currentThread().getName() + "  ] " + msg);
	}

}