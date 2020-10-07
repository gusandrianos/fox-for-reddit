package io.github.gusandrianos.foxforreddit.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.gusandrianos.foxforreddit.R
import io.github.gusandrianos.foxforreddit.data.models.Token
import io.github.gusandrianos.foxforreddit.databinding.FragmentPostsBinding
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModel
import kotlinx.android.synthetic.main.fragment_posts.*

class PostsFragment : Fragment(R.layout.fragment_posts) {


    private var binding: FragmentPostsBinding?= null
    private val _binding get() = binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         val mToken= InjectorUtils.getInstance().provideTokenRepository(activity?.application).token
         val viewModel = ViewModelProvider(this, InjectorUtils.getInstance().providePostViewModelFactory()).get(PostViewModel::class.java)
        binding = FragmentPostsBinding.bind(view)

        val adapter = PostPagingDataAdapter()
        binding.apply {
            recyclerview.setHasFixedSize(true)
            recyclerview.adapter = adapter
        }

        viewModel.getPosts("r/CrackWatch","new",mToken).observe(viewLifecycleOwner){
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}

