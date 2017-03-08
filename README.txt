List of the files used:

cs455.scaling.client.Client: Client deals with parsing server messages and sending 8kb data to server.
cs455.scaling.client.SenderThread: Sends data to server at a specified message rate.

cs455.scaling.server.Sserver: Server deals with parsing client messages and sending hashcodes back to clients.

cs455.scaling.util.ClientMessageTracker: Tracks messages sent and received along with polls data every 10 seconds.
cs455.scaling.util.ServerMessageTracker: Tracks throughput of messages, tracks active connections, and polls data every 5 seconds.
cs455.scaling.util.HashingFunction: Singleton that creates a SHA-1 hash from a byte array of data.

cs455.scaling.threads.Task: Abstract class that all tasks extend.
cs455.scaling.threads.DummyTask: Task that waits a random time up to 10 seconds for testing ThreadPoolManager.
cs455.scaling.threads.ReadTask: Task that reads 8kb data from a ByteBuffer on a SocketChannel and creates a WriteTask for ThreadPoolManager.
cs455.scaling.threads.WriteTask: Task that writes a string to the client from the server.
cs455.scaling.threads.TestThreadPool: Class that tests the ThreadPool implementation.
cs455.scaling.threads.ThreadPool: Class that manages adding and removing Worker threads from ThreadPool.
cs455.scaling.threads.ThreadPoolManager: Class that maintains Tasks as a FIFO and ThreadPool adding/removing Worker threads.
cs455.scaling.threads.Worker: Class that runs tasks and runs as a thread.

------------

Other important information:

Starting the server is done by: java cs455.scaling.server.Server [Port-Number] [Number-of-Threads]
Staritng the client is done by: java cs455.scaling.client.Client [Server-IP] [Server-Port] [Message-Rate]

While testing I used below test-scalability-online.sh where the IP used is madison.cs.colostate.edu on port 2000. I did run into a problem where my machine wouldn't ssh into certain servers
I was stuck running around 95 active Clients but it was hitting the throughput so I would imagine another 5 at that point wouldn't be too much of a slowdown.

CLASSES=ScalingServer
SCRIPT="cd $CLASSES;
java -cp . cs455.scaling.client.Client 129.82.44.156 2000 4 > "

#$1 is the command-line argument
for ((j=1; j<=$1; j++));
do
	for i in `cat machine_list`
	do
		echo 'logging into '$i
		FOLDER="/tmp/$USER/cs455/HW2-PC"
		FILE="$FOLDER/$j"
		ssh $i "mkdir -p $FOLDER;touch $FILE;$SCRIPT$FILE &"
	done
	eval $COMMAND &
done


If you have any questions you can reach me via phone at (707) 599-1696 or email at renaldorini@gmail.com
