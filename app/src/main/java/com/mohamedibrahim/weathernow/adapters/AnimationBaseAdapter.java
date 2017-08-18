package com.mohamedibrahim.weathernow.adapters;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.mohamedibrahim.weathernow.utils.ViewHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


public abstract class AnimationBaseAdapter<T> extends RecyclerView.Adapter<AnimationBaseAdapter<T>.BaseViewHolder> {

    private final WeakReference<Context> contextReference;
    private final int itemLayoutRes;
    private ArrayList<T> items;
    private int mDuration = 300;
    private Interpolator mInterpolator = new LinearInterpolator();
    private int mLastPosition = -1;
    private boolean animateFirstOnly = false, enableAnimation = false;
    protected OnItemClickListener onItemClickListener;
    protected static final float DEFAULT_FROM = .5f;

    public interface OnItemClickListener {
        void onItemClick(Object object);
    }

    public AnimationBaseAdapter(Context context, int itemLayoutRes, ArrayList<T> items) {
        this.contextReference = new WeakReference<>(context);
        this.itemLayoutRes = itemLayoutRes;
        this.items = items;
    }

    protected View initView(ViewGroup parent) {
        return LayoutInflater.from(getContext()).inflate(itemLayoutRes, parent, false);
    }

    protected T getItem(int position) {
        if (items != null && items.size() > 0) {
            return items.get(position);
        }
        return null;
    }

    void addItem(T item) {
        items.add(item);
        notifyItemInserted(getItemCount() - 1);
    }

    void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void notifyDataSetChanged(ArrayList<T> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    protected Context getContext() {
        return contextReference.get();
    }

    @Override
    public final void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    protected abstract Animator getAnimators(View view, int position);

    public void setDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    public void setInterpolator(Interpolator mInterpolator) {
        this.mInterpolator = mInterpolator;
    }

    public void setAnimateFirstOnly(boolean firstOnly) {
        animateFirstOnly = firstOnly;
    }


    public void setEnableAnimation(boolean enableAnimation) {
        this.enableAnimation = enableAnimation;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
    }

    public abstract class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public BaseViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
        }

        void onBind(int adapterPosition) {
            if (enableAnimation && (!animateFirstOnly || adapterPosition > mLastPosition)) {
                Animator animator = getAnimators(itemView, adapterPosition);
                animator.setDuration(mDuration).setInterpolator(mInterpolator);
                animator.start();
                mLastPosition = adapterPosition;
            } else {
                ViewHelper.clear(itemView);
            }
            fillData(adapterPosition);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getItem(getAdapterPosition()));
            }
        }

        protected abstract void fillData(int position);
    }
}