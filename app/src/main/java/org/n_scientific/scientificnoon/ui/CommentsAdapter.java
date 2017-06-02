package org.n_scientific.scientificnoon.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.n_scientific.scientificnoon.R;
import org.n_scientific.scientificnoon.data.pojo.Comment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohammad on 02/06/17.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentHolder> {

    private Context context;
    private List<Comment> data;

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.data = comments;
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, viewGroup, false);

        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentHolder commentHolder, int i) {

        commentHolder.authorName.setText(data.get(i).getAuthorName());

        String trimmed = data.get(i).getContent().getContent();

        if (trimmed.endsWith("\n")) {
            trimmed = trimmed.substring(0, trimmed.length());
        }

        trimmed = trimmed.replaceAll("<p>", "").replaceAll("</p>", "");

        commentHolder.comment.setText(Html.fromHtml(trimmed));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtAuthorName)
        TextView authorName;

        @BindView(R.id.txtComment)
        TextView comment;

        public CommentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
    }

}
