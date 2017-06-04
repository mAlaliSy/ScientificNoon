package org.n_scientific.scientificnoon.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.n_scientific.scientificnoon.Config;
import org.n_scientific.scientificnoon.R;
import org.n_scientific.scientificnoon.data.pojo.Comment;
import org.n_scientific.scientificnoon.utils.DateUtils;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohammad on 02/06/17.
 */

public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int LOADING_HOLDER = 0;
    private final int COMMENT_HOLDER = 1;

    private Context context;
    private List<Comment> data;

    private boolean allLoaded = false;

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.data = comments;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        if (i == LOADING_HOLDER) {
            View view = LayoutInflater.from(context).inflate(R.layout.loading_item, viewGroup, false);
            return new LoadingHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.comment_item, viewGroup, false);

            return new CommentHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        if (viewHolder instanceof CommentHolder) {

            CommentHolder commentHolder = (CommentHolder) viewHolder;

            commentHolder.authorName.setText(data.get(i).getAuthorName());

            String trimmed = data.get(i).getContent().getContent();

            if (trimmed.endsWith("\n")) {
                trimmed = trimmed.substring(0, trimmed.length());
            }

            trimmed = trimmed.replaceAll("<p>", "").replaceAll("</p>", "");

            commentHolder.comment.setText(Html.fromHtml(trimmed));


            Date date = DateUtils.parseDate(data.get(i).getDate().replace("T", "-"), Config.FETCHED_POST_COMMENT_DATE_PATTERN);

            commentHolder.date.setText(DateUtils.smartFormat(date, Config.POST_COMMENT_DATE_PATTERN_NO_YEAR, Config.POST_COMMENT_DATE_PATTERN));


        }

    }

    @Override
    public int getItemCount() {
        return data.size() + (allLoaded ? 0 : 1);
    }

    @Override
    public int getItemViewType(int position) {
        return position == data.size() ? LOADING_HOLDER : COMMENT_HOLDER;
    }

    public class CommentHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtAuthorName)
        TextView authorName;

        @BindView(R.id.txtComment)
        TextView comment;

        @BindView(R.id.txtDate)
        TextView date;

        public CommentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
    }

    public boolean isAllLoaded() {
        return allLoaded;
    }

    public void setAllLoaded(boolean allLoaded) {
        this.allLoaded = allLoaded;
    }
}
