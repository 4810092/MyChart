package ganikhodjaev.mychart.models;

public class SubscribeMsg {

    private String event;
    private String channel;
    private String symbol;

    public SubscribeMsg(String event, String channel, String symbol) {
        this.event = event;
        this.channel = channel;
        this.symbol = symbol;
    }

    public SubscribeMsg() {
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }


    public static final String EVENT_SUBSCRIBE = "subscribe";
    public static final String CHANNEL_TICKER = "ticker";

    public static final String SYMBOL_BTCUSD = "btcusd";
    public static final String SYMBOL_BTCEUR = "btceur";
}
