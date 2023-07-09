package com.livefreebg.android.places.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.livefreebg.android.R
import com.livefreebg.android.databinding.ItemPictureBinding
import javax.inject.Inject

class FirestorePicturesAdapter @Inject constructor(
    private val firebaseStorage: FirebaseStorage
) : RecyclerView.Adapter<FirestorePicturesAdapter.ViewHolder>() {

    private var pictures: List<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = pictures.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pictureId = pictures[position]
        holder.bind(firebaseStorage.reference.child("images/${pictureId}"))
    }

    fun setPictures(pictures: List<String>) {
        this.pictures = pictures
        notifyDataSetChanged()
    }

    data class ViewHolder(val binding: ItemPictureBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(reference: StorageReference) {
            Glide
                .with(binding.root.context)
                .load(reference)
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.picture)
        }
    }
}