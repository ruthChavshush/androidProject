package com.example.sporty.Modules.Maps

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sporty.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.sporty.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.example.sporty.Models.Post
import com.example.sporty.Modules.Adapters.PostAdapter





class MapsFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: GoogleMap
        private lateinit var postAdapter: PostAdapter

        private var allPosts: List<Post> = emptyList()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

private fun fetchPosts() {
    try {
        db.collection("posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                            //Log.d("result", "result ", result)
                val posts = result.toObjects(Post::class.java)
                allPosts = posts
                //postAdapter.submitList(posts) // Initial data set
                //cachePosts(posts)
                //binding.swipeRefreshLayout.isRefreshing = false // Stop the refresh animation
            }
            .addOnFailureListener { exception ->
                // Handle failure case
                Log.e("FeedFragment", "Error getting documents: ", exception)
                //binding.swipeRefreshLayout.isRefreshing = false // Stop the refresh animation
            }
    } catch (e: Exception) {
        // Log any unexpected errors
        Log.e("FeedFragment", "Unexpected error occurred while fetching posts", e)
    }
}

  

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
                try{fetchPosts()} catch (e: Exception) {
                    // Log any unexpected errors
                    Log.e("FeedFragment", "Unexpected error occurred while fetching posts", e)
                }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

  val locations = listOf(
            LatLng(32.0789852,34.8399412), // San Francisco
            LatLng(32.0846768,34.8244938), // Los Angeles
            LatLng(32.0894142,34.8226284)   // New York
        )

        // Add markers
        locations.forEach { latLng ->
        map.addMarker(
    MarkerOptions()
        .position(latLng)
        .title("Pinned Location")
        .snippet("Extra info about this place") // <--- new
)}
map.moveCamera(CameraUpdateFactory.newLatLngZoom(locations.first(), 13f))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
