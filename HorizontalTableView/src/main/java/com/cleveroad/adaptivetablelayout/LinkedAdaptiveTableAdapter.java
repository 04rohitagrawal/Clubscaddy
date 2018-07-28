package com.cleveroad.adaptivetablelayout;


import java.util.ArrayList;
import java.util.List;

/**
 * {@inheritDoc}
 * Common base class of common implementation for an {@link AdaptiveTableAdapter} that
 * can be used in {@link AdaptiveTableLayout}.
 */
public abstract class LinkedAdaptiveTableAdapter<VH extends ViewHolder> implements AdaptiveTableAdapter<VH> {
    protected boolean mIsRtl;
    /**
     * Set with observers
     */
    private final List<AdaptiveTableDataSetObserver> mAdaptiveTableDataSetObservers = new ArrayList<>();

    /**
     * Need to throw item click action
     */
    private OnItemClickListener mOnItemClickListener;

    /**
     * Need to throw long item click action
     */
    private OnItemLongClickListener mOnItemLongClickListener;

    @Override
    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    @Override
    public void setOnItemClickListener( OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public OnItemLongClickListener getOnItemLongClickListener() {
        return mOnItemLongClickListener;
    }

    @Override
    public void setOnItemLongClickListener( OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    public List<AdaptiveTableDataSetObserver> getAdaptiveTableDataSetObservers() {
        return mAdaptiveTableDataSetObservers;
    }

    /**
     * {@inheritDoc}
     *
     * @param observer the object that gets notified when the data set changes.
     */
    @Override
    public void registerDataSetObserver( AdaptiveTableDataSetObserver observer) {
        mAdaptiveTableDataSetObservers.add(observer);
    }

    /**
     * {@inheritDoc}
     *
     * @param observer the object to unregister.
     */
    @Override
    public void unregisterDataSetObserver( AdaptiveTableDataSetObserver observer) {
        mAdaptiveTableDataSetObservers.remove(observer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyDataSetChanged() {
        for (AdaptiveTableDataSetObserver observer : mAdaptiveTableDataSetObservers)
        {
            observer.notifyDataSetChanged();
        }
    }


    public void unregisterDataSetObserver() {
        for (AdaptiveTableDataSetObserver observer : mAdaptiveTableDataSetObservers) {
            mAdaptiveTableDataSetObservers.remove(observer);
        }
    }
    /**
     * {@inheritDoc}
     *
     * @param rowIndex    the row index
     * @param columnIndex the column index
     */
    @Override
    public void notifyItemChanged(int rowIndex, int columnIndex) {
        for (AdaptiveTableDataSetObserver observer : mAdaptiveTableDataSetObservers) {
            observer.notifyItemChanged(rowIndex, columnIndex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param rowIndex the row index
     */
    @Override
    public void notifyRowChanged(int rowIndex) {
        for (AdaptiveTableDataSetObserver observer : mAdaptiveTableDataSetObservers) {
            observer.notifyRowChanged(rowIndex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param columnIndex the column index
     */
    @Override
    public void notifyColumnChanged(int columnIndex) {
        for (AdaptiveTableDataSetObserver observer : mAdaptiveTableDataSetObservers) {
            observer.notifyColumnChanged(columnIndex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyLayoutChanged() {
        for (AdaptiveTableDataSetObserver observer : mAdaptiveTableDataSetObservers) {
            observer.notifyLayoutChanged();
        }
    }

    @Override
    public void onViewHolderRecycled( VH viewHolder) {
        //do something
    }

    public boolean isRtl() {
        return mIsRtl;
    }

    public void setRtl(boolean rtl) {
        mIsRtl = rtl;
    }
}
