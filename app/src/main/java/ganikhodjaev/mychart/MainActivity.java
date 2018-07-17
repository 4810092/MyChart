package ganikhodjaev.mychart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ganikhodjaev.mychart.models.EventResponse;
import ganikhodjaev.mychart.models.SubscribeMsg;
import ganikhodjaev.mychart.models.ValuesMsg;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class MainActivity extends AppCompatActivity {

    LineChart chart;


    List<Entry> entriesBTCUSD = new ArrayList<>();
    List<Entry> entriesBTCEUR = new ArrayList<>();

    LineDataSet dataSetBTC;
    LineDataSet dataSetBTCEUR;

    Gson gson;

    long runTime = System.currentTimeMillis();

    ArrayList<EventResponse> channels = new ArrayList<>();
    ArrayList<ValuesMsg> vm = new ArrayList<>();
    private LineData lineData = new LineData();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gson = new Gson();

        setupSocketWithHttp();

        chart = findViewById(R.id.chart);

    }

    private void setupSocketWithHttp() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url("wss://api.bitfinex.com/ws/2").build();
        WebSocketListener listener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);

                subscribeToTicker(webSocket);


            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);

                if (text.startsWith("[")) {
                    onTickReceive(text);
                } else {
                    onInfoMsgReceive(text);
                }
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
                super.onFailure(webSocket, t, response);
            }
        };
        WebSocket ws = client.newWebSocket(request, listener);

    }

    private void onInfoMsgReceive(String text) {
        EventResponse event = gson.fromJson(text, EventResponse.class);
        if (event.isSubscribed()) channels.add(event);

    }

    private void onTickReceive(String msg) {
        try {
            JSONArray jsonArray = new JSONArray(msg);
            JSONArray jValues = jsonArray.optJSONArray(1);
            if (jValues == null) return;

            ValuesMsg valuesMsg = new ValuesMsg(jsonArray.getInt(0), jValues);
            vm.add(valuesMsg);

            switch (getSymbolByChannelId(valuesMsg.getCHANNEL_ID())) {
                case SubscribeMsg.SYMBOL_BTCUSD:
                    entriesBTCUSD.add(new Entry(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - runTime), (float) valuesMsg.getBID()));
                    updBTCUSDChart();
                    break;
                case SubscribeMsg.SYMBOL_BTCEUR:
                    entriesBTCEUR.add(new Entry(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - runTime), (float) valuesMsg.getBID()));
                    updBTCEURChart();
                    break;

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getSymbolByChannelId(int channel_id) {
        for (EventResponse er : channels) {
            if (er.getChanId() == channel_id) return er.getPair().toLowerCase();
        }
        return "";
    }

    private void subscribeToTicker(WebSocket webSocket) {
        SubscribeMsg subsBTCUSD = new SubscribeMsg(
                SubscribeMsg.EVENT_SUBSCRIBE,
                SubscribeMsg.CHANNEL_TICKER,
                SubscribeMsg.SYMBOL_BTCUSD);

        webSocket.send(gson.toJson(subsBTCUSD));

        SubscribeMsg subsBTCEUR = new SubscribeMsg(
                SubscribeMsg.EVENT_SUBSCRIBE,
                SubscribeMsg.CHANNEL_TICKER,
                SubscribeMsg.SYMBOL_BTCEUR);

        webSocket.send(gson.toJson(subsBTCEUR));
    }


    private void updBTCUSDChart() {

        lineData.removeDataSet(dataSetBTC);

        dataSetBTC = new LineDataSet(entriesBTCUSD, "BTC_USD");
        dataSetBTC.setColor(Color.GREEN);
        dataSetBTC.setCircleColor(Color.GREEN);
        lineData.addDataSet(dataSetBTC);
        chart.setData(lineData);
        chart.invalidate();
    }


    private void updBTCEURChart() {

        lineData.removeDataSet(dataSetBTCEUR);

        dataSetBTCEUR = new LineDataSet(entriesBTCEUR, "BTC_UER");
        lineData.addDataSet(dataSetBTCEUR);
        chart.setData(lineData);
        chart.invalidate();
    }
}
