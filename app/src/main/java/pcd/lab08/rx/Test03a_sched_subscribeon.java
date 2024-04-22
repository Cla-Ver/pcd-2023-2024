package pcd.lab08.rx;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Test03a_sched_subscribeon {

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
		
		System.out.println("\n=== TEST subscribeOn ===\n");

		/* 
		 * subscribeOn:
		 * 
		 * move the computational work of a flow on a specified scheduler
		 */
		Observable<Integer> src = Observable.just(100)	
			.map(v -> { log("map 1 " + v); return v * v; })		
			.map(v -> { log("map 2 " + v); return v + 1; });		
			//SubscribeOn fa eseguire la computazione a thread diversi
			//I due flussi vengono eseguiti concorrentemente su thread diversi
		src
			.subscribeOn(Schedulers.computation()) 	
			.subscribe(v -> {									
				log("sub 1 " + v);
			});

		src
			.subscribeOn(Schedulers.computation()) 	
			.subscribe(v -> {									
				log("sub 2 " + v);
			});

		Thread.sleep(100);
		
		System.out.println("\n=== TEST parallelism  ===\n");

		/* 
		 * Running independent flows on a different scheduler 
		 * and merging their results back into a single flow 
		 * warning: flatMap => no order in merging
		 */
		/*
		 * BlockingSubscribe è utile per osservare i valori prodotti, usando il thread chiamante (in questo caso il main). E' un punto di sincronizzazione.
		 */
		Flowable.range(1, 1000)
		  .flatMap(v ->
		      Flowable.just(v) // Vengono generati 1000 flussi, computati da vari thread in parallelo. La flatMap poi riunisce tutti i flussi in uno unico (senza preservarne l'ordine)
		        .subscribeOn(Schedulers.computation()) //Se non chiamo subscribeOn, la computazione viene eseguita dal main. In questo caso, il Flowable principale viene eseguito dal main, mentre tutti i flussi interni dai vari thread.
				.map(w -> { log("map " + w); return w * w; })		// by the RX comp thread;
		  )
		  .blockingSubscribe(v -> {
			 log("sub > " + v); 
		  });
		
	}
		
	static private void log(String msg) {
		System.out.println("[" + Thread.currentThread().getName() + "] " + msg);
	}
	
}
