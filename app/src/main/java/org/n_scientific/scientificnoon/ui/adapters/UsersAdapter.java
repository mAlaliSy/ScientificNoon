package org.n_scientific.scientificnoon.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.n_scientific.scientificnoon.R;
import org.n_scientific.scientificnoon.data.pojo.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohammad on 14/07/17.
 */

public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int LOADING_ITEM = 0;
    private static final int USER_ITEM = -1;

    private boolean allDownloaded;
    private List<User> data;
    private Context context;

    public UsersAdapter(boolean allDownloaded, List<User> data, Context context) {
        this.allDownloaded = allDownloaded;
        this.data = data;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == LOADING_ITEM)
            return new LoadingHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.loading_item, viewGroup, false));

        return new UserViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        if (viewHolder instanceof UserViewHolder) {
            UserViewHolder userViewHolder = (UserViewHolder) viewHolder;

            User user = data.get(i);

            userViewHolder.txtName.setText(user.getName());
            if (user.getDescription() == null || user.getDescription().length() == 0)
                userViewHolder.txtDesc.setVisibility(View.GONE);
            else {
                userViewHolder.txtDesc.setVisibility(View.VISIBLE);
                userViewHolder.txtDesc.setText(data.get(i).getDescription());
            }

            Glide.with(context)
                    .load(user.getAvatarUrl())
                    .into(userViewHolder.avatar);
        }

    }

    @Override
    public int getItemCount() {
        return allDownloaded ? data.size() : (data.size() + 1);
    }

    @Override
    public int getItemViewType(int position) {
        return position == data.size() ? LOADING_ITEM : USER_ITEM;
    }

    public boolean isAllDownloaded() {
        return allDownloaded;
    }

    public void setAllDownloaded(boolean allDownloaded) {
        this.allDownloaded = allDownloaded;
        notifyDataSetChanged();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.txtName)
        TextView txtName;

        @BindView(R.id.txtDesc)
        TextView txtDesc;

        @BindView(R.id.avatar)
        ImageView avatar;

        public UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(data.get(getAdapterPosition()).getLink()));
            context.startActivity(intent);
        }
    }

}
