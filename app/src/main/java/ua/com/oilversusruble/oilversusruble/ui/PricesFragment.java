package ua.com.oilversusruble.oilversusruble.ui;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.db.chart.model.ChartSet;
import com.db.chart.model.LineSet;
import com.db.chart.model.Point;
import com.db.chart.view.AxisController;
import com.db.chart.view.LineChartView;

import java.util.Random;

import ua.com.oilversusruble.oilversusruble.Price;
import ua.com.oilversusruble.oilversusruble.R;


public class PricesFragment extends Fragment implements View.OnClickListener {



    private Price mPrice = new Price(Price.RUB, "--.-----",  "--.-----", false, false);
    Random random = new Random();

    protected TextView tvPrice1, tvPrice2, tvName;
    protected Button btnChart;
    protected ImageView imageArrow1, imageArrow2, imagePortrait, imageUsd, imageEur;



    public static PricesFragment newInstance() {
        PricesFragment fragment = new PricesFragment();
        return fragment;
    }

    public PricesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_prices, container, false);
        tvPrice1 = (TextView) root.findViewById(R.id.tvPrice1);
        tvPrice2 = (TextView) root.findViewById(R.id.tvPrice2);
        tvName = (TextView) root.findViewById(R.id.tvName);
        btnChart = (Button) root.findViewById(R.id.btnChart);
        imagePortrait = (ImageView) root.findViewById(R.id.imagePortrait);
        imageArrow1 = (ImageView) root.findViewById(R.id.imageArrow1);
        imageArrow2 = (ImageView) root.findViewById(R.id.imageArrow2);
        imageUsd = (ImageView) root.findViewById(R.id.imageUsd);
        imageEur = (ImageView) root.findViewById(R.id.imageEur);



        btnChart.setOnClickListener(this);



        initialize();
        return root;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        YoYo.with(Techniques.Pulse).playOn(v);

        switch (v.getId()) {
            case R.id.btnChart:
                showChart();
                break;
        }
    }




    private void initialize() {
        boolean isPutin;
        isPutin = mPrice.getCategory() != Price.UAH;
        tvPrice1.setText(mPrice.getPrice1());
        tvPrice2.setText(mPrice.getPrice2());

        switch (mPrice.getCategory()){

            case Price.RUB:
                tvName.setText("RUB");
                imageUsd.setImageDrawable(getResources().getDrawable(R.drawable.dollar));
                imageEur.setImageDrawable(getResources().getDrawable(R.drawable.eur));
                if (mPrice.isGrow1()) {
                    imageArrow1.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
                    imagePortrait.setImageDrawable(getResources().getDrawable(getRandomPortrait(true, true)));

                } else {
                    imageArrow1.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));
                    imagePortrait.setImageDrawable(getResources().getDrawable(getRandomPortrait(true, false)));
                }

                if (mPrice.isGrow2()) {
                    imageArrow2.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
                    imagePortrait.setImageDrawable(getResources().getDrawable(getRandomPortrait(true, true)));
                } else {
                    imageArrow2.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));
                    imagePortrait.setImageDrawable(getResources().getDrawable(getRandomPortrait(true, false)));
                }
                break;
            case Price.OIL:

                tvName.setText("Oil");
                imageUsd.setImageDrawable(getResources().getDrawable(R.drawable.brent));
                imageEur.setImageDrawable(getResources().getDrawable(R.drawable.wti));



                if (mPrice.isGrow1()) {
                    imageArrow1.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
                    imagePortrait.setImageDrawable(getResources().getDrawable(getRandomPortrait(true, false)));

                } else {
                    imageArrow1.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));
                    imagePortrait.setImageDrawable(getResources().getDrawable(getRandomPortrait(true, true)));
                }

                if (mPrice.isGrow2()) {
                    imageArrow2.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
                    imagePortrait.setImageDrawable(getResources().getDrawable(getRandomPortrait(true, false)));
                } else {
                    imageArrow2.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));
                    imagePortrait.setImageDrawable(getResources().getDrawable(getRandomPortrait(true, true)));
                }
                break;
            case Price.UAH:
                tvName.setText("UAH");
                imageUsd.setImageDrawable(getResources().getDrawable(R.drawable.dollar));
                imageEur.setImageDrawable(getResources().getDrawable(R.drawable.eur));

                if (mPrice.isGrow1()) {
                    imageArrow1.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
                    imagePortrait.setImageDrawable(getResources().getDrawable(getRandomPortrait(false, true)));

                } else {
                    imageArrow1.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));
                    imagePortrait.setImageDrawable(getResources().getDrawable(getRandomPortrait(false, false)));
                }

                if (mPrice.isGrow2()) {
                    imageArrow2.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
                    imagePortrait.setImageDrawable(getResources().getDrawable(getRandomPortrait(false, true)));
                } else {
                    imageArrow2.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));
                    imagePortrait.setImageDrawable(getResources().getDrawable(getRandomPortrait(false, false)));
                }
                break;
        }



    }


    private int getRandomPortrait(boolean isPutin, boolean isSad) {
        int rand = random.nextInt(3);
        if (isPutin) {
            if (isSad) {
                switch (rand) {
                    case 0:
                        return R.drawable.sad1;
                    case 1:
                        return R.drawable.sad2;
                    case 2:
                        return R.drawable.putin_sad;
                }
            } else {
                switch (rand) {
                    case 0:
                        return R.drawable.putin_happy;
                    case 1:
                        return R.drawable.happy2;
                    case 2:
                        return R.drawable.happy3;
                }
            }
        } else {


            if (isSad) {
                switch (rand) {
                    case 0:
                        return R.drawable.poroshenko_sad1;
                    case 1:
                        return R.drawable.poroshenko_sad2;
                    case 2:
                        return R.drawable.poroshenko_sad3;
                }
            } else {
                switch (rand) {
                    case 0:
                        return R.drawable.poroshenko_happy1;
                    case 1:
                        return R.drawable.poroshenko_happy2;
                    case 2:
                        return R.drawable.poroshenko_happy3;

                }
            }


        }
        return R.drawable.sad1;
    }

    public void setPrice(Price price){
        mPrice = price;
        initialize();
    }


    void showChart(){

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.chart_layout);
        dialog.setTitle("Chart");

        LineChartView chart = (LineChartView) dialog.findViewById(R.id.linechart);
        ImageView imageChartPrice1 = (ImageView) dialog.findViewById(R.id.imageChartPrice1);
        ImageView imageChartPrice2 = (ImageView) dialog.findViewById(R.id.imageChartPrice2);
        FrameLayout priceColor1 = (FrameLayout) dialog.findViewById(R.id.priceColor1);
        FrameLayout priceColor2 = (FrameLayout) dialog.findViewById(R.id.priceColor2);

        imageChartPrice1.setImageDrawable(imageUsd.getDrawable());
        imageChartPrice2.setImageDrawable(imageEur.getDrawable());




        int min = 1000, max = 0;


        LineSet chartSet1 = new LineSet();
        LineSet chartSet2 = new LineSet();
        chartSet1.setLineThickness(5f);
        chartSet1.setLineColor(Color.parseColor("#00B64F"));
        priceColor1.setBackgroundColor(Color.parseColor("#00B64F"));
        chartSet1.setDotsRadius(6f);
        chartSet1.setDots(true);
        chartSet1.setDotsColor(Color.parseColor("#FF6840"));
        chartSet1.setSmooth(true);


        chartSet2.setLineThickness(5f);
        chartSet2.setLineColor(Color.parseColor("#0C5DA5"));
        priceColor2.setBackgroundColor(Color.parseColor("#0C5DA5"));
        chartSet2.setDotsRadius(6f);
        chartSet2.setDots(true);
        chartSet2.setDotsColor(Color.parseColor("#FF9500"));
        chartSet2.setSmooth(true);


        int i = 1;
        for(String price : mPrice.getPrices1() ) {
            chartSet1.addPoint(new Point("" + i++, Float.parseFloat(price)));
            float priceFloat = Float.parseFloat(price);

            if (max < priceFloat) max = (int) priceFloat;
            if (min > priceFloat) min = (int) priceFloat;
        }
        i = 1;
        for(String price : mPrice.getPrices2() ) {
            chartSet2.addPoint(new Point("" + i++, Float.parseFloat(price)));
            float priceFloat = Float.parseFloat(price);

            if (max < priceFloat) max = (int) priceFloat;
            if (min > priceFloat) min = (int) priceFloat;
        }


        chart.setXLabels(AxisController.LabelPosition.OUTSIDE);
        chart.setAxisBorderValues(min - 5, max + 5, 2);
        chart.addData(chartSet1);
        chart.addData(chartSet2);
        chart.show();


        dialog.show();

    }

}
