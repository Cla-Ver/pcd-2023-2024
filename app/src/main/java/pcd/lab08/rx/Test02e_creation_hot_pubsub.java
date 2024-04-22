package pcd.lab08.rx;

import io.reactivex.rxjava3.subjects.PublishSubject;

public class Test02e_creation_hot_pubsub {

	public static void main(String[] args) throws Exception {

		System.out.println("\n=== TEST Hot streams with pubsub ===\n");

		/* Subjects: bridges functioning both as observer and observable */ 
		// Trick che viola le regole degli stream, creando un buffer che contenga gli elementi. Non viene specificata la lambda
		PublishSubject<Integer> source = PublishSubject.<Integer>create();
		 
		log("subscribing.");
		//Viene fatta la sottoscrizione ancora prima che gli elementi vengano generati
		source.subscribe((s) -> {
			log("subscriber A: "+s); 
		}, Throwable::printStackTrace);
		 
		log("generating.");

		new Thread(() -> {
				int i = 0;
				while (i < 100){
					try {
						log("source: "+i); 
						source.onNext(i); // Inizio a mettere gli elementi qui
						Thread.sleep(10);
						i++;
					} catch (Exception ex){}
				}
			}).start();
		

		log("waiting.");

		Thread.sleep(100);
		//Dopo un po' si sottoscrive anche B, e gli elementi vengono solo percepiti dopo senza ricominciare il flusso da capo
		source.subscribe((s) -> {
			log("subscriber B: "+s); 
		}, Throwable::printStackTrace);

	}
	
	static private void log(String msg) {
		System.out.println("[ " + Thread.currentThread().getName() + "  ] " + msg);
	}
	

}
