package io.github.gusandrianos.foxforreddit.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.github.gusandrianos.foxforreddit.R
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

