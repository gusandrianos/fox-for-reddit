package io.github.gusandrianos.foxforreddit.utilities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.gusandrianos.foxforreddit.R

class PostLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<PostLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.post_loading_footer_layout, parent, false)
        return LoadStateViewHolder(view)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadStateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressbar)
        private val mTxtError: TextView = itemView.findViewById(R.id.txt_error)
        private val mBtnRetry: Button = itemView.findViewById(R.id.btn_retry)

        init {
            mBtnRetry.setOnClickListener {
                retry.invoke()  //if failed to load more posts, retry
            }
        }

        fun bind(loadState: LoadState) {
            progressBar.visibility = if (loadState is LoadState.Loading) View.VISIBLE else View.GONE
            mBtnRetry.visibility = if (loadState !is LoadState.Loading) View.VISIBLE else View.GONE
            mTxtError.visibility = if (loadState !is LoadState.Loading) View.VISIBLE else View.GONE
        }
    }
}