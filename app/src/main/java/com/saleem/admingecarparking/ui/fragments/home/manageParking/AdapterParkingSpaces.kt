package com.saleem.admingecarparking.ui.fragments.home.manageParking

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.saleem.admingecarparking.data.ModelParkingSpace
import com.saleem.admingecarparking.databinding.ItemParkingBinding
import com.saleem.admingecarparking.utils.ProgressDialogUtil
import com.saleem.admingecarparking.utils.gone
import com.saleem.admingecarparking.utils.wrapWithRsHr
import com.saleem.admingecarparking.utils.wrapWithSpots

class AdapterParkingSpaces(private val items: List<ModelParkingSpace>,private val parkingItemCallBack:(ModelParkingSpace) -> Unit) :
    RecyclerView.Adapter<AdapterParkingSpaces.ViewHolder>() {
    val db = Firebase.firestore
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        val binding = ItemParkingBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items[position]
        holder.bind(data)
    }
    inner class ViewHolder(val binding: ItemParkingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ModelParkingSpace){
            Glide.with(itemView.context)
                .load(data.spaceImage)
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                        binding.pgImageParking.gone()
                        return false
                    }

                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable?>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        binding.pgImageParking.gone()
                        return false
                    }
                })
                .into(binding.imgParkingArea)
            binding.tvNameParkingArea.text = data.spaceName
            binding.tvLocation.text = data.spaceLocationInText
            binding.tvPrice.text = data.pricePerHour.wrapWithRsHr()
            binding.tvNumOfSpots.text = data.numberOfSpots.toString().wrapWithSpots()
            binding.btnEdit.setOnClickListener {
                parkingItemCallBack.invoke(data)
            }
            binding.btnDelete.setOnClickListener {
                deleteSpace(data.docId)
            }
        }

        private fun deleteSpace(docId: String) {
            Toast.makeText(itemView.context, "deleting...", Toast.LENGTH_SHORT).show()
            val batch = db.batch()

            val docRef = db.collection("parkingSpaces").document(docId)
            batch.delete(docRef)

            db.collection("parkingSpaces").document(docId).collection("spots").get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        val subDocRef = db.collection("parkingSpaces").document(docId).collection("spots").document(document.id)
                        batch.delete(subDocRef)
                    }
                    batch.commit()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(itemView.context, e.message, Toast.LENGTH_SHORT).show()
                }
        }

    }

}