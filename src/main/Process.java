public abstract class Process {
	// make list of channels and make it like how cloud assign
	protected Object response;

	// sends data repeatedly until a response is received
	public synchronized void send(Object data, Channel channel) {
		try {
			this.response = null;
			while (this.response == null) {
				channel.send(data, this);
				this.wait(Channel.timeout);
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public synchronized void receive(Object response, Channel channel) {
		this.response = response;
		this.notify();
	}

}
