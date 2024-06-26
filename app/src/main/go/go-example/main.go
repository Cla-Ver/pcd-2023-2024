package main

import (
	"fmt"
	"time"
)

func main() {
	fmt.Println("Booted.")

	numAgents := 1000000

	time0 := time.Now()

	/* a slice of channels */
	var channels []chan Msg //è come un array di canali la cui dimensione è ignota e può variare nel tempo

	/* spawning agents */
	for i := 0; i < numAgents; i++ {
		agentId := fmt.Sprintf("agent-%d", i)
		ch := spawnMyAgent(agentId) //Restituisce il canale con cui comunicare con quell'agente

		/* collecting agent channels */
		channels = append(channels, ch) //
	}

	/* receiving messages */
	for i := 0; i < len(channels); i++ {
		msg := <-channels[i]
		fmt.Printf("%s from %s at %s\n", msg.content, msg.senderId, msg.time.String())
	}

	time1 := time.Now()
	elapsed := time1.Sub(time0).Milliseconds()

	fmt.Printf("Done in: %d\n", elapsed)

}
