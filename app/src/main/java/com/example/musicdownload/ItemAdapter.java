package com.example.musicdownload;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private final Context context;
    private final Cursor cursor;

    public ItemAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView downloadButton;
        TextView textView;

        public ViewHolder(@NonNull View view) {
            super(view);
            textView = view.findViewById(R.id.text_view);
            downloadButton = view.findViewById(R.id.download_button);
        }

        public ImageView getDownload() {
            return downloadButton;
        }
        public TextView getTextView(){return textView;}
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        try {
            cursor.moveToPosition(position);
            String name = cursor.getString(1);
            String uri = cursor.getString(2);
            TextView textView = viewHolder.getTextView();
            textView.setText(name);
            ImageView download = viewHolder.getDownload();
            download.setOnClickListener(v -> download(name, uri));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void download(String name, String uri) {

            File destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/"+name+".mp3");
            Uri dstUri = FileProvider.getUriForFile(context, context.getPackageName()+".provider", destination);
            Handler handler = new Handler(Looper.getMainLooper());

            new Thread(() -> {
                try {
                    URL url = new URL(uri);
                    URLConnection urlConnection = url.openConnection();
                    InputStream inputStream = urlConnection.getInputStream();
                    OutputStream outputStream = context.getContentResolver().openOutputStream(dstUri);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buf)) > 0) {
                        outputStream.write(buf, 0, len);
                    }
                    inputStream.close();
                    outputStream.close();
                    handler.post(() -> Toast.makeText(context, "Saved to Downloads", Toast.LENGTH_SHORT).show());
                }catch (IOException e){
                    e.printStackTrace();
                }
            }).start();

    }


    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}

