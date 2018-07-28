package com.cleveroad.adaptivetablelayout;


import android.view.ViewGroup;

/**
 * Base AdaptiveTableAdapter Interface for AdaptiveTableLayout's adapter.
 *
 * @param <VH> Implementation of ViewHolder {@link ViewHolder}
 */
interface AdaptiveTableAdapter<VH extends ViewHolder> extends AdaptiveTableDataSetObserver {
    /**
     * @return Item click listener
     */
    OnItemClickListener getOnItemClickListener();

    /**
     * Set new item click listener
     *
     * @param onItemClickListener new item click listener
     */
    void setOnItemClickListener( OnItemClickListener onItemClickListener);

    /**
     * @return Item long click listener
     */
    OnItemLongClickListener getOnItemLongClickListener();

    /**
     * Set new item long click listener
     *
     * @param onItemLongClickListener new item long click listener
     */
    void setOnItemLongClickListener( OnItemLongClickListener onItemLongClickListener);

    /**
     * Register an observer that is called when changes happen to the data used
     * by this adapter.
     *
     * @param observer the object that gets notified when the data set changes.
     */
    void registerDataSetObserver( AdaptiveTableDataSetObserver observer);

    /**
     * Unregister an observer that has previously been registered with this
     * adapter via {@link #registerDataSetObserver}.
     *
     * @param observer the object to unregister.
     */
    void unregisterDataSetObserver( AdaptiveTableDataSetObserver observer);


    /**
     * How many rows are in the data table represented by this Adapter.
     *
     * @return count of rows with header
     */
    int getRowCount();

    /**
     * How many columns are in the data table represented by this Adapter.
     *
     * @return count of columns with header
     */
    int getColumnCount();

    /**
     * Called when {@link AdaptiveTableLayout} needs a new ITEM {@link ViewHolder}
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #onBindViewHolder(ViewHolder, int, int)
     */
    VH onCreateItemViewHolder( ViewGroup parent);

    /**
     * Called when {@link AdaptiveTableLayout} needs a new COLUMN HEADER ITEM {@link ViewHolder}
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #onBindHeaderColumnViewHolder(ViewHolder, int)
     */
    VH onCreateColumnHeaderViewHolder( ViewGroup parent);

    /**
     * Called when {@link AdaptiveTableLayout} needs a new ROW HEADER ITEM {@link ViewHolder}
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #onBindHeaderRowViewHolder(ViewHolder, int)
     */
    VH onCreateRowHeaderViewHolder( ViewGroup parent);

    /**
     * Called when {@link AdaptiveTableLayout} needs a new LEFT TOP HEADER ITEM {@link ViewHolder}
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #onBindLeftTopHeaderViewHolder(ViewHolder)
     */
    VH onCreateLeftTopHeaderViewHolder( ViewGroup parent);


    /**
     * Called by {@link AdaptiveTableLayout} to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#getItemView()} to reflect the item at the given
     * position.
     *
     * @param viewHolder The ITEM {@link ViewHolder} which should be updated to represent the contents of the
     *                   item at the given position in the data set.
     * @param row        The row index of the item within the adapter's data set.
     * @param column     The column index of the item within the adapter's data set.
     */
    void onBindViewHolder( VH viewHolder, int row, int column);

    /**
     * Called by {@link AdaptiveTableLayout} to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#getItemView()} to reflect the item at the given
     * position.
     *
     * @param viewHolder The COLUMN HEADER ITEM {@link ViewHolder} which should be updated to represent the contents of the
     *                   item at the given position in the data set.
     * @param column     The column index of the item within the adapter's data set.
     */
    void onBindHeaderColumnViewHolder( VH viewHolder, int column);

    /**
     * Called by {@link AdaptiveTableLayout} to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#getItemView()} to reflect the item at the given
     * position.
     *
     * @param viewHolder The ROW HEADER ITEM {@link ViewHolder} which should be updated to represent the contents of the
     *                   item at the given position in the data set.
     * @param row        The row index of the item within the adapter's data set.
     */
    void onBindHeaderRowViewHolder( VH viewHolder, int row);

    /**
     * Called by {@link AdaptiveTableLayout} to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#getItemView()} to reflect the item at the given
     * position.
     *
     * @param viewHolder The TOP LEFT HEADER ITEM{@link ViewHolder} which should be updated to represent the contents of the
     *                   item at the given position in the data set.
     */
    void onBindLeftTopHeaderViewHolder( VH viewHolder);


    /**
     * Return the width of the column.
     *
     * @param column the column index.
     * @return The width of the column, in pixels.
     */
    int getColumnWidth(int column);


    /**
     * Return the header column height.
     *
     * @return The header height of the columns, in pixels.
     */
    int getHeaderColumnHeight();

    /**
     * Return the height of the row.
     *
     * @param row the row index.
     * @return The height of the row, in pixels.
     */
    int getRowHeight(int row);

    /**
     * Return the header row width.
     *
     * @return The header width of the rows, in pixels.
     */
    int getHeaderRowWidth();

    /**
     * Called when a view created by this adapter has been recycled.
     * <p>
     * <p>A view is recycled when a {@link AdaptiveTableLayout} decides that it no longer
     * needs to be attached. This can be because it has
     * fallen out of visibility or a set of cached views represented by views still
     * attached to the parent RecyclerView. If an item view has large or expensive data
     * bound to it such as large bitmaps, this may be a good place to release those
     * resources.</p>
     * <p>
     * {@link AdaptiveTableLayout} calls this method right before clearing ViewHolder's internal data and
     * sending it to Recycler.
     *
     * @param viewHolder The {@link ViewHolder} for the view being recycled
     */
    void onViewHolderRecycled( VH viewHolder);


}
