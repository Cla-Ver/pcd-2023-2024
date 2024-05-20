package pcd.lab12.mom.rabbitmq;

import com.rabbitmq.client.*;

public class Test1_Receive {

	private final static String QUEUE_NAME = "hello";

	public static void main(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		channel.basicQos(1); // accept only one unack-ed message at a time (see below)
		
		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			System.out.println(" [x] Received '" + message + "' by thread " + Thread.currentThread().getName());
			try {
				Thread.sleep(2000);
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				System.out.println(" [x] Done");
			    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			}
		};

		boolean autoAck = false; //Lo si mette false quando, ad esempio, si vuole mandare un ack solo quando il messaggio è stato sia ricevuto sia elaborato.
		//Serve ad esempio se il ricevitore va in crash: il mittente si tiene in memoria che non ha ricevuto un ack, permettendo di reinviare il messaggio quando il ricevitore torna online
		channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {});
	}
}
