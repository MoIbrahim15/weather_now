package com.mohamedibrahim.weathernow.adapters;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.mohamedibrahim.weathernow.R;
import com.mohamedibrahim.weathernow.models.Item;
import com.mohamedibrahim.weathernow.ui.views.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CitiesAdapter extends AnimationBaseAdapter<Item> {


    public CitiesAdapter(Context context, ArrayList<Item> items) {
        super(context, R.layout.item, items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(initView(parent));
    }


    class ViewHolder extends AnimationBaseAdapter<Item>.BaseViewHolder {

        @BindView(R.id.tv_city)
        CustomTextView tvCity;
        @BindView(R.id.tv_temp)
        CustomTextView tvTemp;
        @BindView(R.id.tv_humidity)
        CustomTextView tvHumidity;
        @BindView(R.id.tv_wind_speed)
        CustomTextView tvWindSpeed;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void fillData(int position) {
            Item item = getItem(position);
            if (item.getName() != null) {
                tvCity.setText(item.getName());
            }
            if (item.getMain().getTemp() != null) {
                tvTemp.setText(getContext().getResources().getString(R.string.current_temp)
                        .concat(item.getMain().getTemp()));
            }

            if (item.getMain().getHumidity() != null) {
                tvHumidity.setText(getContext().getResources().getString(R.string.humidity).concat(" ").concat(item.getMain().getHumidity()));
            }


            if (item.getWind().getDeg() != null && item.getWind().getSpeed() != null) {
                tvWindSpeed.setText(getContext().getResources().getString(R.string.wind_degree_speed).concat(" ").concat(item.getWind().getDeg()
                        .concat(" - ").concat(item.getWind().getSpeed())));
            }
        }
    }

    @Override
    protected Animator getAnimators(View view, int position) {

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", DEFAULT_FROM, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", DEFAULT_FROM, 1f);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY);
        return set;
    }
}
