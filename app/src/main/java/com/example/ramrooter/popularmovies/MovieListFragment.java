package com.example.ramrooter.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.recyclerview.R.styleable.RecyclerView;

/**
 * Created by Ram Rooter on 10/15/2016.
 */

public class MovieListFragment extends Fragment {
    private RecyclerView mMovieRecyclerView;
    private List<Movie>mMovies = new ArrayList<>();

    public static MovieListFragment newInstance(){
        return new MovieListFragment();
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchItemsTask().execute();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedStateInstance){
        View v = inflater.inflate(R.layout.fragment_movie_list, container, false);
        mMovieRecyclerView = (RecyclerView)v.findViewById(R.id.fragment_movie_list);
        mMovieRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        setUpAdapter();
        return v;
    }

    protected class FetchItemsTask extends AsyncTask<Void, Void, List<Movie>>{
        protected List<Movie>doInBackground(Void... params){
            return new MovieFetcher().fetchItems();
        }

        protected void onPostExecute(List<Movie>movies){
            mMovies = movies;
            setUpAdapter();
        }
    }

    private class MovieHolder extends RecyclerView.ViewHolder{
        private TextView mTitleTextView;

        public MovieHolder(View itemView){
            super(itemView);
            mTitleTextView = (TextView)itemView;
        }

        public void bindMovie(Movie movie){
            mTitleTextView.setText(movie.toString());
        }
    }

    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder>{
        private List<Movie>mMovies;

        public MovieAdapter(List<Movie>movies){
            mMovies = movies;
        }

        public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType){
            TextView textView = new TextView(getActivity());
            return new MovieHolder(textView);
        }

        public void onBindViewHolder(MovieHolder holder, int position){
            Movie movie = mMovies.get(position);
            holder.bindMovie(movie);
        }

        public int getItemCount(){
            return mMovies.size();
        }
    }

    private void setUpAdapter(){
        if(isAdded()){
            mMovieRecyclerView.setAdapter(new MovieAdapter(mMovies));
        }
    }

}
