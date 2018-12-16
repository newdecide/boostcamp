package com.example.ohdongju.pretaskmovie;

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
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{
    private static final String TAG = "MovieAdapter";
    private Context mContext=null;
    private ArrayList<Movie> movieList;

    // MovieAdapter 생성자(constructor)
    public MovieAdapter(Context context, ArrayList<Movie> movieList)
    {
        this.mContext = context;
        this.movieList = movieList;
    }

    // 뷰홀더를 생성하고 뷰를 붙여준다.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    // 뷰홀더 결합하고
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        final int pos = position;
        final String url = movieList.get(pos).getLink();

        Movie mv = movieList.get(pos);
        holder.title.setText(mv.getTitle());
        holder.pubDate.setText(mv.getPubDate());
        holder.director.setText(mv.getDirector());
        holder.actor.setText(mv.getActor());
        holder.rating.setRating(mv.getRating()/2);
        holder.poster.setImageBitmap(null);
        Glide.with(holder.poster.getContext())
                .load(movieList.get(pos).getPoster())
                .into(holder.poster);
        // RecyclerView Item 클릭시 클릭한 영화 사이트로 이동
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mContext.startActivity(i);
            }
        });
    }

    // 데이터 개수 반환
    @Override
    public int getItemCount() {
        return movieList.size();
    }

    // RecyclerView 뷰홀더 패턴 강제함.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView poster;
        public TextView title, pubDate, director, actor;
        public RatingBar rating;

        public ViewHolder(View itemView) {
            super(itemView);
            rating = (RatingBar) itemView.findViewById(R.id.movie_rating);
            poster = (ImageView) itemView.findViewById(R.id.movie_poster);
            title = (TextView) itemView.findViewById(R.id.movie_title);
            pubDate = (TextView) itemView.findViewById(R.id.movie_year);
            director = (TextView) itemView.findViewById(R.id.movie_director);
            actor = (TextView) itemView.findViewById(R.id.movie_actors);
        }
    }
}
