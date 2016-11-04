package net.yanzm.recyclerviewsample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String[] ANDROID_CODE_NAMES = {
            "Cupcake", "Donuts", "Eclair", "Froyo", "Gingerbread", "Honeycomb",
            "IceCreamSandwich", "JellyBean", "Kitkat", "Lollipop", "Marshmallow", "Nougat"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final VersionAdapter adapter = new VersionAdapter() {
            @Override
            protected void onVersionClicked(@NonNull String version) {
                super.onVersionClicked(version);
                // Activity 側でタップされたときの処理を行う
                Toast.makeText(MainActivity.this, version, Toast.LENGTH_SHORT).show();
            }
        };

        recyclerView.setAdapter(adapter);

        final Random random = new Random();
        for (int i = 0; i < 30; i++) {
            final int index = random.nextInt(ANDROID_CODE_NAMES.length);
            adapter.add(ANDROID_CODE_NAMES[index]);
        }
    }

    public static class VersionAdapter extends RecyclerView.Adapter<VersionViewHolder> {

        // タップされたときのインタフェースを定義
        protected void onVersionClicked(@NonNull String version) {
        }

        private final Object lock = new Object();
        private final List<String> versions = new ArrayList<>();

        @Override
        public VersionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            final VersionViewHolder holder = VersionViewHolder.create(inflater, parent);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = holder.getAdapterPosition();
                    final String version = versions.get(position);
                    onVersionClicked(version);
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(VersionViewHolder holder, int position) {
            final String version = versions.get(position);
            holder.textView.setText(version);
        }

        @Override
        public int getItemCount() {
            return versions.size();
        }

        public void add(@NonNull String version) {
            final int position;
            synchronized (lock) {
                position = versions.size();
                versions.add(version);
            }
            notifyItemInserted(position);
        }
    }

    public static class VersionViewHolder extends RecyclerView.ViewHolder {

        private static final int LAYOUT_ID = R.layout.list_item;

        static VersionViewHolder create(@NonNull LayoutInflater inflater, ViewGroup parent) {
            return new VersionViewHolder(inflater.inflate(LAYOUT_ID, parent, false));
        }

        final TextView textView;

        private VersionViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }
}
