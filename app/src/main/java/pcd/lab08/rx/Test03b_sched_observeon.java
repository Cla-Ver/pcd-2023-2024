package pcd.lab08.rx;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Test03b_sched_observeon {

	public static void main(String[] args) throws Exception {

		System.out.println("\n=== TEST No schedulers ===\n");
		
		/*
		 * Without using schedulers, by default all the computation 
		 * is done by the calling thread.
		 * 
		 */
		Observable.just(100)	
			.map(v -> { log("map 1 " + v); return v * v; })
			.map(v -> { log("map 2 " + v); return v + 1; })	
			.subscribe(v -> {					
				log("sub " + v);
			});
		
		System.out.println("\n=== TEST observeOn ===\n");

		/* 
		 * observeOn:
		 * 
		 * move the downstream computation to the specified scheduler
		 * Ha sempre lo stesso obiettivo, però lo si fa a partire da un punto della catena downstream.
		 * Con subscribeOn si sposta l'intera catena, con observeOn solo da lì in avanti
		 * NB: il risultato finale, in ogni caso, non cambia
		 */
		Observable.just(100)	
			.map(v -> { log("map 1 " + v); return v * v; })		// by the current thread (main thread) //Viene eseguito sul thread attuale
			.observeOn(Schedulers.computation()) 				// => use RX comp thread(s) downstream
			.map(v -> { log("map 2 " + v); return v + 1; })		// by the RX comp thread				//Viene eseguito su un altro thread
			.subscribe(v -> {						// by the RX comp thread
				log("sub " + v);
			});

		Thread.sleep(100);

		System.out.println("\n=== TEST observeOn with blockingSubscribe ===\n");
			//Si possono combinare
		Observable.just(100)	
			.map(v -> { log("map 1 " + v); return v * v; })		// by the current thread (main thread) //Parte con il main
			.observeOn(Schedulers.computation()) 				// => use RX comp thread(s) downstream //Esegue su altri thread
			.map(v -> { log("map 2 " + v); return v + 1; })		// by the RX comp thread
			.blockingSubscribe(v -> {							// by the current thread (main thread = invoker) //Viene eseguito nuovamente dal thread chiamante (main)
				log("sub " + v);
			});

		
		System.out.println("\n=== TEST observeOn with multiple subs ===\n");

		Observable<Integer> src2 = Observable.just(100)	
				.map(v -> { log("map 1 " + v); return v * v; })		// by the current thread (main thread)
				.observeOn(Schedulers.computation()) 				// => use RX comp thread(s) downstream
				.map(v -> { log("map 2 " + v); return v + 1; });		// by the RX comp thread;
		
		src2.subscribe(v -> {						// by the RX comp thread
			log("sub 1 " + v);
		});

		src2.subscribe(v -> {						// by the RX comp thread
			log("sub 2 " + v);
		});

		Thread.sleep(100);

		
	}
		
	static private void log(String msg) {
		System.out.println("[" + Thread.currentThread().getName() + "] " + msg);
	}
	
}
