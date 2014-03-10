public abstract class Process {
	// make list of channels and make it like how cloud assign

	// sends data repeatedly until a response is received
	public synchronized void send(Object data, Channel channel) {
		try {
			while (channel.getResponse(this) == null) {
				channel.send(data, this);
				this.wait(Channel.timeout);
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	
	public synchronized void receive(Channel channel) {
		this.notify();
	}
	
	public abstract void println(String s);
}
