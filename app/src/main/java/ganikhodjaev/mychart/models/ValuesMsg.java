package ganikhodjaev.mychart.models;

import org.json.JSONArray;
import org.json.JSONException;

public class ValuesMsg {

    private int CHANNEL_ID;//	integer	Channel ID
    private double BID;//float	Price of last highest bid
    private double BID_SIZE;//	float	Size of the last highest bid
    private double ASK;//	float	Price of last lowest ask
    private double ASK_SIZE;//	float	Size of the last lowest ask
    private double DAILY_CHANGE;//	float	Amount that the last price has changed since yesterday
    private double DAILY_CHANGE_PERC;//	float	Amount that the price has changed expressed in percentage terms
    private double LAST_PRICE;//	float	Price of the last trade.
    private double VOLUME;//	float	Daily volume
    private double HIGH;//	float	Daily high
    private double LOW;//	float	Daily low

    public ValuesMsg(int channelId, JSONArray jValues) {
        this.CHANNEL_ID = channelId;
        parse(jValues);

    }

    private void parse(JSONArray jValues) {
        try {
            BID = jValues.getDouble(0);
            BID_SIZE = jValues.getDouble(1);
            ASK = jValues.getDouble(2);
            ASK_SIZE = jValues.getDouble(3);
            DAILY_CHANGE = jValues.getDouble(4);
            DAILY_CHANGE_PERC = jValues.getDouble(5);
            LAST_PRICE = jValues.getDouble(6);
            VOLUME = jValues.getDouble(7);
            HIGH = jValues.getDouble(8);
            LOW = jValues.getDouble(9);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getCHANNEL_ID() {
        return CHANNEL_ID;
    }

    public double getBID() {
        return BID;
    }

    public double getBID_SIZE() {
        return BID_SIZE;
    }

    public double getASK() {
        return ASK;
    }

    public double getASK_SIZE() {
        return ASK_SIZE;
    }

    public double getDAILY_CHANGE() {
        return DAILY_CHANGE;
    }

    public double getDAILY_CHANGE_PERC() {
        return DAILY_CHANGE_PERC;
    }

    public double getLAST_PRICE() {
        return LAST_PRICE;
    }

    public double getVOLUME() {
        return VOLUME;
    }

    public double getHIGH() {
        return HIGH;
    }

    public double getLOW() {
        return LOW;
    }
}
