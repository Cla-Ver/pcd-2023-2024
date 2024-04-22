package pcd.lab08.rx;

import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Test03c_sched_callable {

	public static void main(String[] args) throws Exception {

		System.out.println("\n=== TEST fromCallable | schedulers ===\n");
		//Chiamata asincrona con cui il risultato è un flusso
		Flowable.fromCallable(() -> {
		    log("started.");
		    Thread.sleep(1000); 
		    log("completed.");
		    return "Done";
		})
		.subscribeOn(Schedulers.io()) 		// use a background thread for the callable // Faccio fare la chiamata ad un pool di thread io
		.observeOn(Schedulers.single())		// use a single UI thread-like for subscribers //Si può creare un pool di thread con un solo elemento per tutto ciò che segue. E' una sorta di merge
		.subscribe(s -> {
			log("result: " + s);
		});

		
		Thread.sleep(2000); // <--- wait for the flow to finish
	}
	
	static private void log(String msg) {
		System.out.println("[ " + Thread.currentThread().getName() + "  ] " + msg);
	}

}
