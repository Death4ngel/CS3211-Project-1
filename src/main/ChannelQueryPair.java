public class ChannelQueryPair<Channel, Query> {
	private final Channel channel;
	private final Query query;

	public ChannelQueryPair(Channel channel, Query query) {
		this.channel = channel;
		this.query = query;
	}

	public Channel getChannel() {
		return channel;
	}

	public Query getQuery() {
		return query;
	}
}
