package ua.com.oilversusruble.oilversusruble;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ua.com.oilversusruble.oilversusruble.ui.LoginDialog;
import ua.com.oilversusruble.oilversusruble.ui.PricesFragment;
import ua.com.oilversusruble.oilversusruble.ui.RegDialog;


public class MainActivity extends FragmentActivity implements SlidingUpPanelLayout.PanelSlideListener, View.OnClickListener {

    FrameLayout rubContainer, oilContainer, uahContainer, headerContainer, signInLayout, sendLayout;
    LinearLayout chatContainer;
    PricesFragment rubFragment, oilFragment, uahFragment;
    SlidingUpPanelLayout slidingUpLayout;
    EditText etNewMessage;
    ImageView imageSend;
    Button btnLogin, btnRegister, btnUpd;
    Context context;

    LayoutInflater inflater;

    Updater updater;

    boolean isHeaderPrice;


    Price rubPrice, oilPrice, uahPrice;
    ArrayList<ChatMessage> chatHistory = new ArrayList<>();


    Stock usdUah;
    Stock eurUah;
    Stock rubUsd;
    Stock rubEur;
    Stock brent;
    Stock wti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        rubContainer = (FrameLayout) findViewById(R.id.rubContainer);
        oilContainer = (FrameLayout) findViewById(R.id.oilContainer);
        uahContainer = (FrameLayout) findViewById(R.id.uahContainer);
        signInLayout = (FrameLayout) findViewById(R.id.signInLayout);
        sendLayout = (FrameLayout) findViewById(R.id.sendLayout);
        headerContainer = (FrameLayout) findViewById(R.id.headerContainer);
        chatContainer = (LinearLayout) findViewById(R.id.chatContainer);
        slidingUpLayout = (SlidingUpPanelLayout) findViewById(R.id.slidingLayout);
        etNewMessage = (EditText) findViewById(R.id.etNewMessage);
        imageSend = (ImageView) findViewById(R.id.imageSend);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnUpd = (Button) findViewById(R.id.btnUpd);

        imageSend.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnUpd.setOnClickListener(this);
        etNewMessage.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    etNewMessage.setText("");
                    sendMessage(etNewMessage.getText().toString());
                    getChat();
                    return true;
                }
                return false;
            }
        });


        rubFragment = new PricesFragment();
        oilFragment = new PricesFragment();
        uahFragment = new PricesFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();


        fragmentTransaction.add(R.id.rubContainer, rubFragment);
        fragmentTransaction.add(R.id.oilContainer, oilFragment);
        fragmentTransaction.add(R.id.uahContainer, uahFragment);
        fragmentTransaction.commit();

        inflater = getLayoutInflater();
        context = this;
        updater = new Updater(this);

        try {
            getPrices();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        updater.startUpdater();


    }


    protected void getPrices() throws ParseException {


                ParseQuery<ParseObject> query = ParseQuery.getQuery("PricesHistory");
                query.orderByDescending("createdAt");
                query.setLimit(40);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(final List<ParseObject> list, ParseException e) {

                        Calendar c = Calendar.getInstance();
                        long diff = c.getTimeInMillis() - list.get(0).getCreatedAt().getTime();
                        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);


                        if (minutes > 60) {
                            PriceUpdater priceUpdater = new PriceUpdater(){
                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    getPricesFromParse(list);
                                }
                            };
                            priceUpdater.execute();
                        }else{
                            getPricesFromParse(list);
                        }


                    }
                });





    }


    @Override
    public void onPanelSlide(View view, float v) {

    }

    @Override
    public void onPanelCollapsed(View view) {

    }

    @Override
    public void onPanelExpanded(View view) {

    }

    @Override
    public void onPanelAnchored(View view) {

    }

    @Override
    public void onPanelHidden(View view) {

    }


    void changeHeader() {
        if (isHeaderPrice) {
            headerContainer.removeAllViews();
            TextView lastMessage = new TextView(this);

        }

    }


    void getChat() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ChatHistory");
        query.addAscendingOrder("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> chatList, ParseException e) {
                if (e == null) {
                    chatHistory.clear();
                    for (ParseObject parseObject : chatList) {
                        ChatMessage chatMessage = new ChatMessage(parseObject.getParseUser("author"), parseObject.getString("message"), parseObject.getCreatedAt());
                        chatHistory.add(chatMessage);

                    }
                    publishChat();


                } else {
                    Log.d("[Chat] ", "Error: " + e.getMessage());
                }
            }
        });

    }

    void publishChat() {
        chatContainer.removeAllViews();
        for (ChatMessage chatMessage : chatHistory) {
            if (ParseUser.getCurrentUser() == chatMessage.getAuthor()) {
                View view = inflater.inflate(R.layout.my_chat_message, null, false);
                TextView tvTitle = (TextView) view.findViewById(R.id.tvMyTitle);
                TextView tvMessage = (TextView) view.findViewById(R.id.tvMyMessage);
                tvTitle.setText(chatMessage.getDate().getHours() + ":" + chatMessage.getDate().getMinutes() + " (Me)");
                tvMessage.setText(chatMessage.getMessage());
                chatContainer.addView(view);
            } else {
                View view = inflater.inflate(R.layout.chat_message, null, false);
                TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
                TextView tvMessage = (TextView) view.findViewById(R.id.tvMessage);
                tvTitle.setText(chatMessage.getDate().getHours() + ":" + chatMessage.getDate().getMinutes() + " Author: " + chatMessage.getAuthor());
                tvMessage.setText(chatMessage.getMessage());
                chatContainer.addView(view);
            }
        }
    }

    void sendMessage(String message) {
        if (!message.isEmpty()) {
            ParseObject gameScore = new ParseObject("ChatHistory");
            gameScore.put("message", message);
            gameScore.put("author", ParseUser.getCurrentUser());
            gameScore.saveInBackground();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageSend:
                etNewMessage.setText("");
                sendMessage(etNewMessage.getText().toString());
                YoYo.with(Techniques.Pulse)
                        .playOn(imageSend);
                getChat();
                break;
            case R.id.btnLogin:
                showDialog(false);
                break;
            case R.id.btnRegister:
                showDialog(true);
                break;
            case R.id.btnUpd:
                try {
                    getPrices();

                } catch (ParseException e) {
                    Toast.makeText(this, "Internet Error", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    void showDialog(boolean isRegister) {
        if (isRegister) {
            RegDialog regDialog = new RegDialog(this);
            regDialog.show();
        } else {
            LoginDialog loginDialog = new LoginDialog(this);
            loginDialog.show();
        }


    }

    public void updateUserParams() {
        if (ParseUser.getCurrentUser() != null) {
            sendLayout.setVisibility(View.VISIBLE);
            signInLayout.setVisibility(View.GONE);
        } else {
            sendLayout.setVisibility(View.GONE);
            signInLayout.setVisibility(View.VISIBLE);
        }
    }


    public void update() {
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                getChat();
                updateUserParams();

            }
        });

    }


    protected void getPricesFromParse(List<ParseObject> list){
        ArrayList<String>
                rubPrices1 = new ArrayList<String>(),
                rubPrices2 = new ArrayList<String>(),
                oilPrices1 = new ArrayList<String>(),
                oilPrices2 = new ArrayList<String>(),
                uahPrices1 = new ArrayList<String>(),
                uahPrices2 = new ArrayList<String>();


        boolean grow1 = false, grow2 = false;
        if (Float.parseFloat(list.get(list.size() - 1).getString("rubUsdPrice")) < Float.parseFloat(list.get(list.size() - 2).getString("rubUsdPrice"))) grow1 = true;
        if (Float.parseFloat(list.get(list.size() - 1).getString("rubEurPrice")) < Float.parseFloat(list.get(list.size() - 2).getString("rubEurPrice"))) grow2 = true;

        rubPrice = new Price(
                Price.RUB,
                list.get(0).getString("rubUsdPrice"),
                list.get(0).getString("rubEurPrice"),
                grow1, grow2);

        grow1 = false; grow2 = false;
        if (Float.parseFloat(list.get(list.size() - 1).getString("brentPrice")) < Float.parseFloat(list.get(list.size() - 2).getString("brentPrice"))) grow1 = true;
        if (Float.parseFloat(list.get(list.size() - 1).getString("wtiPrice")) < Float.parseFloat(list.get(list.size() - 2).getString("wtiPrice"))) grow2 = true;
        oilPrice = new Price(
                Price.OIL,
                list.get(0).getString("brentPrice"),
                list.get(0).getString("wtiPrice"),
                grow1, grow2);
        grow1 = false; grow2 = false;
        if (Float.parseFloat(list.get(list.size() - 1).getString("uahUsdPrice")) < Float.parseFloat(list.get(list.size() - 2).getString("uahUsdPrice"))) grow1 = true;
        if (Float.parseFloat(list.get(list.size() - 1).getString("uahEurPrice")) < Float.parseFloat(list.get(list.size() - 2).getString("uahEurPrice"))) grow2 = true;
        uahPrice = new Price(
                Price.UAH,
                list.get(0).getString("uahUsdPrice"),
                list.get(0).getString("uahEurPrice"),
                grow1, grow2);





        for (ParseObject object : list) {
            rubPrices1.add(object.getString("rubUsdPrice"));
            rubPrices2.add(object.getString("rubEurPrice"));
            oilPrices1.add(object.getString("brentPrice"));
            oilPrices2.add(object.getString("wtiPrice"));
            uahPrices1.add(object.getString("uahUsdPrice"));
            uahPrices2.add(object.getString("uahEurPrice"));

            rubPrice.setPrices1(rubPrices1);
            rubPrice.setPrices2(rubPrices2);
            oilPrice.setPrices1(oilPrices1);
            oilPrice.setPrices2(oilPrices2);
            uahPrice.setPrices1(uahPrices1);
            uahPrice.setPrices2(uahPrices2);

            rubFragment.setPrice(rubPrice);
            oilFragment.setPrice(oilPrice);
            uahFragment.setPrice(uahPrice);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        updater.stopUpdater();
    }

    public class PriceUpdater extends AsyncTask<Void, Void, Void> {






        @Override
        protected Void doInBackground(Void... params) {
            try {


                ParseQuery<ParseObject> query = ParseQuery.getQuery("Tickets");

                ParseObject object = query.get("gqrBsHeHVO");

                String brentTicket, wtiTicket;
                brentTicket = object.getString("brentTicket");
                wtiTicket = object.getString("wtiTicket");
                Stock usdUah = StockFetcher.getStock("USDUAH=X");
                Stock eurUah = StockFetcher.getStock("EURUAH=X");
                Stock rubUsd = StockFetcher.getStock("USDRUB=X");
                Stock rubEur = StockFetcher.getStock("EURRUB=X");
                Stock brent = StockFetcher.getStock(brentTicket);
                Stock wti = StockFetcher.getStock(wtiTicket);

                ParseObject pricesHistory = new ParseObject("PricesHistory");
                pricesHistory.put("uahUsdPrice", usdUah.getPrice() + "");
                pricesHistory.put("uahEurPrice", eurUah.getPrice() + "");
                pricesHistory.put("rubUsdPrice", rubUsd.getPrice() + "");
                pricesHistory.put("rubEurPrice", rubEur.getPrice() + "");
                pricesHistory.put("brentPrice", brent.getPrice() + "");
                pricesHistory.put("wtiPrice", wti.getPrice() + "");
                pricesHistory.save();



            } catch (NullPointerException | ParseException e) {
                Log.e("MainActivity", e.getMessage());
            }
            return null;
        }
    }





}
