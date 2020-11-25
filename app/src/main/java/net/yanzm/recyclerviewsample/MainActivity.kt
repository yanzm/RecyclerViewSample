package net.yanzm.recyclerviewsample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<View>(R.id.recycler_view) as RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = VersionAdapter { version ->
            Toast.makeText(this, version, Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter

        repeat(30) {
            ANDROID_CODE_NAMES.random()
            adapter.add(ANDROID_CODE_NAMES.random())
        }
    }
}

class VersionAdapter(private val clickListener: (version: String) -> Unit) :
    RecyclerView.Adapter<VersionViewHolder>() {

    private val lock = Any()

    private val versions = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VersionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return VersionViewHolder.create(inflater, parent).apply {
            itemView.setOnClickListener {
                val version = versions[adapterPosition]
                clickListener(version)
            }
        }
    }

    override fun onBindViewHolder(holder: VersionViewHolder, position: Int) {
        val version = versions[position]
        holder.textView.text = version
    }

    override fun getItemCount(): Int {
        return versions.size
    }

    fun add(version: String) {
        val position: Int
        synchronized(lock) {
            position = versions.size
            versions.add(version)
        }
        notifyItemInserted(position)
    }
}

class VersionViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val textView: TextView = itemView as TextView

    companion object {
        private const val LAYOUT_ID = R.layout.list_item

        fun create(inflater: LayoutInflater, parent: ViewGroup?): VersionViewHolder {
            return VersionViewHolder(inflater.inflate(LAYOUT_ID, parent, false))
        }
    }
}

private val ANDROID_CODE_NAMES = arrayOf(
    "Cupcake",
    "Donuts",
    "Eclair",
    "Froyo",
    "Gingerbread",
    "Honeycomb",
    "IceCreamSandwich",
    "JellyBean",
    "Kitkat",
    "Lollipop",
    "Marshmallow",
    "Nougat"
)
