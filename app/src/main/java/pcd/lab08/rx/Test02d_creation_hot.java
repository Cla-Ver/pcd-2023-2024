package pcd.lab08.rx;

import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.flowables.ConnectableFlowable;
import io.reactivex.rxjava3.observables.ConnectableObservable;

public class Test02d_creation_hot {

	public static void main(String[] args) throws Exception {

		System.out.println("\n=== TEST Hot streams  ===\n");
		
		Observable<Integer> source = Observable.create(emitter -> {		     
			new Thread(() -> {
				int i = 0;
				while (i < 200){
					try {
						log("source: "+i); 
						emitter.onNext(i);
						Thread.sleep(10);
						i++;
					} catch (Exception ex){}
				}
			}).start();
		     //emitter.setCancellable(c::close);
		 });
		//Questo invece è chiamato HOT perchè inizia a computare anche se non ci sono sottoscrittori.
		//Si fa un publish sul flusso e si chiama connect per renderlo hot.
		//Si può vedere, tramite le stampe, che all'inizio c'è solo SOURCE, e solamente dopo viene sottoscritto un altro oggetto, che riceve solo i numeri che produce da quel punto in poi
		//Questo può avere il vantaggio che potrei voler osservare un fenomeno il cui flusso esiste a priori e non può essere rigenerato, o se non si vuole perdere elementi.
		//Anche se, in generale, sono più utili i flussi cold
		ConnectableObservable<Integer> hotObservable = source.publish();
		hotObservable.connect();
	
		/* give time for producing some data before any subscription */
		Thread.sleep(500);
		
		log("Subscribing A.");
		
		hotObservable.subscribe((s) -> {
			log("subscriber A: "+s);
			// Thread.sleep(5000);
		});	
		
		/* give time for producing some data before second subscriber */
		Thread.sleep(500);
		
		log("Subscribing B.");
		
		hotObservable.subscribe((s) -> {
			log("subscriber B: "+s); 
		});	
		
		log("Done.");
		
		Thread.sleep(10_000);

	}
	
	static private void log(String msg) {
		System.out.println("[ " + Thread.currentThread().getName() + "  ] " + msg);
	}
	

}
