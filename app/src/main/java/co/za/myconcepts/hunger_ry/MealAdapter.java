package co.za.myconcepts.hunger_ry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Random;

import co.za.myconcepts.hunger_ry.model.Meal;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder>{

    private List<Meal> mealList;
    private List<String> mImageURLS;

    public MealAdapter(List<Meal> list, List<String> imageURLS){
        this.mealList = list;
        this.mImageURLS = imageURLS;
    }

    @Override
    public MealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.meal_card_view, parent, false);
        return new MealViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MealViewHolder holder, int position) {
        Meal m = mealList.get(position);
        holder.tvTitle.setText(m.getTitle());
        holder.tvDescription.setText(m.getDescription());
        Random rand = new Random();
        ImageDownloader imageDownloader = new ImageDownloader(holder.imageView);
        int indexPosition = rand.nextInt(7);
        String imageURL = mImageURLS.get(indexPosition);
        imageDownloader.execute(imageURL);
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle, tvDescription;
        private ImageView imageView;

        public MealViewHolder(View v){
            super(v);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvDescription = v.findViewById(R.id.tvDescription);
            imageView = v.findViewById(R.id.imageView3);
        }
    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        ImageView mImageView;

        public ImageDownloader(ImageView imageView){
            this.mImageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream istream = connection.getInputStream();
                return BitmapFactory.decodeStream(istream);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mImageView.setImageBitmap(bitmap);
        }
    }
}
