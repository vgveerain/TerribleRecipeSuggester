package com.bumos.vgvee.terriblerecipesuggester;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapter3 extends RecyclerView.Adapter<adapter3.ViewHolder> {
    Context context;
    ArrayList<recipes> recipes;
    DatabaseReference childreference;

    public adapter3(Context context, ArrayList<com.bumos.vgvee.terriblerecipesuggester.recipes> recipes, DatabaseReference childreference) {
        this.context = context;
        this.recipes = recipes;
        this.childreference = childreference;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(context);
        View view = li.inflate(R.layout.item_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Picasso.get().load(recipes.get(position).image_url).into(holder.imageView);
        holder.title.setText(recipes.get(position).title);
        holder.bookmarkTag.setImageResource(R.drawable.ic_bookmark_black_24dp);
        holder.sourceUrl.setText(recipes.get(position).source_url);
        holder.sourceUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(recipes.get(position).image_url));
                context.startActivity(intent);
            }
        });
        holder.bookmarkTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.bookmarkTag.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
//                Main2Activity.savePost(recipes.get(position));
//                Main2Activity.savePost(recipes.get(position),childreference);
                Query applesQuery = childreference.orderByChild("id").equalTo(recipes.get(position).id);
                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                        recipes.remove(position);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("TAG", "onCancelled", databaseError.toException());
                    }
                });
//                Main3Activity.removePost(applesQuery);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView title,sourceUrl;
        ImageView bookmarkTag;
        public ViewHolder(View itemView) {
            super(itemView);
            bookmarkTag = itemView.findViewById(R.id.bookmarkTag);
            imageView = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.titleText);
            sourceUrl = itemView.findViewById(R.id.sourceUrl);
        }
    }
}
