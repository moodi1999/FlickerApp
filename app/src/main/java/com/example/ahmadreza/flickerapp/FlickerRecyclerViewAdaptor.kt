package com.example.ahmadreza.flickerapp

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

/**
 * Created by ahmadreza on 8/19/18.
 */
class FlickerRecyclerViewAdaptor(var mphotoList: ArrayList<Photo>?, val context: Context) : RecyclerView.Adapter<FlickerRecyclerViewAdaptor.FlickerImageViewHolder>() {

    override fun onBindViewHolder(holder: FlickerImageViewHolder, position: Int) {

        val photo = mphotoList!!.get(position)
        Picasso.with(context).load(photo.mImage)
                .error(R.drawable.ic_image)
                .placeholder(R.drawable.ic_image)
                .into(holder.thumbnail)

        holder.title?.text = photo.mTitle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlickerImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.browse, parent, false)
        return FlickerImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (mphotoList != null && mphotoList!!.size != 0) mphotoList!!.size else 0
    }

    fun loadNewData(newPhotos: ArrayList<Photo>) {
        mphotoList = newPhotos
        notifyDataSetChanged()
    }

    fun getPhoto(position: Int): Photo? {
        return if (mphotoList != null && mphotoList!!.size != 0) mphotoList!!.get(position) else null
    }

    inner class FlickerImageViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){
        var thumbnail = itemView?.findViewById<ImageView>(R.id.thumbmail)
        var title = itemView?.findViewById<TextView>(R.id.title)



    }
}